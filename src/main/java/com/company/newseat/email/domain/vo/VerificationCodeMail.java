package com.company.newseat.email.domain.vo;

import java.util.Random;

import static com.company.newseat.email.util.MailConstants.MAIL_TEMPLATE_CODE;
import static com.company.newseat.email.util.MailConstants.MAIL_TITLE_CODE;
import static com.company.newseat.email.util.MailConstants.KEY_CODE;

public class VerificationCodeMail extends Mail {
    public static final long CODE_VALID_DURATION_SECONDS = 60 * 3L;

    public VerificationCodeMail() {
        super(MAIL_TEMPLATE_CODE, MAIL_TITLE_CODE);
        super.values.put(KEY_CODE, generateCode());
    }

    private String generateCode() {
        final int CODE_LENGTH = 6;
        StringBuilder codeBuilder = new StringBuilder(CODE_LENGTH);
        Random random = new Random();
        for (int i = 0; i < CODE_LENGTH; i++) {
            codeBuilder.append(random.nextInt(10));
        }
        return codeBuilder.toString();
    }
}
