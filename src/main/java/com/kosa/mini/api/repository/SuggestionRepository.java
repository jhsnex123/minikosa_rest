package com.kosa.mini.api.repository;


import com.kosa.mini.api.dto.admin.SuggestionDetailDTO;
import com.kosa.mini.api.entity.ContactUs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionRepository extends JpaRepository<ContactUs,Integer> {
    Page<ContactUs> findByTitleContaining(String title, Pageable pageable);
    Page<ContactUs> findByContentContaining(String content, Pageable pageable);
}
