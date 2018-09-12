package com.mine.rd.services.plan.pojo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.kit.DateKit;
import com.mine.pub.pojo.BaseDao;

public class PlanDao extends BaseDao {
	

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
}
