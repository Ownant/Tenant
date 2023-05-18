package org.ownant.tenant.data;

import org.ownant.tenant.domain.TenantRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface TenantRepository extends MongoRepository<TenantRecord, String> {
}
