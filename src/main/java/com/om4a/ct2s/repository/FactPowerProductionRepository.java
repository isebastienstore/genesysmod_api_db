package com.om4a.ct2s.repository;

import com.om4a.ct2s.domain.FactPowerProduction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the FactPowerProduction entity.
 */
@Repository
public interface FactPowerProductionRepository extends MongoRepository<FactPowerProduction, String> {}
