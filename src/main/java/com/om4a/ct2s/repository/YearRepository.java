package com.om4a.ct2s.repository;

import com.om4a.ct2s.domain.Year;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Year entity.
 */
@Repository
public interface YearRepository extends MongoRepository<Year, String> {}
