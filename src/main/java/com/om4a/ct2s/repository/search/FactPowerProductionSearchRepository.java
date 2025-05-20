package com.om4a.ct2s.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.om4a.ct2s.domain.FactPowerProduction;
import com.om4a.ct2s.repository.FactPowerProductionRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link FactPowerProduction} entity.
 */
public interface FactPowerProductionSearchRepository
    extends ElasticsearchRepository<FactPowerProduction, String>, FactPowerProductionSearchRepositoryInternal {}

interface FactPowerProductionSearchRepositoryInternal {
    Stream<FactPowerProduction> search(String query);

    Stream<FactPowerProduction> search(Query query);

    @Async
    void index(FactPowerProduction entity);

    @Async
    void deleteFromIndexById(String id);
}

class FactPowerProductionSearchRepositoryInternalImpl implements FactPowerProductionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FactPowerProductionRepository repository;

    FactPowerProductionSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, FactPowerProductionRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<FactPowerProduction> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<FactPowerProduction> search(Query query) {
        return elasticsearchTemplate.search(query, FactPowerProduction.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(FactPowerProduction entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(String id) {
        elasticsearchTemplate.delete(String.valueOf(id), FactPowerProduction.class);
    }
}
