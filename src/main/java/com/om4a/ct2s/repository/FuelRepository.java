package com.om4a.ct2s.repository;

import com.om4a.ct2s.domain.Fuel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Fuel entity.
 */
@Repository
public interface FuelRepository extends MongoRepository<Fuel, String> {}
