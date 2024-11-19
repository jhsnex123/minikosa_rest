package com.kosa.mini.api.controller.admin;

import com.kosa.mini.api.dto.admin.SuggestionDetailDTO;
import com.kosa.mini.api.dto.member.UserSearchDTO;
import com.kosa.mini.api.dto.request.AssignOwnerRequest;
import com.kosa.mini.api.dto.request.StoreExistenceRequest;
import com.kosa.mini.api.dto.request.UserExistenceRequest;
import com.kosa.mini.api.dto.response.AssignOwnerResponse;
import com.kosa.mini.api.dto.store.*;
import com.kosa.mini.api.entity.ContactUs;
import com.kosa.mini.api.exception.FileStorageException;
import com.kosa.mini.api.exception.ResourceNotFoundException;
import com.kosa.mini.api.exception.StoreNotFoundException;
import com.kosa.mini.api.service.member.SuggestionService;
import com.kosa.mini.api.service.menu.MenuApiService;
import com.kosa.mini.api.service.store.StoreApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/v1/admin")
@RestController
public class AdminApiController {

    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private StoreApiService storeApiService;
    @Autowired
    private MenuApiService menuService;

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/suggestion/list")
    public ResponseEntity<Page<ContactUs>> getListSuggestion(@AuthenticationPrincipal UserDetails userDetails,
                                                             @RequestParam(name = "type", defaultValue = "title") String type,
                                                             @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "5") int size) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer memberId;
        try {
            memberId = Integer.valueOf(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ContactUs> suggestions = suggestionService.getSuggestions(type, keyword, pageable);

        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/suggestion/{contactId}")
    public ResponseEntity<SuggestionDetailDTO> getSuggestion(@AuthenticationPrincipal UserDetails userDetails,
                                                   @PathVariable Integer contactId) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer memberId;
        try {
            memberId = Integer.valueOf(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            suggestionService.incrementViews(contactId);
            SuggestionDetailDTO suggestion = suggestionService.getSuggestionById(contactId);
            return ResponseEntity.ok(suggestion);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/suggestion/{contactId}")
    public ResponseEntity<Void> deleteSuggestion(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer contactId) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer memberId;
        try {
            memberId = Integer.valueOf(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            suggestionService.deleteSuggestion(contactId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/stores/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Integer id) {
        try {
            storeApiService.deleteStore(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (StoreNotFoundException ex) {
            return ResponseEntity.status(404).build(); // 404 Not Found
        }
    }

    // 사용자 검색 API
    @GetMapping("/search/users")
    public ResponseEntity<List<UserSearchDTO>> searchUsers(@AuthenticationPrincipal UserDetails userDetails,
                                                           @RequestParam String email) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer memberId;
        try {
            memberId = Integer.valueOf(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<UserSearchDTO> users = storeApiService.searchUsersByEmail(email);
        return ResponseEntity.ok(users);
    }

    // 가게 검색 API
    @GetMapping("/search/stores")
    public ResponseEntity<List<StoreSearchDTO>> searchStores(@AuthenticationPrincipal UserDetails userDetails,
                                                             @RequestParam String storeName) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer memberId;
        try {
            memberId = Integer.valueOf(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<StoreSearchDTO> stores = storeApiService.searchStoresByName(storeName);
        return ResponseEntity.ok(stores);
    }

    @PostMapping("/assign/owner")
    public ResponseEntity<AssignOwnerResponse> assignOwner(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AssignOwnerRequest assignOwnerRequest) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer adminId;
        try {
            adminId = Integer.valueOf(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            AssignOwnerResponse response = storeApiService.assignOwnerToStore(assignOwnerRequest);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException | StoreNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 가게 생성 API
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/stores")
    public ResponseEntity<?> createStore(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart MultipartFile storePhoto,
            @RequestPart StoreCreateDTO store) {
        System.out.println("===================" + storePhoto.getName());

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer adminId;
        try {
            adminId = Integer.valueOf(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            StoreCreateDTO createdStore = storeApiService.createStore(store, storePhoto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStore);
        } catch (StoreNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (FileStorageException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("가게 생성에 실패했습니다.");
        }
    }

    // 가게 수정 API
    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/stores/{storeId}")
    public ResponseEntity<?> updateStore(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer storeId,
            @RequestPart MultipartFile storePhoto,
            @RequestPart StoreCreateDTO store) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer adminId;
        try {
            adminId = Integer.valueOf(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            StoreCreateDTO updatedStore = storeApiService.updateStore(storeId, store, storePhoto);
            return ResponseEntity.ok(updatedStore);
        } catch (StoreNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (FileStorageException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("가게 수정에 실패했습니다.");
        }
    }

    @PostMapping("/menu/{storeId}")
    public ResponseEntity<?> createMenu(@PathVariable Integer storeId,
                                        @RequestPart MultipartFile menuPhoto,
                                        @RequestPart MenuAdminDTO menuAdminDTO) throws Exception {
        menuAdminDTO = menuService.createMenu(storeId, menuPhoto, menuAdminDTO);
        return ResponseEntity.status(HttpStatus.OK).body(menuAdminDTO);
    }

    @PatchMapping("/menu/{menuId}")
    public ResponseEntity<?> updateStoreMenus(@PathVariable Integer menuId,
                                        @RequestPart MultipartFile menuPhoto,
                                        @RequestPart MenuAdminDTO menuAdminDTO) throws Exception {
        menuAdminDTO = menuService.updateStoreMenus(menuId, menuPhoto, menuAdminDTO);
        return ResponseEntity.status(HttpStatus.OK).body(menuAdminDTO);
    }

    @DeleteMapping("/menu/{menuId}")
    public ResponseEntity<?> deleteStoreMenus(@PathVariable Integer menuId) {
        boolean result = menuService.deleteStoreMenus(menuId);
        if(result){
            return ResponseEntity.status(HttpStatus.OK).body("메뉴가 정상적으로 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
