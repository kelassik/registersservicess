package com.digital.solution.generalservice.utils.logging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class RegexConstant {

    RegexConstant(){}

    public static final String REGEX_REPLACE_EMAIL = "(?<=.{2}).(?=[^@]*?.@)";

    public static final String REGEX_REPLACE_PHONE_NUMBER = "(?<=.{5}).(?=.{4})";

    public static final String REGEX_REPLACE_CARD_NUMBER = "(?<=.{6}).(?=.{4})";

    public static final String REGEX_REPLACE_NAME = "\\b(?i)[a-z]{2}";

    private static final ArrayList<String> LIST_REGEX_ALL = new ArrayList<>(Arrays.asList(
            "\"?dateOfBirth\"?\\s*[=:]\\s*\"?'?(.*?)[,\")}']",
            "\"?placeOfBirth\"?\\s*[=:]\\s*\"?'?(.*?)[,\")}']",
            "\"?username\"?\\s*[=:]\\s*\"?'?(.*?)[,\")}']",
            "\"?newPassword\"?\\s*[=:]\\s*\"?'?(.*?)[,\")}']",
            "\"?newUserId\"?\\s*[=:]\\s*\"?'?(.*?)[,\")}']",
            "\"?password\"?\\s*[=:]\\s*\"?'?(.*?)[,\")}']",
            "\"?secureWord\"?\\s*[=:]\\s*\"?'?(.*?)[,\")}']",
//            "\"?cimbConsumerId\"?\\s*[=:]\\s*\"?'?(.*?)[,\")}']",
//            "\"?cimbConsumerPasswd\"?\\s*[=:]\\s*\"?'?(.*?)[,\")}']",
            "\"?expiredDate\"?\\s*[=:]\\s*\"?'?(.*?)[,\")}']",
            "\"?Authorization\"?\\s*[=:]\\s*\"?'?\\[?Bearer (.*?)[,\")}'\\]]",
            "\"?access_token\"?\\s*[=:]\\s*\\[(.*?)]"
//            "<ns2:CIMB_ConsumerId>(.*?)<\\/ns2:CIMB_ConsumerId>",
//            "<ns2:CIMB_ConsumerPasswd>(.*?)<\\/ns2:CIMB_ConsumerPasswd>",
//            "<ns2:CardEmbossNum>(.*?)<\\/ns2:CardEmbossNum>",
//            "<esb:CIMB_ConsumerId>(.*?)<\\/esb:CIMB_ConsumerId>",
//            "<esb:CIMB_ConsumerPasswd>(.*?)<\\/esb:CIMB_ConsumerPasswd>",
//            "<esb:CardEmbossNum>(.*?)<\\/esb:CardEmbossNum>"
    ));

    private static final ArrayList<String> LIST_PHONE_REGEX = new ArrayList<>(Arrays.asList(
            "\"?mobilePhone\"?\\s*[=:]\\s*\"?'?(.*?)[,\")}']",
            "\"?unmaskedPhone\"?\\s*[=:]\\s*\"?'?(.*?)[,\")}']"
    ));

    private static final ArrayList<String> LIST_EMAIL_REGEX = new ArrayList<>(Arrays.asList(
            "\"?email\"?\\s*[=:]\\s*\"?'?(.*?)[,\")}']",
            "\"?emailaddress\"?\\s*[=:]\\s*\"?'?(.*?)[,\")}']"
    ));

    private static final ArrayList<String> LIST_REGEX_SENSITIVE_INFORMATION = new ArrayList<>(Arrays.asList(
            "\"?nameFull\"?\\s*[=:]\\s*\"?'?(.*?)[,\")}']",
            "\"?nameShort\"?\\s*[=:]\\s*\"?'?(.*?)[,\")}']"
    ));

    private static final ArrayList<String> LIST_REGEX_CARD_NUMBER = new ArrayList<>(List.of(
            "\"?cardNumber\"?\\s*[=:]\\s*\"?(.*?)[,\")]"
    ));

    public static final Pattern LIST_PATTERN_MASKING_ALL = Pattern.compile(String.join("|", LIST_REGEX_ALL), Pattern.MULTILINE);

    public static final Pattern LIST_PATTERN_MASKING_PHONE = Pattern.compile(String.join("|", LIST_PHONE_REGEX), Pattern.MULTILINE);

    public static final Pattern LIST_PATTERN_MASKING_EMAIL = Pattern.compile(String.join("|", LIST_EMAIL_REGEX), Pattern.MULTILINE);

    public static final Pattern LIST_PATTERN_MASKING_CARD_NUMBER = Pattern.compile(String.join("|", LIST_REGEX_CARD_NUMBER), Pattern.MULTILINE);

    public static final Pattern LIST_PATTERN_MASKING_SENSITIVE_INFORMATION = Pattern.compile(String.join("|", LIST_REGEX_SENSITIVE_INFORMATION), Pattern.MULTILINE);
}
