package com.om4a.ct2s.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.om4a.ct2s.domain.Technology;
import com.om4a.ct2s.repository.TechnologyRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Technology} entity.
 */
public interface TechnologySearchRepository extends ElasticsearchRepository<Technology, String>, TechnologySearchRepositoryInternal {}

interface TechnologySearchRepositoryInternal {
    Stream<Technology> search(String query);

    Stream<Technology> search(Query query);

    @Async
    void index(Technology entity);

    @Async
    void deleteFromIndexById(String id);
}

class TechnologySearchRepositoryInternalImpl implements TechnologySearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TechnologyRepository repository;

    TechnologySearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TechnologyRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Technology> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Technology> search(Query query) {
        return elasticsearchTemplate.search(query, Technology.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Technology entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(String id) {
        elasticsearchTemplate.delete(String.valueOf(id), Technology.class);
    }
}
