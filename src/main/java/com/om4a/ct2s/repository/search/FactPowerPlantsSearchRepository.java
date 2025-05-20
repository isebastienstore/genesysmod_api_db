package com.om4a.ct2s.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.om4a.ct2s.domain.FactPowerPlants;
import com.om4a.ct2s.repository.FactPowerPlantsRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link FactPowerPlants} entity.
 */
public interface FactPowerPlantsSearchRepository
    extends ElasticsearchRepository<FactPowerPlants, String>, FactPowerPlantsSearchRepositoryInternal {}

interface FactPowerPlantsSearchRepositoryInternal {
    Stream<FactPowerPlants> search(String query);

    Stream<FactPowerPlants> search(Query query);

    @Async
    void index(FactPowerPlants entity);

    @Async
    void deleteFromIndexById(String id);
}

class FactPowerPlantsSearchRepositoryInternalImpl implements FactPowerPlantsSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FactPowerPlantsRepository repository;

    FactPowerPlantsSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, FactPowerPlantsRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<FactPowerPlants> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<FactPowerPlants> search(Query query) {
        return elasticsearchTemplate.search(query, FactPowerPlants.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(FactPowerPlants entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(String id) {
        elasticsearchTemplate.delete(String.valueOf(id), FactPowerPlants.class);
    }
}
