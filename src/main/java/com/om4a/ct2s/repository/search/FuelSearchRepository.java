package com.om4a.ct2s.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.om4a.ct2s.domain.Fuel;
import com.om4a.ct2s.repository.FuelRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Fuel} entity.
 */
public interface FuelSearchRepository extends ElasticsearchRepository<Fuel, String>, FuelSearchRepositoryInternal {}

interface FuelSearchRepositoryInternal {
    Stream<Fuel> search(String query);

    Stream<Fuel> search(Query query);

    @Async
    void index(Fuel entity);

    @Async
    void deleteFromIndexById(String id);
}

class FuelSearchRepositoryInternalImpl implements FuelSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FuelRepository repository;

    FuelSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, FuelRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Fuel> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Fuel> search(Query query) {
        return elasticsearchTemplate.search(query, Fuel.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Fuel entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(String id) {
        elasticsearchTemplate.delete(String.valueOf(id), Fuel.class);
    }
}
