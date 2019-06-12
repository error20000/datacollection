package com.jian.collection.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jian.collection.dao.UserDao;
import com.jian.collection.entity.User;
import com.jian.collection.service.UserService;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	@Autowired
	private UserDao dao;

	@Override
	public void initDao() {
		super.baseDao = dao;
	}
	
	
}
