package com.ixtx.projectmoviesq.enums.regexp;

public enum RegExp implements Regex {
    EMAIL("^(?=.{10,50}$)([\\da-zA-Z\\-_\\.]{5,25})@([\\da-z][\\da-z\\-]*[\\da-z]\\.)?([\\da-z][\\da-z\\-]*[\\da-z])\\.([a-z]{2,15})(\\.[a-z]{2})?$"),
    PASSWORD("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$"),
    CONTACT("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$"),
    NAME("^[가-힣]{2,4}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$"),
    BIRTHDAY("^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$"),
    DATE_BIRTH("^(\\d{2})(\\d{2})(\\d{2})$"),
    CODE("^[0-9]{6}$"),
    SALT("^([\\da-f]{128})$"),
    CARD_NUMBER("^[0-9]{16}$"),
    CARD_PASSWORD("^[0-9]{2}$");

    private final String exp;

    RegExp(String exp) {
        this.exp = exp;
    }

    @Override
    public boolean matches(String input) {
        return input != null && input.matches(this.exp);
    }

}
