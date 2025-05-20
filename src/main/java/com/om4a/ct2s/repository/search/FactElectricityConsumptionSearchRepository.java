package com.om4a.ct2s.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.om4a.ct2s.domain.FactElectricityConsumption;
import com.om4a.ct2s.repository.FactElectricityConsumptionRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link FactElectricityConsumption} entity.
 */
public interface FactElectricityConsumptionSearchRepository
    extends ElasticsearchRepository<FactElectricityConsumption, String>, FactElectricityConsumptionSearchRepositoryInternal {}

interface FactElectricityConsumptionSearchRepositoryInternal {
    Stream<FactElectricityConsumption> search(String query);

    Stream<FactElectricityConsumption> search(Query query);

    @Async
    void index(FactElectricityConsumption entity);

    @Async
    void deleteFromIndexById(String id);
}

class FactElectricityConsumptionSearchRepositoryInternalImpl implements FactElectricityConsumptionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FactElectricityConsumptionRepository repository;

    FactElectricityConsumptionSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        FactElectricityConsumptionRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<FactElectricityConsumption> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<FactElectricityConsumption> search(Query query) {
        return elasticsearchTemplate.search(query, FactElectricityConsumption.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(FactElectricityConsumption entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(String id) {
        elasticsearchTemplate.delete(String.valueOf(id), FactElectricityConsumption.class);
    }
}
