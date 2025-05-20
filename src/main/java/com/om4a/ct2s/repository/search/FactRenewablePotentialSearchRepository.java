package com.om4a.ct2s.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.om4a.ct2s.domain.FactRenewablePotential;
import com.om4a.ct2s.repository.FactRenewablePotentialRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link FactRenewablePotential} entity.
 */
public interface FactRenewablePotentialSearchRepository
    extends ElasticsearchRepository<FactRenewablePotential, String>, FactRenewablePotentialSearchRepositoryInternal {}

interface FactRenewablePotentialSearchRepositoryInternal {
    Stream<FactRenewablePotential> search(String query);

    Stream<FactRenewablePotential> search(Query query);

    @Async
    void index(FactRenewablePotential entity);

    @Async
    void deleteFromIndexById(String id);
}

class FactRenewablePotentialSearchRepositoryInternalImpl implements FactRenewablePotentialSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FactRenewablePotentialRepository repository;

    FactRenewablePotentialSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        FactRenewablePotentialRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<FactRenewablePotential> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<FactRenewablePotential> search(Query query) {
        return elasticsearchTemplate.search(query, FactRenewablePotential.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(FactRenewablePotential entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(String id) {
        elasticsearchTemplate.delete(String.valueOf(id), FactRenewablePotential.class);
    }
}
