package com.mine.rd.services.plan.pojo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.kit.DateKit;
import com.mine.pub.pojo.BaseDao;

public class PlanDao extends BaseDao {
	
	List<Record> cityList = CacheKit.get("mydict", "city_q");
	/**
	 * @author woody
	 * @date 20180902
	 * 方法：管理计划列表
	 */
	public Map<String, Object> queryEpList(int pn, int ps, Object searchContent, Object statusValue, List<Object> statusCache,String epId){
		String sql = "from Z_WOBO_PLAN_MAIN A , Z_PUB_DICT b where a.status = b.dict_id and b.id_main = '9' and b.status = '1' and a.ep_id = '"+epId+"' ";
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " and (A.EP_NAME like '%"+searchContent+"%' or A.EP_ID like '%"+searchContent+"%' or CONVERT(varchar(50), A.BEGINDATE, 126)  like '%"+searchContent+"%' or CONVERT(varchar(50), A.sysdate, 126)  like '%"+searchContent+"%') ";
		}
		if(statusValue != null && !"".equals(statusValue)){
			sql = sql + " and A.STATUS in ("+statusValue+")";
		}
		sql = sql + " order by a.sysdate desc ";
		Page<Record> page = Db.paginate(pn, ps, "select A.* ,b.dict_value statusname ", sql );
		List<Record> eps = page.getList();
		List<Map<String, Object>> epList = new ArrayList<>();
		Map<String, Object> resMap = new HashMap<>();
		if(eps != null){
			for(Record record : eps){
				Map<String, Object> map = new HashMap<>();
				map.put("TP_ID", record.get("TP_ID"));
				map.put("EP_ID", record.get("EP_ID"));
				map.put("EP_NAME", record.get("EP_NAME"));
				map.put("BEGINDATE", record.get("BEGINDATE"));
				map.put("ENDDATE", record.get("ENDDATE"));
				map.put("STATUS", record.get("STATUS"));
				map.put("STATUSNAME", record.get("statusname"));
				map.put("applyYear", DateKit.toStr(record.getDate("BEGINDATE"), "YYYY"));
				map.put("applyDate", DateKit.toStr(record.getDate("sysdate"), "YYYY-MM-dd"));
				epList.add(map);
			}
		}
		resMap.put("epList", epList);
		if(statusCache != null && !"".equals(statusCache)){
			resMap.put("statusList", super.queryDict("plan_status", "value", "text", statusCache));
		}else{
			resMap.put("statusList", super.queryDict("plan_status", "value", "text"));
		}
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	public int checkApply(String epId){
		String sql = "select count(1) as num from Z_WOBO_PLAN_MAIN where DATEDIFF(yyyy,BEGINDATE,GETDATE()) = 0 and ep_id = ? ";
		Record record = Db.findFirst(sql,epId);
		if(record != null){
			return record.getInt("num");
		}else{
			return 0;
		}
	}
	
	public Map<String,Object> createPlan(String epId,String epName) throws ParseException{
		Record record = new Record();
		record.set("TP_ID", super.getSeqId("Z_WOBO_PLAN_MAIN"));
		record.set("EP_ID", epId);
		record.set("EP_NAME", epName);
		record.set("STATUS", "00");
		String begindate = DateKit.toStr(super.getSysdate(), "yyyy-01-01");
		String enddate = DateKit.toStr(super.getSysdate(), "yyyy-12-31");
		record.set("BEGINDATE", begindate);
		record.set("ENDDATE",  enddate);
		record.set("sysdate", super.getSysdate());
		Db.save("Z_WOBO_PLAN_MAIN", record);
		return record != null && record.getColumns() != null ? record.getColumns() : null;
	}
	
	public Map<String,Object> checkApplyList(String TP_ID){
		Record record = Db.findFirst("select * from Z_WOBO_APPLY_LIST where BIZ_ID = ? ",TP_ID);
		return record != null && record.getColumns() != null ? record.getColumns() : null;
	}
	
	public String createApply(String tpId,String epId,String epName,String belongSepa){
		Record record = new Record();
		String id = super.getSeqId("Z_WOBO_APPLY_LIST");
		record.set("AYL_ID", id);
		record.set("BIZ_ID", tpId);
		record.set("BIZ_NAME", "管理计划");
		record.set("EP_ID", epId);
		record.set("EP_NAME", epName);
		record.set("BIZ_VERSION", 0);
		record.set("APPLY_DATE", super.getSysdate());
		record.set("STATUS", "00");
		record.set("BELONG_SEPA", belongSepa);
		record.set("sysdate", super.getSysdate());
		boolean flag = Db.save("Z_WOBO_APPLY_LIST", record);
		return flag ? id : "";
	}
	
	public boolean updateApply(String tpId){
		int flag = Db.update("update Z_WOBO_APPLY_LIST set STATUS = '01', BIZ_VERSION = BIZ_VERSION+1, APPLY_DATE = ? where BIZ_ID = ? ",getSysdate(),tpId);
		if(flag > 0){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean updatePlanMain(String tpId){
		int flag = Db.update("update Z_WOBO_PLAN_MAIN set STATUS = '01' where TP_ID = ? ",tpId);
		if(flag > 0){
			return true;
		}else{
			return false;
		}
	}
	
	public Map<String,Object> initBaseInfo(String epId,String tpId){
		Record record = Db.findFirst("select a.*,a.EP_NAME 'epName' from ENTERPRISE a where a.EP_ID = ? " ,epId);
		Record recordPlan = Db.findFirst("select * from Z_WOBO_PLAN_MAIN where tp_id = ? " , tpId);
		Map<String,Object> map = record.getColumns();
		StringBuffer sb = new StringBuffer();
		map.put("sbAdress", sb.append("天津市").append(convert(cityList, record.get("SB_ADRESS_Q"))).append(record.getStr("SB_ADRESS_J"))) ;
		sb.setLength(0);
		map.put("epAdress", sb.append("天津市").append(convert(cityList, record.get("EP_ADRESS_Q"))).append(record.getStr("EP_ADRESS_J"))) ;
		String industryCode = record.get("INDUSTRY_CODE") == null ? "" : record.get("INDUSTRY_CODE").toString();
		map.put("dictname", Db.queryStr("select dictname from EOS_DICT_ENTRY where dictid = ? ",industryCode));
		Date date = recordPlan.getDate("BEGINDATE");
		map.put("planYear",DateKit.toStr(date, "yyyy")+"年");
		return map;
	}
	
	public Map<String,Object> queryEpExtend(String epId){
		Record recordExtend =  Db.findFirst("select * from Z_WOBO_EP_EXTEND where ep_id = ? " , epId);
		Map<String,Object> map = null;
		if(recordExtend != null && recordExtend.get("EP_ID") != null){
			map = recordExtend.getColumns();
			List<Record> list = Db.find("select * from Z_WOBO_EP_PEOPLE where  EP_ID =? ",epId);
			List<Map<String,Object>> sons = new ArrayList<Map<String,Object>>();
			for(int i=0;i<list.size(); i++){
				sons.add(list.get(i).getColumns());
			}
			map.put("sons", sons);
		}
		return map;
	}
	
	public boolean saveBaseInfo(String epId,String TOTAL_INVESTMENT,String TOTAL_INVESTMENT_UNIT,String TOTAL_OUTPUTVALUE,String TOTAL_OUTPUTVALUE_UNIT,String FLOOR_AREA,String FLOOR_AREA_UNIT,String EMPLOYEES_NUM,String PRINCIPAL,String LINKMAN,String LINK_NUM,String FAX_TEL,String MAIL,String WEBSITE,String DEPARTMENT,String DEPARTMENT_HEAD,String MANAGER,String SYS_MANAGER,String SYS_RESPONSIBILITY,String SYS_OPERATION,String SYS_LEDGER,String SYS_TRAINING,String SYS_ACCIDENT,String MANAGEMENT_ORG){
		Record record = Db.findFirst("select count(1) num from Z_WOBO_EP_EXTEND where ep_id = ? ",epId);
		if(record != null){
			int num = record.getInt("num");
			if(num > 0){
				int resInt = Db.update("delete from Z_WOBO_EP_EXTEND where ep_id = ? ",epId);
				return resInt < 0 ? false : saveRecordBaseInfo(epId, TOTAL_INVESTMENT, TOTAL_INVESTMENT_UNIT, TOTAL_OUTPUTVALUE, TOTAL_OUTPUTVALUE_UNIT, FLOOR_AREA, FLOOR_AREA_UNIT, EMPLOYEES_NUM, PRINCIPAL, LINKMAN, LINK_NUM, FAX_TEL, MAIL, WEBSITE, DEPARTMENT, DEPARTMENT_HEAD, MANAGER, SYS_MANAGER, SYS_RESPONSIBILITY, SYS_OPERATION, SYS_LEDGER, SYS_TRAINING, SYS_ACCIDENT, MANAGEMENT_ORG);
			}else{
				return saveRecordBaseInfo(epId, TOTAL_INVESTMENT, TOTAL_INVESTMENT_UNIT, TOTAL_OUTPUTVALUE, TOTAL_OUTPUTVALUE_UNIT, FLOOR_AREA, FLOOR_AREA_UNIT, EMPLOYEES_NUM, PRINCIPAL, LINKMAN, LINK_NUM, FAX_TEL, MAIL, WEBSITE, DEPARTMENT, DEPARTMENT_HEAD, MANAGER, SYS_MANAGER, SYS_RESPONSIBILITY, SYS_OPERATION, SYS_LEDGER, SYS_TRAINING, SYS_ACCIDENT, MANAGEMENT_ORG);
			}
		}else{
			return saveRecordBaseInfo(epId, TOTAL_INVESTMENT, TOTAL_INVESTMENT_UNIT, TOTAL_OUTPUTVALUE, TOTAL_OUTPUTVALUE_UNIT, FLOOR_AREA, FLOOR_AREA_UNIT, EMPLOYEES_NUM, PRINCIPAL, LINKMAN, LINK_NUM, FAX_TEL, MAIL, WEBSITE, DEPARTMENT, DEPARTMENT_HEAD, MANAGER, SYS_MANAGER, SYS_RESPONSIBILITY, SYS_OPERATION, SYS_LEDGER, SYS_TRAINING, SYS_ACCIDENT, MANAGEMENT_ORG);
		}
	}
	
	private boolean saveRecordBaseInfo(String epId,String TOTAL_INVESTMENT,String TOTAL_INVESTMENT_UNIT,String TOTAL_OUTPUTVALUE,String TOTAL_OUTPUTVALUE_UNIT,String FLOOR_AREA,String FLOOR_AREA_UNIT,String EMPLOYEES_NUM,String PRINCIPAL,String LINKMAN,String LINK_NUM,String FAX_TEL,String MAIL,String WEBSITE,String DEPARTMENT,String DEPARTMENT_HEAD,String MANAGER,String SYS_MANAGER,String SYS_RESPONSIBILITY,String SYS_OPERATION,String SYS_LEDGER,String SYS_TRAINING,String SYS_ACCIDENT,String MANAGEMENT_ORG){
		Record baseInfo = new Record();
		baseInfo.set("EP_ID", epId);
		baseInfo.set("TOTAL_INVESTMENT", TOTAL_INVESTMENT);
		baseInfo.set("TOTAL_INVESTMENT_UNIT", TOTAL_INVESTMENT_UNIT);
		baseInfo.set("TOTAL_OUTPUTVALUE", TOTAL_OUTPUTVALUE);
		baseInfo.set("TOTAL_OUTPUTVALUE_UNIT", TOTAL_OUTPUTVALUE_UNIT);
		baseInfo.set("FLOOR_AREA", FLOOR_AREA);
		baseInfo.set("FLOOR_AREA_UNIT", FLOOR_AREA_UNIT);
		baseInfo.set("EMPLOYEES_NUM", EMPLOYEES_NUM);
		baseInfo.set("PRINCIPAL", PRINCIPAL);
		baseInfo.set("LINKMAN", LINKMAN);
		baseInfo.set("LINK_NUM", LINK_NUM);
		baseInfo.set("FAX_TEL", FAX_TEL);
		baseInfo.set("MAIL", MAIL);
		baseInfo.set("WEBSITE", WEBSITE);
		baseInfo.set("DEPARTMENT", DEPARTMENT);
		baseInfo.set("DEPARTMENT_HEAD", DEPARTMENT_HEAD);
		baseInfo.set("MANAGER", MANAGER);
		baseInfo.set("SYS_ACCIDENT", SYS_ACCIDENT);
		baseInfo.set("SYS_LEDGER", SYS_LEDGER);
		baseInfo.set("SYS_MANAGER", SYS_MANAGER);
		baseInfo.set("SYS_OPERATION", SYS_OPERATION);
		baseInfo.set("SYS_RESPONSIBILITY", SYS_RESPONSIBILITY);
		baseInfo.set("SYS_TRAINING", SYS_TRAINING);
		baseInfo.set("MANAGEMENT_ORG", MANAGEMENT_ORG);
		baseInfo.set("STATUS", "00");
		baseInfo.set("sysdate", getSysdate());
		return Db.save("Z_WOBO_EP_EXTEND", baseInfo);
	}
	
	public boolean saveBaseInfoList(String epId,List<Map<String, Object>> sons){
		Db.update("delete from Z_WOBO_EP_PEOPLE where ep_id = ? ",epId);
		boolean res = false;
		for(int i = 0 ; i < sons.size() ; i++){
			Record record = new Record();
			Map<String,Object> map = sons.get(i);
			record.set("EP_ID", epId);
			record.set("ID", i+1);
			record.set("TECHNICAL_DIRECTER",map.get("TECHNICAL_DIRECTER"));
			record.set("EDU_LEVEL",map.get("EDU_LEVEL"));
			res = Db.save("Z_WOBO_EP_PEOPLE", record);
		}
		return res;
	}
	
	public Map<String,Object> initProductInfo(String tpId){
		Record record = Db.findFirst("select * from Z_WOBO_PRODUCT_INFO where tp_id=? ",tpId);
		Map<String,Object> map = null;
		if(record !=null && record.getColumns() !=null ){
			map = record.getColumns();
		}
		return map;
	}
	
	public List<Map<String,Object>> initProductOri(String tpId){
		List<Record> records = Db.find("select * from Z_WOBO_PRODUCT_ORI where tp_id=? ",tpId);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i<records.size() ; i++){
			list.add(records.get(i).getColumns());
		}
		return list;
	}
	
	public List<Map<String,Object>> initProductEqu(String tpId){
		List<Record> records = Db.find("select * from Z_WOBO_PRODUCT_EQU where tp_id=? ",tpId);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i<records.size() ; i++){
			list.add(records.get(i).getColumns());
		}
		return list;
	}
	
	public List<Map<String,Object>> initProductOutput(String tpId){
		List<Record> records = Db.find("select * from Z_WOBO_PRODUCT_OUTPUT  where tp_id=? ",tpId);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i<records.size() ; i++){
			list.add(records.get(i).getColumns());
		}
		return list;
	}
	
	public boolean saveProductInfo(String tpId,String epId,String desc){
		Db.update("delete from Z_WOBO_PRODUCT_INFO where tp_id = ? " ,tpId);
		Record record = new Record();
		record.set("TP_ID", tpId);
		record.set("EP_ID", epId);
		record.set("PRODUCT_DESC", desc);
		record.set("STATUS", "00");
		record.set("sysdate", getSysdate());
		return Db.save("Z_WOBO_PRODUCT_INFO", record);
	}
	
	public boolean saveProductOri(String tpId,List<Map<String,Object>> list){
		Db.update("delete from Z_WOBO_PRODUCT_ORI where tp_id = ? " ,tpId);
		boolean res = false;
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			Record record = new Record();
			record.set("TP_ID", tpId);
			record.set("Id", i+1);
			record.set("NAME", map.get("NAME"));
			record.set("UNIT", map.get("UNIT"));
			record.set("LAST_NUM", map.get("LAST_NUM"));
			record.set("YEAR_NUM", map.get("YEAR_NUM"));
			res = Db.save("Z_WOBO_PRODUCT_ORI", record);
		}
		return res;
	}
	
	public boolean saveProductEqu(String tpId,List<Map<String,Object>> list){
		Db.update("delete from Z_WOBO_PRODUCT_EQU where tp_id = ? " ,tpId);
		boolean res = false;
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			Record record = new Record();
			record.set("TP_ID", tpId);
			record.set("Id", i+1);
			record.set("NAME", map.get("NAME"));
			record.set("UNIT", map.get("UNIT"));
			record.set("LAST_NUM", map.get("LAST_NUM"));
			record.set("YEAR_NUM", map.get("YEAR_NUM"));
			res = Db.save("Z_WOBO_PRODUCT_EQU", record);
		}
		return res;
	}
	
	public boolean saveProductOutput(String tpId,List<Map<String,Object>> list){
		Db.update("delete from Z_WOBO_PRODUCT_OUTPUT where tp_id = ? " ,tpId);
		boolean res = false;
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			Record record = new Record();
			record.set("TP_ID", tpId);
			record.set("Id", i+1);
			record.set("NAME", map.get("NAME"));
			record.set("UNIT", map.get("UNIT"));
			record.set("LAST_NUM", map.get("LAST_NUM"));
			record.set("YEAR_NUM", map.get("YEAR_NUM"));
			res = Db.save("Z_WOBO_PRODUCT_OUTPUT", record);
		}
		return res;
	}
	
	public List<Map<String,Object>> initOverviewList(String tpId){
		List<Record> records = Db.find("select * from Z_WOBO_OVERVIEWLIST where tp_id=? ",tpId);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i<records.size() ; i++){
			list.add(records.get(i).getColumns());
		}
		return list;
	}
	
	public List<Map<String,Object>> sumOverviewList(String tpId){
		List<Record> records = Db.find("select unit , sum(cast(last_num as numeric(18,2))) last_num_sum,sum(cast(year_num as numeric(18,2))) year_num_sum from Z_WOBO_OVERVIEWLIST where tp_id=? group by unit",tpId);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i<records.size() ; i++){
			Record record = records.get(i);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("last_num_sum", record.get("last_num_sum")+record.getStr("unit"));
			map.put("year_num_sum", record.get("year_num_sum")+record.getStr("unit"));
			list.add(map);
		}
		return list;
	}
	
	public List<Map<String,Object>> sumHandleList(String tpId){
		List<Record> records = Db.find("select unit , sum(cast(last_num as numeric(18,2))) last_num_sum,sum(cast(year_num as numeric(18,2))) year_num_sum from Z_WOBO_HANDLE_LIST where tp_id=? group by unit",tpId);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i<records.size() ; i++){
			Record record = records.get(i);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("last_num_sum", record.get("last_num_sum")+record.getStr("unit"));
			map.put("year_num_sum", record.get("year_num_sum")+record.getStr("unit"));
			list.add(map);
		}
		return list;
	}
	
	public boolean saveOverview(String tpId){
		Db.update("delete from Z_WOBO_OVERVIEW where tp_id = ? " ,tpId);
		Record record = new Record();
		record.set("TP_ID", tpId);
		record.set("STATUS", "00");
		record.set("sysdate", getSysdate());
		return Db.save("Z_WOBO_OVERVIEW", record);
	}
	
	public boolean saveOverviewList(String tpId,List<Map<String,Object>> list){
		Db.update("delete from Z_WOBO_OVERVIEWLIST where tp_id = ? " ,tpId);
		boolean res = false;
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			Record record = new Record();
			record.set("TP_ID", tpId);
			record.set("Id", i+1);
			record.set("D_NAME", map.get("D_NAME"));
			record.set("UNIT", map.get("UNIT"));
			record.set("LAST_NUM", map.get("LAST_NUM"));
			record.set("YEAR_NUM", map.get("YEAR_NUM"));
			record.set("BIG_CATEGORY_ID", map.get("BIG_CATEGORY_ID"));
			record.set("BIG_CATEGORY_NAME", map.get("BIG_CATEGORY_NAME"));
			record.set("SAMLL_CATEGORY_ID", map.get("SAMLL_CATEGORY_ID"));
			record.set("SAMLL_CATEGORY_NAME", map.get("SAMLL_CATEGORY_NAME"));
			record.set("W_SHAPE", map.get("W_SHAPE"));
			record.set("W_NAME", map.get("W_NAME"));
			record.set("CHARACTER", map.get("CHARACTER"));
			record.set("SOURCE_PROCESS", map.get("SOURCE_PROCESS"));
			res = Db.save("Z_WOBO_OVERVIEWLIST", record);
		}
		return res;
	}
	
	public List<Map<String,Object>> getBigCategoryList(){
		List<Record> list = Db.find("select * from BIG_CATEGORY where big_id like 'HW%' order by big_id");
		List<Map<String,Object>> resList = new ArrayList<Map<String,Object>>();
		for(int i = 0 ; i<list.size(); i++){
			resList.add(list.get(i).getColumns());
		}
		return resList;
	}
	
	public Map<String,List<Record>> getSmallCategoryList(){
		List<Record> list = Db.find("select * from BIG_CATEGORY where big_id like 'HW%' order by big_id");
		Map<String,List<Record>> map = new HashMap<String,List<Record>>();
		for(int i = 0 ; i<list.size(); i++){
			List<Record> smallList = Db.find("select * from SMALL_CATEGORY where big_id = ? ",list.get(i).getStr("BIG_ID"));
			map.put(list.get(i).getStr("BIG_ID"), smallList);
		}
		return map;
	}
	
	public Map<String,Object> initReduction(String tpId){
		Record record = Db.findFirst("select * from Z_WOBO_REDUCTION where tp_id=? ",tpId);
		Map<String,Object> map = null;
		if(record !=null && record.getColumns() !=null ){
			map = record.getColumns();
		}
		return map;
	}
	
	public boolean saveReduction(String tpId,String PLAN_REDUCTION,String MEASURES_REDUCTION){
		Db.update("delete from Z_WOBO_REDUCTION where tp_id = ? " ,tpId);
		Record record = new Record();
		record.set("TP_ID", tpId);
		record.set("PLAN_REDUCTION", PLAN_REDUCTION);
		record.set("MEASURES_REDUCTION", MEASURES_REDUCTION);
		record.set("STATUS", "00");
		record.set("sysdate", getSysdate());
		return Db.save("Z_WOBO_REDUCTION", record);
	}
	
	public Map<String,Object> initTransfer(String tpId){
		Record record = Db.findFirst("select * from Z_WOBO_TRANSFER where tp_id=? ",tpId);
		Map<String,Object> map = null;
		if(record !=null && record.getColumns() !=null ){
			map = record.getColumns();
		}
		return map;
	}
	
	public List<Map<String,Object>> initProductFacility(String tpId){
		List<Record> records = Db.find("select * from Z_WOBO_TRANSFER_FACILITY where tp_id=? ",tpId);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i<records.size() ; i++){
			list.add(records.get(i).getColumns());
		}
		return list;
	}
	
	public List<Map<String,Object>> initProductCc(String tpId){
		List<Record> records = Db.find("select * from Z_WOBO_TRANSFER_CC where tp_id=? ",tpId);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i<records.size() ; i++){
			list.add(records.get(i).getColumns());
		}
		return list;
	}
	
	public List<Map<String,Object>> initProductYs(String tpId){
		List<Record> records = Db.find("select * from Z_WOBO_TRANSFER_YS where tp_id=? ",tpId);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i<records.size() ; i++){
			list.add(records.get(i).getColumns());
		}
		return list;
	}
	
	public boolean saveTransfer(String tpId,String CC_1,String CC_2,String CC_3,String CC_4,String CC_5,String CC_PROCESS){
		Db.update("delete from Z_WOBO_TRANSFER where tp_id = ? " ,tpId);
		Record record = new Record();
		record.set("TP_ID", tpId);
		record.set("CC_1", CC_1);
		record.set("CC_2", CC_2);
		record.set("CC_3", CC_3);
		record.set("CC_4", CC_4);
		record.set("CC_5", CC_5);
		record.set("CC_PROCESS", CC_PROCESS);
		record.set("STATUS", "00");
		record.set("sysdate", getSysdate());
		return Db.save("Z_WOBO_TRANSFER", record);
	}
	
	public boolean saveTransferFacility(String tpId,List<Map<String,Object>> list){
		Db.update("delete from Z_WOBO_TRANSFER_FACILITY where tp_id = ? " ,tpId);
		boolean res = false;
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			Record record = new Record();
			record.set("TP_ID", tpId);
			record.set("Id", i+1);
			record.set("NAME", map.get("NAME"));
			record.set("STORE", map.get("STORE"));
			record.set("UNIT", map.get("UNIT"));
			record.set("NUM", map.get("NUM"));
			record.set("NUM_UNIT", map.get("NUM_UNIT"));
			record.set("AREA", map.get("AREA"));
			record.set("AREA_UNIT", map.get("AREA_UNIT"));
			record.set("TYPE", map.get("TYPE"));
			res = Db.save("Z_WOBO_TRANSFER_FACILITY", record);
		}
		return res;
	}
	
	public boolean saveTransferCc(String tpId,List<Map<String,Object>> list){
		Db.update("delete from Z_WOBO_TRANSFER_CC where tp_id = ? " ,tpId);
		boolean res = false;
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			Record record = new Record();
			record.set("TP_ID", tpId);
			record.set("Id", i+1);
			record.set("D_NAME", map.get("D_NAME"));
			record.set("BIG_CATEGORY_ID", map.get("BIG_CATEGORY_ID"));
			record.set("BIG_CATEGORY_NAME", map.get("BIG_CATEGORY_NAME"));
			record.set("STORE_REASON", map.get("STORE_REASON"));
			record.set("STORE_PLAN", map.get("STORE_PLAN"));
			record.set("STORE_PLAN_UNIT", map.get("STORE_PLAN_UNIT"));
			record.set("STORE_LAST", map.get("STORE_LAST"));
			record.set("STORE_LAST_UNIT", map.get("STORE_LAST_UNIT"));
			record.set("STORE_LASTSUM", map.get("STORE_LASTSUM"));
			record.set("STORE_LASTSUM_UNIT", map.get("STORE_LASTSUM_UNIT"));
			res = Db.save("Z_WOBO_TRANSFER_CC", record);
		}
		return res;
	}
	
	public boolean saveTransferYs(String tpId,List<Map<String,Object>> list){
		Db.update("delete from Z_WOBO_TRANSFER_YS where tp_id = ? " ,tpId);
		boolean res = false;
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			Record record = new Record();
			record.set("TP_ID", tpId);
			record.set("Id", i+1);
			record.set("EN_ID_YS", map.get("EN_ID_YS"));
			record.set("EN_NAME_YS", map.get("EN_NAME_YS"));
			record.set("YS_ZZ", map.get("YS_ZZ"));
			record.set("YS_1", map.get("YS_1"));
			record.set("YS_2", map.get("YS_2"));
			record.set("YS_3", map.get("YS_3"));
			record.set("YS_PROCESS", map.get("YS_PROCESS"));
			res = Db.save("Z_WOBO_TRANSFER_YS", record);
		}
		return res;
	}
	
	public Map<String,Object> initHandleSelf(String tpId){
		Record record = Db.findFirst("select * from Z_WOBO_HANDLE_SELF where tp_id=? ",tpId);
		Map<String,Object> map = null;
		if(record !=null && record.getColumns() !=null ){
			map = record.getColumns();
		}
		return map;
	}
	
	public List<Map<String,Object>> initHandleSelfList(String tpId){
		List<Record> records = Db.find("select * from Z_WOBO_HANDLESELF_LIST where tp_id=? ",tpId);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i<records.size() ; i++){
			list.add(records.get(i).getColumns());
		}
		return list;
	}
	
	public boolean saveHandleSelf(String tpId,Map<String,String> map){
		Db.update("delete from Z_WOBO_HANDLE_SELF where tp_id = ? " ,tpId);
		Record record = new Record();
		record.set("TP_ID", tpId);
		record.set("FACILITY_NAME", map.get("FACILITY_NAME"));
		record.set("FACILITY_TYPE", map.get("FACILITY_TYPE"));
		record.set("FACILITY_ADDRESS", map.get("FACILITY_ADDRESS"));
		record.set("INVEST_SUM", map.get("INVEST_SUM"));
		record.set("INVEST_SUM_UNIT", map.get("INVEST_SUM_UNIT"));
		record.set("DESIGN", map.get("DESIGN"));
		record.set("DESIGN_TIME", map.get("DESIGN_TIME"));
		record.set("RUN_TIME", map.get("RUN_TIME"));
		record.set("RUN_MONEY", map.get("RUN_MONEY"));
		record.set("RUN_MONEY_UNIT", map.get("RUN_MONEY_UNIT"));
		record.set("FACILITY_SUM", map.get("FACILITY_SUM"));
		record.set("HANDLE_EFFECT", map.get("HANDLE_EFFECT"));
		record.set("DB_1", map.get("DB_1"));
		record.set("DB_2", map.get("DB_2"));
		record.set("DESC_CONTENT", map.get("DESC_CONTENT"));
		record.set("MEASURE", map.get("MEASURE"));
		record.set("STATUS", "00");
		record.set("sysdate", getSysdate());
		return Db.save("Z_WOBO_HANDLE_SELF", record);
	}
	
	public boolean saveHandleSelfList(String tpId,List<Map<String,Object>> list){
		Db.update("delete from Z_WOBO_HANDLESELF_LIST where tp_id = ? " ,tpId);
		boolean res = false;
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			Record record = new Record();
			record.set("TP_ID", tpId);
			record.set("Id", i+1);
			record.set("D_NAME", map.get("D_NAME"));
			record.set("STORE_YEAR", map.get("STORE_YEAR"));
			record.set("STORE_PLAN_UNIT", map.get("STORE_PLAN_UNIT"));
			record.set("STORE_LAST", map.get("STORE_LAST"));
			record.set("STORE_LAST_UNIT", map.get("STORE_LAST_UNIT"));
			res = Db.save("Z_WOBO_HANDLESELF_LIST", record);
		}
		return res;
	}
	
	public List<Map<String,Object>> initHandleList(String tpId){
		List<Record> records = Db.find("select * from Z_WOBO_HANDLE_LIST where tp_id=? ",tpId);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i<records.size() ; i++){
			list.add(records.get(i).getColumns());
		}
		return list;
	}
	
	public List<Map<String,Object>> initEpCzList(){
		List<Record> records = Db.find("select EP_ID EN_ID_CZ, EP_NAME EN_NAME_CZ from ENTERPRISE where IF_HANDLE = '1' and status = '2' ");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i<records.size() ; i++){
			list.add(records.get(i).getColumns());
		}
		return list;
	}
	
	public boolean saveHandle(String tpId){
		Db.update("delete from Z_WOBO_HANDLE_MAIN where tp_id = ? " ,tpId);
		Record record = new Record();
		record.set("TP_ID", tpId);
		record.set("STATUS", "00");
		record.set("sysdate", getSysdate());
		return Db.save("Z_WOBO_HANDLE_MAIN", record);
	}
	
	public boolean saveHandleList(String tpId,List<Map<String,Object>> list){
		Db.update("delete from Z_WOBO_HANDLE_LIST where tp_id = ? " ,tpId);
		boolean res = false;
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			Record record = new Record();
			record.set("TP_ID", tpId);
			record.set("Id", i+1);
			record.set("EN_ID_CZ", map.get("EN_ID_CZ"));
			String ep_name = Db.queryStr("select ep_name from ENTERPRISE where ep_id = ?",map.get("EN_ID_CZ"));
			record.set("EN_NAME_CZ", ep_name);
			record.set("LINCENSE_NO", map.get("LINCENSE_NO"));
			record.set("D_NAME", map.get("D_NAME"));
			record.set("BIG_CATEGORY_ID", map.get("BIG_CATEGORY_ID"));
			record.set("SAMLL_CATEGORY_ID", map.get("SAMLL_CATEGORY_ID"));
			record.set("HANDLE_TYPE", map.get("HANDLE_TYPE"));
			record.set("YEAR_NUM", map.get("YEAR_NUM"));
			record.set("LAST_NUM", map.get("LAST_NUM"));
			record.set("UNIT", map.get("UNIT"));
			res = Db.save("Z_WOBO_HANDLE_LIST", record);
		}
		return res;
	}
}
