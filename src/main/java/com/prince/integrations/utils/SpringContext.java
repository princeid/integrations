package com.prince.integrations.utils;


import com.prince.integrations.exceptions.ApplicationException;
import com.prince.integrations.exceptions.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(SpringContext.class);

    private static ApplicationContext context;

    public static <T> T getBean(Class<T> beanClass) {

        if (context == null) {
            logger.error("Application context is not fully booted");
            throw new ApplicationException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }

        return context.getBean(beanClass);
    }

    public static <T> T getBean(Class<T> beanClass, String qualifier) {

        if (context == null) {
            logger.error("Application context is not fully booted");
            throw new ApplicationException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }

        return BeanFactoryAnnotationUtils.qualifiedBeanOfType(context.getAutowireCapableBeanFactory(), beanClass, qualifier);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
