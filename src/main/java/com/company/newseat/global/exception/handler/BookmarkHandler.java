package com.company.newseat.global.exception.handler;

import com.company.newseat.global.exception.GeneralException;
import com.company.newseat.global.exception.code.BaseErrorCode;

public class BookmarkHandler extends GeneralException {
    public BookmarkHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
