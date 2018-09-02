package com.mine.rd.services.plan.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.pojo.BaseDao;

public class PlanDao extends BaseDao {
	
	List<Record> dictList = CacheKit.get("mydict", "forgetpwd_status");
	List<Record> cityList = CacheKit.get("mydict", "city_q");

	/**
	 * @author woody
	 * @date 20180902
	 * 方法：管理计划列表
	 */
	public Map<String, Object> queryEpList(int pn, int ps, Object searchContent, Object statusValue, List<Object> statusCache){
		String sql = "";
		sql = "from Z_WOBO_PLANLIST A";
		Page<Record> page = Db.paginate(pn, ps, "select A.*", sql);
		List<Record> eps = page.getList();
		List<Map<String, Object>> epList = new ArrayList<>();
		Map<String, Object> resMap = new HashMap<>();
		if(eps != null){
			for(Record record : eps){
				Map<String, Object> map = new HashMap<>();
				map.put("EP_ID", record.get("id"));
				map.put("EP_NAME", record.get("ep_name"));
				map.put("EPTYPE", record.get("year"));
				map.put("CSEPTYPE", record.get("applydate"));
				map.put("STATUS", record.get("status"));
				map.put("STATUSNAME", record.get("statusname"));
				epList.add(map);
			}
		}
		resMap.put("epList", epList);
		if(statusCache != null && !"".equals(statusCache)){
			resMap.put("statusList", super.queryDict("approve_status", "value", "text", statusCache));
		}else{
			resMap.put("statusList", super.queryDict("approve_status", "value", "text"));
		}
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
}
