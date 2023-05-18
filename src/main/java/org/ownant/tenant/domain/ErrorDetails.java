package org.ownant.tenant.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDetails {
    String message;
    Integer code;
}
