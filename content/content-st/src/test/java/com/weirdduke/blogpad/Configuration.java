package com.weirdduke.blogpad;

import org.eclipse.microprofile.config.ConfigProvider;

import java.net.URI;

public interface Configuration {

    static String getValue(String key) {
        var provider = ConfigProvider.getConfig();
        return provider.getValue(key, String.class);
    }

    static boolean getBooleanValue(String key) {
        var provider = ConfigProvider.getConfig();
        return provider
                .getOptionalValue(key, Boolean.class)
                .orElse(false);
    }
}
