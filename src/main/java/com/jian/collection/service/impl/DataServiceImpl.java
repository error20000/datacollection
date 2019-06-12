package com.jian.collection.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jian.collection.dao.DataDao;
import com.jian.collection.entity.Data;
import com.jian.collection.service.DataService;

@Service
public class DataServiceImpl extends BaseServiceImpl<Data> implements DataService {

	@Autowired
	private DataDao dao;

	@Override
	public void initDao() {
		super.baseDao = dao;
	}
	
	
}
