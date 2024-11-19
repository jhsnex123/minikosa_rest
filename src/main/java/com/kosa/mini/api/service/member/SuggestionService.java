package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.admin.SuggestionDetailDTO;
import com.kosa.mini.api.dto.member.ContactUsDTO;
import com.kosa.mini.api.entity.ContactUs;
import com.kosa.mini.api.exception.ResourceNotFoundException;
import com.kosa.mini.api.exception.StoreNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SuggestionService {
    ContactUs create(ContactUsDTO dto);
    Page<ContactUs> getSuggestions(String type, String keyword, Pageable pageable);
    void incrementViews(Integer contactId) throws ResourceNotFoundException;
    SuggestionDetailDTO getSuggestionById(Integer contactId) throws ResourceNotFoundException;
    void deleteSuggestion(Integer contactId) throws ResourceNotFoundException;
}
