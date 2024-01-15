package com.relax.relax.common.exceptions.proxy;

public class ProxyInjectionException extends ProxyException {
    public ProxyInjectionException() {
    }

    public ProxyInjectionException(String message) {
        super(message);
    }

    public ProxyInjectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyInjectionException(Throwable cause) {
        super(cause);
    }

    public ProxyInjectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
