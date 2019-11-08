package com.mine.rd.services.lab.service;

import java.sql.SQLException;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.lab.pojo.LabDao;

public class LabService extends BaseService{
	private int pn = 0;
	private int ps = 0;
	private LabDao dao = new LabDao();
	public LabService(BaseController controller) {
		super(controller);
	}

	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	if("initEp".equals(getLastMethodName(7))){
	            		initEp();
	            	}
	            	else if("initEp".equals(getLastMethodName(7))){
	            		initEp();
	            	}
	            	else if("initEpEdit".equals(getLastMethodName(7))){
	            		initEpEdit();
	            	}
	            	else if("epEdit".equals(getLastMethodName(7))){
	            		epEdit();
	            	}
	            	else if("initFlow".equals(getLastMethodName(7))){
	            		initFlow();
	            	}
	            	else if("addFlow".equals(getLastMethodName(7))){
	            		addFlow();
	            	}
	            	else if("getFlowList".equals(getLastMethodName(7))){
	            		getFlowList();
	            	}
	            	else if("initStock".equals(getLastMethodName(7))){
	            		initStock();
	            	}
	            	else if("updatePwd".equals(getLastMethodName(7))){
	            		updatePwd();
	            	}
	            	else if("updateTel".equals(getLastMethodName(7))){
	            		updateTel();
	            	}
	            	else if("initFlowEp".equals(getLastMethodName(7))){
	            		initFlowEp();
	            	}
	            	else if("addFlowEp".equals(getLastMethodName(7))){
	            		addFlowEp();
	            	}
	            	else if("getFlowEpList".equals(getLastMethodName(7))){
	            		getFlowEpList();
	            	}
	            	else if("getFlowEpOutList".equals(getLastMethodName(7))){
	            		getFlowEpOutList();
	            	}
	            	else if("getFlowEpInList".equals(getLastMethodName(7))){
	            		getFlowEpInList();
	            	}
	            	else if("initFlowTranfer".equals(getLastMethodName(7))){
	            		initFlowTranfer();
	            	}
	            	else if("initFlowTranferIn".equals(getLastMethodName(7))){
	            		initFlowTranferIn();
	            	}
	            	else if("addFlowEpOut".equals(getLastMethodName(7))){
	            		addFlowEpOut();
	            	}
	            	else if("addFlowEpIn".equals(getLastMethodName(7))){
	            		addFlowEpIn();
	            	}
	            	else if("queryEp2List".equals(getLastMethodName(7))){
	            		queryEp2List();
	            	}
	            	else if("queryEp1List".equals(getLastMethodName(7))){
	            		queryEp1List();
	            	}
	            	else if("updateEpStatus".equals(getLastMethodName(7))){
	            		updateEpStatus();
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
	
	private void initEp(){
		String userId = controller.getMySession("userId").toString();
		controller.setAttr("user",dao.initEp(userId));
		controller.setAttr("resFlag", "0");
	}
	
	private void initEpEdit(){
		String userId = controller.getMySession("userId").toString();
		Map<String,Object> user = dao.initEp(userId);
		controller.setAttr("user",user);
		if("1".equals(user.get("TYPE"))){
			controller.setAttr("epList", dao.queryEpList());
		}
		controller.setAttr("belongSepaList", dao.queryOrganList());
		controller.setAttr("resFlag", "0");
	}
	
	private void epEdit(){
		Map<String, Object> map = controller.getMyParamMap("user");
		dao.epEdit(map);
		controller.setAttr("resFlag", "0");
	}
	
	private void initFlow(){
		String id = controller.getMyParam("id").toString();
		controller.setAttr("item",dao.initFlow(id));
		controller.setAttr("resFlag", "0");
	}
	
	private void initFlowEp(){
		String id = controller.getMyParam("id") == null ? "" : controller.getMyParam("id").toString();
		if(!"".equals(id)){
			controller.setAttr("item",dao.initFlowEp(id));
		}
		String epId = controller.getMySession("epId").toString();
		controller.setAttr("epBelongList",dao.queryBelongEpList(epId));
		controller.setAttr("resFlag", "0");
	}
	
	private void initFlowTranfer(){
		String id = controller.getMyParam("id") == null ? "" : controller.getMyParam("id").toString();
		if(!"".equals(id)){
			Map<String,Object> item = dao.initFlowEpOut(id);
			controller.setAttr("item",item);
			controller.setAttr("ysitem", dao.getYs(item.get("TP_ID").toString()));
		}
		String epId = controller.getMySession("epId").toString();
		controller.setAttr("planList",dao.queryPlanList(epId));
		controller.setAttr("resFlag", "0");
	}
	
	private void initFlowTranferIn(){
		String id = controller.getMyParam("id") == null ? "" : controller.getMyParam("id").toString();
		Map<String,Object> item = dao.initFlowEpIn(id);
		controller.setAttr("item",item);
		controller.setAttr("resFlag", "0");
	}
	
	private void addFlow(){
		Map<String, Object> map = controller.getMyParamMap("item");
		String userId = controller.getMySession("userId").toString();
		Map<String, Object> ep = dao.initEp(userId);
		map.put("EP_ID", ep.get("EP_ID"));
		if(map.get("ID") == null || "".equals(map.get("ID"))){
			if(dao.saveStock(map) && dao.addFlow(map)){
				controller.setAttr("item", map);
				controller.setAttr("resFlag", "0");
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "保存失败");
			}
		}
		else{
			if(dao.updateStock(map) && dao.updateFlow(map)){
				controller.setAttr("item", map);
				controller.setAttr("resFlag", "0");
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "更新失败");
			}
		}
	}
	
	private void addFlowEp(){
		Map<String, Object> map = controller.getMyParamMap("item");
		String userId = controller.getMySession("userId").toString();
		Map<String, Object> ep = dao.initEp(userId);
		map.put("EN_ID_CZ", ep.get("EP_ID"));
		map.put("EN_NAME_CZ", ep.get("EP_NAME"));
		map.put("EN_TEL_CZ", ep.get("TEL"));
		map.put("EP_ID", ep.get("EP_ID"));
		if("1".equals(map.get("TYPE")) || dao.checkCar(map)){
			if(map.get("ID") == null || "".equals(map.get("ID"))){
				if(dao.saveStockEp(map) && dao.addFlowEp(map)){
					controller.setAttr("item", map);
					controller.setAttr("resFlag", "0");
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "保存失败");
				}
			}
			else{
				if(dao.updateStockEp(map) && dao.updateFlowEp(map)){
					controller.setAttr("item", map);
					controller.setAttr("resFlag", "0");
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "更新失败");
				}
			}
		}else{
			controller.setAttr("resFlag", "2");
			controller.setAttr("msg", "该车没有在平台注册");
		}
	}
	
	private void addFlowEpOut(){
		Map<String, Object> map = controller.getMyParamMap("item");
		String userId = controller.getMySession("userId").toString();
		Map<String, Object> ep = dao.initEp(userId);
		map.put("EN_ID_CS", ep.get("EP_ID"));
		map.put("EN_NAME_CS", ep.get("EP_NAME"));
		map.put("EN_TEL_CS", ep.get("TEL"));
		map.put("CSY", ep.get("NAME"));
		if(dao.checkCar(map)){
			if(map.get("ID") == null || "".equals(map.get("ID"))){
				if(dao.addFlowEpOut(map)){
					controller.setAttr("item", map);
					controller.setAttr("resFlag", "0");
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "保存失败");
				}
			}
			else{
				if(dao.updateFlowEpOut(map)){
					controller.setAttr("item", map);
					controller.setAttr("resFlag", "0");
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "更新失败");
				}
			}
		}else{
			controller.setAttr("resFlag", "2");
			controller.setAttr("msg", "该车没有在平台注册");
		}
	}
	
	private void addFlowEpIn(){
		Map<String, Object> map = controller.getMyParamMap("item");
		String userId = controller.getMySession("userId").toString();
		Map<String, Object> ep = dao.initEp(userId);
		map.put("EN_TEL_CZ", ep.get("TEL"));
		map.put("CZY", ep.get("NAME"));
		if(dao.updateStockEpInByType1(map) && dao.updateStockEpInByType2(map) && dao.updateFlowEpIn(map)){
			controller.setAttr("item", map);
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "更新失败");
		}
	}
	
	private void getFlowList(){
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		Object searchContent = controller.getMyParam("searchContent");
		String userId = controller.getMySession("userId").toString();
		Map<String, Object> ep = dao.initEp(userId);
		controller.setAttrs(dao.getFlowList(pn, ps,ep.get("EP_ID").toString(),searchContent));
		controller.setAttr("resFlag", "0");
	}
	
	private void getFlowEpList(){
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		Object searchContent = controller.getMyParam("searchContent");
		String epId = controller.getMySession("epId").toString();
		controller.setAttrs(dao.getFlowEpList(pn, ps,epId,searchContent));
		controller.setAttr("resFlag", "0");
	}
	
	private void getFlowEpOutList(){
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		Object searchContent = controller.getMyParam("searchContent");
		String epId = controller.getMySession("epId").toString();
		controller.setAttrs(dao.getFlowEpOutList(pn, ps,epId,searchContent));
		controller.setAttr("resFlag", "0");
	}
	
	private void getFlowEpInList(){
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		Object searchContent = controller.getMyParam("searchContent");
		String epId = controller.getMySession("epId").toString();
		if(controller.getMyParam("status") != null && "02".equals(controller.getMyParam("status"))){
			controller.setAttrs(dao.getFlowEpInListFor02(pn, ps,epId,searchContent));
		}else{
			controller.setAttrs(dao.getFlowEpInList(pn, ps,epId,searchContent));
		}
		controller.setAttr("resFlag", "0");
	}
	
	private void initStock(){
		String epId = controller.getMyParam("epId").toString();
		controller.setAttr("item",dao.initStock(epId));
		controller.setAttr("resFlag", "0");
	}
	
	private void updatePwd(){
		String userId = controller.getMySession("userId").toString();
		Map<String, Object> map = controller.getMyParamMap("user");
		map.put("ID", userId);
		controller.setAttr("item",dao.updatePwd(map));
		controller.setAttr("resFlag", "0");
	}
	
	private void updateTel(){
		String userId = controller.getMySession("userId").toString();
		Map<String, Object> map = controller.getMyParamMap("user");
		map.put("ID", userId);
		dao.updateTel(map);
		controller.setAttr("resFlag", "0");
	}
	
	private void queryEp2List(){
		controller.setAttrs(dao.queryEp2List());
	}
	private void queryEp1List(){
		controller.setAttrs(dao.queryEp1List());
	}
	
	private void updateEpStatus(){
		String epId = controller.getMyParam("epId").toString();
		if(dao.updateEpStatus(epId) > 0){
			controller.setAttr("resFlag", "0");
			controller.setAttr("msg", "操作成功");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "操作失败");
		}
	}
}
