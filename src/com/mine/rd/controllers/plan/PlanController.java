package com.mine.rd.controllers.plan;

import org.apache.log4j.Logger;
import com.mine.pub.controller.BaseController;

public class PlanController extends BaseController {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PlanController.class);
		
	public void index(){
		renderJsonForCors();
	}
	
	public void planMain(){
		renderJsonForCors("hello w");
	}
}
