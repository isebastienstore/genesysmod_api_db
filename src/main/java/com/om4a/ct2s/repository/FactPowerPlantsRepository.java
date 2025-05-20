package com.om4a.ct2s.repository;

import com.om4a.ct2s.domain.FactPowerPlants;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the FactPowerPlants entity.
 */
@Repository
public interface FactPowerPlantsRepository extends MongoRepository<FactPowerPlants, String> {}
