package com.security.spring.thirdpartygames.gameprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderSortItem {
    private Long product;
    private Integer sortNumber;
}
