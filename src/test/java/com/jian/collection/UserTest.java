package com.jian.collection;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jian.collection.entity.User;
import com.jian.collection.service.UserService;
import com.jian.collection.utils.Utils;

@RunWith(SpringRunner.class)   
@SpringBootTest(classes={App.class})
public class UserTest {
    
	@Autowired
	private UserService service;

	@Test
	public void Add(){
		User user = new User();
		user.setPid(Utils.newId());
		user.setUsername("test");
		user.setPassword("test");
		int res = service.add(user);
		System.out.println("------add------"+res);
	}
	

	/*@Test
	public void update(){
		User user = new User();
		user.setUsername("test");
		user.setPassword("test222");
		int res = service.modify(user);
		System.out.println("------modify------"+res);
	}*/
	
	@Test
	public void findAll(){
		List<User> list = service.findAll();
		for (User user : list) {
			System.out.println(user.getUsername()+"------------"+user.getPassword());
		}
	}
}
