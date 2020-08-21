package com.example.service;

import com.example.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    public String getPasswordByUserName(String userName){
        System.out.println("从数据库中读取数据");
        return userDao.getPasswordByUserName(userName);
    }
}
