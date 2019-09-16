package com.mine.rd.services.login.service;

import java.sql.SQLException;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.kit.Md5kit;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.login.pojo.LoginDao;

public class LoginService extends BaseService {

	private LoginDao dao = new LoginDao();
	
	public LoginService(BaseController controller) {
		super(controller);
	}
	
	/**
	 * @author ouyangxu
	 * @throws Exception 
	 * @date 20170221
	 * 方法：管理员登录
	 */
	private void adminLogin() throws Exception{
		controller.doIWBSESSION();
		String username = controller.getMyParam("adminName").toString();
		String pwd = controller.getMyParam("adminPwd").toString();
		//sysadmin-系统管理员
		if("sysadmin".equals(username)){
			Map<String , Object> user = dao.loginSysadmin(username);
			if(user != null && user.get("userId") != null && !"".equals(user.get("userId"))){
				if(pwd.equals(user.get("pwd"))){
					String belongS = "";
					String belongQ = "";
					String userPortrait = "";
					controller.setMySession("userId",user.get("userId"));
					controller.setMySession("userType",user.get("userType"));
					controller.setMySession("epId",user.get("epId"));
					controller.setMySession("epName",user.get("epName"));
					controller.setMySession("nickName",user.get("nickName"));
					controller.setMySession("ifLogin","0");
					controller.setMySession("belongQ",belongQ);
					controller.setMySession("belongS",belongS);
					controller.setMySession("belongSepa","");
					controller.setMySession("sepaName","");
					controller.setMySession("orgCode",user.get("orgCode"));
					controller.setMySession("userPortrait",userPortrait);
					//token值
					controller.setMySession("WJWT", dao.getToken());
					controller.setAttr("resFlag", "0");
					controller.setAttr("msg", "登录成功！");
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "密码错误！");
				}
			}else{   // 查询不到用户信息
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "用户不存在！");
			}
		}else{
			Map<String, Object> user = dao.adminLogin(username);
			if(user.size() > 0 && username.length() < 10){
				//md5加密
				if(Md5kit.MD5_64bit(pwd).equals(user.get("pwd"))){
					String belongS = "";
					String belongQ = "";
					if(user.get("orgAddr") != null && !"".equals(user.get("orgAddr"))){
						belongS = user.get("orgAddr").toString();
					}
					if(user.get("orgArea") != null && !"".equals(user.get("orgArea"))){
						belongQ = user.get("orgArea").toString();
					}
					if(dao.manageEpRole(user.get("operatorId").toString(), "QJGLY")){
						controller.setMySession("userId",user.get("operatorId").toString());
						controller.setMySession("nickName",username);
						controller.setMySession("userType", "admin");
						controller.setMySession("ifLogin","0");
						controller.setMySession("belongQ",belongQ);
						controller.setMySession("belongS",belongS);
						controller.setMySession("orgCode",user.get("orgCode").toString());
						controller.setMySession("userPortrait","");
						controller.setMySession("belongSepa", "");
						controller.setMySession("sepaName", belongQ);
						controller.setMySession("ROLEID", user.get("ROLEID"));
						controller.setAttr("btoId", user.get("btoId"));
						controller.setAttr("btoName", user.get("btoName"));
						controller.setAttr("btofId", user.get("btofId"));
						controller.setAttr("btofName", user.get("btofName"));
						controller.setAttr("ROLEID", user.get("ROLEID"));
						//token值
						controller.setMySession("WJWT", dao.getToken());
						controller.setAttr("resFlag", "0");
						controller.setAttr("msg", "登录成功！");
					}
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "密码错误！");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "用户不存在！");
			}
		}
	}
	
	/**
	 * @author woody
	 * @throws Exception 
	 * @date 20180825
	 * 方法：产生单位登录
	 */
	private void epAdminLogin() throws Exception{
		controller.doIWBSESSION();
		if(controller.getMyParam("epCode") != null && !"".equals(controller.getMyParam("epCode"))  && controller.getMyParam("epAdminPwd") != null && !"".equals(controller.getMyParam("epAdminPwd"))){
			String REGISTERCODE = controller.getMyParam("epCode").toString();
			String PWD = controller.getMyParam("epAdminPwd").toString();
			Map<String, Object> admin = dao.epAdminLogin(REGISTERCODE);
			if(admin.size() > 0){
				if(Md5kit.MD5_64bit(PWD).equals(admin.get("pwd"))){
					if(dao.manageEpRole(REGISTERCODE, "CSEP")){
						controller.setMySession("userId", admin.get("USER_ID"));
						controller.setMySession("nickName", admin.get("NAME"));
						controller.setMySession("userType", "CSEP");
						controller.setMySession("ifLogin","0");
						controller.setMySession("epId", admin.get("EP_ID"));
						controller.setMySession("epName", admin.get("EP_NAME"));
						controller.setMySession("belongSepa",admin.get("belongSepa"));
						controller.setMySession("sepaName",admin.get("sepaName"));
						controller.setMySession("ROLEID", "CSEP");
						controller.setAttr("ROLEID", "CSEP");
						//token值
						controller.setMySession("WJWT", dao.getToken());
						controller.setAttr("resFlag", "0");
						controller.setAttr("msg", "登录成功！");
					}
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "密码有误！");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "用户名不存在！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	private void labLoginForAPP() throws Exception{
		controller.doIWBSESSION();
		String tel = controller.getMyParam("tel").toString();
		String pwd = controller.getMyParam("pwd").toString();
		String type = controller.getMyParam("type").toString();
		Map<String , Object> user = dao.labLoginForAPP(tel,type);
		if(user != null && user.get("ID") != null && !"".equals(user.get("ID"))){
			if(pwd.equals(user.get("PWD"))){
				controller.setMySession("userId",user.get("ID"));
				controller.setMySession("epId",user.get("EP_ID"));
				controller.setMySession("tel",tel);
				controller.setMySession("userName",user.get("EP_NAME") == null ? "" : user.get("EP_NAME"));
				controller.setMySession("epName",user.get("EP_NAME") == null ? "" : user.get("EP_NAME"));
				controller.setMySession("status",user.get("STATUS"));
				controller.setMySession("ifLogin","0");
				controller.setMySession("userPortrait",user.get("IMGPATH")== null ? "" : user.get("IMGPATH"));
				//token值
				controller.setMySession("WJWT", dao.getToken());
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "登录成功！");
			}else{
				controller.setAttr("resFlag", "1");
				controller.setMySession("ifLogin","");
				controller.setAttr("msg", "密码错误！");
			}
		}else{   // 查询不到用户信息
			controller.setAttr("resFlag", "1");
			controller.setMySession("ifLogin","");
			controller.setAttr("msg", "用户不存在！");
		}
	}
	
	private void labRegisterForAPP() throws Exception{
		String tel = controller.getMyParam("tel").toString();
		String pwd = controller.getMyParam("pwd").toString();
		String type = controller.getMyParam("type").toString();
		Map<String , Object> user = dao.labLoginForAPP(tel,type);
		if(user != null && user.get("ID") != null && !"".equals(user.get("ID"))){
			controller.setAttr("resFlag", "1");
			controller.setMySession("ifLogin","");
			controller.setAttr("msg", "用户已存在！");
		}else{   // 查询不到用户信息
			Map<String , Object> registerUser = dao.labRegisterForAPP(tel,pwd);
			if(registerUser != null){
				controller.doIWBSESSION();
				controller.setMySession("tel",tel);
				controller.setMySession("userId",registerUser.get("ID"));
				controller.setMySession("userName","");
				controller.setMySession("status",registerUser.get("STATUS"));
				controller.setMySession("ifLogin","0");
				controller.setMySession("userPortrait","");
				//token值
				controller.setMySession("WJWT", dao.getToken());
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "登录成功！");
			}
		}
	}
	
	private void labRegisterForAPPEP() throws Exception{
		String epId = controller.getMyParam("EP_ID").toString();
		String tel = controller.getMyParam("tel").toString();
		String pwd = controller.getMyParam("pwd").toString();
		String type = controller.getMyParam("type").toString();
		Map<String , Object> user = dao.labLoginForAPP(tel,type);
		Map<String , Object> ep = dao.getEp(epId);
		if(user != null && user.get("ID") != null && !"".equals(user.get("ID"))){
			controller.setAttr("resFlag", "1");
			controller.setMySession("ifLogin","");
			controller.setAttr("msg", "用户已存在！");
		}
		else if(ep == null){
			controller.setAttr("resFlag", "2");
			controller.setMySession("ifLogin","");
			controller.setAttr("msg", "EP账号不存在！");
		}
		else{   // 查询不到用户信息
			Map<String , Object> registerUser = dao.labRegisterForAPP(tel,pwd,epId,type);
			if(registerUser != null){
				controller.doIWBSESSION();
				controller.setMySession("tel",tel);
				controller.setMySession("userId",registerUser.get("ID"));
				controller.setMySession("epId",epId);
				controller.setMySession("userName","");
				controller.setMySession("status",registerUser.get("STATUS"));
				controller.setMySession("ifLogin","0");
				controller.setMySession("userPortrait","");
				//token值
				controller.setMySession("WJWT", dao.getToken());
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "登录成功！");
			}
		}
	}
	
	
	private void forgetPwdEp() throws Exception{
		String epId = controller.getMyParam("EP_ID").toString();
		String tel = controller.getMyParam("tel").toString();
		String pwd = controller.getMyParam("pwd").toString();
		String type = controller.getMyParam("type").toString();
		Map<String , Object> user = dao.labLoginForAPP(tel,type);
		Map<String , Object> ep = dao.getEp(epId);
		if(user == null){
			controller.setAttr("resFlag", "1");
			controller.setMySession("ifLogin","");
			controller.setAttr("msg", "用户不存在！");
		}
		else if(ep == null){
			controller.setAttr("resFlag", "2");
			controller.setMySession("ifLogin","");
			controller.setAttr("msg", "EP账号不存在！");
		}
		else{   
			if(dao.forgetPwdEp(tel,pwd,epId) > 0){
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "修改成功！");
			}else{
				controller.setAttr("resFlag", "3");
				controller.setAttr("msg", "修改失败！");
			}
		}
	}
	
	private void forgetPwd() throws Exception{
		String tel = controller.getMyParam("tel").toString();
		String pwd = controller.getMyParam("pwd").toString();
		String type = controller.getMyParam("type").toString();
		Map<String , Object> user = dao.labLoginForAPP(tel,type);
		if(user == null){
			controller.setAttr("resFlag", "1");
			controller.setMySession("ifLogin","");
			controller.setAttr("msg", "用户不存在！");
		}
		else{   
			if(dao.forgetPwd(tel,pwd) > 0){
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "修改成功！");
			}else{
				controller.setAttr("resFlag", "3");
				controller.setAttr("msg", "修改失败！");
			}
		}
	}
	
	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	if("adminLogin".equals(getLastMethodName(7))){
	        			adminLogin();
	        		}
	            	else if("epAdminLogin".equals(getLastMethodName(7))){
	        			epAdminLogin();
	        		}
	            	else if("labLoginForAPP".equals(getLastMethodName(7))){
	            		labLoginForAPP();
	            	}
	            	else if("labRegisterForAPP".equals(getLastMethodName(7))){
	            		labRegisterForAPP();
	            	}
	            	else if("labRegisterForAPPEP".equals(getLastMethodName(7))){
	            		labRegisterForAPPEP();
	            	}
	            	else if("forgetPwdEp".equals(getLastMethodName(7))){
	            		forgetPwdEp();
	            	}
	            	else if("forgetPwd".equals(getLastMethodName(7))){
	            		forgetPwd();
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

}
