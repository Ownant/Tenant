package org.ownant.tenant.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Year;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document("Rent_Data")
public class RentRecord {
    String tenantId;
    Integer amountPaid;
    Month forMonth;
    Integer inYear;
    ErrorCode error;
}
