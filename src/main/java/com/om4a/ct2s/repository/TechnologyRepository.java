package com.om4a.ct2s.repository;

import com.om4a.ct2s.domain.Technology;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Technology entity.
 */
@Repository
public interface TechnologyRepository extends MongoRepository<Technology, String> {
    Optional<Technology> findByName(String name);
}
