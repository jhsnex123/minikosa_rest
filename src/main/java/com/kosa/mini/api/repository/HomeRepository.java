package com.kosa.mini.api.repository;

import com.kosa.mini.api.dto.home.HomeStoreDTO;
import com.kosa.mini.api.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HomeRepository extends JpaRepository<Store, Integer> {

    @Query("select new com.kosa.mini.api.dto.home.HomeStoreDTO(" +
            "   s.storeId, " +
            "   s.storeName, " +
            "   s.storePhoto, " +
            "   s.storeDescription, " +
            "   round(avg(r.rating),1) as ratingAvg, " +
            "   c.categoryName ) " +
            "from " +
            "   Store s " +
            "left join " +
            "   s.reviews r " +
            "left join " +
            "   s.category c " +
            "group by " +
            "   s.storeId")
    List<HomeStoreDTO> index();
}
