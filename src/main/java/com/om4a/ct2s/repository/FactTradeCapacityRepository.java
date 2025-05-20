package com.om4a.ct2s.repository;

import com.om4a.ct2s.domain.FactTradeCapacity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the FactTradeCapacity entity.
 */
@Repository
public interface FactTradeCapacityRepository extends MongoRepository<FactTradeCapacity, String> {}
