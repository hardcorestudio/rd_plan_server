package com.mine.pub.pojo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.json.Jackson;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.kit.DESKit;
import com.mine.pub.kit.DateKit;
import com.mine.pub.kit.DecryptKit;
import com.mine.pub.kit.StrKit;
import com.mine.pub.proc.ProcForQuery;

/**
 * @author woody
 * @date 20160214
 * @dao公用类
 * */
public abstract class BaseDao 
{
	/**
	 * @author woody
	 * @date 2015-3-26
	 * @将list转为符合datetable的字符串
	 * */
	public String toDTstr(List<Record> list)
	{
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		for(Record record : list)
		{
			listMap.add(record.getColumns());
		}
		return StrKit.toDTstr(listMap);
	}

	/**
	 * @author woody
	 * @date 2015-3-13
	 * @param 表名
	 * @return 前缀+时间戳+seq
	 * */
	public String getSeqId(String tablename)
	{
		Record record = Db.findFirst("select * from Z_PUB_SEQ where TABLENAME=?", tablename);
		String seq = "";
		if(record.getInt("NUM") == record.getInt("MAXNUM"))
		{
			seq = "1";
		}else
		{
			int addres = record.getInt("NUM") + record.getInt("ADDNUM");
			seq = addres + "";
		}
		Db.update("update Z_PUB_SEQ set num = ? where TABLENAME = ?", Integer.parseInt(seq) , tablename);
		while(seq.length()<4)
		{
			seq = "0"+seq;
		}
		return record.getStr("ID") + DateKit.getTimestamp(getSysdate()) + seq;
	}

	/**
	 * @author woody
	 * @date 20160215
	 * @获取时间戳
	 * */
	public Timestamp getSysdate()
	{
		//postgresql
//		return Db.queryTimestamp("select current_timestamp(0)::timestamp without time zone ");
		//sqlServer
		return Db.queryTimestamp("select getdate()");
	}
	
	
	/**
	 * @author woody
	 * @throws UnsupportedEncodingException 
	 * @date 20151010
	 * @保存数据库的session表
	 * @先删后插
	 * @删除原有记录
	 * */
	public void updateSession(String sessionId,String key,String obj) 
	{
		Db.update("delete from z_iwobo_session where SESSION_ID = ? and IWB_KEY = ?", sessionId,key);
		Db.update("insert into z_iwobo_session values (?,?,?,?)",sessionId,key,obj,getSysdate());
	}
	
	/**
	 * @author woody
	 * @date 2015-3-18
	 * @根据权限查询菜单列表
	 * */
	@SuppressWarnings("unchecked")
	public static List<Map<String,Object>> roleMenu(String userId)
	{
		Object[] objs = new Object[1];
		objs[0] = userId;
		ProcForQuery proc = new ProcForQuery("zqueryFuncs", objs);
		return (List<Map<String, Object>>) Db.execute(proc);
	}
	
	/**
	 * @author ouyangxu
	 * @date 2017-4-20
	 * @数据库区分大小写
	 * */
	public String checkLowerAndUpper(){
		return "collate Chinese_PRC_CS_AI";
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170512
	 * 方法：查询字典列表 for select
	 */
	public List<Map<String, Object>> queryDictForSelect(String name, String dictId, String dictValue){
		List<Record> lists = CacheKit.get("mydict", name);
		List<Map<String, Object>> returnList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put(dictId, "0");
		map.put(dictValue, "请选择");
		returnList.add(map);
		if(lists != null){
			for(Record list : lists){
				map = new HashMap<>();
				map.put(dictId, list.get("dict_id"));
				map.put(dictValue, list.get("dict_value"));
				returnList.add(map);
			}
		}
		return returnList;
	}
	
	/**
	 * @author weizanting
	 * @date 20170512
	 * 方法：表中数据与字典表中数据转换
	 */
	public String convert(List<Record> list, Object field){
		String str = "";
		if(field != null && !"".equals(field)){
			if(list.size() > 0){
				for(int i = 0; i < list.size(); i ++){
					if(field.equals(list.get(i).get("dict_id"))){
						str = list.get(i).get("dict_value");
						break;
					}
				}
			}
		}
		return str;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170602
	 * 方法：查询字典列表
	 */
	public List<Map<String, Object>> queryDict(String name, String dictId, String dictValue){
		List<Record> lists = CacheKit.get("mydict", name);
		List<Map<String, Object>> returnList = new ArrayList<>();
		if(lists != null){
			for(Record list : lists){
				Map<String, Object> map = new HashMap<>();
				map.put(dictId, list.get("dict_id"));
				map.put(dictValue, list.get("dict_value"));
				returnList.add(map);
			}
		}
		return returnList;
	}
	
	//按条件筛选查询字典列表
	public List<Map<String, Object>> queryDict(String name, String dictId, String dictValue, List<Object> objs){
		List<Record> lists = CacheKit.get("mydict", name);
		List<Map<String, Object>> returnList = new ArrayList<>();
		if(lists != null){
			for(Record list : lists){
				Map<String, Object> map = new HashMap<>();
				for(Object obj : objs){
					if(obj.equals(list.get("dict_id"))){
						map.put(dictId, list.get("dict_id"));
						map.put(dictValue, list.get("dict_value"));
						returnList.add(map);
					}
				}
			}
		}
		return returnList;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170607
	 * 方法：查询用户是否可访问该菜单
	 */
	public boolean checkUserMenu(String userId, String currentUrl){
		List<Record> urls = Db.find("select e.A_ID from Z_WOBO_USERROLE a,Z_WOBO_ROLE b,Z_WOBO_ROLEFUNC c,Z_WOBO_FUNC d,Z_WOBO_FUNC_ATTR e where a.USER_ID=? and a.ROLE_ID=b.ROLE_ID and b.ROLE_ID=c.ROLE_ID and c.FUNC_ID=d.FUNC_ID and d.FUNC_ID=e.FUNC_ID and a.STATUS='1' and b.STATUS='1' and c.STATUS='1' and d.STATUS='1'", userId);
		if(urls.size() > 0){
			for(Record url : urls){
				Object A_ID = url.get("A_ID");
				if(A_ID != null && !"".equals(A_ID)){
					if(currentUrl.contains(url.getStr("A_ID"))){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170221
	 * 方法：用户注销
	 */
	public void logout(String sessionId){
		Db.update("delete from Z_IWOBO_SESSION where SESSION_ID = ?", sessionId);
	}
	
	/**
	 * @author ouyangxu
	 * @throws Exception 
	 * @date 20170807
	 * 方法：获取token
	 */
    public String getToken() throws Exception{
		long loginToken = (this.getSysdate()).getTime();
		Map<String, Object> map = new HashMap<>();
		map.put("loginToken", loginToken);
		String WJWT = Jackson.getJson().toJson(map);
		//des加密
		String key = PropKit.get("codeKey");
		WJWT = DESKit.encrypt(WJWT,key);
		//base64传输
		WJWT = DecryptKit.encode(WJWT);
		return WJWT;
	}
}
