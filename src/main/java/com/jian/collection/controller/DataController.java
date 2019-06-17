package com.jian.collection.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jian.collection.entity.Data;
import com.jian.collection.entity.User;
import com.jian.collection.service.DataService;
import com.jian.tools.core.JsonTools;
import com.jian.tools.core.ResultKey;
import com.jian.tools.core.ResultTools;
import com.jian.tools.core.Tips;
import com.jian.tools.core.Tools;

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
		String startTime = Tools.getReqParamSafe(req, "start");
		String endTime = Tools.getReqParamSafe(req, "end");
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
		String wsql = " 1=1 ";
		for (String key : condition.keySet()) {
			wsql += " and "+key+" = :"+key;
		}
		if(!Tools.isNullOrEmpty(startTime)) {
			wsql += " and createtime >= :startTime";
			condition.put("startTime", startTime);
		}
		if(!Tools.isNullOrEmpty(endTime)) {
			wsql += " and createtime <= :endTime";
			condition.put("endTime", endTime);
		}
		
		
		List<Data> list = service.getDao().findList(wsql, condition, start, Tools.parseInt(rows));
		long total = service.getDao().size(wsql, condition);
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

	@RequestMapping("/excel")
    @ResponseBody
	public String excel(HttpServletRequest req, HttpServletResponse resp) {
		
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

		String wsql = " 1=1 ";
		String start = Tools.getReqParamSafe(req, "start");
		String end = Tools.getReqParamSafe(req, "end");
		//查询
		List<Data> list = null;
		Map<String, Object> condition = Tools.getReqParamsToMap(req, Data.class);
		for (String key : condition.keySet()) {
			wsql += " and "+key+" = :"+key;
		}
		if(!Tools.isNullOrEmpty(start)) {
			wsql += " and createtime >= :start";
			condition.put("start", start);
		}
		if(!Tools.isNullOrEmpty(end)) {
			wsql += " and createtime <= :end";
			condition.put("end", end);
		}
		if(condition == null || condition.isEmpty()){
			list = service.findAll();
		}else {
			list = service.getDao().findList(wsql, condition);
		}

		//执行
		resp.addHeader("Content-Disposition","attachment;filename=data.xls");
		resp.setContentType("application/vnd.ms-excel;charset=utf-8");
		try {
			OutputStream toClient = new BufferedOutputStream(resp.getOutputStream());
			//实例化HSSFWorkbook
            HSSFWorkbook workbook = new HSSFWorkbook();
            //创建一个Excel表单，参数为sheet的名字
            HSSFSheet sheet = workbook.createSheet("sheet");

			//设置表头
			String head = "Pid,序列号,接收时间,报警标识,S1,S2,S3,S4,X轴角度,Y轴角度,GPS状态,东西经,经度,南北纬,纬度,年,月,日,时,分,秒,自动校时";
			String[] heads = head.split(",");
            HSSFRow row = sheet.createRow(0);
            //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
            for (int i = 0; i <= heads.length; i++) {
            	sheet.setColumnWidth(i, (int)(( 15 + 0.72) * 256)); // 15 在EXCEL文档中实际列宽为14.29
            }
            //设置为居中加粗,格式化时间格式
            HSSFCellStyle style = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy/MM/dd HH:mm:ss"));
            //创建表头名称
            HSSFCell cell;
            for (int j = 0; j < heads.length; j++) {
                cell = row.createCell(j);
                cell.setCellValue(heads[j]);
                cell.setCellStyle(style);
            }
			//遍历导出数据
			for (int i = 0; i < list.size(); i++) {
				Data node = list.get(i);

				HSSFRow rowc = sheet.createRow(i+1);
				rowc.createCell(0).setCellValue(node.getPid()+"");
				rowc.createCell(1).setCellValue(node.getSn());
				rowc.createCell(2).setCellValue(node.getCreatetime());
				rowc.createCell(3).setCellValue(node.getAf());
				rowc.createCell(4).setCellValue(node.getS1());
				rowc.createCell(5).setCellValue(node.getS2());
				rowc.createCell(6).setCellValue(node.getS3());
				rowc.createCell(7).setCellValue(node.getS4());
				rowc.createCell(8).setCellValue(node.getAx()+"");
				rowc.createCell(9).setCellValue(node.getAy()+"");
				rowc.createCell(10).setCellValue(node.getGs());
				rowc.createCell(11).setCellValue(node.getDxj());
				rowc.createCell(12).setCellValue(node.getJd()+"");
				rowc.createCell(13).setCellValue(node.getNbw());
				rowc.createCell(14).setCellValue(node.getWd()+"");
				rowc.createCell(15).setCellValue(node.getTy());
				rowc.createCell(16).setCellValue(node.getTm());
				rowc.createCell(17).setCellValue(node.getTd());
				rowc.createCell(18).setCellValue(node.getTh());
				rowc.createCell(19).setCellValue(node.getTmm());
				rowc.createCell(20).setCellValue(node.getTs());
				rowc.createCell(21).setCellValue(node.getAct());
			}
			workbook.write(toClient);
			workbook.close();
			toClient.flush();
			toClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
}
