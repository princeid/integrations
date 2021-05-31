package com.prince.integrations.utils;

import com.prince.integrations.exceptions.ApplicationException;
import org.bouncycastle.util.encoders.DecoderException;
import org.slf4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

public class Commons {

    public static String generateSignature(final String httpMethod, final String serviceURL, final String timeStamp, final String generatedNonce, final String clientId, final String secretKey, final Logger log) throws UnsupportedEncodingException, DecoderException, URISyntaxException {
        final String baseStringToBeSigned = httpMethod.toUpperCase() + "&" + encodeURL(serviceURL) + "&" + timeStamp + "&" + generatedNonce + "&" + clientId + "&" + secretKey;
        log.debug("String to be encoded for Signature >>> " + baseStringToBeSigned);
// final String signature = Base64.encode();
        byte[] str = decodeHex(sha1Hash(baseStringToBeSigned, log));
// final String signature = java.util.Base64.getEncoder().encodeToString(str);
        final String signature = Base64.getEncoder().encodeToString(str);
        log.debug("Signature String >>> " + signature);
        return signature;
    }

    public static String encodeURL(final String serviceURL) throws URISyntaxException {
        String encodedURL = "";
        try {
            encodedURL = URLEncoder.encode(serviceURL, StandardCharsets.UTF_8.toString());
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedURL;
    }

    public static byte[] decodeHex(final String data) throws DecoderException {
        return decodeHex(data.toCharArray());
    }
    public static  byte[] decodeHex(final char[] data) throws DecoderException {
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

    protected static int toDigit(final char ch, final int index) throws DecoderException {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new ApplicationException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    public static String sha1Hash(final String input, final Logger logger) {
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

    public static String getTime() {
        final long ts = System.currentTimeMillis() / 1000L;
        return new StringBuilder(String.valueOf(ts)).toString();
    }
    public static String generateNonce() {
        final UUID uniqueKey = UUID.randomUUID();
        String uniqueId = String.valueOf((int)(System.currentTimeMillis() & 0xFFFFFFFL)) + uniqueKey.toString().substring(1, 20);
        uniqueId = uniqueId.replaceAll("-", "");
        return uniqueId;
    }

    public static String generateAuth(final String clientId) throws UnsupportedEncodingException {
        final String userCred = "InterswitchAuth " + Base64.getEncoder().encodeToString(clientId.getBytes());
        System.out.println("Auth string >>>> " + userCred);
        return userCred;
    }
}
