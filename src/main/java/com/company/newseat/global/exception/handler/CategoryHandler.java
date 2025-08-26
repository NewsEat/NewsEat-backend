package com.company.newseat.global.exception.handler;

import com.company.newseat.global.exception.GeneralException;
import com.company.newseat.global.exception.code.BaseErrorCode;

public class CategoryHandler extends GeneralException {
    public CategoryHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
