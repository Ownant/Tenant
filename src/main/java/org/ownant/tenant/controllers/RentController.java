package org.ownant.tenant.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.ownant.tenant.data.RentHistory;
import org.ownant.tenant.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RentController {
    private final String APARTMENT_SERVICE_ENDPOINT = "http://apartment-service:8082/ownant/apartment/get";

    @Autowired
    RentHistory rentHistory;

    @PostMapping("/tenant/payRent")
    public RentRecord payRent(@RequestBody PayRentRequest request) {

        try {
            if (rentHistory.findAll().stream().anyMatch(r -> r.getTenantId().equals(request.getTenantId())
                    && r.getForMonth().equals(request.getRentMonth())
                    && r.getInYear().equals(request.getRentYear()))) {
                return RentRecord.builder().error(ErrorCode.RENT_ALREADY_PAID).build();
            }

            RentRecord record = RentRecord.builder()
                    .forMonth(request.getRentMonth())
                    .inYear(request.getRentYear())
                    .amountPaid(request.getAmount())
                    .tenantId(request.getTenantId())
                    .build();
            rentHistory.save(record);
            return record;



        } catch (Exception e) {
            return RentRecord.builder().error(ErrorCode.INVALID_REQUEST).build();
        }

    }

    private Integer getRentForTheApartment(String tenantId) {
        HttpClient httpClient = HttpClients.createDefault();
        try {
            log.info("External call -  {}", APARTMENT_SERVICE_ENDPOINT);
            HttpGet httpGet = new HttpGet(APARTMENT_SERVICE_ENDPOINT);
            httpGet.addHeader("tenantId", tenantId);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            String jsonResponse = EntityUtils.toString(httpResponse.getEntity());

            ObjectMapper mapper = new ObjectMapper();
            EnrollToApartmentResponse response = mapper.readValue(jsonResponse, EnrollToApartmentResponse.class);
            if  (null == response.getError()) {
                return -1;
            } else {
                return response.getRentAmount();
            }
        } catch (Exception e) {
            log.error("Exception:"+e.getMessage());
            return -1;
        }


    }
}
