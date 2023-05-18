package org.ownant.tenant.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document("Tenant_Data")
public class TenantRecord {
    @Id
    String id;
    String name;
    Address address;
    Status status;
    String username;
    String password;
    String email;
    ErrorCode error;
}
