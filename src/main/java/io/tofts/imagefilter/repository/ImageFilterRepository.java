package io.tofts.imagefilter.repository;

import io.tofts.imagefilter.models.imageformatmodel.Jpg;
import org.springframework.data.repository.CrudRepository;


public interface ImageFilterRepository extends CrudRepository<Jpg,String> {

}
