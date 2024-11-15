package com.kosa.mini.api.service.search;

import com.kosa.mini.api.dto.search.SearchDTO;
import com.kosa.mini.api.dto.search.SearchResultDTO;
import com.kosa.mini.api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchApiServiceImpl implements SearchApiService {
    private final StoreRepository storeRepository;

    @Override
    public SearchResultDTO searchStore(SearchDTO searchDTO) {
        SearchResultDTO searchResultDTO = new SearchResultDTO();
        searchResultDTO.setQuery(searchDTO.getQuery());


        return null;
    }
}
