package com.om4a.ct2s.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.om4a.ct2s.domain.FactTransport;
import com.om4a.ct2s.repository.FactTransportRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link FactTransport} entity.
 */
public interface FactTransportSearchRepository
    extends ElasticsearchRepository<FactTransport, String>, FactTransportSearchRepositoryInternal {}

interface FactTransportSearchRepositoryInternal {
    Stream<FactTransport> search(String query);

    Stream<FactTransport> search(Query query);

    @Async
    void index(FactTransport entity);

    @Async
    void deleteFromIndexById(String id);
}

class FactTransportSearchRepositoryInternalImpl implements FactTransportSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FactTransportRepository repository;

    FactTransportSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, FactTransportRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<FactTransport> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<FactTransport> search(Query query) {
        return elasticsearchTemplate.search(query, FactTransport.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(FactTransport entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(String id) {
        elasticsearchTemplate.delete(String.valueOf(id), FactTransport.class);
    }
}
