package com.mine.rd.controllers.plan;

import org.apache.log4j.Logger;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.services.plan.service.PlanService;

public class PlanController extends BaseController {

	private Logger logger = Logger.getLogger(PlanController.class);
		
	public void index(){
		renderJsonForCors();
	}
	
	public void planMain(){
		logger.info("管理计划");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理计划异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void planList(){
		logger.info("管理计划列表");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理计划列表异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void apply(){
		logger.info("申请管理计划");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("申请管理计划异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void checkPlan(){
		logger.info("判断管理计划状态");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("判断管理计划状态异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void apply2q(){
		logger.info("提交申请");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("判断管理计划状态异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void initBaseInfo()
	{
		logger.info("进入基本信息的初始信息");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("进入基本信息的初始信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void saveBaseInfo()
	{
		logger.info("保存基本信息的初始信息");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("保存基本信息的初始信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void initProductInfo()
	{
		logger.info("进入产品生产情况");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("进入产品生产情况异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void saveProductInfo()
	{
		logger.info("保存产品生产情况");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("保存产品生产情况异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
}
