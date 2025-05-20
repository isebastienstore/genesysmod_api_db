package com.om4a.ct2s.repository;

import com.om4a.ct2s.domain.FactElectricityConsumption;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the FactElectricityConsumption entity.
 */
@Repository
public interface FactElectricityConsumptionRepository extends MongoRepository<FactElectricityConsumption, String> {}
