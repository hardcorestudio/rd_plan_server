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
//		String sql = "select count(1) as num from Z_WOBO_PLAN_MAIN where DATEDIFF(yyyy,BEGINDATE,DATEADD(MONTH,1,GETDATE())) = 0 and ep_id = ? ";
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
//		String sysdate_str =DateKit.toStr(super.getSysdate(), "yyyy-MM-dd");
//		Date sysdate_date = DateKit.toDate(sysdate_str, "yyyy-MM-dd");
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(sysdate_date);
//		calendar.add(Calendar.MONTH, 1);
//		Date date_new = calendar.getTime();
//		String begindate = DateKit.toStr(date_new, "yyyy-01-01");
//		String enddate = DateKit.toStr(date_new, "yyyy-12-31");
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
		sb = new StringBuffer("");
		map.put("epAdress", sb.append("天津市").append(convert(cityList, record.get("EP_ADRESS_Q"))).append(record.getStr("EP_ADRESS_J"))) ;
		String industryCode = record.get("INDUSTRY_CODE") == null ? "" : record.get("INDUSTRY_CODE").toString();
		map.put("dictname", Db.queryStr("select dictname from EOS_DICT_ENTRY where dictid = ? ",industryCode));
		Date date = recordPlan.getDate("BEGINDATE");
		map.put("planYear",DateKit.toStr(date, "yyyy")+"年");
		return map;
	}
	
	public Map<String,Object> queryEpExtend(String tpId){
		Record recordExtend =  Db.findFirst("select * from Z_WOBO_EP_EXTEND where tp_id = ? " , tpId);
		Map<String,Object> map = null;
		if(recordExtend != null && recordExtend.get("EP_ID") != null){
			map = recordExtend.getColumns();
			List<Record> list = Db.find("select * from Z_WOBO_EP_PEOPLE where  tp_ID =? ",tpId);
			List<Map<String,Object>> sons = new ArrayList<Map<String,Object>>();
			for(int i=0;i<list.size(); i++){
				sons.add(list.get(i).getColumns());
			}
			map.put("sons", sons);
		}
		return map;
	}
	
	public boolean saveBaseInfo(String tpId,String epId,String TOTAL_INVESTMENT,String TOTAL_INVESTMENT_UNIT,String TOTAL_OUTPUTVALUE,String TOTAL_OUTPUTVALUE_UNIT,String FLOOR_AREA,String FLOOR_AREA_UNIT,String EMPLOYEES_NUM,String PRINCIPAL,String LINKMAN,String LINK_NUM,String FAX_TEL,String MAIL,String WEBSITE,String DEPARTMENT,String DEPARTMENT_HEAD,String MANAGER,String SYS_MANAGER,String SYS_RESPONSIBILITY,String SYS_OPERATION,String SYS_LEDGER,String SYS_TRAINING,String SYS_ACCIDENT,String MANAGEMENT_ORG){
		Record record = Db.findFirst("select count(1) num from Z_WOBO_EP_EXTEND where tp_id = ? ",tpId);
		if(record != null){
			int num = record.getInt("num");
			if(num > 0){
				int resInt = Db.update("delete from Z_WOBO_EP_EXTEND where tp_id = ? ",tpId);
				return resInt < 0 ? false : saveRecordBaseInfo(tpId,epId, TOTAL_INVESTMENT, TOTAL_INVESTMENT_UNIT, TOTAL_OUTPUTVALUE, TOTAL_OUTPUTVALUE_UNIT, FLOOR_AREA, FLOOR_AREA_UNIT, EMPLOYEES_NUM, PRINCIPAL, LINKMAN, LINK_NUM, FAX_TEL, MAIL, WEBSITE, DEPARTMENT, DEPARTMENT_HEAD, MANAGER, SYS_MANAGER, SYS_RESPONSIBILITY, SYS_OPERATION, SYS_LEDGER, SYS_TRAINING, SYS_ACCIDENT, MANAGEMENT_ORG);
			}else{
				return saveRecordBaseInfo(tpId,epId, TOTAL_INVESTMENT, TOTAL_INVESTMENT_UNIT, TOTAL_OUTPUTVALUE, TOTAL_OUTPUTVALUE_UNIT, FLOOR_AREA, FLOOR_AREA_UNIT, EMPLOYEES_NUM, PRINCIPAL, LINKMAN, LINK_NUM, FAX_TEL, MAIL, WEBSITE, DEPARTMENT, DEPARTMENT_HEAD, MANAGER, SYS_MANAGER, SYS_RESPONSIBILITY, SYS_OPERATION, SYS_LEDGER, SYS_TRAINING, SYS_ACCIDENT, MANAGEMENT_ORG);
			}
		}else{
			return saveRecordBaseInfo(tpId,epId, TOTAL_INVESTMENT, TOTAL_INVESTMENT_UNIT, TOTAL_OUTPUTVALUE, TOTAL_OUTPUTVALUE_UNIT, FLOOR_AREA, FLOOR_AREA_UNIT, EMPLOYEES_NUM, PRINCIPAL, LINKMAN, LINK_NUM, FAX_TEL, MAIL, WEBSITE, DEPARTMENT, DEPARTMENT_HEAD, MANAGER, SYS_MANAGER, SYS_RESPONSIBILITY, SYS_OPERATION, SYS_LEDGER, SYS_TRAINING, SYS_ACCIDENT, MANAGEMENT_ORG);
		}
	}
	
	private boolean saveRecordBaseInfo(String tpId,String epId,String TOTAL_INVESTMENT,String TOTAL_INVESTMENT_UNIT,String TOTAL_OUTPUTVALUE,String TOTAL_OUTPUTVALUE_UNIT,String FLOOR_AREA,String FLOOR_AREA_UNIT,String EMPLOYEES_NUM,String PRINCIPAL,String LINKMAN,String LINK_NUM,String FAX_TEL,String MAIL,String WEBSITE,String DEPARTMENT,String DEPARTMENT_HEAD,String MANAGER,String SYS_MANAGER,String SYS_RESPONSIBILITY,String SYS_OPERATION,String SYS_LEDGER,String SYS_TRAINING,String SYS_ACCIDENT,String MANAGEMENT_ORG){
		Record baseInfo = new Record();
		baseInfo.set("TP_ID", tpId);
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
	
	public boolean saveBaseInfoList(String tpId,String epId,List<Map<String, Object>> sons){
		Db.update("delete from Z_WOBO_EP_PEOPLE where tp_id = ? ",tpId);
		boolean res = false;
		for(int i = 0 ; i < sons.size() ; i++){
			Record record = new Record();
			Map<String,Object> map = sons.get(i);
			record.set("TP_ID", tpId);
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
	
	public boolean deleteProductInfo(String tpId){
		Db.update("delete from Z_WOBO_PRODUCT_INFO where tp_id = ? " ,tpId);
		return true;
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
	
	public boolean deleteProductOri(String tpId){
		Db.update("delete from Z_WOBO_PRODUCT_ORI where tp_id = ? " ,tpId);
		return true;
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
	
	public boolean deleteProductEqu(String tpId){
		Db.update("delete from Z_WOBO_PRODUCT_EQU where tp_id = ? " ,tpId);
		return true;
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
	
	public boolean deleteProductOutput(String tpId){
		Db.update("delete from Z_WOBO_PRODUCT_OUTPUT where tp_id = ? " ,tpId);
		return true;
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
			record.set("BIG_CATEGORY_NAME", getBigCategoryName(map.get("BIG_CATEGORY_ID").toString()));
			record.set("SAMLL_CATEGORY_ID", map.get("SAMLL_CATEGORY_ID"));
			record.set("SAMLL_CATEGORY_NAME", getSmallCategoryName(map.get("SAMLL_CATEGORY_ID").toString()));
			record.set("W_SHAPE", map.get("W_SHAPE"));
			record.set("W_NAME", map.get("W_NAME"));
			record.set("CHARACTER", map.get("CHARACTER"));
			record.set("SOURCE_PROCESS", map.get("SOURCE_PROCESS"));
			res = Db.save("Z_WOBO_OVERVIEWLIST", record);
		}
		return res;
	}
	
	public String getBigCategoryName(String id){
		return Db.queryStr("select big_name from BIG_CATEGORY where big_id = ? ",id);
	}
	
	public String getSmallCategoryName(String id){
		return Db.queryStr("select small_name from SMALL_CATEGORY where small_id = ? ",id);
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
	
	public List<String> getSmallCategoryType(){
		List<Record> list = Db.find("select text from Z_WOBO_TYPE where status = '1' and type='category' ");
		List<String> types = new ArrayList<String>();
		for(int i = 0 ; i<list.size(); i++){
			types.add(list.get(i).getStr("text"));
		}
		return types;
	}
	
	public List<String> getHandleType(){
		List<Record> list = Db.find("select text from Z_WOBO_TYPE where status = '1' and type='handle' ");
		List<String> types = new ArrayList<String>();
		for(int i = 0 ; i<list.size(); i++){
			types.add(list.get(i).getStr("text"));
		}
		return types;
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
	
	public List<Map<String,Object>> initEPys(){
		List<Record> records = Db.find("select EP_ID,EP_NAME from ENTERPRISE where status = '2' and IF_TRANSPORT = '1' order by ep_id  ");
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
	
//	public Map<String,Object> initHandleSelf(String tpId){
//		Record record = Db.findFirst("select * from Z_WOBO_HANDLE_SELF where tp_id=? ",tpId);
//		Map<String,Object> map = null;
//		if(record !=null && record.getColumns() !=null ){
//			map = record.getColumns();
//		}
//		return map;
//	}
//	
//	public List<Map<String,Object>> initHandleSelfList(String tpId){
//		List<Record> records = Db.find("select * from Z_WOBO_HANDLESELF_LIST where tp_id=? ",tpId);
//		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//		for(int i = 0; i<records.size() ; i++){
//			list.add(records.get(i).getColumns());
//		}
//		return list;
//	}
	
//	public boolean saveHandleSelf(String tpId,Map<String,String> map){
//		Db.update("delete from Z_WOBO_HANDLE_SELF where tp_id = ? " ,tpId);
//		Record record = new Record();
//		record.set("TP_ID", tpId);
//		record.set("FACILITY_NAME", map.get("FACILITY_NAME"));
//		record.set("FACILITY_TYPE", map.get("FACILITY_TYPE"));
//		record.set("FACILITY_ADDRESS", map.get("FACILITY_ADDRESS"));
//		record.set("INVEST_SUM", map.get("INVEST_SUM"));
//		record.set("INVEST_SUM_UNIT", map.get("INVEST_SUM_UNIT"));
//		record.set("DESIGN", map.get("DESIGN"));
//		record.set("DESIGN_TIME", map.get("DESIGN_TIME"));
//		record.set("RUN_TIME", map.get("RUN_TIME"));
//		record.set("RUN_MONEY", map.get("RUN_MONEY"));
//		record.set("RUN_MONEY_UNIT", map.get("RUN_MONEY_UNIT"));
//		record.set("FACILITY_SUM", map.get("FACILITY_SUM"));
//		record.set("HANDLE_EFFECT", map.get("HANDLE_EFFECT"));
//		record.set("DB_1", map.get("DB_1"));
//		record.set("DB_2", map.get("DB_2"));
//		record.set("DESC_CONTENT", map.get("DESC_CONTENT"));
//		record.set("MEASURE", map.get("MEASURE"));
//		record.set("STATUS", "00");
//		record.set("sysdate", getSysdate());
//		return Db.save("Z_WOBO_HANDLE_SELF", record);
//	}
//	
//	public boolean deleteHandleSelf(String tpId){
//		Db.update("delete from Z_WOBO_HANDLE_SELF where tp_id = ? " ,tpId);
//		return true;
//	}
//	
//	public boolean saveHandleSelfList(String tpId,List<Map<String,Object>> list){
//		Db.update("delete from Z_WOBO_HANDLESELF_LIST where tp_id = ? " ,tpId);
//		boolean res = false;
//		for(int i=0;i<list.size();i++){
//			Map<String,Object> map = list.get(i);
//			Record record = new Record();
//			record.set("TP_ID", tpId);
//			record.set("Id", i+1);
//			record.set("D_NAME", map.get("D_NAME"));
//			record.set("STORE_YEAR", map.get("STORE_YEAR"));
//			record.set("STORE_PLAN_UNIT", map.get("STORE_PLAN_UNIT"));
//			record.set("STORE_LAST", map.get("STORE_LAST"));
//			record.set("STORE_LAST_UNIT", map.get("STORE_LAST_UNIT"));
//			res = Db.save("Z_WOBO_HANDLESELF_LIST", record);
//		}
//		return res;
//	}
//	
//	public boolean deleteHandleSelfList(String tpId){
//		Db.update("delete from Z_WOBO_HANDLESELF_LIST where tp_id = ? " ,tpId);
//		return true;
//	}
	
	public List<Map<String,Object>> initHandleSelf(String tpId){
		List<Record> records = Db.find("select * from Z_WOBO_HANDLE_SELF where tp_id=? ",tpId);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i<records.size() ; i++){
			list.add(records.get(i).getColumns());
		}
		return list;
	}
	
	public List<Map<String,Object>> initHandleSelfList(String tpId,String mainId){
		List<Record> records = Db.find("select * from Z_WOBO_HANDLESELF_LIST where tp_id=? and main_Id = ? ",tpId,mainId);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i<records.size() ; i++){
			list.add(records.get(i).getColumns());
		}
		return list;
	}
	
	public boolean saveHandleSelf(String tpId,Map<String,String> map){
//		Db.update("delete from Z_WOBO_HANDLE_SELF where tp_id = ? " ,tpId);
		Record record = new Record();
		record.set("TP_ID", tpId);
		record.set("ID", map.get("ID"));
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
	
	public boolean deleteHandleSelf(String tpId){
		Db.update("delete from Z_WOBO_HANDLE_SELF where tp_id = ? " ,tpId);
		return true;
	}
	
	public boolean saveHandleSelfList(String tpId,String mainId,List<Map<String,Object>> list){
//		Db.update("delete from Z_WOBO_HANDLESELF_LIST where tp_id = ? " ,tpId);
		boolean res = false;
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			Record record = new Record();
			record.set("TP_ID", tpId);
			record.set("MAIN_ID", mainId);
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
	
	public boolean deleteHandleSelfList(String tpId){
		Db.update("delete from Z_WOBO_HANDLESELF_LIST where tp_id = ? " ,tpId);
		return true;
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
		List<Record> records = Db.find("select EP_ID EN_ID_CZ, EP_NAME EN_NAME_CZ,LICENSE_NO from HANDLE_LICENSE ");
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
			record.set("LICENSE_NO", map.get("LICENSE_NO"));
			record.set("D_NAME", map.get("D_NAME"));
			record.set("BIG_CATEGORY_ID", map.get("BIG_CATEGORY_ID"));
			record.set("SAMLL_CATEGORY_ID", map.get("SMALL_CATEGORY_ID"));
			record.set("BIG_CATEGORY_NAME", map.get("BIG_CATEGORY_NAME"));
			record.set("SAMLL_CATEGORY_NAME", map.get("SMALL_CATEGORY_NAME"));
			record.set("HANDLE_TYPE", map.get("HANDLE_TYPE"));
			record.set("YEAR_NUM", map.get("YEAR_NUM"));
			record.set("LAST_NUM", map.get("LAST_NUM"));
			record.set("UNIT", map.get("UNIT"));
			res = Db.save("Z_WOBO_HANDLE_LIST", record);
		}
		return res;
	}
	
	public Map<String,Object> initEnv(String tpId){
		Record record = Db.findFirst("select * from Z_WOBO_ENV where tp_id=? ",tpId);
		Map<String,Object> map = null;
		if(record !=null && record.getColumns() !=null ){
			map = record.getColumns();
		}
		return map;
	}
	
	public boolean saveEnv(String tpId,String env1,String env2,String env3,String env4){
		Db.update("delete from Z_WOBO_ENV where tp_id = ? " ,tpId);
		Record record = new Record();
		record.set("TP_ID", tpId);
		record.set("ENV_1", env1);
		record.set("ENV_2", env2);
		record.set("ENV_3", env3);
		record.set("ENV_4", env4);
		record.set("STATUS", "00");
		record.set("sysdate", getSysdate());
		return Db.save("Z_WOBO_ENV", record);
	}
	
	public Map<String,Object> initLastInfo(String tpId){
		Record record = Db.findFirst("select * from Z_WOBO_LASTINFO where tp_id=? ",tpId);
		Map<String,Object> map = null;
		if(record !=null && record.getColumns() !=null ){
			map = record.getColumns();
		}
		return map;
	}
	
	public boolean saveLastInfo(String tpId,String li1,String li2,String c1,String c2,String c3,String c4,String c5,String c6,String c7,String c8,String c9){
		Db.update("delete from Z_WOBO_LASTINFO where tp_id = ? " ,tpId);
		Record record = new Record();
		record.set("TP_ID", tpId);
		record.set("LI_1", li1);
		record.set("LI_2", li2);
		record.set("C_1", c1);
		record.set("C_2", c2);
		record.set("C_3", c3);
		record.set("C_4", c4);
		record.set("C_5", c5);
		record.set("C_6", c6);
		record.set("C_7", c7);
		record.set("C_8", c8);
		record.set("C_9", c9);
		record.set("STATUS", "00");
		record.set("sysdate", getSysdate());
		return Db.save("Z_WOBO_LASTINFO", record);
	}

	public boolean checkSub(String tpId){
		Record r1 = Db.findFirst("select count(1) num from Z_WOBO_EP_EXTEND a,Z_WOBO_OVERVIEW b ,Z_WOBO_TRANSFER c ,Z_WOBO_HANDLE_MAIN d where a.tp_id = b.tp_id and a.tp_id = c.tp_id and a.tp_id = d.tp_id and a.status != '02' and b.status != '02' and c.status != '02' and d.status != '02' and a.tp_id = ? ",tpId);
		boolean flag = false;
		if(r1 != null && r1.getInt("num") > 0){
			flag = true;
		}
		return flag;
	}
	
	public Map<String,Object> previewPlan(String epId,String tpId){
		Map<String,Object> map = new HashMap<String,Object>();
		StringBuffer epSql = new StringBuffer();
		epSql.append("select a.EP_NAME,a.EP_ADRESS_S,a.EP_ADRESS_Q,a.EP_ADRESS_J ,a.LINKMAN fr,a.INDUSTRY_CODE, ");
		epSql.append("b.LINKMAN,b.LINK_NUM,b.MAIL ");
		epSql.append("from ENTERPRISE a , Z_WOBO_EP_EXTEND b ");
		epSql.append("where a.ep_id = ? and a.EP_ID = b.EP_ID and b.TP_ID = ?  ");
		Record ep = Db.findFirst(epSql.toString(),epId,tpId);
		if(ep != null && ep.getColumns() != null){
			map.put("EP_NAME", ep.getStr("EP_NAME"));
			StringBuffer epAddress = new StringBuffer();
			epAddress.append("022".equals(ep.getStr("EP_ADRESS_S")) ? "天津市" : "外省");
			epAddress.append(convert(cityList, ep.get("EP_ADRESS_Q")));
			epAddress.append(ep.getStr("EP_ADRESS_J"));
			map.put("epAdress", epAddress.toString());
			String industryCode = ep.get("INDUSTRY_CODE") == null ? "" : ep.get("INDUSTRY_CODE").toString();
			map.put("industryName", Db.queryStr("select dictname from EOS_DICT_ENTRY where dictid = ? ",industryCode));
			map.put("fr", ep.getStr("fr"));
			map.put("LINKINFO",ep.getStr("LINKMAN") + "/" + ep.getStr("LINK_NUM"));
			map.put("MAIL", ep.getStr("MAIL"));
		}else{
			map.put("EP_NAME", "");
			map.put("epAdress", "");
			map.put("industryName", "");
			map.put("fr", "");
			map.put("LINKINFO", "");
			map.put("MAIL", "");
		}
		//危险废物产生规模及数量
		String outputSql = "select a.D_NAME,sum(case when b.STORE_YEAR is null then cast(a.year_num as numeric(18,2)) when a.year_num is null then cast(b.STORE_YEAR as numeric(18,2)) else cast(a.year_num as numeric(18,2))+cast(STORE_YEAR as numeric(18,2)) end ) num,UNIT from Z_WOBO_HANDLE_LIST a full join Z_WOBO_HANDLESELF_LIST b on a.TP_ID = b.TP_ID and a.D_NAME = b.D_NAME where a.tp_id= ?  GROUP BY a.D_NAME,unit ";
		List<Record> output = Db.find(outputSql,tpId);
		StringBuffer outputSb = new StringBuffer();
		for(int i = 0 ; i < output.size() ; i++){
			outputSb.append(output.get(i).getStr("D_NAME")).append(":").append(output.get(i).get("num")).append(output.get(i).getStr("UNIT")).append(";");
		}
		map.put("output",outputSb.toString());
		//危险废物名称及类别
		String overviewSql = "select D_NAME, BIG_CATEGORY_ID  from Z_WOBO_OVERVIEWLIST where tp_id = ? GROUP BY D_NAME, BIG_CATEGORY_ID ";
		List<Record> overview = Db.find(overviewSql,tpId);
		StringBuffer overviewSb = new StringBuffer();
		for(int i = 0 ; i < overview.size() ; i++){
			overviewSb.append(overview.get(i).getStr("D_NAME")).append(":").append(overview.get(i).getStr("BIG_CATEGORY_ID")).append(";");
		}
		map.put("overview",overviewSb.toString());
		//计划委托利用/处置危险废物数量
		String handleSql = "select D_NAME, sum(cast(year_num as numeric(18,2))) num ,UNIT from Z_WOBO_HANDLE_LIST where tp_id = ? GROUP BY D_NAME, UNIT";
		List<Record> handle = Db.find(handleSql,tpId);
		StringBuffer handleSb = new StringBuffer();
		for(int i = 0 ; i < handle.size() ; i++){
			handleSb.append(handle.get(i).getStr("D_NAME")).append(":").append(handle.get(i).get("num")).append(handle.get(i).getStr("UNIT")).append(";");
		}
		map.put("handle",handleSb.toString());
		//计划委托利用/处置危险废物数量
		String handleSelfSql = "select D_NAME, sum(cast(STORE_YEAR as numeric(18,2))) num ,STORE_PLAN_UNIT from Z_WOBO_HANDLESELF_LIST where tp_id = ? GROUP BY D_NAME, STORE_PLAN_UNIT";
		List<Record> handleSelf = Db.find(handleSelfSql,tpId);
		StringBuffer handleSelfSb = new StringBuffer();
		for(int i = 0 ; i < handleSelf.size() ; i++){
			handleSelfSb.append(handleSelf.get(i).getStr("D_NAME")).append(":").append(handleSelf.get(i).get("num")).append(handleSelf.get(i).getStr("STORE_PLAN_UNIT")).append(";");
		}
		map.put("handleSelf",handleSelfSb.toString());
		return map;
	}
	
	public boolean checkIfupdateOverview(String tpId,String names,String namebigIds,String namebigsmallIds){
		boolean ccflag = true;
		Record ccRecord = Db.findFirst("select count(1) num from Z_WOBO_TRANSFER_CC where tp_id = ? and d_name != '' and d_name is not null ",tpId);
		if(ccRecord != null){
			if(ccRecord.getInt("num") > 0 ){
				Record ccRecord2 = Db.findFirst("select count(1) num from Z_WOBO_TRANSFER_CC where tp_id = ?  and D_NAME+BIG_CATEGORY_ID+STORE_PLAN_UNIT in ("+ namebigIds +") ",tpId);
				if(ccRecord2 != null){
					if(ccRecord.getInt("num").intValue() != ccRecord2.getInt("num").intValue()){
						ccflag = false;
					}
				}
			}
		}
		boolean selfflag = true;
		Record selfRecord = Db.findFirst("select count(1) num from Z_WOBO_HANDLESELF_LIST where tp_id = ? ",tpId);
		if(selfRecord != null){
			if(selfRecord.getInt("num") > 0 ){
				Record selfRecord2 = Db.findFirst("select count(1) num from Z_WOBO_HANDLESELF_LIST where  tp_id = ? and  D_NAME+STORE_PLAN_UNIT in ("+names+") ",tpId);
				if(selfRecord2 != null){
					if(selfRecord2.getInt("num").intValue() != selfRecord.getInt("num").intValue() ){
						selfflag = false;
					}
				}
			}
		}
		boolean handleflag = true;
		Record handleRecord = Db.findFirst("select count(1) num from Z_WOBO_HANDLE_LIST where tp_id = ? and d_name != '' and d_name is not null ",tpId);
		if(handleRecord != null){
			if(handleRecord.getInt("num") > 0 ){
				Record handleRecord2 = Db.findFirst("select count(1) num from Z_WOBO_HANDLE_LIST where tp_id = ?  and D_NAME+BIG_CATEGORY_ID+SAMLL_CATEGORY_ID+UNIT in ("+namebigsmallIds+") ",tpId);
				if(handleRecord2 != null){
					if(handleRecord2.getInt("num").intValue() != handleRecord.getInt("num").intValue() ){
						handleflag = false;
					}
				}
			}
		}
		return ccflag && selfflag && handleflag;
	}
	public String checkLastYearId(String tpId){
		String res = "";
		String epId = Db.queryStr("select ep_id from Z_WOBO_PLAN_MAIN where tp_Id = ? ",tpId);
		StringBuffer sql = new StringBuffer();
		sql.append("select TP_ID from Z_WOBO_PLAN_MAIN where status != '02' and DATEDIFF(yyyy,BEGINDATE,DATEADD(year,-1,GETDATE())) = 0 and ep_id = ? ");
		Record record = Db.findFirst(sql.toString(),epId);
		if(record != null){
			res = record.getStr("TP_ID");
		}
		return res;
	}
	public String getLastTBSum(String epId,String en_cz_id,String bigId,String smallId,String unit){
		String last_unit_num = "0";
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(cast(b.UNIT_NUM as numeric(18,2))) as last_unit_num from TRANSFERPLAN_BILL a , TRANSFERPLAN_BILL_LIST b ");
		sb.append(" where a.TB_ID = b.TB_ID and status in ('03','10','11','12') ");
		sb.append(" and b.UNIT = ? and b.BIG_CATEGORY_ID = ? and SAMLL_CATEGORY_ID = ? and EN_ID_CS = ? and EN_ID_CZ = ? ");
		sb.append(" and DATEDIFF(yyyy,a.ACTIONDATE,DATEADD(year,-1,GETDATE())) = 0 ");
		Record record = Db.findFirst(sb.toString(),unit,bigId,smallId,epId,en_cz_id);
		if(record != null && record.get("last_unit_num") != null){
			last_unit_num = record.get("last_unit_num") + "";
		}
		return last_unit_num;
	}
	public Map<String,Object> initPt(String tpId){
		Record record = Db.findFirst("select * from Z_WOBO_TRANSFER_PLAN_PT where tp_id=? ",tpId);
		Map<String,Object> map = null;
		if(record !=null && record.getColumns() !=null ){
			map = record.getColumns();
		}
		return map;
	}
	public List<Map<String,Object>> initPtList(String tpId){
		List<Record> records = Db.find("select * from Z_WOBO_TRANSFER_PLAN_LIST_PT where tp_id=? ",tpId);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i<records.size() ; i++){
			list.add(records.get(i).getColumns());
		}
		return list;
	}

	public boolean savePt(String tpId, String EN_ID_CS, String EN_NAME_CS,String ysdwmc,
			String lINKMAN, String lINKTEL, String ysdwdz, String ysdwlxr,
			String ysdwlxrsj, String ysdwdlyszh, String fwjsdwwxfwjyxkzh, String yrsxzqhdm, String wfjsdwmc,
			String wfjsdz, String wfjsdwlxrsj, String wfjsdwlxr, String belongSepa) {
		Db.update("delete from Z_WOBO_TRANSFER_PLAN_PT where tp_id = ? " ,tpId);
		Record record = new Record();
		record.set("TP_ID", tpId);
		record.set("EN_ID_CS", EN_ID_CS);
		record.set("EN_NAME_CS", EN_NAME_CS);
		record.set("EN_NAME_YS", ysdwmc);
		record.set("EN_NAME_CZ", wfjsdwmc);
		record.set("lINKMAN", lINKMAN);
		String begindate = DateKit.toStr(super.getSysdate(), "yyyy-01-01");
		String enddate = DateKit.toStr(super.getSysdate(), "yyyy-12-31");
		record.set("BEGINTIME", begindate);
		record.set("ENDTIME", enddate);
		record.set("IF_ADDITIONAL", "0");
		record.set("IF_TP_ADDITIONAL", "0");
		record.set("STATUS", "0");
		record.set("ACTIONDATE", super.getSysdate());
		record.set("LINKPHONE", lINKTEL);
		record.set("BTO_ID_CS", belongSepa);
		String ycsxzqhdm  = Db.queryStr("select id from REGION_CODE where bto_id = ? ",belongSepa);
		record.set("ycsxzqhdm", ycsxzqhdm);
		record.set("wfycdwbm", EN_ID_CS);
		record.set("wfycdwmc", EN_NAME_CS);
		Record recordEP = Db.findFirst("select a.*,a.EP_NAME 'epName',linkman,tel from ENTERPRISE a where a.EP_ID = ? " ,EN_ID_CS);
		StringBuffer sb = new StringBuffer();
		sb.append("天津市").append(convert(cityList, recordEP.get("EP_ADRESS_Q"))).append(recordEP.getStr("EP_ADRESS_J"));
		record.set("wfycdwdz",sb.toString());
		record.set("fwycdwlxrsj", recordEP.getStr("tel"));
		record.set("wfycdwlxr", recordEP.getStr("linkman"));
		record.set("ysdwmc", ysdwmc);
		record.set("ysdwdz", ysdwdz);
		record.set("ysdwlxr", ysdwlxr);
		record.set("ysdwlxrsj", ysdwlxrsj);
		record.set("ysdwdlyszh", ysdwdlyszh);
		record.set("fwjsdwwxfwjyxkzh", fwjsdwwxfwjyxkzh);
		record.set("yrsxzqhdm", yrsxzqhdm);
		record.set("wfjsdwmc", wfjsdwmc);
		record.set("wfjsdz", wfjsdz);
		record.set("wfjsdwlxrsj", wfjsdwlxrsj);
		record.set("wfjsdwlxr", wfjsdwlxr);
		record.set("ksrq", begindate);
		record.set("jsrq", enddate);
		record.set("jhqrsxzqh", "120000");
		record.set("sysdate", super.getSysdate());
		return Db.save("Z_WOBO_TRANSFER_PLAN_PT", record);
	}
	public boolean savePtList(String tpId,List<Map<String,Object>> list){
		Db.update("delete from Z_WOBO_TRANSFER_PLAN_LIST_PT where tp_id = ? " ,tpId);
		boolean res = false;
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			Record record = new Record();
			record.set("TP_ID", tpId);
			record.set("Id", i+1);
			record.set("D_NAME", map.get("D_NAME"));
			record.set("UNIT", map.get("UNIT"));
			record.set("UNIT_NUM", map.get("UNIT_NUM"));
			record.set("BIG_CATEGORY_ID", map.get("BIG_CATEGORY_ID"));
			record.set("BIG_CATEGORY_NAME", map.get("BIG_CATEGORY_NAME"));
			record.set("SAMLL_CATEGORY_ID", map.get("SAMLL_CATEGORY_ID"));
			record.set("SAMLL_CATEGORY_NAME", map.get("SAMLL_CATEGORY_NAME"));
			record.set("wxfwmc", map.get("D_NAME"));
			record.set("wxfwdm", map.get("SAMLL_CATEGORY_ID"));
			record.set("zysl", map.get("UNIT_NUM"));
			record.set("jldw", map.get("UNIT"));
			record.set("LAST_NUM", map.get("LAST_NUM"));
			res = Db.save("Z_WOBO_TRANSFER_PLAN_LIST_PT", record);
		}
		return res;
	}
}
