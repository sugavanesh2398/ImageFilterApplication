package io.tofts.imagefilter.repository;

import com.drew.lang.annotations.Nullable;
import io.tofts.imagefilter.models.imageformatmodel.Jpg;
import io.tofts.imagefilter.models.searchmodel.JpgSearchModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImageFilterRepository extends JpaRepository<Jpg, String> {

    @Transactional
    @Modifying
    @Query(value = "Delete from jpg where user_name=:username", nativeQuery = true)
    void deleteByUserName(@Param("username") String username);

}
