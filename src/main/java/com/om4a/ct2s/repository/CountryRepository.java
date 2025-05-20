package com.om4a.ct2s.repository;

import com.om4a.ct2s.domain.Country;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Country entity.
 */
@Repository
public interface CountryRepository extends MongoRepository<Country, String> {}
