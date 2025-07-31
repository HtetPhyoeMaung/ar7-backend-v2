package com.security.spring.change_password.service;

import com.security.spring.change_password.dto.ChangePasswordRequest;
import com.security.spring.change_password.dto.ChangePasswordResponse;

import javax.naming.NoPermissionException;

public interface ChangePasswordService {
    ChangePasswordResponse changePassword(ChangePasswordRequest request, String userAr7Id) throws NoPermissionException;
}
