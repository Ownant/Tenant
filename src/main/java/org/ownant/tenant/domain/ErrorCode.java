package org.ownant.tenant.domain;

public enum ErrorCode {
    TENANT_EMAIL_ALREADY_PRESENT("Tenant with given email address already registered", 40901),
    TENANT_USERNAME_ALREADY_PRESENT("Tenant with given username address already registered", 40902),
    INVALID_REQUEST("Request does not contain required information", 40001),
    INACTIVE_TENANT("Processed with errors. Error  while  activating the tenant. Please enroll with valid apartment id and tenant activation code ", 40002);;

    String message;
    int code;

    ErrorCode(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
