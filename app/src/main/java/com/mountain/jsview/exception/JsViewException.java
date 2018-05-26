package com.mountain.jsview.exception;

public class JsViewException extends RuntimeException {
    public JsViewException() {
    }

    public JsViewException(String detailMessage) {
        super(detailMessage);
    }

    public JsViewException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public JsViewException(Throwable throwable) {
        super(throwable);
    }
}
