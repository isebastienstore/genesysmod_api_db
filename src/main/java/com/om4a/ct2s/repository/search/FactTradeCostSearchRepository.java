package com.om4a.ct2s.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.om4a.ct2s.domain.FactTradeCost;
import com.om4a.ct2s.repository.FactTradeCostRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link FactTradeCost} entity.
 */
public interface FactTradeCostSearchRepository
    extends ElasticsearchRepository<FactTradeCost, String>, FactTradeCostSearchRepositoryInternal {}

interface FactTradeCostSearchRepositoryInternal {
    Stream<FactTradeCost> search(String query);

    Stream<FactTradeCost> search(Query query);

    @Async
    void index(FactTradeCost entity);

    @Async
    void deleteFromIndexById(String id);
}

class FactTradeCostSearchRepositoryInternalImpl implements FactTradeCostSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FactTradeCostRepository repository;

    FactTradeCostSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, FactTradeCostRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<FactTradeCost> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<FactTradeCost> search(Query query) {
        return elasticsearchTemplate.search(query, FactTradeCost.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(FactTradeCost entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(String id) {
        elasticsearchTemplate.delete(String.valueOf(id), FactTradeCost.class);
    }
}
