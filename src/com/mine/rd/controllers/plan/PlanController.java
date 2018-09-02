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
	
}
