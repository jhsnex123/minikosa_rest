package com.kosa.mini.api.service.search;

import com.kosa.mini.api.dto.search.SearchDTO;
import com.kosa.mini.api.dto.search.SearchResultDTO;

public interface SearchApiService {
    SearchResultDTO searchStore(SearchDTO searchDTO);
}
