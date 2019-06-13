package com.jian.collection.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jian.collection.entity.Data;
import com.jian.collection.entity.User;
import com.jian.collection.service.DataService;
import com.jian.collection.utils.Utils;
import com.jian.tools.core.CacheTools;
import com.jian.tools.core.JsonTools;
import com.jian.tools.core.MapTools;
import com.jian.tools.core.ResultKey;
import com.jian.tools.core.ResultTools;
import com.jian.tools.core.Tips;
import com.jian.tools.core.Tools;
import com.jian.tools.core.cache.CacheObject;

@Controller
@RequestMapping("/api/data")
public class DataController extends BaseController<Data> {

	@Autowired
	private DataService service;
	
	@Override
	public void initService() {
		super.service = service;
	}
	
	//TODO 基本方法
	
	@Override
	@RequestMapping("/findPage")
    @ResponseBody
	public String findPage(HttpServletRequest req) {
		
		Map<String, Object> vMap = null;
		//登录
		vMap = verifyLogin(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		//sign
		vMap = verifySign(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		//权限
		vMap = verifyAuth(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		
		//登录用户
		User user = getLoginUser(req);
		if(user == null){
			return ResultTools.custom(Tips.ERROR111).toJSONString();
		}
		/*if(user.getAdmin() != 1){
			return ResultTools.custom(Tips.ERROR201).toJSONString();
		}*/
		
		//参数
		String page = Tools.getReqParamSafe(req, "page");
		String rows = Tools.getReqParamSafe(req, "rows");
		vMap = Tools.verifyParam("page", page, 0, 0, true);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		vMap = Tools.verifyParam("rows", rows, 0, 0, true);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		int start = Tools.parseInt(page) <= 1 ? 0 : (Tools.parseInt(page) - 1) * Tools.parseInt(rows);
		//参数
		Map<String, Object> condition = Tools.getReqParamsToMap(req, Data.class);
		
		List<Data> list = service.findPage(condition, start, Tools.parseInt(rows));
		long total = service.size(condition);
        return ResultTools.custom(Tips.ERROR1).put(ResultKey.TOTAL, total).put(ResultKey.DATA, list).toJSONString();
	}
	
	@Override
	@RequestMapping("/findAll")
    @ResponseBody
	public String findAll(HttpServletRequest req) {
		
		Map<String, Object> vMap = null;
		//登录
		vMap = verifyLogin(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		//sign
		vMap = verifySign(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		//权限
		vMap = verifyAuth(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		
		//登录用户
		User user = getLoginUser(req);
		if(user == null){
			return ResultTools.custom(Tips.ERROR111).toJSONString();
		}
		/*if(user.getAdmin() != 1){
			return ResultTools.custom(Tips.ERROR201).toJSONString();
		}*/
		
		List<Data> list = service.findAll();
        return ResultTools.custom(Tips.ERROR1).put(ResultKey.DATA, list).toJSONString();
	}

	//TODO 自定义方法

	@RequestMapping("/collection")
    @ResponseBody
	public String collection(HttpServletRequest req) {
		Map<String, Object> vMap = null;
		//sign
		vMap = verifySign(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		
		//参数
		String username = Tools.getReqParamSafe(req, "username");
		String password = Tools.getReqParamSafe(req, "password");
		vMap = Tools.verifyParam("username", username, 0, 0);
		if(vMap != null){
			return ResultTools.custom(Tips.ERROR206, "username").toJSONString();
		}
		vMap = Tools.verifyParam("password", password, 0, 0);
		if(vMap != null){
			return ResultTools.custom(Tips.ERROR206, "password").toJSONString();
		}
		
		//检查
		User user = service.findOne(MapTools.custom().put("username", username).build());
		if(user == null){
			return ResultTools.custom(Tips.ERROR109).toJSONString();
		}
		if(!user.getPassword().equals(Tools.md5(password))){
			return ResultTools.custom(Tips.ERROR110).toJSONString();
		}
		
		//保存
//		HttpSession session = req.getSession();
//		session.setAttribute(config.login_session_key, user);
		user.setPassword("");
		CacheTools.setCacheObj("login_user_"+user.getPid(), user);
		return ResultTools.custom(Tips.ERROR1).toJSONString();
	}


	@RequestMapping("/test")
    @ResponseBody
	public String test(HttpServletRequest req) {
		Map<String, Object> vMap = null;
		//sign
		vMap = verifySign(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		
		//参数
		String username = Tools.getReqParamSafe(req, "username");
		String password = Tools.getReqParamSafe(req, "password");
		vMap = Tools.verifyParam("username", username, 0, 0);
		if(vMap != null){
			return ResultTools.custom(Tips.ERROR206, "username").toJSONString();
		}
		vMap = Tools.verifyParam("password", password, 0, 0);
		if(vMap != null){
			return ResultTools.custom(Tips.ERROR206, "password").toJSONString();
		}
		
		//检查
		User user = service.findOne(MapTools.custom().put("username", username).build());
		if(user == null){
			return ResultTools.custom(Tips.ERROR109).toJSONString();
		}
		if(!user.getPassword().equals(Tools.md5(password))){
			return ResultTools.custom(Tips.ERROR110).toJSONString();
		}
		
		//保存
//		HttpSession session = req.getSession();
//		session.setAttribute(config.login_session_key, user);
		user.setPassword("");
		CacheTools.setCacheObj("login_user_"+user.getPid(), user);
		return ResultTools.custom(Tips.ERROR1).toJSONString();
	}
	
}
