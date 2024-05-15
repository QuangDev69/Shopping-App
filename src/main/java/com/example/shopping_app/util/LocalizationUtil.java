package com.example.shopping_app.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LocalizationUtil {
    private final MessageSource messageSource;


    public String setLocaleMessage(String messageKey, Object ...params) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageKey, params, locale);
    }
}
