package com.security.spring.user.dto;

import com.security.spring.user.role.Role;
import com.security.spring.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO{
    private EntityManager entityManager;

    @Autowired
    public UserDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public User saveUser(User user) {
        User userFromDB = entityManager.merge(user);
        return userFromDB;
    }

    @Override
    public User findUserByAR7Id(String ar7Id) {
        TypedQuery<User> theQuery = entityManager.createQuery("FROM User WHERE ar7Id=:theData", User.class);
        theQuery.setParameter("theData", ar7Id);
        List<User> userList = theQuery.getResultList();
        User user = userList.get(0);
        return user;
    }

    public List<User> findUserAll(){
        TypedQuery<User> theQuery = entityManager.createQuery("from User",User.class);
        List<User> userList = theQuery.getResultList();
        return userList;
    }

    public List<User> findUserByRole(Role role){
        TypedQuery<User> theQuery = entityManager.createQuery("FROM User WHERE role=:theData", User.class);
        theQuery.setParameter("theData", role);
        List<User> userList = theQuery.getResultList();
        return userList;
    }
}
