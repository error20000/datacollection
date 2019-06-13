package com.jian.collection.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jian.collection.entity.User;
import com.jian.collection.service.UserService;
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
@RequestMapping("/api/user")
public class UserController extends BaseController<User> {

	@Autowired
	private UserService service;
	
	@Override
	public void initService() {
		super.service = service;
	}
	
	//TODO 基本方法
	
	@Override
	@RequestMapping("/add")
    @ResponseBody
	public String add(HttpServletRequest req) {
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
		
		//保存
		User obj = Tools.getReqParamsToObject(req, new User());
		obj.setPid(Utils.newId());
		obj.setPassword(Tools.md5(obj.getPassword()));
		int res = service.add(obj);
		if(res > 0){
			return ResultTools.custom(Tips.ERROR1).put(ResultKey.DATA, res).toJSONString();
		}else{
			return ResultTools.custom(Tips.ERROR0).toJSONString();
		}
	}


	@Override
	@RequestMapping("/update")
    @ResponseBody
	public String update(HttpServletRequest req) {
		
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
		List<String> pkeys = Utils.getPrimaryKeys(User.class);//获取主键
		if(pkeys == null || pkeys.isEmpty()){
			return ResultTools.custom(Tips.ERROR206).toJSONString();
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		for (String str : pkeys) {
			String strv = Tools.getReqParamSafe(req, str);
			vMap = Tools.verifyParam(str, strv, 0, 0);
			if(vMap != null){
				return ResultTools.custom(Tips.ERROR206, str).toJSONString();
			}
			condition.put(str, strv);
		}
		Map<String, Object> setValues = Tools.getReqParamsToMap(req, User.class);
		setValues.remove("password");
		//保存
		int res = service.modify(setValues, condition);
		if(res > 0){
			return ResultTools.custom(Tips.ERROR1).toJSONString();
		}else{
			return ResultTools.custom(Tips.ERROR0).put(ResultKey.DATA, res).toJSONString();
		}
	}

	@Override
	@RequestMapping("/delete")
    @ResponseBody
	public String delete(HttpServletRequest req) {
		
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
		List<String> pkeys = Utils.getPrimaryKeys(User.class);//获取主键
		if(pkeys == null || pkeys.isEmpty()){
			return ResultTools.custom(Tips.ERROR206).toJSONString();
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		for (String str : pkeys) {
			String strv = Tools.getReqParamSafe(req, str);
			vMap = Tools.verifyParam(str, strv, 0, 0);
			if(vMap != null){
				return ResultTools.custom(Tips.ERROR206, str).toJSONString();
			}
			condition.put(str, strv);
		}
		//保存
		int res = service.delete(condition);
		if(res > 0){
			return ResultTools.custom(Tips.ERROR1).toJSONString();
		}else{
			return ResultTools.custom(Tips.ERROR0).put(ResultKey.DATA, res).toJSONString();
		}
	}

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
		Map<String, Object> condition = Tools.getReqParamsToMap(req, User.class);
		
		List<User> list = service.findPage(condition, start, Tools.parseInt(rows));
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
		
		List<User> list = service.findAll();
        return ResultTools.custom(Tips.ERROR1).put(ResultKey.DATA, list).toJSONString();
	}

	//TODO 自定义方法

	@RequestMapping("/login")
    @ResponseBody
	public String login(HttpServletRequest req) {
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
		return ResultTools.custom(Tips.ERROR1).put(ResultKey.DATA, user).toJSONString();
	}
	
	@RequestMapping("/logout")
    @ResponseBody
	public String logout(HttpServletRequest req) {
		//保存
//		HttpSession session = req.getSession();
//		session.removeAttribute(config.login_session_key);
		
		String userId = req.getHeader("userId");
		if(Tools.isNullOrEmpty(userId)) {
			userId = Tools.getReqParamSafe(req, "userId");
		}
		CacheTools.clearCacheObj("login_user_"+userId);
		return ResultTools.custom(Tips.ERROR1).toJSONString();
	}
	
	@RequestMapping("/isLogin")
    @ResponseBody
	public String isLogin(HttpServletRequest req) {
		//保存
//		HttpSession session = req.getSession();
//		Object test = session.getAttribute(config.login_session_key);
		
		String userId = req.getHeader("userId");
		if(Tools.isNullOrEmpty(userId)) {
			userId = Tools.getReqParamSafe(req, "userId");
		}
		CacheObject test = CacheTools.getCacheObj("login_user_"+userId);
		if(test == null){
			return ResultTools.custom(Tips.ERROR0).toJSONString();
		}else {
			return ResultTools.custom(Tips.ERROR1).put(ResultKey.DATA, test.getValue()).toJSONString();
		}
	}
	
	@RequestMapping("/register")
    @ResponseBody
	public String register(HttpServletRequest req) {
		Map<String, Object> vMap = null;
		//sign
		vMap = verifySign(req);
		if(vMap != null){
			return JsonTools.toJsonString(vMap);
		}
		
		//参数
		String username = Tools.getReqParamSafe(req, "username");
		String password = Tools.getReqParamSafe(req, "password");
//		String nick = Tools.getReqParamSafe(req, "nick");
//		String color = Tools.getReqParamSafe(req, "color");
		vMap = Tools.verifyParam("username", username, 0, 0);
		if(vMap != null){
			return ResultTools.custom(Tips.ERROR206, "username").toJSONString();
		}
		vMap = Tools.verifyParam("password", password, 0, 0);
		if(vMap != null){
			return ResultTools.custom(Tips.ERROR206, "password").toJSONString();
		}
		
		//检查
		User test = service.findOne(MapTools.custom().put("username", username).build());
		if(test != null){
			return ResultTools.custom(Tips.ERROR105, username).toJSONString();
		}
		
		//保存
		User user = new User();
		user.setUsername(username);
		user.setPassword(Tools.md5(password));
//		user.setNick(Tools.isNullOrEmpty(nick) ? username : nick);
//		user.setAdmin(0);
//		user.setColor(color);
		int res = service.add(user);
		if(res > 0){
			return ResultTools.custom(Tips.ERROR1).put(ResultKey.DATA, res).toJSONString();
		}else{
			return ResultTools.custom(Tips.ERROR0).toJSONString();
		}
		
	}

	@RequestMapping("/changePWD")
    @ResponseBody
	public String changePWD(HttpServletRequest req) {
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

		User luser = getLoginUser(req);
		if(luser == null){
			return ResultTools.custom(Tips.ERROR111).toJSONString();
		}
		
		//参数
		String oldPwd = Tools.getReqParamSafe(req, "oldPwd");
		String newPwd = Tools.getReqParamSafe(req, "newPwd");
		vMap = Tools.verifyParam("oldPwd", oldPwd, 0, 0);
		if(vMap != null){
			return ResultTools.custom(Tips.ERROR206, "oldPwd").toJSONString();
		}
		vMap = Tools.verifyParam("newPwd", newPwd, 0, 0);
		if(vMap != null){
			return ResultTools.custom(Tips.ERROR206, "newPwd").toJSONString();
		}
		
		User test = service.findOne(MapTools.custom().put("pid", luser.getPid()).build());
		if(!test.getPassword().equals(Tools.md5(oldPwd))){
			return ResultTools.custom(Tips.ERROR213, "密码").toJSONString();
		}
		
		//保存
		int res = service.modify(MapTools.custom().put("password", Tools.md5(newPwd)).build(), MapTools.custom().put("pid", luser.getPid()).build());
		if(res > 0){
			return ResultTools.custom(Tips.ERROR1).toJSONString();
		}else{
			return ResultTools.custom(Tips.ERROR0).put(ResultKey.DATA, res).toJSONString();
		}
	}

	@RequestMapping("/resetPWD")
    @ResponseBody
	public String resetPWD(HttpServletRequest req) {
		
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
		String pid = Tools.getReqParamSafe(req, "pid");
		String password = Tools.getReqParamSafe(req, "password");
		vMap = Tools.verifyParam("pid", pid, 0, 0);
		if(vMap != null){
			return ResultTools.custom(Tips.ERROR206, "pid").toJSONString();
		}
		vMap = Tools.verifyParam("password", password, 0, 0);
		if(vMap != null){
			return ResultTools.custom(Tips.ERROR206, "password").toJSONString();
		}
		
		User test = service.findOne(MapTools.custom().put("pid", pid).build());
		if(test == null){
			return ResultTools.custom(Tips.ERROR106, "用户").toJSONString();
		}
		test.setPassword(Tools.md5(password));
		
		//保存
		int res = service.modify(test);
		if(res > 0){
			return ResultTools.custom(Tips.ERROR1).toJSONString();
		}else{
			return ResultTools.custom(Tips.ERROR0).put(ResultKey.DATA, res).toJSONString();
		}
	}
	
}
