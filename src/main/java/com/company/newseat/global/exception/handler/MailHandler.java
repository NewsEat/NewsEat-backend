package com.company.newseat.global.exception.handler;

import com.company.newseat.global.exception.GeneralException;
import com.company.newseat.global.exception.code.BaseErrorCode;

public class MailHandler extends GeneralException {

    public MailHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
