package io.tofts.imagefilter.repository;

import io.tofts.imagefilter.models.imageformatmodel.JpgElasticSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MetadataElasticSearchRepository extends ElasticsearchRepository<JpgElasticSearch,String> {


}
