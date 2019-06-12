package com.jian.collection;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jian.collection.entity.Data;
import com.jian.collection.entity.User;
import com.jian.collection.service.DataService;
import com.jian.collection.utils.Utils;
import com.jian.tools.core.DateTools;
import com.jian.tools.core.JsonTools;
import com.jian.tools.core.MapTools;

@RunWith(SpringRunner.class)   
@SpringBootTest(classes={App.class})
public class DataTest {
    
	@Autowired
	private DataService service;
	
	@Test
	public void Add(){
		Data obj = new Data();
		obj.setCreatetime(DateTools.formatDate());
		obj.setPid(Utils.newId());
		obj.setSn("test");
		obj.setAx(1.3f);
		obj.setJd(3331.2223f);
		obj.setGs("N");
		obj.setS2(99);
		int res = service.add(obj);
		System.out.println("------add------"+res);
	}
	
	@Test
	public void findAll(){
		List<Data> list = service.findAll();
		System.out.println("----->"+list.get(0).getPid());
		for (Data obj : list) {
			System.out.println(obj.getPid()+"----->");
		}
	}
}
