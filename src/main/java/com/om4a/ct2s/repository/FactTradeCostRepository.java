package com.om4a.ct2s.repository;

import com.om4a.ct2s.domain.FactTradeCost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the FactTradeCost entity.
 */
@Repository
public interface FactTradeCostRepository extends MongoRepository<FactTradeCost, String> {}
