package com.company.newseat.global.exception.handler;

import com.company.newseat.global.exception.GeneralException;
import com.company.newseat.global.exception.code.BaseErrorCode;

public class NewsHandler extends GeneralException {

    public NewsHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
