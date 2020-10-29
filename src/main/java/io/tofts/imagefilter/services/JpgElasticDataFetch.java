package io.tofts.imagefilter.services;

import io.tofts.imagefilter.models.imageformatmodel.JpgElasticSearch;
import io.tofts.imagefilter.models.searchmodel.JpgSearchModels;
import io.tofts.imagefilter.repository.MetadataElasticSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class JpgElasticDataFetch {
    @Autowired
    MetadataElasticSearchRepository metadataElasticSearchRepository;

    public List<JpgElasticSearch> getResultList(JpgSearchModels name){
        BoolQueryBuilder query= QueryBuilders.boolQuery();
        log.info(String.valueOf(name.getIsoMax()));
        if(name.getIsoMin()>0) {
            query.filter(QueryBuilders.rangeQuery("iso").gte(name.getIsoMin()));
        }
        if(name.getIsoMax()>0){
            query.filter(QueryBuilders.rangeQuery("iso").lte(name.getIsoMax()));
        }
        if(name.getFocalLengthMin()>0) {
            query.filter(QueryBuilders.rangeQuery("focalLength").gte(name.getFocalLengthMin()));
        }
        if(name.getFocalLengthMax()>0) {
            query.filter(QueryBuilders.rangeQuery("focalLength").lte(name.getFocalLengthMax()));
        }
        if(name.getFilesizeMax()>0) {
            query.filter(QueryBuilders.rangeQuery("fileSize").lte(name.getFilesizeMax()));
        }
        if(name.getFileSizeMin()>0) {
            query.filter(QueryBuilders.rangeQuery("fileSize").gte(name.getFileSizeMin()));
        }
        if(name.getImageHeightMax()>0) {
            query.filter(QueryBuilders.rangeQuery("imageHeight").lte(name.getImageHeightMax()));
        }
        if(name.getImageHeightMin()>0) {
            query.filter(QueryBuilders.rangeQuery("imageHeight").gte(name.getImageHeightMin()));
        }


        if(name.getImageWidthMax()>0) {
            query.filter(QueryBuilders.rangeQuery("imageWidth").lte(name.getImageWidthMax()));
        }
        if(name.getImageWidthMin()>0) {
            query.filter(QueryBuilders.rangeQuery("imageWidth").gte(name.getImageWidthMin()));
        }

        if(!StringUtils.isEmpty(name.getMake())) {
            query.filter(QueryBuilders.termQuery("make", name.getMake()));
        }
        if(!StringUtils.isEmpty(name.getModel())) {
            query.filter(QueryBuilders.termQuery("model", name.getModel()));
        }
        if(!StringUtils.isEmpty(name.getShutterSpeed())) {
            query.filter(QueryBuilders.termQuery("shutterSpeed", name.getShutterSpeed()));
        }
        if(!StringUtils.isEmpty(name.getAperture())) {
            query.filter(QueryBuilders.termQuery("aperture", name.getAperture()));
        }
        if(!StringUtils.isEmpty(name.getFNUmber())) {
            query.filter(QueryBuilders.termQuery("filename", name.getFNUmber()));
        }
        if(!StringUtils.isEmpty(name.getFilename())) {
            query.filter(QueryBuilders.termQuery("filename", name.getFilename()));
        }
        BoolQueryBuilder bb=QueryBuilders.boolQuery()
                .filter(query);

            Iterable<JpgElasticSearch> jpges = metadataElasticSearchRepository.search(bb);
            List<JpgElasticSearch> jj=new ArrayList<>();
            jpges.forEach(jpgElasticSearch1 -> {
                log.info(String.valueOf(jpgElasticSearch1));
                jj.add(jpgElasticSearch1);
            });
            log.info(jj.toString());

            return jj;

    }
}
