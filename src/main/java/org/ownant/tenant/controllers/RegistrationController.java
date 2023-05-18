package org.ownant.tenant.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.ownant.tenant.data.TenantRepository;
import org.ownant.tenant.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class RegistrationController {

    private final String APARTMENT_SERVICE_ENDPOINT = "http://apartment-service:8082/ownant/apartment/add/tenant";

    @Autowired
    TenantRepository tenantRepository;

    @PostMapping("/tenant/register")
    public TenantRecord registerNewTenant(@RequestBody RegisterTenantRequest request) {
        if (tenantRepository.findAll().stream().anyMatch(r -> r.getUsername().equals(request.getUsername()))) {
            return TenantRecord.builder().error(ErrorCode.TENANT_USERNAME_ALREADY_PRESENT).build();
        } else if (tenantRepository.findAll().stream().anyMatch(r -> r.getEmail().equals(request.getEmail()))) {
            return TenantRecord.builder().error(ErrorCode.TENANT_EMAIL_ALREADY_PRESENT).build();
        } else if (request.getApartmentId().isBlank() || null == request.getTenantActivationCode()) {
             TenantRecord record = TenantRecord.builder()
                    .id(UUID.randomUUID().toString())
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .email(request.getEmail())
                    .name(request.getName())
                    .status(Status.INACTIVE)
                    .address(request.getAddress())
                    .build();
             tenantRepository.save(record);
             return record;
        } else {

            String tenantId = UUID.randomUUID().toString();
            TenantRecord record = TenantRecord.builder()
                    .id(tenantId)
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .email(request.getEmail())
                    .name(request.getName())
                    .address(request.getAddress())
                    .build();

            ResponseEntity enrollToApartment = enrollToApartment(tenantId, request.getApartmentId(), request.getTenantActivationCode());

            if (enrollToApartment.getStatusCodeValue() == 200) {
                record = record.toBuilder().status(Status.ACTIVE).build();

            } else {
                record = record.toBuilder().status(Status.INACTIVE).error(ErrorCode.INACTIVE_TENANT).build();
            }

            tenantRepository.save(record);
            return record;
        }
    }

    @PostMapping("/tenant/{tenantId}/apartment/{apartmentId}/enroll")
    public ResponseEntity enrollToApartment(@PathVariable(value = "tenantId") String tenantId,
                                            @PathVariable(value = "apartmentId") String apartmentId,
                                            @RequestBody Integer  activationCode) {
        HttpClient httpClient = HttpClients.createDefault();
        try {
            log.info("External call -  {}", APARTMENT_SERVICE_ENDPOINT);
            HttpPost httpPost = new HttpPost(APARTMENT_SERVICE_ENDPOINT);
            httpPost.addHeader("apartmentId", apartmentId);
            httpPost.addHeader("tenantActivationCode", String.valueOf(activationCode));
            httpPost.addHeader("tenantId", tenantId);


            HttpResponse httpResponse = httpClient.execute(httpPost);
            String jsonResponse = EntityUtils.toString(httpResponse.getEntity());

            ObjectMapper mapper = new ObjectMapper();
            EnrollToApartmentResponse response = mapper.readValue(jsonResponse, EnrollToApartmentResponse.class);
            if  (null == response.getError()) {
                return ResponseEntity.status(HttpStatus.SC_OK).body("Tenant Enrollment successful");
            } else {
                return ResponseEntity.status(HttpStatus.SC_EXPECTATION_FAILED).body("Tenant Enrollment unsuccessful");
            }
        } catch (Exception e) {
            log.error("Exception:"+e.getMessage());
            return ResponseEntity.status(HttpStatus.SC_EXPECTATION_FAILED).body("Tenant Enrollment unsuccessful");
        }


    }
}
