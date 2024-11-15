package com.kosa.mini.api.controller.search;

import com.kosa.mini.api.dto.search.SearchDTO;
import com.kosa.mini.api.dto.search.SearchResultDTO;
import com.kosa.mini.api.service.search.SearchApiService;
import com.kosa.mini.mvc.service.search.SearchService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SearchApiController {

    private final SearchApiService searchApiService;

    /*
        // 가게 검색 - 동적 정렬 적용
    @Select("SELECT " +
            "s.store_id, " +
            "s.store_name, " +
            "s.store_photo, " +
            "s.store_description, " +
            "ROUND(AVG(r.rating), 1) AS ratingAvg, " +
            "c.category_name " +
            "FROM stores s " +
            "LEFT JOIN reviews r ON s.store_id = r.store_id " +
            "LEFT JOIN categories c ON s.category_id = c.category_id " +
            "WHERE s.store_name LIKE CONCAT('%', #{query}, '%') " +
            "OR s.store_description LIKE CONCAT('%', #{query}, '%') " +
            "GROUP BY s.store_id " +
            "ORDER BY ${sort}")
    List<StoreDTO> searchStores(@Param("query") String query, @Param("sort") String sort);

     */

    @GetMapping("/search")
    public ResponseEntity<?> searchStore(@RequestBody SearchDTO searchDTO) {
        SearchResultDTO searchResultDTO = searchApiService.searchStore(searchDTO);

        return null;
    }
}
