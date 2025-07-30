package com.company.newseat.global.exception.handler;

import com.company.newseat.global.exception.GeneralException;
import com.company.newseat.global.exception.code.BaseErrorCode;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}