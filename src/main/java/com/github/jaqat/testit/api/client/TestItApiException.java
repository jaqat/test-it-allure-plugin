package com.github.jaqat.testit.api.client;

class TestItApiException extends RuntimeException {

    TestItApiException(String message) {
        super(message);
    }

    TestItApiException(String message, Throwable e) {
        super(message, e);
    }


}
