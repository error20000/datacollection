package com.jian.collection.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jian.collection.dao.BeaconDao;
import com.jian.collection.entity.Beacon;
import com.jian.collection.service.BeaconService;

@Service
public class BeaconServiceImpl extends BaseServiceImpl<Beacon> implements BeaconService {

	@Autowired
	private BeaconDao dao;

	@Override
	public void initDao() {
		super.baseDao = dao;
	}
	
	
}
