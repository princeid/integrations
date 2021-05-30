package com.prince.integrations.providers.interswitch.services;

import com.prince.integrations.AbstractBillerService;
import com.prince.integrations.exceptions.ApplicationException;
import com.prince.integrations.exceptions.ErrorResponse;
import com.prince.integrations.exceptions.InterswitchErrorResponse;
import com.prince.integrations.providers.interswitch.pojo.AuthenticateRequest;
import com.prince.integrations.providers.interswitch.pojo.AuthenticateResponse;
import com.prince.integrations.providers.interswitch.pojo.GetBillerResponse;
import com.prince.integrations.utils.JsonUtils;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

@Service
@Slf4j
public class InterswitchServiceImpl extends AbstractBillerService implements InterswitchService {

    private static final Logger log = LoggerFactory.getLogger(InterswitchServiceImpl.class);

    @Value("${interswitch.url}")
    private String url;

    @Value("${interswitch.client.id}")
    private String clientId;

    @Value("${interswitch.secret.key}")
    private String secretKey;

    @Value("${interswitch.terminal.id}")
    private String terminalId;



    public AuthenticateResponse login(){
        String narration = "calling login end point";

        AuthenticateRequest authenticateRequest = new AuthenticateRequest();
        authenticateRequest.setGrant_type("client_credentials");

        AuthenticateResponse authenticateResponse = Try.of(() -> callClient( url + "/passport/oauth/token", HttpMethod.POST, authenticateRequest,  getLoginRestHttpHeaders(), narration,  AuthenticateResponse.class, true))
                .onFailure(RestClientResponseException.class, throwable -> {

                    InterswitchErrorResponse errorResponse = JsonUtils.cast(throwable.getResponseBodyAsString(), InterswitchErrorResponse.class);
                    throw new ApplicationException(errorResponse == null ? "An unknown error occurred." : errorResponse.getErrors().get(0).getMessage());
                }).get().getBody();

        return authenticateResponse;
    }


    protected HttpHeaders getLoginRestHttpHeaders() {
        HttpHeaders httpHeaders = super.getRestHttpHeaders();
        httpHeaders.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + secretKey).getBytes()));
        return httpHeaders;
    }


    protected HttpHeaders getHttpHeaders(String method, String url, final Logger log) throws UnsupportedEncodingException, URISyntaxException {
        HttpHeaders httpHeaders = super.getRestHttpHeaders();
        String nonce = generateNonce();
        httpHeaders.set("Authorization", generateAuth(clientId));
        httpHeaders.set("Nonce", nonce);
        String time = getTime();
        httpHeaders.set("Timestamp", time);
        httpHeaders.set("SignatureMethod", "SHA1");
        httpHeaders.set("TerminalId", terminalId);
        httpHeaders.set("Signature", generateSignature(method, url, time, nonce, clientId, secretKey, log));
        return httpHeaders;
    }

    @Override
    public GetBillerResponse getBillers() {
        String narration = "calling get-Billers endpoint";

//        final Logger log = null;

        String getBillersUrl = url + "/api/v2/quickteller/billers";
        String httpMethod = "GET";

        GetBillerResponse getBillerResponse = Try.of(() -> callClient( getBillersUrl, getHttpHeaders(httpMethod, getBillersUrl, log), narration,  GetBillerResponse.class, true))
                .onFailure(RestClientResponseException.class, throwable -> {
                    ErrorResponse errorResponse = JsonUtils.cast(throwable.getResponseBodyAsString(), ErrorResponse.class);
                    throw new ApplicationException(errorResponse == null ? "An unknown error occurred." : errorResponse.getMessage());
                }).get().getBody();

        return getBillerResponse;
    }

    public String generateSignature(final String httpMethod, final String serviceURL, final String timeStamp, final String generatedNonce, final String clientId, final String secretKey, final Logger log) throws UnsupportedEncodingException, DecoderException, URISyntaxException {
        final String baseStringToBeSigned = httpMethod.toUpperCase() + "&" + encodeURL(serviceURL) + "&" + timeStamp + "&" + generatedNonce + "&" + clientId + "&" + secretKey;
        log.debug("String to be encoded for Signature >>> " + baseStringToBeSigned);
// final String signature = Base64.encode();
        byte[] str = decodeHex(sha1Hash(baseStringToBeSigned, log));
// final String signature = java.util.Base64.getEncoder().encodeToString(str);
        final String signature = Base64.getEncoder().encodeToString(str);
        log.debug("Signature String >>> " + signature);
        return signature;
    }

    public String encodeURL(final String serviceURL) throws URISyntaxException {
        String encodedURL = "";
        try {
            encodedURL = URLEncoder.encode(serviceURL, StandardCharsets.UTF_8.toString());
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedURL;
    }

    public byte[] decodeHex(final String data) throws DecoderException {
        return decodeHex(data.toCharArray());
    }
    public byte[] decodeHex(final char[] data) throws DecoderException {
        final int len = data.length;
        if ((len & 0x1) != 0x0) {
            throw new ApplicationException("Odd number of characters.");
        }
        final byte[] out = new byte[len >> 1];
        int f;
        for (int i = 0, j = 0; j < len; ++j, f |= toDigit(data[j], j), ++j, out[i] = (byte)(f & 0xFF), ++i) {
            f = toDigit(data[j], j) << 4;
        }
        return out;
    }

    protected int toDigit(final char ch, final int index) throws DecoderException {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new ApplicationException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    public String sha1Hash(final String input, final Logger logger) {
        String sha1 = null;
        try {
            final MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
            msdDigest.update(input.getBytes("UTF-8"), 0, input.length());
            sha1 = DatatypeConverter.printHexBinary(msdDigest.digest());
        }
        catch (Exception e) {
            logger.error("Exception thrown while hashing : " + e.fillInStackTrace());
        }
        return sha1;
    }

    public String getTime() {
        final long ts = System.currentTimeMillis() / 1000L;
        return new StringBuilder(String.valueOf(ts)).toString();
    }
    public String generateNonce() {
        final UUID uniqueKey = UUID.randomUUID();
        String uniqueId = String.valueOf((int)(System.currentTimeMillis() & 0xFFFFFFFL)) + uniqueKey.toString().substring(1, 20);
        uniqueId = uniqueId.replaceAll("-", "");
        return uniqueId;
    }

    public String generateAuth(final String clientId) throws UnsupportedEncodingException {
        final String userCred = "InterswitchAuth " + Base64.getEncoder().encodeToString(clientId.getBytes());
        System.out.println("Auth string >>>> " + userCred);
        return userCred;
    }
}
