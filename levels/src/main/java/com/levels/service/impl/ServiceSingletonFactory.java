package com.levels.service.impl;

import com.levels.service.KeyGenerator;

public class ServiceSingletonFactory {

    private static final ServiceSingletonFactory INSTANCE = new ServiceSingletonFactory();
    private static final KeyGeneratorReasonablyUnique ID_GENERATOR_REASON_UNIQUE = new KeyGeneratorReasonablyUnique();

    private ServiceSingletonFactory() {

    }

    public static ServiceSingletonFactory getInstance() {
        return INSTANCE;
    }

    public KeyGenerator getIdGeneratorReasonablyUnique() {
        return ID_GENERATOR_REASON_UNIQUE;
    }

}
