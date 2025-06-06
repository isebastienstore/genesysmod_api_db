package com.om4a.ct2s.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.om4a.ct2s.domain.Year;
import com.om4a.ct2s.repository.YearRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Year} entity.
 */
public interface YearSearchRepository extends ElasticsearchRepository<Year, String>, YearSearchRepositoryInternal {}

interface YearSearchRepositoryInternal {
    Stream<Year> search(String query);

    Stream<Year> search(Query query);

    @Async
    void index(Year entity);

    @Async
    void deleteFromIndexById(String id);
}

class YearSearchRepositoryInternalImpl implements YearSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final YearRepository repository;

    YearSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, YearRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Year> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Year> search(Query query) {
        return elasticsearchTemplate.search(query, Year.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Year entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(String id) {
        elasticsearchTemplate.delete(String.valueOf(id), Year.class);
    }
}
