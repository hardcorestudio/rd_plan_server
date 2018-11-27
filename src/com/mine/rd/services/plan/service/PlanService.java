package com.mine.rd.services.plan.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.plan.pojo.PlanDao;
import com.mine.rd.websocket.MyWebSocket;

public class PlanService extends BaseService{

	private PlanDao dao = new PlanDao();
	
	private int pn = 0;
	private int ps = 0;
	
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
	            	else if("planList".equals(getLastMethodName(7))){
	            		planList();
	            	}
	            	else if("apply".equals(getLastMethodName(7))){
	            		apply();
	            	}
	            	else if("checkPlan".equals(getLastMethodName(7))){
	            		checkPlan();
	            	}
	            	else if("apply2q".equals(getLastMethodName(7))){
	            		apply2q();
	            	}
	            	else if("initBaseInfo".equals(getLastMethodName(7))){
	            		initBaseInfo();
	            	}
	            	else if("saveBaseInfo".equals(getLastMethodName(7))){
	            		saveBaseInfo();
	            	}
	            	else if("initProductInfo".equals(getLastMethodName(7))){
	            		initProductInfo();
	            	}
	            	else if("saveProductInfo".equals(getLastMethodName(7))){
	            		saveProductInfo();
	            	}
	            	else if("initOverview".equals(getLastMethodName(7))){
	            		initOverview();
	            	}
	            	else if("saveOverview".equals(getLastMethodName(7))){
	            		saveOverview();
	            	}
	            	else if("initReduction".equals(getLastMethodName(7))){
	            		initReduction();
	            	}
	            	else if("saveReduction".equals(getLastMethodName(7))){
	            		saveReduction();
	            	}
	            	else if("initTransfer".equals(getLastMethodName(7))){
	            		initTransfer();
	            	}
	            	else if("saveTransfer".equals(getLastMethodName(7))){
	            		saveTransfer();
	            	}
	            	else if("initHandleSelf".equals(getLastMethodName(7))){
	            		initHandleSelf();
	            	}
	            	else if("saveHandleSelf".equals(getLastMethodName(7))){
	            		saveHandleSelf();
	            	}
	            	else if("initHandle".equals(getLastMethodName(7))){
	            		initHandle();
	            	}
	            	else if("saveHandle".equals(getLastMethodName(7))){
	            		saveHandle();
	            	}
	            	else if("initEnv".equals(getLastMethodName(7))){
	            		initEnv();
	            	}
	            	else if("saveEnv".equals(getLastMethodName(7))){
	            		saveEnv();
	            	}
	            	else if("initLastInfo".equals(getLastMethodName(7))){
	            		initLastInfo();
	            	}
	            	else if("saveLastInfo".equals(getLastMethodName(7))){
	            		saveLastInfo();
	            	}
	            	else if("previewPlan".equals(getLastMethodName(7))){
	            		previewPlan();
	            	}
	            	else if("getLastTBSum".equals(getLastMethodName(7))){
	            		getLastTBSum();
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
	
	private void planList(){
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		Object searchContent = controller.getMyParam("searchContent");
		Object statusValue = controller.getMyParam("statusValue");
		String epId = controller.getMySession("epId").toString();
		@SuppressWarnings("unchecked")
		List<Object> statusCache = (List<Object>) controller.getMyParam("statusCache");
		controller.setAttrs(dao.queryEpList(pn, ps,searchContent,statusValue,statusCache,epId));
		controller.setAttr("resFlag", "0");
	}
	
	private void planMain(){
		String url = controller.getMyParam("url").toString();
//		if("baseInfo".equals(url)){
//			this.baseInfo();
//		}
//		else if("productionSituation".equals(url)){
//			this.productionSituation();
//		}
//		else if("produceSituation".equals(url)){
//			this.produceSituation();
//		}
//		else if("decrementPlan".equals(url)){
//			this.decrementPlan();
//		}
//		else if("transferStuation".equals(url)){
//			this.transferStuation();
//		}
//		else if("selfDisposalMeasures".equals(url)){
//			this.selfDisposalMeasures();
//		}
//		else if("entrustDisposalMeasures".equals(url)){
//			this.entrustDisposalMeasures();
//		}
//		else if("environmentalMonitoring".equals(url)){
//			this.environmentalMonitoring();
//		}
//		else if("lastYearManagePlanRecord".equals(url)){
//			this.lastYearManagePlanRecord();
//		}
//		else if("previewPlan".equals(url)){
//			this.previewPlanPage();
//		}
		controller.setAttr("sub_url", getUrl(url));
		controller.setAttr("resFlag", "0");
	}
		
//	private void baseInfo(){
//		controller.setAttr("sub_url", PropKit.get("rd_plan_sub_url")+"/baseInfo");
//		controller.setAttr("resFlag", "0");
//	}
//	
//	private void productionSituation(){
//		controller.setAttr("sub_url", PropKit.get("rd_plan_sub_url")+"/productionSituation");
//		controller.setAttr("resFlag", "0");
//	}
//	
//	private void produceSituation(){
//		controller.setAttr("sub_url",  PropKit.get("rd_plan_sub_url")+"/produceSituation");
//		controller.setAttr("resFlag", "0");
//	}
//	
//	private void decrementPlan(){
//		controller.setAttr("sub_url", "http://localhost:9002/rdplan/rd_plan_sub/index.html#/decrementPlan");
//		controller.setAttr("resFlag", "0");
//	}
//	
//	private void transferStuation(){
//		controller.setAttr("sub_url", "http://localhost:9002/rdplan/rd_plan_sub/index.html#/transferStuation");
//		controller.setAttr("resFlag", "0");
//	}
//	
//	private void selfDisposalMeasures(){
//		controller.setAttr("sub_url", "http://localhost:9002/rdplan/rd_plan_sub/index.html#/selfDisposalMeasures");
//		controller.setAttr("resFlag", "0");
//	}
//	
//	private void entrustDisposalMeasures(){
//		controller.setAttr("sub_url", "http://localhost:9002/rdplan/rd_plan_sub/index.html#/entrustDisposalMeasures");
//		controller.setAttr("resFlag", "0");
//	}
//	
//	private void environmentalMonitoring(){
//		controller.setAttr("sub_url", "http://localhost:9002/rdplan/rd_plan_sub/index.html#/environmentalMonitoring");
//		controller.setAttr("resFlag", "0");
//	}
//	
//	private void lastYearManagePlanRecord(){
//		controller.setAttr("sub_url", "http://localhost:9002/rdplan/rd_plan_sub/index.html#/lastYearManagePlanRecord");
//		controller.setAttr("resFlag", "0");
//	}
//	
//	private void previewPlanPage(){
//		controller.setAttr("sub_url", "http://localhost:9002/rdplan/rd_plan_sub/index.html#/registrationForm");
//		controller.setAttr("resFlag", "0");
//	}
	
	private void apply() throws ParseException{
		String epId = controller.getMyParam("epId").toString();
		String epName = controller.getMySession("epName").toString();
		String belongSepa = controller.getMySession("belongSepa").toString();
		int resInt = dao.checkApply(epId);
		if(resInt < 1){
			Map<String,Object> map = dao.createPlan(epId,epName);
			String applyId = dao.createApply(map.get("TP_ID").toString(),epId,epName,belongSepa);
			if(applyId != ""){
				controller.setAttr("resFlag", "0");
			}else{
				controller.setAttr("resFlag", "2");
				controller.setAttr("resMsg", "操作失败");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("resMsg", "当年不能重复申报");
		}
	}
	
	private void checkPlan(){
		String TP_ID = controller.getMyParam("TP_ID").toString();
		Map<String,Object> map =  dao.checkApplyList(TP_ID);
		controller.setAttr("applyListStatus", map == null ? "" : map.get("STATUS"));
		if(map != null)
		{
			controller.setAttr("baseInfoFlag", dao.queryEpExtend(TP_ID) == null ? "0" : "1");
			controller.setAttr("productionSituationFlag", dao.initProductInfo(TP_ID) == null ? "0" : "1");
			controller.setAttr("produceSituationFlag", dao.initOverviewList(TP_ID) == null || dao.initOverviewList(TP_ID).size() == 0 ? "0" : "1");
			controller.setAttr("decrementPlanFlag", dao.initReduction(TP_ID) == null ? "0" : "1");
			controller.setAttr("transferStuationFlag", dao.initTransfer(TP_ID) == null ? "0" : "1");
			controller.setAttr("selfDisposalMeasuresFlag", dao.initHandleSelf(TP_ID) == null ? "0" : "1");
			controller.setAttr("entrustDisposalMeasuresFlag", dao.initHandleList(TP_ID) == null || dao.initHandleList(TP_ID).size() == 0 ? "0" : "1");
			controller.setAttr("envFlag", dao.initEnv(TP_ID) == null  ? "0" : "1");
			controller.setAttr("lastInfoFlag", dao.initLastInfo(TP_ID) == null ? "0" : "1");
		}
	}
	
	private void apply2q(){
		String tpId = controller.getMyParam("TP_ID").toString();
		boolean check = dao.checkSub(tpId);
		if(check){
			boolean flag = dao.updateApply(tpId);
			boolean flagMain = dao.updatePlanMain(tpId);
			if(flag && flagMain){
				controller.setAttr("resFlag", "0");
				controller.setAttr("resMsg", "提交成功");
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("resMsg", "提交失败");
			}
		}else{
			controller.setAttr("resFlag", "3");
			controller.setAttr("resMsg", "基本信息,危险废物产生概况,危险废物转移情况,危险废物委托利用/处置措施为必录项");
		}
		
	}
	
	private void initBaseInfo(){
		String tpId = controller.getMyParam("TP_ID").toString();
		String epId = controller.getMyParam("EP_ID").toString();
		Map<String,Object> map = dao.initBaseInfo(epId,tpId);
		Map<String,Object> mapEpExtend = dao.queryEpExtend(tpId);
		if(mapEpExtend == null){
			String last_tpId = dao.checkLastYearId(tpId);
			if(!"".equals(last_tpId)){
				mapEpExtend = dao.queryEpExtend(last_tpId);
			}
		}
		controller.setAttr("initRes", map == null ? "" : map);
		controller.setAttr("initEpExtend", mapEpExtend == null ? "" : mapEpExtend);
	}
	
	private void saveBaseInfo(){
		String tpId = controller.getMyParam("TP_ID").toString();
		String epId = controller.getMyParam("EP_ID").toString();
		String TOTAL_INVESTMENT = controller.getMyParam("TOTAL_INVESTMENT").toString();
		String TOTAL_INVESTMENT_UNIT = controller.getMyParam("TOTAL_INVESTMENT_UNIT").toString();
		String TOTAL_OUTPUTVALUE = controller.getMyParam("TOTAL_OUTPUTVALUE").toString();
		String TOTAL_OUTPUTVALUE_UNIT = controller.getMyParam("TOTAL_OUTPUTVALUE_UNIT").toString();
		String FLOOR_AREA = controller.getMyParam("FLOOR_AREA").toString();
		String FLOOR_AREA_UNIT = controller.getMyParam("FLOOR_AREA_UNIT").toString();
		String EMPLOYEES_NUM = controller.getMyParam("EMPLOYEES_NUM").toString();
		String PRINCIPAL = controller.getMyParam("PRINCIPAL").toString();
		String LINKMAN = controller.getMyParam("LINKMAN").toString();
		String LINK_NUM = controller.getMyParam("LINK_NUM").toString();
		String FAX_TEL = controller.getMyParam("FAX_TEL").toString();
		String MAIL = controller.getMyParam("MAIL").toString();
		String WEBSITE = controller.getMyParam("WEBSITE").toString();
		String DEPARTMENT = controller.getMyParam("DEPARTMENT").toString();
		String DEPARTMENT_HEAD = controller.getMyParam("DEPARTMENT_HEAD").toString();
		String MANAGER = controller.getMyParam("MANAGER").toString();
		String SYS_MANAGER = controller.getMyParam("SYS_MANAGER").toString();
		String SYS_RESPONSIBILITY = controller.getMyParam("SYS_RESPONSIBILITY").toString();
		String SYS_OPERATION = controller.getMyParam("SYS_OPERATION").toString();
		String SYS_LEDGER = controller.getMyParam("SYS_LEDGER").toString();
		String SYS_TRAINING = controller.getMyParam("SYS_TRAINING").toString();
		String SYS_ACCIDENT = controller.getMyParam("SYS_ACCIDENT").toString();
		String MANAGEMENT_ORG = controller.getMyParam("MANAGEMENT_ORG").toString();
		List<Map<String, Object>> sons = controller.getMyParamList("sons");
		boolean flag = dao.saveBaseInfo(tpId,epId,TOTAL_INVESTMENT,TOTAL_INVESTMENT_UNIT,TOTAL_OUTPUTVALUE,TOTAL_OUTPUTVALUE_UNIT,FLOOR_AREA,FLOOR_AREA_UNIT,EMPLOYEES_NUM,PRINCIPAL,LINKMAN,LINK_NUM,FAX_TEL,MAIL,WEBSITE,DEPARTMENT,DEPARTMENT_HEAD,MANAGER,SYS_MANAGER,SYS_RESPONSIBILITY,SYS_OPERATION,SYS_LEDGER,SYS_TRAINING,SYS_ACCIDENT,MANAGEMENT_ORG);
		boolean flagSon = dao.saveBaseInfoList(tpId,epId,sons);
		if(flag && flagSon){
			controller.setAttr("resFlag", "0");
			controller.setAttr("resMsg", "提交成功");
			this.ws("baseInfo", "func_done");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("resMsg", "提交失败");
		}
	}
	
	private void initProductInfo(){
		String tpId = controller.getMyParam("TP_ID").toString();
		Map<String,Object> initProductInfo = dao.initProductInfo(tpId);
		List<Map<String,Object>> initProductOri = dao.initProductOri(tpId);
		List<Map<String,Object>> initProductEqu = dao.initProductEqu(tpId);
		List<Map<String,Object>> initProductOutput = dao.initProductOutput(tpId);
		String last_tpId = dao.checkLastYearId(tpId);
		if(initProductInfo == null){
			if(!"".equals(last_tpId)){
				initProductInfo = dao.initProductInfo(last_tpId);
			}
		}
		if(initProductOri.size() < 1){
			if(!"".equals(last_tpId)){
				initProductOri = dao.initProductOri(last_tpId);
			}
		}
		if(initProductEqu.size() < 1){
			if(!"".equals(last_tpId)){
				initProductEqu = dao.initProductEqu(last_tpId);
			}
		}
		if(initProductOutput.size() < 1){
			if(!"".equals(last_tpId)){
				initProductOutput = dao.initProductOutput(last_tpId);
			}
		}
		controller.setAttr("initProductInfo", initProductInfo == null ? "" : initProductInfo);
		controller.setAttr("ifsave", initProductInfo == null ? "0" : "1");
		controller.setAttr("initProductOri", initProductOri);
		controller.setAttr("initProductEqu", initProductEqu);
		controller.setAttr("initProductOutput", initProductOutput);
	}
	
	private void saveProductInfo(){
		String tpId = controller.getMyParam("TP_ID").toString();
		String ifsave = controller.getMyParam("ifsave").toString();
		boolean flag_info = false;
		boolean flag_ori = false;
		boolean flag_equ = false;
		boolean flag_output = false;
		if("1".equals(ifsave)){
			String epId = controller.getMyParam("EP_ID").toString();
			String PRODUCT_DESC = controller.getMyParam("PRODUCT_DESC").toString();
			List<Map<String, Object>> PRODUCT_ORI = controller.getMyParamList("PRODUCT_ORI");
			List<Map<String, Object>> PRODUCT_EQU = controller.getMyParamList("PRODUCT_EQU");
			List<Map<String, Object>> PRODUCT_OUTPUT = controller.getMyParamList("PRODUCT_OUTPUT");
			flag_info = dao.saveProductInfo(tpId,epId,PRODUCT_DESC);
			flag_ori = dao.saveProductOri(tpId,PRODUCT_ORI);
			flag_equ = dao.saveProductEqu(tpId,PRODUCT_EQU);
			flag_output = dao.saveProductOutput(tpId,PRODUCT_OUTPUT);
		}else{
			flag_info = dao.deleteProductInfo(tpId);
			flag_ori = dao.deleteProductOri(tpId);
			flag_equ = dao.deleteProductEqu(tpId);
			flag_output = dao.deleteProductOutput(tpId);
		}
		if(flag_info && flag_ori && flag_equ && flag_output){
			controller.setAttr("resFlag", "0");
			controller.setAttr("resMsg", "提交成功");
			if("1".equals(ifsave)){
				this.ws("productInfo", "func_done");
			}else{
				this.ws("productInfo", "func_class");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("resMsg", "提交失败");
		}
	}
	
	private void initOverview(){
		String tpId = controller.getMyParam("TP_ID").toString();
		List<Map<String,Object>> initOverviewList = dao.initOverviewList(tpId);
		if(initOverviewList.size() < 1){
			String last_tpId = dao.checkLastYearId(tpId);
			if(!"".equals(last_tpId)){
				initOverviewList = dao.initOverviewList(last_tpId);
			}
		}
		controller.setAttr("initOverviewList", initOverviewList);
		controller.setAttr("bigCategoryList", dao.getBigCategoryList());
		controller.setAttr("smallCategoryList", dao.getSmallCategoryList());
		controller.setAttr("smallCategoryType", dao.getSmallCategoryType());
		controller.setAttr("sumOverviewList", dao.sumOverviewList(tpId));
	}
	
	private void saveOverview(){
		String tpId = controller.getMyParam("TP_ID").toString();
		List<Map<String, Object>> overviewList = controller.getMyParamList("LIST");
		StringBuffer names = new StringBuffer();
		StringBuffer namebigIds = new StringBuffer();
		StringBuffer namebigsmallIds = new StringBuffer();
		for(int i = 0 ; i < overviewList.size() ; i++){
			if(i < overviewList.size() - 1){
				names.append("'").append(overviewList.get(i).get("D_NAME")).append(overviewList.get(i).get("UNIT")).append("'").append(",");
				namebigIds.append("'").append(overviewList.get(i).get("D_NAME")).append(overviewList.get(i).get("BIG_CATEGORY_ID")).append(overviewList.get(i).get("UNIT")).append("'").append(",");
				namebigsmallIds.append("'").append(overviewList.get(i).get("D_NAME")).append(overviewList.get(i).get("BIG_CATEGORY_ID")).append(overviewList.get(i).get("SAMLL_CATEGORY_ID")).append(overviewList.get(i).get("UNIT")).append("'").append(",");
			}else{
				names.append("'").append(overviewList.get(i).get("D_NAME")).append(overviewList.get(i).get("UNIT")).append("'");
				namebigIds.append("'").append(overviewList.get(i).get("D_NAME")).append(overviewList.get(i).get("BIG_CATEGORY_ID")).append(overviewList.get(i).get("UNIT")).append("'");
				namebigsmallIds.append("'").append(overviewList.get(i).get("D_NAME")).append(overviewList.get(i).get("BIG_CATEGORY_ID")).append(overviewList.get(i).get("SAMLL_CATEGORY_ID")).append(overviewList.get(i).get("UNIT")).append("'");
			}
		}
		boolean checkflag = true;
		checkflag = dao.checkIfupdateOverview(tpId,names.toString(),namebigIds.toString(),namebigsmallIds.toString());
		if(!checkflag){
			controller.setAttr("resFlag", "2");
			controller.setAttr("resMsg", "请检查表5，表6，表7的危险废物信息");
		}else{
			boolean flag = dao.saveOverview(tpId);
			boolean flag_list = dao.saveOverviewList(tpId,overviewList);
			if(flag && flag_list){
				controller.setAttr("resFlag", "0");
				controller.setAttr("resMsg", "提交成功");
				this.ws("overview", "func_done");
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("resMsg", "提交失败");
			}
		}
	}
	
	private void initReduction(){
		String tpId = controller.getMyParam("TP_ID").toString();
		Map<String,Object> initReduction = dao.initReduction(tpId);
		if(initReduction == null){
			String last_tpId = dao.checkLastYearId(tpId);
			if(!"".equals(last_tpId)){
				initReduction = dao.initReduction(last_tpId);
			}
		}
		controller.setAttr("initReduction", initReduction == null ? "" : initReduction);
	}
	
	private void saveReduction(){
		String tpId = controller.getMyParam("TP_ID").toString();
		String PLAN_REDUCTION = controller.getMyParam("PLAN_REDUCTION").toString();
		String MEASURES_REDUCTION = controller.getMyParam("MEASURES_REDUCTION").toString();
		boolean flag = dao.saveReduction(tpId,PLAN_REDUCTION,MEASURES_REDUCTION);
		if(flag){
			controller.setAttr("resFlag", "0");
			controller.setAttr("resMsg", "提交成功");
			this.ws("reduction", "func_done");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("resMsg", "提交失败");
		}
	}
	
	private void initTransfer(){
		String tpId = controller.getMyParam("TP_ID").toString();
		Map<String,Object> initTransfer = dao.initTransfer(tpId);
		List<Map<String,Object>> initProductFacility = dao.initProductFacility(tpId);
		List<Map<String,Object>> initProductCc = dao.initProductCc(tpId);
		List<Map<String,Object>> initProductYs = dao.initProductYs(tpId);
		List<Map<String,Object>> initEPys = dao.initEPys();
		List<Map<String,Object>> initOverviewList = dao.initOverviewList(tpId);
		String last_tpId = dao.checkLastYearId(tpId);
		if(initTransfer == null){
			if(!"".equals(last_tpId)){
				initTransfer = dao.initTransfer(last_tpId);
			}
		}
		if(initProductFacility.size() < 1){
			if(!"".equals(last_tpId)){
				initProductFacility = dao.initProductFacility(last_tpId);
			}
		}
		if(initProductCc.size() < 1){
			if(!"".equals(last_tpId)){
				initProductCc = dao.initProductCc(last_tpId);
			}
		}
		if(initProductYs.size() < 1){
			if(!"".equals(last_tpId)){
				initProductYs = dao.initProductYs(last_tpId);
			}
		}
		if(initOverviewList.size() < 1){
			if(!"".equals(last_tpId)){
				initOverviewList = dao.initOverviewList(last_tpId);
			}
		}
		controller.setAttr("initTransfer", initTransfer == null ? "" : initTransfer);
		controller.setAttr("initProductFacility", initProductFacility);
		controller.setAttr("initProductCc", initProductCc);
		controller.setAttr("initProductYs", initProductYs);
		controller.setAttr("initEPys", initEPys);
		controller.setAttr("initOverviewList", initOverviewList);
	}
	
	private void saveTransfer(){
		String tpId = controller.getMyParam("TP_ID").toString();
		String CC_1 = controller.getMyParam("CC_1").toString();
		String CC_2 = controller.getMyParam("CC_2").toString();
		String CC_3 = controller.getMyParam("CC_3").toString();
		String CC_4 = controller.getMyParam("CC_4").toString();
		String CC_5 = controller.getMyParam("CC_5").toString();
		String CC_PROCESS = controller.getMyParam("CC_PROCESS").toString();
		List<Map<String, Object>> TRANSFER_FACILITY = controller.getMyParamList("TRANSFER_FACILITY");
		List<Map<String, Object>> TRANSFER_CC = controller.getMyParamList("TRANSFER_CC");
		List<Map<String, Object>> TRANSFER_YS = controller.getMyParamList("TRANSFER_YS");
		boolean flag = dao.saveTransfer(tpId, CC_1, CC_2, CC_3, CC_4, CC_5, CC_PROCESS);
		boolean flag_f = dao.saveTransferFacility(tpId, TRANSFER_FACILITY);
		boolean flag_c = dao.saveTransferCc(tpId, TRANSFER_CC);
		boolean flag_y = dao.saveTransferYs(tpId, TRANSFER_YS);
		if(flag && flag_f && flag_c && flag_y){
			controller.setAttr("resFlag", "0");
			controller.setAttr("resMsg", "提交成功");
			this.ws("transfer", "func_done");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("resMsg", "提交失败");
		}
	}
	
	private void initHandleSelf(){
		String tpId = controller.getMyParam("TP_ID").toString();
		Map<String,Object> initHandleSelf = dao.initHandleSelf(tpId);
		List<Map<String,Object>> initHandleSelfList = dao.initHandleSelfList(tpId);
		List<Map<String,Object>> initOverviewList = dao.initOverviewList(tpId);
		String last_tpId = dao.checkLastYearId(tpId);
		if(initHandleSelf == null){
			if(!"".equals(last_tpId)){
				initHandleSelf = dao.initHandleSelf(last_tpId);
			}
		}
		if(initHandleSelfList.size() < 1){
			if(!"".equals(last_tpId)){
				initHandleSelfList = dao.initHandleSelfList(last_tpId);
			}
		}
		if(initOverviewList.size() < 1){
			if(!"".equals(last_tpId)){
				initOverviewList = dao.initOverviewList(last_tpId);
			}
		}
		controller.setAttr("initHandleSelf", initHandleSelf == null ? "" : initHandleSelf);
		controller.setAttr("ifsave", initHandleSelf == null ? "0" : "1");
		controller.setAttr("initHandleSelfList", initHandleSelfList);
		controller.setAttr("initOverviewList", initOverviewList);
	}
	
	private void saveHandleSelf(){
		String tpId = controller.getMyParam("TP_ID").toString();
		String ifsave = controller.getMyParam("ifsave").toString();
		if("1".equals(ifsave)){
			String FACILITY_NAME = controller.getMyParam("FACILITY_NAME").toString();
			String FACILITY_TYPE = controller.getMyParam("FACILITY_TYPE").toString();
			String FACILITY_ADDRESS = controller.getMyParam("FACILITY_ADDRESS").toString();
			String INVEST_SUM = controller.getMyParam("INVEST_SUM").toString();
			String INVEST_SUM_UNIT = controller.getMyParam("INVEST_SUM_UNIT").toString();
			String DESIGN = controller.getMyParam("DESIGN").toString();
			String DESIGN_TIME = controller.getMyParam("DESIGN_TIME").toString();
			String RUN_TIME = controller.getMyParam("RUN_TIME").toString();
			String RUN_MONEY = controller.getMyParam("RUN_MONEY").toString();
			String RUN_MONEY_UNIT = controller.getMyParam("RUN_MONEY_UNIT").toString();
			String FACILITY_SUM = controller.getMyParam("FACILITY_SUM").toString();
			String HANDLE_EFFECT = controller.getMyParam("HANDLE_EFFECT").toString();
			String DB_1 = controller.getMyParam("DB_1").toString();
			String DB_2 = controller.getMyParam("DB_2").toString();
			String DESC_CONTENT = controller.getMyParam("DESC_CONTENT").toString();
			String MEASURE = controller.getMyParam("MEASURE").toString();
			Map<String,String> map = new HashMap<String, String>();
			map.put("FACILITY_NAME", FACILITY_NAME);
			map.put("FACILITY_TYPE", FACILITY_TYPE);
			map.put("FACILITY_ADDRESS", FACILITY_ADDRESS);
			map.put("INVEST_SUM", INVEST_SUM);
			map.put("INVEST_SUM_UNIT", INVEST_SUM_UNIT);
			map.put("DESIGN", DESIGN);
			map.put("DESIGN_TIME", DESIGN_TIME);
			map.put("RUN_TIME", RUN_TIME);
			map.put("RUN_MONEY", RUN_MONEY);
			map.put("RUN_MONEY_UNIT", RUN_MONEY_UNIT);
			map.put("FACILITY_SUM", FACILITY_SUM);
			map.put("HANDLE_EFFECT", HANDLE_EFFECT);
			map.put("DB_1", DB_1);
			map.put("DB_2", DB_2);
			map.put("DESC_CONTENT", DESC_CONTENT);
			map.put("MEASURE", MEASURE);
			List<Map<String, Object>> HANDLE_LIST = controller.getMyParamList("HANDLE_LIST");
			boolean flag = dao.saveHandleSelf(tpId,map);
			boolean flag_handle = dao.saveHandleSelfList(tpId, HANDLE_LIST);
			if(flag && flag_handle){
				controller.setAttr("resFlag", "0");
				controller.setAttr("resMsg", "提交成功");
				this.ws("handleSelf", "func_done");
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("resMsg", "提交失败");
			}
		}
		else{
			boolean flag = dao.deleteHandleSelf(tpId);
			boolean flag_handle = dao.deleteHandleSelfList(tpId);
			if(flag && flag_handle){
				controller.setAttr("resFlag", "0");
				controller.setAttr("resMsg", "提交成功");
				this.ws("handleSelf", "func_class");
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("resMsg", "提交失败");
			}
		}
	}
	
	private void initHandle(){
		String tpId = controller.getMyParam("TP_ID").toString();
		List<Map<String,Object>> initHandleList = dao.initHandleList(tpId);
		List<Map<String,Object>> initOverviewList = dao.initOverviewList(tpId);
		List<Map<String,Object>> initEpCzList = dao.initEpCzList();
		String last_tpId = dao.checkLastYearId(tpId);
		if(initHandleList.size() < 1){
			if(!"".equals(last_tpId)){
				initHandleList = dao.initHandleList(last_tpId);
			}
		}
		if(initOverviewList.size() < 1){
			if(!"".equals(last_tpId)){
				initOverviewList = dao.initOverviewList(last_tpId);
			}
		}
		controller.setAttr("initHandleList", initHandleList);
		controller.setAttr("initOverviewList", initOverviewList);
		controller.setAttr("initEpCzList", initEpCzList);
		controller.setAttr("HANDLE_TYPE_LIST", dao.getHandleType());
		controller.setAttr("sumHandleList", dao.sumHandleList(tpId));
	}
	
	private void saveHandle(){
		String tpId = controller.getMyParam("TP_ID").toString();
		List<Map<String, Object>> handleList = controller.getMyParamList("LIST");
		boolean flag = dao.saveHandle(tpId);
		boolean flag_list = dao.saveHandleList(tpId,handleList);
		if(flag && flag_list){
			controller.setAttr("resFlag", "0");
			controller.setAttr("resMsg", "提交成功");
			this.ws("handle", "func_done");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("resMsg", "提交失败");
		}
	}
	
	private void initEnv(){
		String tpId = controller.getMyParam("TP_ID").toString();
		Map<String,Object> initEnv = dao.initEnv(tpId);
		if(initEnv == null){
			String last_tpId = dao.checkLastYearId(tpId);
			if(!"".equals(last_tpId)){
				initEnv = dao.initEnv(last_tpId);
			}
		}
		controller.setAttr("initEnv", initEnv == null ? "" : initEnv);
	}
	
	private void saveEnv(){
		String tpId = controller.getMyParam("TP_ID").toString();
		String ENV1 = controller.getMyParam("ENV_1").toString();
		String ENV2 = controller.getMyParam("ENV_2").toString();
		String ENV3 = controller.getMyParam("ENV_3").toString();
		String ENV4 = controller.getMyParam("ENV_4").toString();
		boolean flag = dao.saveEnv(tpId,ENV1,ENV2,ENV3,ENV4);
		if(flag){
			controller.setAttr("resFlag", "0");
			controller.setAttr("resMsg", "提交成功");
			this.ws("env","func_done");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("resMsg", "提交失败");
		}
	}
	
	private void initLastInfo(){
		String tpId = controller.getMyParam("TP_ID").toString();
		Map<String,Object> initLastInfo = dao.initLastInfo(tpId);
		if(initLastInfo == null){
			String last_tpId = dao.checkLastYearId(tpId);
			if(!"".equals(last_tpId)){
				initLastInfo = dao.initLastInfo(last_tpId);
			}
		}
		controller.setAttr("initLastInfo", initLastInfo == null ? "" : initLastInfo);
	}
	
	private void saveLastInfo(){
		String tpId = controller.getMyParam("TP_ID").toString();
		String li1 = controller.getMyParam("LI_1").toString();
		String li2 = controller.getMyParam("LI_2").toString();
		String c1 = controller.getMyParam("C_1").toString();
		String c2 = controller.getMyParam("C_2").toString();
		String c3 = controller.getMyParam("C_3").toString();
		String c4 = controller.getMyParam("C_4").toString();
		String c5 = controller.getMyParam("C_5").toString();
		String c6 = controller.getMyParam("C_6").toString();
		String c7 = controller.getMyParam("C_7").toString();
		String c8 = controller.getMyParam("C_8").toString();
		String c9 = controller.getMyParam("C_9").toString();
		boolean flag = dao.saveLastInfo(tpId,li1,li2,c1,c2,c3,c4,c5,c6,c7,c8,c9);
		if(flag){
			controller.setAttr("resFlag", "0");
			controller.setAttr("resMsg", "提交成功");
			this.ws("lastInfo", "func_done");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("resMsg", "提交失败");
		}
	}
	
	private void ws(String key,String value){
		for(MyWebSocket item: MyWebSocket.webSocketSet){  
            try {
            	if(item != null && item.wsMap !=null ){
            		String userId = item.wsMap.get("userId").toString();
                	if(userId.equals(controller.getMySession("userId").toString())){
                		item.sendMessage("{key:'"+key+"',value:'"+value+"'}");
                	}
            	}
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
		}
	}
	
	private void previewPlan(){
		String epId = controller.getMyParam("EP_ID").toString();
		String tpId = controller.getMyParam("TP_ID").toString();
		Map<String,Object> previewPlan = dao.previewPlan(epId,tpId);
		controller.setAttr("previewPlan", previewPlan == null ? "" : previewPlan);
	}
	
	private void getLastTBSum(){
		String epId = controller.getMyParam("EP_ID").toString();
		String EN_ID_CZ = controller.getMyParam("EN_ID_CZ").toString();
		String BIG_CATEGORY_ID = controller.getMyParam("BIG_CATEGORY_ID").toString();
		String SAMLL_CATEGORY_ID = controller.getMyParam("SAMLL_CATEGORY_ID").toString();
		String UNIT = controller.getMyParam("UNIT").toString();
		String last_unit_num = dao.getLastTBSum(epId,EN_ID_CZ,BIG_CATEGORY_ID,SAMLL_CATEGORY_ID,UNIT);
		controller.setAttr("last_unit_num", last_unit_num);
		controller.setAttr("resFlag", "0");
		controller.setAttr("resMsg", "成功");
	}
}
