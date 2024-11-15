package com.kosa.mini.api.dto.search;

import com.kosa.mini.api.dto.home.HomeStoreDTO;
import com.kosa.mini.mvc.domain.store.StoreReviewDTO;
import lombok.Data;

import java.util.List;

@Data
public class SearchDTO {
    private String query;
    private String sort = "latest";
    private String type = "store";
}
