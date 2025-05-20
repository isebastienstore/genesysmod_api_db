package com.om4a.ct2s.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.om4a.ct2s.domain.FactTradeCapacity;
import com.om4a.ct2s.repository.FactTradeCapacityRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link FactTradeCapacity} entity.
 */
public interface FactTradeCapacitySearchRepository
    extends ElasticsearchRepository<FactTradeCapacity, String>, FactTradeCapacitySearchRepositoryInternal {}

interface FactTradeCapacitySearchRepositoryInternal {
    Stream<FactTradeCapacity> search(String query);

    Stream<FactTradeCapacity> search(Query query);

    @Async
    void index(FactTradeCapacity entity);

    @Async
    void deleteFromIndexById(String id);
}

class FactTradeCapacitySearchRepositoryInternalImpl implements FactTradeCapacitySearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FactTradeCapacityRepository repository;

    FactTradeCapacitySearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, FactTradeCapacityRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<FactTradeCapacity> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<FactTradeCapacity> search(Query query) {
        return elasticsearchTemplate.search(query, FactTradeCapacity.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(FactTradeCapacity entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(String id) {
        elasticsearchTemplate.delete(String.valueOf(id), FactTradeCapacity.class);
    }
}
