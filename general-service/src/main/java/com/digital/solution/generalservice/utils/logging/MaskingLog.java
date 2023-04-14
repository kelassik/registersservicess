package com.digital.solution.generalservice.utils.logging;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.regex.Matcher;

import static com.digital.solution.generalservice.utils.logging.RegexConstant.LIST_PATTERN_MASKING_ALL;
import static com.digital.solution.generalservice.utils.logging.RegexConstant.LIST_PATTERN_MASKING_CARD_NUMBER;
import static com.digital.solution.generalservice.utils.logging.RegexConstant.LIST_PATTERN_MASKING_EMAIL;
import static com.digital.solution.generalservice.utils.logging.RegexConstant.LIST_PATTERN_MASKING_PHONE;
import static com.digital.solution.generalservice.utils.logging.RegexConstant.LIST_PATTERN_MASKING_SENSITIVE_INFORMATION;
import static com.digital.solution.generalservice.utils.logging.RegexConstant.REGEX_REPLACE_CARD_NUMBER;
import static com.digital.solution.generalservice.utils.logging.RegexConstant.REGEX_REPLACE_EMAIL;
import static com.digital.solution.generalservice.utils.logging.RegexConstant.REGEX_REPLACE_NAME;
import static com.digital.solution.generalservice.utils.logging.RegexConstant.REGEX_REPLACE_PHONE_NUMBER;

@SuppressWarnings("all")
public class MaskingLog extends PatternLayout {

    @Override
    public String doLayout(ILoggingEvent event) {
        return maskMessage(super.doLayout(event)); // calling superclass method is required
    }

    private String maskMessage(String message) {
        StringBuilder log = new StringBuilder(message);

        //all value is masked with stars(*)
        generateLogMaskingAllPattern(log);

        // phone value will be masking with the rule REGEX_REPLACE_PHONE_NUMBER
        generateLogMaskingPhone(log);

        // email value will be masking with the rule REGEX_REPLACE_EMAIL
        generateLogMaskingEmail(log);

        // card number will be masking with rule REGEX_REPLACE_CARD_NUMBER
        generateLogMaskingCardNumber(log);

        // sensitive information will be masking with rule REGEX_REPLACE_NAME
        generateLogMaskingSensitiveInformation(log);
        return log.toString();
    }

    private void generateLogMaskingAllPattern(StringBuilder log) {
        Matcher matcherAll = LIST_PATTERN_MASKING_ALL.matcher(log);
        while (matcherAll.find()) {
            for (int i = 1; i <= matcherAll.groupCount(); i++) {
                if (matcherAll.group(i) != null) {
                    log.replace(matcherAll.start(i), matcherAll.end(i), matcherAll.group(i).replace(".", "*"));
                }
            }
        }
    }

    private void generateLogMaskingPhone(StringBuilder log) {
        try {
            Matcher matcherPhone = LIST_PATTERN_MASKING_PHONE.matcher(log);
            while (matcherPhone.find()) {
                for (int i = 1; i <= matcherPhone.groupCount(); i++) {
                    if (matcherPhone.group(i) != null && !matcherPhone.group(i).equals("null")) {
                        log.replace(matcherPhone.start(i), matcherPhone.end(i), matcherPhone.group(i).replaceAll(REGEX_REPLACE_PHONE_NUMBER, "*"));
                    }
                }
            }
        } catch (Exception ignored) {
            //by pass error
        }
    }

    private void generateLogMaskingEmail(StringBuilder log) {
        try {
            Matcher matcherEmail = LIST_PATTERN_MASKING_EMAIL.matcher(log);
            while (matcherEmail.find()) {
                for (int i = 1; i <= matcherEmail.groupCount(); i++) {
                    if (matcherEmail.group(i) != null && !matcherEmail.group(i).equals("null")) {
                        log.replace(matcherEmail.start(i), matcherEmail.end(i), matcherEmail.group(i).replaceAll(REGEX_REPLACE_EMAIL, "*"));
                    }
                }
            }
        } catch (Exception ignored) {
            // by pass error
        }
    }

    private void generateLogMaskingCardNumber(StringBuilder log) {
        try {
            Matcher matcherCardNumber = LIST_PATTERN_MASKING_CARD_NUMBER.matcher(log);
            while (matcherCardNumber.find()) {
                for (int i = 1; i <= matcherCardNumber.groupCount(); i++) {
                    if (matcherCardNumber.group(i) != null && !matcherCardNumber.group(i).equals("null")) {
                        log.replace(matcherCardNumber.start(i), matcherCardNumber.end(i), matcherCardNumber.group(i).replaceAll(REGEX_REPLACE_CARD_NUMBER, "*"));
                    }
                }
            }
        } catch (Exception ignored) {
            // by pass error
        }
    }

    private void generateLogMaskingSensitiveInformation(StringBuilder log) {
        try {
            Matcher matcherSensitive = LIST_PATTERN_MASKING_SENSITIVE_INFORMATION.matcher(log);
            while (matcherSensitive.find()) {
                for (int i = 1; i <= matcherSensitive.groupCount(); i++) {
                    if (matcherSensitive.group(i) != null && !matcherSensitive.group(i).equals("null")) {
                        log.replace(matcherSensitive.start(i), matcherSensitive.end(i), matcherSensitive.group(i).replaceAll(REGEX_REPLACE_NAME, "*"));
                    }
                }
            }
        } catch (Exception ignored) {
            // by pass error
        }
    }
}
