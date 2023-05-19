package org.ownant.tenant.data;

import org.ownant.tenant.domain.RentRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface RentHistory extends MongoRepository<RentRecord, String> {
}
