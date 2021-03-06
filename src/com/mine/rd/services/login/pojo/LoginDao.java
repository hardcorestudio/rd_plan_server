package com.mine.rd.services.login.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.pojo.BaseDao;

public class LoginDao extends BaseDao {
	
	List<Record> cityList = CacheKit.get("mydict", "city_q");
	
	/**
	 * 
	 * @author weizanting
	 * @date 20161221
	 * 方法：登录--是否存在该用户
	 * 
	 */
	public Map<String, Object> loginSysadmin(String username){
		String sql = "select a.USER_ID,a.NAME,c.PORTRAIT,a.PWD from Z_WOBO_USER a, Z_WOBO_USER_INFO c where a.STATUS = '1'  and a.USER_ID = c.USER_ID  and a.NAME=?  "+super.checkLowerAndUpper();
		Record record =  Db.findFirst(sql, username);
		Map<String, Object> map = new HashMap<>();
		if(record != null){
			map.put("userId", record.get("USER_ID"));
			map.put("epId", "");
			map.put("epName", record.get("NAME"));
			map.put("nickName", record.get("NAME"));
			map.put("ifLogin", "0");
			map.put("pwd", record.get("PWD"));
			map.put("belongQ", "");
			map.put("belongS", "");
			map.put("orgCode", record.get("REGISTERCODE"));
			map.put("userPortrait", record.get("PORTRAIT"));
			map.put("belongSepa", "");
			map.put("status", record.get("STATUS"));
			map.put("sepaName", "");
			map.put("userType", "sysAdmin");
			map.put("orgCode", "");
		}
		return map;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170216
	 * 方法：单位与角色关联
	 */
	public boolean manageEpRole(String userId, String roleId){
		Record userRole = Db.findFirst("select * from Z_WOBO_USERROLE where USER_ID = ? and ROLE_ID = ? and STATUS = '1'", userId, roleId);
		if(userRole == null){
			Record record = new Record();
			record.set("USER_ID", userId);
			record.set("ROLE_ID", roleId);
			record.set("STATUS", "1");
			return Db.save("Z_WOBO_USERROLE", record);
		}
		return true;
	}
	
	/**
	 * 
	 * @author ouyangxu
	 * @date 20170221
	 * 方法：管理员登录
	 * 
	 */
	public Map<String, Object> adminLogin(String userId){
		String sql = "select c.OPERATORID, c.PASSWORD, b.ORGADDR, b.ORGNAME, b.ORGCODE,b.ORGID,c.USERID,c.OPERATORNAME from OM_EMPLOYEE a, OM_ORGANIZATION b ,AC_OPERATOR c where a.ORGID = b.ORGID and a.OPERATORID = c.OPERATORID and c.USERID = ? "+super.checkLowerAndUpper()+" and c.STATUS = 'running'";
		Record record = Db.findFirst(sql, userId);
		Map<String, Object> map = new HashMap<>();
		if(record != null){
			map.put("operatorId", record.get("OPERATORID"));
			map.put("pwd", record.get("PASSWORD"));
			map.put("orgAddr", record.get("ORGADDR"));
			map.put("orgArea", record.get("ORGNAME"));
			map.put("orgCode", record.get("ORGCODE"));
			map.put("btoId", record.get("ORGID"));
			map.put("btoName", record.get("ORGNAME"));
			map.put("btofId", record.get("USERID"));
			map.put("btofName", record.get("OPERATORNAME"));
			//查询管理员角色
			Record role = Db.findFirst("select ROLEID from AC_OPERATORROLE where OPERATORID = ? and ROLEID in ('QJSPROLE','SJSPROLE')", record.get("OPERATORID"));
			if(role != null){
				map.put("ROLEID", role.get("ROLEID"));
			}else{
				map.put("ROLEID", "");
			}
		}
		return map;
	}
	
	/**
	 * 
	 * @author woody
	 * @date 20180825
	 * 方法：单位管理员登录
	 * 
	 */
	public Map<String, Object> epAdminLogin(String userId){
		String sql = "select c.OPERATORID, c.PASSWORD, a.EP_ID, a.EP_NAME, a.BELONG_SEPA,c.USERID,c.OPERATORNAME from ENTERPRISE a ,AC_OPERATOR c where a.EP_ID = c.USERID and a.IF_PRODUCE = '1' and a.STATUS= '2' and c.STATUS='running' and a.EP_ID = ? "+super.checkLowerAndUpper()+" and c.STATUS = 'running'";
		Record record = Db.findFirst(sql, userId);
		Map<String, Object> map = new HashMap<>();
		if(record != null){
			map.put("operatorId", record.get("OPERATORID"));
			map.put("pwd", record.get("PASSWORD"));
			map.put("USER_ID", record.get("EP_ID"));
			map.put("NAME", record.get("EP_NAME"));
			map.put("EP_ID", record.get("EP_ID"));
			map.put("EP_NAME", record.get("EP_NAME"));
			map.put("belongSepa", record.get("BELONG_SEPA"));
			map.put("sepaName", convert(cityList, record.get("BELONG_SEPA")));
		}
		return map;
	}
	
	public Map<String, Object> labLoginForAPP(String tel,String type){
		String sql = "select * from A_LAB_USER where status != '2' and tel = ? and type=? ";
		Record record = Db.findFirst(sql, tel,type);
		return record != null ? record.getColumns() : null;
	}
	
	public Map<String, Object> getEp(String epId){
		String sql = "select * from ENTERPRISE where ep_id = ? ";
		Record record = Db.findFirst(sql, epId);
		return record != null ? record.getColumns() : null;
	}
	
	public Map<String, Object> labRegisterForAPP(String tel,String pwd,String BELONG_EP){
		Record record = new Record();
		record.set("ID", getSeqId("A_LAB_USER"));
		record.set("EP_ID", getSeqId("EP_ID"));
		record.set("TEL", tel);
		record.set("PWD", pwd);
		record.set("STATUS", "0");
		record.set("TYPE", "1");
		record.set("BELONG_EP", BELONG_EP);
		record.set("sysdate", getSysdate());
		boolean flag = Db.save("A_LAB_USER", record);
		return flag ? record.getColumns() : null;
	}
	
	public Map<String, Object> labRegisterForAPP(String tel,String pwd,String epId,String type){
		Record record = new Record();
		record.set("ID", getSeqId("A_LAB_USER"));
		record.set("EP_ID", epId);
		record.set("TEL", tel);
		record.set("PWD", pwd);
		record.set("STATUS", "0");
		record.set("TYPE", type);
		record.set("sysdate", getSysdate());
		boolean flag = Db.save("A_LAB_USER", record);
		return flag ? record.getColumns() : null;
	}
	
	/**
	 * @author woody
	 * @date 20190728
	 * 方法：修改密码
	 */
	public int forgetPwdEp(String tel,String pwd,String epId){
		return Db.update("update A_LAB_USER set pwd = ? where tel =? and ep_id = ? ",pwd,tel,epId);
	}
	
	public int forgetPwd(String tel,String pwd){
		return Db.update("update A_LAB_USER set pwd = ? where tel =? and type = '1' ",pwd,tel);
	}
	
	public boolean ifgetCode(String tel , String BELONG_EP){
		boolean flag = false;
		Record record = Db.findFirst("select datediff(n,sysdate,getdate()) diff from A_LAB_MAILCODE where tel = ? and belong_ep = ? ",tel,BELONG_EP);
		if(record == null || record.getInt("diff") > 10){
			flag = true;
		}
		return flag;
	}
	
	public boolean checkCode(String tel , String BELONG_EP,String code){
		boolean flag = false;
		Record record = Db.findFirst("select datediff(n,sysdate,getdate()) diff from A_LAB_MAILCODE where tel = ? and belong_ep = ? and code = ? ",tel,BELONG_EP,code);
		if(record != null && record.getInt("diff") <= 10){
			flag = true;
		}
		return flag;
	}
	
	public void saveGetCode(String tel , String BELONG_EP,String code){
		Db.update("delete from A_LAB_MAILCODE where tel = ? and belong_ep = ? ",tel,BELONG_EP);
		Db.update("insert into A_LAB_MAILCODE values (?,?,getdate(),?) ",tel,BELONG_EP,code);
	}
	
	public String getEpMail(String EpId){
		return Db.queryStr("select mail from A_LAB_USER where ep_id = ? ",EpId);
	}
}
