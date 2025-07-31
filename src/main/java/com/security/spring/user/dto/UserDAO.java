package com.security.spring.user.dto;

import com.security.spring.user.role.Role;
import com.security.spring.user.entity.User;

import java.util.List;

public interface UserDAO {
    public User saveUser(User user);
    public User findUserByAR7Id(String ar7Id);
    public List<User> findUserAll();
    public List<User> findUserByRole(Role role);
}
