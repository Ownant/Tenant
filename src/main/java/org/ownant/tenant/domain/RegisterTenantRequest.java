package org.ownant.tenant.domain;

import lombok.Data;

@Data
public class RegisterTenantRequest {
    String name;
    String username;
    String password;
    String email;
    Address address;
    String apartmentId;
    Integer tenantActivationCode;
}
