package org.ownant.tenant.domain;

import lombok.Data;

import java.time.Year;

@Data
public class PayRentRequest {
    String tenantId;
    Integer amount;
    Month rentMonth;
    Integer rentYear;
}
