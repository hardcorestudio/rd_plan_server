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
	
	public void initOverview()
	{
		logger.info("进入危险废物产生概况");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("进入危险废物产生概况异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void saveOverview()
	{
		logger.info("保存危险废物产生概况");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("保存危险废物产生概况异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void initReduction()
	{
		logger.info("进入危险废物减量化计划和措施");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("进入危险废物减量化计划和措施异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void saveReduction()
	{
		logger.info("保存危险废物减量化计划和措施");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("保存危险废物减量化计划和措施异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void initTransfer()
	{
		logger.info("进入危险废物转移情况");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("进入危险废物转移情况异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void saveTransfer()
	{
		logger.info("保存危险废物转移情况");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("保存危险废物转移情况异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void initHandleSelf()
	{
		logger.info("进入危险废物自行利用/处置措施");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("进入危险废物自行利用/处置措施异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void saveHandleSelf()
	{
		logger.info("进入危险废物自行利用/处置措施");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("进入危险废物自行利用/处置措施异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void initHandle()
	{
		logger.info("进入危险废物委托利用/处置措施");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("进入危险废物委托利用/处置措施异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void saveHandle()
	{
		logger.info("进入危险废物委托利用/处置措施");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("进入危险废物委托利用/处置措施异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void initEnv()
	{
		logger.info("进入环境监测情况");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("进入环境监测情况异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void saveEnv()
	{
		logger.info("保存环境监测情况");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("保存环境监测情况异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void initLastInfo()
	{
		logger.info("进入上年度管理计划回顾");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("进入上年度管理计划回顾异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void saveLastInfo()
	{
		logger.info("保存上年度管理计划回顾");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("保存上年度管理计划回顾异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void previewPlan(){
		logger.info("预览管理计划");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("预览管理计划异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void getLastTBSum(){
		logger.info("获取去年实际转移量");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("获取去年实际转移量异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void initPt(){
		logger.info("进入跨省转移计划");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("进入跨省转移计划异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void savePt(){
		logger.info("保存跨省转移计划");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("保存跨省转移计划异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
}
