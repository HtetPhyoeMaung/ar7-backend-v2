package com.security.spring.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String message;
    private boolean status;
    private int statusCode;
    private List<UserResponseObj> userResponseList;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int pageSize;
}
