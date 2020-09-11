package io.tofts.imagefilter.repository;

import io.tofts.imagefilter.models.imageformatmodel.Jpg;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageFilterRepository extends CrudRepository<Jpg,String> {

}
