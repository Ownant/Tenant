package org.ownant.tenant.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrollToApartmentResponse {
    String apartmentId;
    String tenantActivationCode;
    String ownerId;
    String tenantId;
    Integer rentAmount;
    Address address;
    ErrorDetails error;
}
