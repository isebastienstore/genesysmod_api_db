package com.om4a.ct2s.repository;

import com.om4a.ct2s.domain.FactTransport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the FactTransport entity.
 */
@Repository
public interface FactTransportRepository extends MongoRepository<FactTransport, String> {}
