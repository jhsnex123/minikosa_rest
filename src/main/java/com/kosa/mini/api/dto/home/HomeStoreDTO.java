package com.kosa.mini.api.dto.home;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeStoreDTO {
    private Integer storeId;
    private String storeName;
    private String storePhoto;
    private String storeDescription;
    private double ratingAvg;
    private String categoryName;

}
