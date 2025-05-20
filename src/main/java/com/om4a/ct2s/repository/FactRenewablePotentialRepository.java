package com.om4a.ct2s.repository;

import com.om4a.ct2s.domain.FactRenewablePotential;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the FactRenewablePotential entity.
 */
@Repository
public interface FactRenewablePotentialRepository extends MongoRepository<FactRenewablePotential, String> {}
