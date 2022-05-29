package com.perseus.userservice.exception;

public enum ExceptionMessagesEnum {

    CONTACT_ID_SHOULD_BE_NULL("A new contact cannot already have an ID"),
    NOT_FOUND_CONTACT_BY_ID("Not found contact by id : "),
    EMAIL_ID_SHOULD_BE_NULL("A new email cannot already have an ID"),
    NUMBER_ID_SHOULD_BE_NULL("A new number cannot already have an ID"),
    NOT_FOUND_NUMBER_BY_ID("Not found number by id : "),
    NOT_FOUND_EMAIL_BY_ID("Not found email by id : "),
    EMAIL_FORMAT_IS_INVALID("Email format is invalid : "),
    NUMBER_FORMAT_IS_INVALID("Number format is invalid : ");


    private final String message;

    ExceptionMessagesEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
