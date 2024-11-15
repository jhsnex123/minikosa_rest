package com.kosa.mini.api.service.home;

import com.kosa.mini.api.dto.home.HomeStoreDTO;
import com.kosa.mini.api.repository.HomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeApiApiServiceImpl implements HomeApiService {

    @Autowired
    HomeRepository homeRepository;

    public List<HomeStoreDTO> home() {
        return homeRepository.index();
    }
}
