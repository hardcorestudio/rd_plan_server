package com.mine.rd.controllers.lab;

import org.apache.log4j.Logger;
import com.mine.pub.service.Service;
import com.mine.rd.services.lab.service.LabService;
import com.ext.plugin.config.ConfigKit;
import com.jfinal.aop.Clear;
import com.jfinal.kit.PropKit;
import com.mine.pub.controller.BaseController;

public class LabController extends BaseController {
	private Logger logger = Logger.getLogger(LabController.class);
	
	public void initEp(){
		logger.info("企业信息");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("企业信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void initEpEdit(){
		logger.info("初始化查询企业信息编辑页面");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("初始化查询企业信息编辑页面异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void epEdit(){
		logger.info("保存企业信息编辑页面");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("保存企业信息编辑页面异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void initFlow(){
		logger.info("初始化查询流水页面");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("初始化查询流水页面异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void initFlowEp(){
		logger.info("初始化查询流水页面");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("初始化查询流水页面异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void addFlow(){
		logger.info("增加流水");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("增加流水异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void addFlowEp(){
		logger.info("增加流水");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("增加流水异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void addFlowEpOut(){
		logger.info("增加流水");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("增加流水异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void addFlowEpIn(){
		logger.info("接收流水");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("接收流水异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void getFlowList(){
		logger.info("流水列表");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("流水列表异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void getFlowEpList(){
		logger.info("流水列表");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("流水列表异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void getFlowEpOutList(){
		logger.info("流水列表");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("流水列表异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void getFlowEpInList(){
		logger.info("流水列表");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("流水列表异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void initStock(){
		logger.info("初始化我的库存页面");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("初始化我的库存页面异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void updatePwd(){
		logger.info("修改密码");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("修改密码异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void updateTel(){
		logger.info("修改手机号");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("修改手机号异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void initFlowTranfer(){
		logger.info("初始化录入转移页面");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("初始化录入转移页面异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void initFlowTranferIn(){
		logger.info("初始化接收转移页面");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("初始化接收转移页面异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	@Clear
	public void queryEp2List(){
		logger.info("集中转运点列表");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("集中转运点列表异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void queryEp1List(){
		logger.info("终端列表");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("终端列表异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void updateEpStatus(){
		logger.info("更新终端状态");
		Service service = new LabService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("更新终端状态异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	public void checkVersion(){
		setAttr("version", PropKit.get("lab_version"));
		renderJsonForCors();
	}
}
