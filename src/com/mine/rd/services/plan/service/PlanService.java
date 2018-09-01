package com.mine.rd.services.plan.service;

import java.sql.SQLException;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.BaseService;

public class PlanService extends BaseService{

	public PlanService(BaseController controller) {
		super(controller);
	}

	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
        		try {
	            	if("planMain".equals(getLastMethodName(7))){
	            		planMain();
	        		}
	            } catch (Exception e) {
	                e.printStackTrace();
	            	controller.setAttr("msg", "系统异常，请重新登录！");
	    			controller.setAttr("resFlag", "1");
	                return false;
	            }
	            return true;
	        }
		});
	}
	
	private String getUrl(String url){
		return PropKit.get("rd_plan_sub_url")+url;
	}
	
	private void planMain(){
		String url = controller.getMyParam("url").toString();
		if("baseInfo".equals(url)){
			this.baseInfo();
		}
		else if("productionSituation".equals(url)){
			this.productionSituation();
		}
		else if("produceSituation".equals(url)){
			this.produceSituation();
		}
		else if("decrementPlan".equals(url)){
			this.decrementPlan();
		}
		else if("transferStuation".equals(url)){
			this.transferStuation();
		}
		else if("selfDisposalMeasures".equals(url)){
			this.selfDisposalMeasures();
		}
		else if("entrustDisposalMeasures".equals(url)){
			this.entrustDisposalMeasures();
		}
		else if("environmentalMonitoring".equals(url)){
			this.environmentalMonitoring();
		}
		else if("lastYearManagePlanRecord".equals(url)){
			this.lastYearManagePlanRecord();
		}
		controller.setAttr("sub_url", getUrl(url));
	}
		
	private void baseInfo(){
		controller.setAttr("sub_url", PropKit.get("rd_plan_sub_url")+"/baseInfo");
		controller.setAttr("resFlag", "0");
	}
	
	private void productionSituation(){
		controller.setAttr("sub_url", PropKit.get("rd_plan_sub_url")+"/productionSituation");
		controller.setAttr("resFlag", "0");
	}
	
	private void produceSituation(){
		controller.setAttr("sub_url",  PropKit.get("rd_plan_sub_url")+"/produceSituation");
		controller.setAttr("resFlag", "0");
	}
	
	private void decrementPlan(){
		controller.setAttr("sub_url", "http://localhost:9002/rdplan/rd_plan_sub/index.html#/decrementPlan");
		controller.setAttr("resFlag", "0");
	}
	
	private void transferStuation(){
		controller.setAttr("sub_url", "http://localhost:9002/rdplan/rd_plan_sub/index.html#/transferStuation");
		controller.setAttr("resFlag", "0");
	}
	
	private void selfDisposalMeasures(){
		controller.setAttr("sub_url", "http://localhost:9002/rdplan/rd_plan_sub/index.html#/selfDisposalMeasures");
		controller.setAttr("resFlag", "0");
	}
	
	private void entrustDisposalMeasures(){
		controller.setAttr("sub_url", "http://localhost:9002/rdplan/rd_plan_sub/index.html#/entrustDisposalMeasures");
		controller.setAttr("resFlag", "0");
	}
	
	private void environmentalMonitoring(){
		controller.setAttr("sub_url", "http://localhost:9002/rdplan/rd_plan_sub/index.html#/environmentalMonitoring");
		controller.setAttr("resFlag", "0");
	}
	
	private void lastYearManagePlanRecord(){
		controller.setAttr("sub_url", "http://localhost:9002/rdplan/rd_plan_sub/index.html#/lastYearManagePlanRecord");
		controller.setAttr("resFlag", "0");
	}
}
