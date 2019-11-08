package com.mine.rd.services.lab.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.pojo.BaseDao;

public class LabDao extends BaseDao{
	
	public Map<String,Object> initEp(String id){
		Map<String,Object> map = null;
		Record record = Db.findFirst("select * from A_LAB_USER where id = ? ",id);
		if(record != null && "1".equals(record.get("TYPE"))){
			map = record.getColumns();
		}
		else if(record != null && !"1".equals(record.get("TYPE"))){
			record = Db.findFirst("select a.*,b.EP_NAME,b.BELONG_SEPA from A_LAB_USER a, ENTERPRISE b where a.ep_id = b.ep_id and id = ? ",id);
			map = record.getColumns();
		}
		return map;
	}
	
	public List<Map<String,Object>> queryOrganList(){
		List<Map<String,Object>> resList = new ArrayList<Map<String,Object>>();
		List<Record> list = Db.find("select * from B_ORGAN where bto_type = '2' and ifsp='1' ");
		for(Record record : list){
			resList.add(record.getColumns());
		}
		return resList;
	}
	
	public List<Map<String,Object>> queryEpList(){
		List<Map<String,Object>> resList = new ArrayList<Map<String,Object>>();
		List<Record> list = Db.find("select b.EP_ID,b.EP_NAME from A_LAB_USER a,ENTERPRISE b where a.EP_ID = b.EP_ID ");
		for(Record record : list){
			resList.add(record.getColumns());
		}
		return resList;
	}
	
	public List<Map<String,Object>> queryPlanList(String epId){
		List<Map<String,Object>> resList = new ArrayList<Map<String,Object>>();
		List<Record> list = Db.find("select DISTINCT a.*,CONVERT(varchar(100), a.begintime, 23) begintimeFormat,CONVERT(varchar(100), a.endtime, 23) endtimeFormat from TRANSFER_PLAN a,TRANSFER_PLAN_LIST b where a.TP_ID = b.TP_ID and BEGINTIME <= GETDATE() and ENDTIME >= GETDATE() and status= '2' and b.BIG_CATEGORY_ID = 'HW49' and b.SAMLL_CATEGORY_ID= '900-044-49' and a.en_id_cs = ? ",epId);
		for(Record record : list){
			resList.add(record.getColumns());
		}
		return resList;
	}
	
	public List<Map<String,Object>> queryBelongEpList(String epId){
		List<Map<String,Object>> resList = new ArrayList<Map<String,Object>>();
		List<Record> list = Db.find("select distinct a.EP_ID,a.EP_NAME,a.TEL from A_LAB_USER a where a.BELONG_EP = ? ",epId);
		for(Record record : list){
			resList.add(record.getColumns());
		}
		return resList;
	}
	
	public boolean epEdit(Map<String,Object> map){
		Record record = new Record();
		record.set("ID", map.get("ID"));
		record.set("NAME", map.get("NAME"));
		record.set("MAIL", map.get("MAIL"));
		record.set("BELONG_SEPA", map.get("BELONG_SEPA"));
		record.set("BELONG_EP", map.get("BELONG_EP"));
		record.set("REGISTERCODE", map.get("REGISTERCODE") == null ? "" : map.get("REGISTERCODE"));
		record.set("ADDRESS", map.get("ADDRESS") == null ? "" : map.get("ADDRESS"));
		record.set("EP_ID", map.get("EP_ID") == null ? "" : map.get("EP_ID"));
		record.set("EP_NAME", map.get("EP_NAME") == null ? "" : map.get("EP_NAME"));
		record.set("STATUS", "1");
		return Db.update("A_LAB_USER", "ID",record);
	}
	
	public boolean addFlow(Map<String,Object> map){
		Record record = new Record();
		String ID = getSeqId("A_LAB_FLOW");
		map.put("ID", ID);
		record.set("ID", ID);
		record.set("BRAND", map.get("BRAND")== null ? "" : map.get("BRAND"));
		record.set("MODEL", map.get("MODEL")== null ? "" : map.get("MODEL"));
		record.set("COUNT", map.get("COUNT"));
		record.set("NUM", map.get("NUM"));
		record.set("NUM_UNIT", "吨");
		record.set("SOURCE", map.get("SOURCE"));
		record.set("TYPE", map.get("TYPE"));
		record.set("EP_ID", map.get("EP_ID"));
		record.set("DIRECTION", map.get("DIRECTION"));
		record.set("actiondate", map.get("actiondate"));
		record.set("sysdate", getSysdate());
		return Db.save("A_LAB_FLOW", "ID",record);
	}
	
	public boolean updateFlow(Map<String,Object> map){
		Record record = new Record();
		record.set("ID", map.get("ID"));
		record.set("BRAND", map.get("BRAND")== null ? "" : map.get("BRAND"));
		record.set("MODEL", map.get("MODEL")== null ? "" : map.get("MODEL"));
		record.set("COUNT", map.get("COUNT"));
		record.set("NUM", map.get("NUM"));
		record.set("NUM_UNIT", "吨");
		record.set("SOURCE", map.get("SOURCE"));
		record.set("TYPE", map.get("TYPE"));
		record.set("EP_ID", map.get("EP_ID"));
		record.set("DIRECTION", map.get("DIRECTION"));
		record.set("actiondate", map.get("actiondate"));
		return Db.update("A_LAB_FLOW", "ID",record);
	}
	
	public boolean addFlowEp(Map<String,Object> map){
		Record record = new Record();
		String ID = getSeqId("A_LAB_FLOW");
		map.put("ID", ID);
		record.set("ID", ID);
		record.set("PLATE_NUM", map.get("PLATE_NUM")== null ? "" : map.get("PLATE_NUM"));
		record.set("DRIVER", map.get("DRIVER")== null ? "" : map.get("DRIVER"));
		record.set("COUNT", map.get("COUNT"));
		record.set("NUM", map.get("NUM"));
		record.set("NUM_UNIT", "吨");
		record.set("TYPE", map.get("TYPE"));
		record.set("DIRECTION", map.get("DIRECTION"));
		record.set("EN_ID_CS", map.get("EN_ID_CS"));
		record.set("EN_NAME_CS", map.get("EN_NAME_CS"));
		record.set("EN_TEL_CS", map.get("EN_TEL_CS"));
		record.set("EN_ID_CZ", map.get("EN_ID_CZ"));
		record.set("EN_NAME_CZ", map.get("EN_NAME_CZ"));
		record.set("EN_TEL_CZ", map.get("EN_TEL_CZ"));
		record.set("actiondate", map.get("actiondate"));
		record.set("sysdate", getSysdate());
		return Db.save("A_LAB_FLOWEP", "ID",record);
	}
	
	public boolean addTb(Map<String,Object> map){
		Record record = new Record();
		String TB_ID = getSeqId("TB_ID");
		map.put("TB_ID", TB_ID);
		record.set("TB_ID", TB_ID);
		record.set("TP_ID", map.get("TP_ID"));
		record.set("EN_ID_CS", map.get("EN_ID_CS"));
		record.set("EN_NAME_CS", map.get("EN_NAME_CS"));
		record.set("EN_ID_CZ", map.get("EN_ID_CZ"));
		record.set("EN_NAME_CZ", map.get("EN_NAME_CZ"));
		record.set("EN_ID_YS", map.get("EN_ID_YS"));
		record.set("EN_NAME_YS", map.get("EN_NAME_YS"));
		record.set("ACTIONDATE", getSysdate());
		record.set("TBDATE", getSysdate());
		record.set("YSDATE", getSysdate());
		record.set("BEGINTIME", map.get("BEGINTIME"));
		record.set("ENDTIME", map.get("ENDTIME"));
		record.set("CSY", map.get("CSY"));
		record.set("YSY", map.get("DRIVER"));
		record.set("CREATEPERSON", map.get("CSY"));
		record.set("STATUS", "02");
		record.set("sysdate", getSysdate());
		return Db.save("TRANSFERPLAN_BILL", "TB_ID",record);
	}
	
	public boolean updateTb(Map<String,Object> map){
		Record record = new Record();
		record.set("TB_ID", map.get("TB_ID"));
		record.set("TP_ID", map.get("TP_ID"));
		record.set("EN_ID_CS", map.get("EN_ID_CS"));
		record.set("EN_NAME_CS", map.get("EN_NAME_CS"));
		record.set("EN_ID_CZ", map.get("EN_ID_CZ"));
		record.set("EN_NAME_CZ", map.get("EN_NAME_CZ"));
		record.set("EN_ID_YS", map.get("EN_ID_YS"));
		record.set("EN_NAME_YS", map.get("EN_NAME_YS"));
		record.set("ACTIONDATE", getSysdate());
		record.set("TBDATE", getSysdate());
		record.set("YSDATE", getSysdate());
		record.set("BEGINTIME", map.get("BEGINTIME"));
		record.set("ENDTIME", map.get("ENDTIME"));
		record.set("CSY", map.get("CSY"));
		record.set("YSY", map.get("DRIVER"));
		record.set("CREATEPERSON", map.get("CSY"));
		record.set("STATUS", "02");
		record.set("sysdate", getSysdate());
		return Db.update("TRANSFERPLAN_BILL", "TB_ID",record);
	}
	
	public boolean updateTbIn(Map<String,Object> map){
		Record record = new Record();
		record.set("TB_ID", map.get("TB_ID"));
		record.set("ACTIONDATE", getSysdate());
		record.set("JSDATE", getSysdate());
		record.set("CZY", map.get("CZY"));
		record.set("STATUS", "03");
		return Db.update("TRANSFERPLAN_BILL", "TB_ID",record);
	}
	
	public boolean addTbList(Map<String,Object> map){
		Db.update("delete from TRANSFERPLAN_BILL_LIST where TB_ID = ? ",map.get("TB_ID"));
		Record record = new Record();
		record.set("BL_ID", "000001");
		record.set("TL_ID", "000001");
		record.set("TB_ID", map.get("TB_ID"));
		record.set("TP_ID", map.get("TP_ID"));
		BigDecimal num1 = new BigDecimal(map.get("NUM1").toString());
		BigDecimal num2 = new BigDecimal(map.get("NUM2").toString());
		record.set("UNIT_NUM", num1.add(num2));
		record.set("UNIT", "吨");
		record.set("BIG_CATEGORY_ID", "HW49");
		record.set("BIG_CATEGORY_NAME", "其他废物");
		record.set("SAMLL_CATEGORY_ID", "900-044-49");
		record.set("D_NAME", map.get("D_NAME"));
		record.set("sysdate", getSysdate());
		return Db.save("TRANSFERPLAN_BILL_LIST",record);
	}
	
	public boolean addFlowEpOut(Map<String,Object> map){
		addTb(map);
		addTbList(map);
		Record record = new Record();
		String ID = getSeqId("A_LAB_FLOW");
		map.put("ID", ID);
		record.set("ID", ID);
		record.set("TP_ID", map.get("TP_ID"));
		record.set("TB_ID", map.get("TB_ID"));
		record.set("PLATE_NUM", map.get("PLATE_NUM")== null ? "" : map.get("PLATE_NUM"));
		record.set("DRIVER", map.get("DRIVER")== null ? "" : map.get("DRIVER"));
		record.set("NUM1", map.get("NUM1"));
		record.set("NUM2", map.get("NUM2"));
		record.set("NUM_UNIT", "吨");
		record.set("EN_ID_CS", map.get("EN_ID_CS"));
		record.set("EN_NAME_CS", map.get("EN_NAME_CS"));
		record.set("EN_TEL_CS", map.get("EN_TEL_CS"));
		record.set("EN_ID_CZ", map.get("EN_ID_CZ"));
		record.set("EN_NAME_CZ", map.get("EN_NAME_CZ"));
		record.set("EN_TEL_CZ", map.get("EN_TEL_CZ"));
		record.set("actiondate", getSysdate());
		record.set("sysdate", getSysdate());
		return Db.save("A_LAB_FLOWEP", "ID",record);
	}
	
	public boolean updateFlowEp(Map<String,Object> map){
		Record record = new Record();
		record.set("ID", map.get("ID"));
		record.set("PLATE_NUM", map.get("PLATE_NUM")== null ? "" : map.get("PLATE_NUM"));
		record.set("DRIVER", map.get("DRIVER")== null ? "" : map.get("DRIVER"));
		record.set("COUNT", map.get("COUNT"));
		record.set("NUM", map.get("NUM"));
		record.set("NUM_UNIT", "吨");
		record.set("TYPE", map.get("TYPE"));
		record.set("DIRECTION", map.get("DIRECTION"));
		record.set("EN_ID_CS", map.get("EN_ID_CS"));
		record.set("EN_NAME_CS", map.get("EN_NAME_CS"));
		record.set("EN_TEL_CS", map.get("EN_TEL_CS"));
		record.set("EN_ID_CZ", map.get("EN_ID_CZ"));
		record.set("EN_NAME_CZ", map.get("EN_NAME_CZ"));
		record.set("EN_TEL_CZ", map.get("EN_TEL_CZ"));
		record.set("actiondate", map.get("actiondate"));
		return Db.update("A_LAB_FLOWEP", "ID",record);
	}
	
	public boolean updateFlowEpOut(Map<String,Object> map){
		updateTb(map);
		addTbList(map);
		Record record = new Record();
		record.set("ID", map.get("ID"));
		record.set("TP_ID", map.get("TP_ID"));
		record.set("TB_ID", map.get("TB_ID"));
		record.set("PLATE_NUM", map.get("PLATE_NUM")== null ? "" : map.get("PLATE_NUM"));
		record.set("DRIVER", map.get("DRIVER")== null ? "" : map.get("DRIVER"));
		record.set("NUM1", map.get("NUM1"));
		record.set("NUM2", map.get("NUM2"));
		record.set("NUM_UNIT", "吨");
		record.set("EN_ID_CS", map.get("EN_ID_CS"));
		record.set("EN_NAME_CS", map.get("EN_NAME_CS"));
		record.set("EN_TEL_CS", map.get("EN_TEL_CS"));
		record.set("EN_ID_CZ", map.get("EN_ID_CZ"));
		record.set("EN_NAME_CZ", map.get("EN_NAME_CZ"));
		record.set("EN_TEL_CZ", map.get("EN_TEL_CZ"));
		record.set("actiondate", getSysdate());
		return Db.update("A_LAB_FLOWEP", "ID",record);
	}
	
	public boolean updateFlowEpIn(Map<String,Object> map){
		updateTbIn(map);
		Record record = new Record();
		record.set("ID", map.get("ID"));
		record.set("EN_TEL_CZ", map.get("EN_TEL_CZ"));
		record.set("actiondate", getSysdate());
		return Db.update("A_LAB_FLOWEP", "ID",record);
	}
	
	public Map<String, Object> getFlowList(int pn, int ps,String epId, Object searchContent){
		String sql = "from A_LAB_FLOW a  where ep_id = '"+epId+"' ";
//		if(searchContent != null && !"".equals(searchContent)){
//			sql = sql + " and (a.sysdate like '%"+searchContent+"%'  ) ";
//		}
		sql = sql + " order by a.sysdate desc ";
		Page<Record> page = Db.paginate(pn, ps, "select a.*,CONVERT(varchar(100), a.actiondate, 23) flowdate", sql );
		List<Record> eps = page.getList();
		List<Map<String, Object>> epList = new ArrayList<>();
		Map<String, Object> resMap = new HashMap<>();
		if(eps != null){
			for(Record record : eps){
				epList.add(record.getColumns());
			}
		}
		resMap.put("dataList", epList);
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	public Map<String, Object> getFlowEpList(int pn, int ps,String epId, Object searchContent){
		String sql = "from A_LAB_FLOWEP a  where en_id_cz = '"+epId+"' ";
//		if(searchContent != null && !"".equals(searchContent)){
//			sql = sql + " and (a.sysdate like '%"+searchContent+"%'  ) ";
//		}
		sql = sql + " order by a.sysdate desc ";
		Page<Record> page = Db.paginate(pn, ps, "select a.*,CONVERT(varchar(100), a.actiondate, 23) flowdate", sql );
		List<Record> eps = page.getList();
		List<Map<String, Object>> epList = new ArrayList<>();
		Map<String, Object> resMap = new HashMap<>();
		if(eps != null){
			for(Record record : eps){
				epList.add(record.getColumns());
			}
		}
		resMap.put("dataList", epList);
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	public Map<String, Object> getFlowEpOutList(int pn, int ps,String epId, Object searchContent){
		String sql = "from A_LAB_FLOWEP a , TRANSFERPLAN_BILL b,TRANSFERPLAN_BILL_LIST c where a.tb_id = b.tb_id and b.tb_id = c.tb_id and a.en_id_cs = '"+epId+"' ";
//		if(searchContent != null && !"".equals(searchContent)){
//			sql = sql + " and (a.sysdate like '%"+searchContent+"%'  ) ";
//		}
		sql = sql + " order by a.sysdate desc ";
		Page<Record> page = Db.paginate(pn, ps, "select a.*,b.EN_ID_YS,b.EN_NAME_YS,c.D_NAME,CONVERT(varchar(100), a.actiondate, 23) flowdate", sql );
		List<Record> eps = page.getList();
		List<Map<String, Object>> epList = new ArrayList<>();
		Map<String, Object> resMap = new HashMap<>();
		if(eps != null){
			for(Record record : eps){
				epList.add(record.getColumns());
			}
		}
		resMap.put("dataList", epList);
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	public Map<String, Object> getFlowEpInList(int pn, int ps,String epId, Object searchContent){
		String sql = "from A_LAB_FLOWEP a , TRANSFERPLAN_BILL b,TRANSFERPLAN_BILL_LIST c ,EOS_DICT_ENTRY d where a.tb_id = b.tb_id and b.tb_id = c.tb_id and d.DICTTYPEID = 'TB_STATUS' and b.status = d.dictid and a.en_id_cz = '"+epId+"' ";
//		if(searchContent != null && !"".equals(searchContent)){
//			sql = sql + " and (a.sysdate like '%"+searchContent+"%'  ) ";
//		}
		sql = sql + " order by a.sysdate desc ";
		Page<Record> page = Db.paginate(pn, ps, "select a.*,b.EN_ID_YS,b.EN_NAME_YS,c.D_NAME,CONVERT(varchar(100), a.actiondate, 23) flowdate,CONVERT(varchar(100), b.YSDATE, 23) ysdatetime,case when jsdate is null then CONVERT(varchar(100), GETDATE(), 120) else jsdate end jsdatetime,d.dictname statusname ", sql );
		List<Record> eps = page.getList();
		List<Map<String, Object>> epList = new ArrayList<>();
		Map<String, Object> resMap = new HashMap<>();
		if(eps != null){
			for(Record record : eps){
				epList.add(record.getColumns());
			}
		}
		resMap.put("dataList", epList);
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	public Map<String, Object> getFlowEpInListFor02(int pn, int ps,String epId, Object searchContent){
		String sql = "from A_LAB_FLOWEP a , TRANSFERPLAN_BILL b,TRANSFERPLAN_BILL_LIST c ,EOS_DICT_ENTRY d where a.tb_id = b.tb_id and b.tb_id = c.tb_id and d.DICTTYPEID = 'TB_STATUS' and b.status = d.dictid and b.status='02' and a.en_id_cz = '"+epId+"' ";
//		if(searchContent != null && !"".equals(searchContent)){
//			sql = sql + " and (a.sysdate like '%"+searchContent+"%'  ) ";
//		}
		sql = sql + " order by a.sysdate desc ";
		Page<Record> page = Db.paginate(pn, ps, "select a.*,b.EN_ID_YS,b.EN_NAME_YS,c.D_NAME,CONVERT(varchar(100), a.actiondate, 23) flowdate,d.dictname statusname ", sql );
		List<Record> eps = page.getList();
		List<Map<String, Object>> epList = new ArrayList<>();
		Map<String, Object> resMap = new HashMap<>();
		if(eps != null){
			for(Record record : eps){
				epList.add(record.getColumns());
			}
		}
		resMap.put("dataList", epList);
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	public Map<String,Object> initFlow(String id){
		Map<String,Object> map = null;
		Record record = Db.findFirst("select a.*,CONVERT(varchar(100), a.actiondate, 23) flowdate from A_LAB_FLOW a where id = ? ",id);
		if(record != null){
			map = record.getColumns();
		}
		return map;
	}
	
	public Map<String,Object> initFlowEp(String id){
		Map<String,Object> map = null;
		Record record = Db.findFirst("select a.*,CONVERT(varchar(100), a.actiondate, 23) flowdate from A_LAB_FLOWEP a where id = ? ",id);
		if(record != null){
			map = record.getColumns();
		}
		return map;
	}
	
	public Map<String,Object> initFlowEpOut(String id){
		Map<String,Object> map = null;
		Record record = Db.findFirst("select a.*,CONVERT(varchar(100), a.actiondate, 23) flowdate,b.EN_ID_YS,b.EN_NAME_YS,c.D_NAME from A_LAB_FLOWEP a,TRANSFERPLAN_BILL b, TRANSFERPLAN_BILL_LIST c where a.tb_id=b.tb_id and b.tb_id = c.tb_id and a.id = ? ",id);
		if(record != null){
			map = record.getColumns();
		}
		return map;
	}
	
	public Map<String,Object> initFlowEpIn(String id){
		Map<String,Object> map = null;
		Record record = Db.findFirst("select a.*,CONVERT(varchar(100), a.actiondate, 23) flowdate,CONVERT(varchar(100), b.YSDATE, 23) ysdatetime,case when b.JSDATE is NULL then CONVERT(varchar(100), GETDATE(), 120) else CONVERT(varchar(100), b.JSDATE, 120) end jsdatetime,b.CSY,b.EN_ID_YS,b.EN_NAME_YS,c.D_NAME,b.STATUS from A_LAB_FLOWEP a,TRANSFERPLAN_BILL b, TRANSFERPLAN_BILL_LIST c where a.tb_id=b.tb_id and b.tb_id = c.tb_id and a.id = ? ",id);
		if(record != null){
			map = record.getColumns();
		}
		return map;
	}
	
	public Map<String,Object> getYs(String tpId){
		Map<String,Object> map = null;
		Record record = Db.findFirst("select * from TRANSFER_PLAN where  tp_id = ? ",tpId);
		if(record != null){
			map = record.getColumns();
		}
		return map;
	}
	
	public boolean saveStock(Map<String,Object> map){
		Record old_Record = Db.findFirst("select * from A_LAB_STOCK where ep_id = ? and type = ? ",map.get("EP_ID"),map.get("TYPE"));
		Record record = new Record();
		if(old_Record == null){
			record.set("EP_ID", map.get("EP_ID"));
			record.set("COUNT", map.get("COUNT"));
			record.set("NUM", map.get("NUM"));
			record.set("NUM_UNIT", "吨");
			record.set("actiondate", map.get("actiondate"));
			record.set("TYPE", map.get("TYPE"));
			return Db.save("A_LAB_STOCK",record);
		}else{
			Db.update("delete from A_LAB_STOCK where ep_id = ? and type = ? ",map.get("EP_ID"),map.get("TYPE"));
			record.set("EP_ID", map.get("EP_ID"));
			BigDecimal oldCount = new BigDecimal(old_Record.getStr("COUNT"));
			BigDecimal newCount = new BigDecimal(map.get("COUNT").toString());
			record.set("COUNT", oldCount.add(newCount));
			BigDecimal oldNum = new BigDecimal(old_Record.getStr("NUM"));
			BigDecimal newNum = new BigDecimal(map.get("NUM").toString());
			record.set("NUM", oldNum.add(newNum));
			record.set("NUM_UNIT", old_Record.get("NUM_UNIT"));
			record.set("actiondate", map.get("actiondate"));
			record.set("TYPE", map.get("TYPE"));
			return Db.save("A_LAB_STOCK",record);
		}
	}
	
	public boolean saveStockEp(Map<String,Object> map){
		Record old_Record = Db.findFirst("select * from A_LAB_STOCK where ep_id = ? and type = ? ",map.get("EP_ID"),map.get("TYPE"));
		Record old_Record_CS = Db.findFirst("select * from A_LAB_STOCK where ep_id = ? and type = ? ",map.get("EN_ID_CS"),map.get("TYPE"));
		if(old_Record_CS != null){
			Db.update("delete from A_LAB_STOCK where ep_id = ? and type = ? ",map.get("EN_ID_CS"),map.get("TYPE"));
			Record recordCS = new Record();
			recordCS.set("EP_ID", map.get("EN_ID_CS"));
			BigDecimal oldCount = new BigDecimal(old_Record_CS.getStr("COUNT"));
			BigDecimal newCount = new BigDecimal(map.get("COUNT").toString());
			recordCS.set("COUNT", oldCount.subtract(newCount));
			BigDecimal oldNum = new BigDecimal(old_Record_CS.getStr("NUM"));
			BigDecimal newNum = new BigDecimal(map.get("NUM").toString());
			recordCS.set("NUM", oldNum.subtract(newNum));
			recordCS.set("NUM_UNIT", old_Record == null ? "吨" : old_Record.get("NUM_UNIT"));
			recordCS.set("actiondate", map.get("actiondate"));
			recordCS.set("TYPE", map.get("TYPE"));
			Db.save("A_LAB_STOCK",recordCS);
		}
		Record record = new Record();
		if(old_Record == null){
			record.set("EP_ID", map.get("EP_ID"));
			record.set("COUNT", map.get("COUNT"));
			record.set("NUM", map.get("NUM"));
			record.set("NUM_UNIT", "吨");
			record.set("TYPE", map.get("TYPE"));
			record.set("actiondate", map.get("actiondate"));
			return Db.save("A_LAB_STOCK",record);
		}else{
			Db.update("delete from A_LAB_STOCK where ep_id = ? and type = ? ",map.get("EP_ID"),map.get("TYPE"));
			record.set("EP_ID", map.get("EP_ID"));
			BigDecimal oldCount = new BigDecimal(old_Record.getStr("COUNT"));
			BigDecimal newCount = new BigDecimal(map.get("COUNT").toString());
			record.set("COUNT", oldCount.add(newCount));
			BigDecimal oldNum = new BigDecimal(old_Record.getStr("NUM"));
			BigDecimal newNum = new BigDecimal(map.get("NUM").toString());
			record.set("NUM", oldNum.add(newNum));
			record.set("NUM_UNIT", old_Record.get("NUM_UNIT"));
			record.set("actiondate", map.get("actiondate"));
			record.set("TYPE", map.get("TYPE"));
			return Db.save("A_LAB_STOCK",record);
		}
	}
	
	public boolean updateStock(Map<String,Object> map){
		Record old_Record = Db.findFirst("select * from A_LAB_STOCK where ep_id = ? and type = ? ",map.get("EP_ID"),map.get("TYPE"));
		Record old_RecordFLow = Db.findFirst("select * from A_LAB_FLOW where id = ? ",map.get("ID"));
		Record record = new Record();
		if(old_Record == null){
			record.set("EP_ID", map.get("EP_ID"));
			record.set("COUNT", map.get("COUNT"));
			record.set("NUM", map.get("NUM"));
			record.set("NUM_UNIT", "吨");
			record.set("actiondate", map.get("actiondate"));
			record.set("TYPE", map.get("TYPE"));
			return Db.save("A_LAB_STOCK",record);
		}else{
			Db.update("delete from A_LAB_STOCK where ep_id = ? and type = ? ",map.get("EP_ID"),map.get("TYPE"));
			record.set("EP_ID", map.get("EP_ID"));
			BigDecimal oldCountFlow = new BigDecimal(old_RecordFLow.getStr("COUNT"));
			BigDecimal oldCount = new BigDecimal(old_Record.getStr("COUNT"));
			BigDecimal newCount = new BigDecimal(map.get("COUNT").toString());
			BigDecimal addCount = oldCount.add(newCount);
			record.set("COUNT", addCount.subtract(oldCountFlow));
			BigDecimal oldNumFlow = new BigDecimal(old_RecordFLow.getStr("NUM"));
			BigDecimal oldNum = new BigDecimal(old_Record.getStr("NUM"));
			BigDecimal newNum = new BigDecimal(map.get("NUM").toString());
			BigDecimal addNum = oldNum.add(newNum);
			record.set("NUM", addNum.subtract(oldNumFlow));
			record.set("NUM_UNIT", old_Record.get("NUM_UNIT"));
			record.set("actiondate", map.get("actiondate"));
			record.set("TYPE", map.get("TYPE"));
			return Db.save("A_LAB_STOCK",record);
		}
	}
	
	public boolean updateStockEp(Map<String,Object> map){
		Record old_Record = Db.findFirst("select * from A_LAB_STOCK where ep_id = ? and type = ? ",map.get("EP_ID"),map.get("TYPE"));
		Record old_RecordFLow = Db.findFirst("select * from A_LAB_FLOWEP where id = ? ",map.get("ID"));
		Record old_Record_CS = Db.findFirst("select * from A_LAB_STOCK where ep_id = ? and type = ? ",map.get("EN_ID_CS"),map.get("TYPE"));
		if(old_Record_CS != null){
			Db.update("delete from A_LAB_STOCK where ep_id = ? and type = ? ",map.get("EN_ID_CS"),map.get("TYPE"));
			Record recordCS = new Record();
			recordCS.set("EP_ID", map.get("EN_ID_CS"));
			BigDecimal oldCountFlow = new BigDecimal(old_RecordFLow.getStr("COUNT"));
			BigDecimal oldCount = new BigDecimal(old_Record_CS.getStr("COUNT"));
			BigDecimal newCount = new BigDecimal(map.get("COUNT").toString());
			BigDecimal subCount = oldCount.subtract(newCount);
			recordCS.set("COUNT", subCount.add(oldCountFlow));
			BigDecimal oldNumFlow = new BigDecimal(old_RecordFLow.getStr("NUM"));
			BigDecimal oldNum = new BigDecimal(old_Record_CS.getStr("NUM"));
			BigDecimal newNum = new BigDecimal(map.get("NUM").toString());
			BigDecimal subNum = oldNum.subtract(newNum);
			recordCS.set("NUM", subNum.add(oldNumFlow));
			recordCS.set("NUM_UNIT", old_Record== null ? "吨" : old_Record.get("NUM_UNIT"));
			recordCS.set("actiondate", map.get("actiondate"));
			recordCS.set("TYPE", map.get("TYPE"));
			Db.save("A_LAB_STOCK",recordCS);
		}
		Record record = new Record();
		if(old_Record == null){
			record.set("EP_ID", map.get("EP_ID"));
			record.set("COUNT", map.get("COUNT"));
			record.set("NUM", map.get("NUM"));
			record.set("NUM_UNIT", "吨");
			record.set("actiondate", map.get("actiondate"));
			record.set("TYPE", map.get("TYPE"));
			return Db.save("A_LAB_STOCK",record);
		}else{
			Db.update("delete from A_LAB_STOCK where ep_id = ? and type = ? ",map.get("EP_ID"),map.get("TYPE"));
			record.set("EP_ID", map.get("EP_ID"));
			BigDecimal oldCountFlow = new BigDecimal(old_RecordFLow.getStr("COUNT"));
			BigDecimal oldCount = new BigDecimal(old_Record.getStr("COUNT"));
			BigDecimal newCount = new BigDecimal(map.get("COUNT").toString());
			BigDecimal addCount = oldCount.add(newCount);
			record.set("COUNT", addCount.subtract(oldCountFlow));
			BigDecimal oldNumFlow = new BigDecimal(old_RecordFLow.getStr("NUM"));
			BigDecimal oldNum = new BigDecimal(old_Record.getStr("NUM"));
			BigDecimal newNum = new BigDecimal(map.get("NUM").toString());
			BigDecimal addNum = oldNum.add(newNum);
			record.set("NUM", addNum.subtract(oldNumFlow));
			record.set("NUM_UNIT", old_Record.get("NUM_UNIT"));
			record.set("actiondate", map.get("actiondate"));
			record.set("TYPE", map.get("TYPE"));
			return Db.save("A_LAB_STOCK",record);
		}
	}
	
	public boolean updateStockEpIn(Map<String,Object> map){
		Record old_Record_CS = Db.findFirst("select * from A_LAB_STOCK where ep_id = ? and type = ? ",map.get("EN_ID_CS"), map.get("TYPE"));
		Record old_Record_CZ = Db.findFirst("select * from A_LAB_STOCK where ep_id = ? and type = ? ",map.get("EN_ID_CZ"), map.get("TYPE"));
		if(old_Record_CS != null){
			Db.update("delete from A_LAB_STOCK where ep_id = ? and type = ? ",map.get("EN_ID_CS"),map.get("TYPE"));
			Record recordCS = new Record();
			recordCS.set("EP_ID", map.get("EN_ID_CS"));
			BigDecimal oldNum = new BigDecimal(old_Record_CS.getStr("NUM"));
			BigDecimal newNum = new BigDecimal(map.get("NUM").toString());
			BigDecimal subNum = oldNum.subtract(newNum);
			recordCS.set("NUM", subNum);
			recordCS.set("actiondate", getSysdate());
			recordCS.set("TYPE", map.get("TYPE"));
			Db.save("A_LAB_STOCK",recordCS);
		}
		Record record = new Record();
		if(old_Record_CZ == null){
			record.set("EP_ID", map.get("EN_ID_CZ"));
			record.set("NUM", map.get("NUM"));
			record.set("NUM_UNIT", "吨");
			record.set("actiondate",getSysdate());
			record.set("TYPE", map.get("TYPE"));
			return Db.save("A_LAB_STOCK",record);
		}else{
			Db.update("delete from A_LAB_STOCK where ep_id = ? and type = ? ",map.get("EN_ID_CZ"),map.get("TYPE"));
			record.set("EP_ID", map.get("EN_ID_CZ"));
			BigDecimal oldNum = new BigDecimal(old_Record_CZ.getStr("NUM"));
			BigDecimal newNum = new BigDecimal(map.get("NUM").toString());
			BigDecimal addNum = oldNum.add(newNum);
			record.set("NUM", addNum);
			record.set("actiondate", getSysdate());
			record.set("TYPE", map.get("TYPE"));
			return Db.save("A_LAB_STOCK",record);
		}
	}
	
	public boolean updateStockEpInByType1(Map<String,Object> map){
		Record old_Record_CS = Db.findFirst("select * from A_LAB_STOCK where ep_id = ? and type = '1' ",map.get("EN_ID_CS"));
		Record old_Record_CZ = Db.findFirst("select * from A_LAB_STOCK where ep_id = ? and type = '1' ",map.get("EN_ID_CZ"));
		if(old_Record_CS != null){
			BigDecimal oldNum = new BigDecimal(old_Record_CS.getStr("NUM"));
			BigDecimal newNum = new BigDecimal(map.get("NUM1").toString());
			BigDecimal subNum = oldNum.subtract(newNum);
			Db.update("update A_LAB_STOCK set num = ? ,actiondate=getdate() where ep_id = ? and type = '1' " ,subNum,map.get("EN_ID_CS"));
		}
		Record record = new Record();
		if(old_Record_CZ == null){
			record.set("EP_ID", map.get("EN_ID_CZ"));
			record.set("NUM", map.get("NUM1"));
			record.set("NUM_UNIT", "吨");
			record.set("actiondate",getSysdate());
			record.set("TYPE", "1");
			return Db.save("A_LAB_STOCK",record);
		}else{
			BigDecimal oldNum = new BigDecimal(old_Record_CZ.getStr("NUM"));
			BigDecimal newNum = new BigDecimal(map.get("NUM1").toString());
			BigDecimal addNum = oldNum.add(newNum);
			return Db.update("update A_LAB_STOCK set num = ? ,actiondate=getdate() where ep_id = ? and type = '1' " ,addNum,map.get("EN_ID_CZ")) > 0;
		}
	}
	
	public boolean updateStockEpInByType2(Map<String,Object> map){
		Record old_Record_CS = Db.findFirst("select * from A_LAB_STOCK where ep_id = ? and type = '2' ",map.get("EN_ID_CS"));
		Record old_Record_CZ = Db.findFirst("select * from A_LAB_STOCK where ep_id = ? and type = '2' ",map.get("EN_ID_CZ"));
		if(old_Record_CS != null){
			BigDecimal oldNum = new BigDecimal(old_Record_CS.getStr("NUM"));
			BigDecimal newNum = new BigDecimal(map.get("NUM2").toString());
			BigDecimal subNum = oldNum.subtract(newNum);
			Db.update("update A_LAB_STOCK set num = ? ,actiondate=getdate() where ep_id = ? and type = '2' " ,subNum,map.get("EN_ID_CS"));
		}
		Record record = new Record();
		if(old_Record_CZ == null){
			record.set("EP_ID", map.get("EN_ID_CZ"));
			record.set("NUM", map.get("NUM2"));
			record.set("NUM_UNIT", "吨");
			record.set("actiondate",getSysdate());
			record.set("TYPE", "2");
			return Db.save("A_LAB_STOCK",record);
		}else{
			BigDecimal oldNum = new BigDecimal(old_Record_CZ.getStr("NUM"));
			BigDecimal newNum = new BigDecimal(map.get("NUM2").toString());
			BigDecimal addNum = oldNum.add(newNum);
			return Db.update("update A_LAB_STOCK set num = ? ,actiondate=getdate() where ep_id = ? and type = '2' " ,addNum,map.get("EN_ID_CZ")) > 0;
		}
	}
	
	public List<Map<String,Object>> initStock(String epId){
		List<Record> records = Db.find("select * from A_LAB_STOCK where ep_id = ? ",epId);
		List<Map<String, Object>> resList = new ArrayList<>();
		for(Record record : records){
			resList.add(record.getColumns());
		}
		return resList;
	}
	
	public boolean updatePwd(Map<String,Object> map){
		Record record = new Record();
		record.set("ID", map.get("ID"));
		record.set("PWD", map.get("PWD"));
		return Db.update("A_LAB_USER", "ID",record);
	}
	
	public boolean updateTel(Map<String,Object> map){
		Record record = new Record();
		record.set("ID", map.get("ID"));
		record.set("TEL", map.get("TEL"));
		return Db.update("A_LAB_USER", "ID",record);
	}
	
	public Map<String, Object> queryEp2List(){
		String sql = "select b.ep_id,b.ep_name from A_LAB_USER A , ENTERPRISE b where a.ep_id = b.ep_id and a.type = '2' and a.status != '2' and a.mail is not null and a.mail != '' ";
		List<Record> eps = Db.find(sql);
		List<Map<String, Object>> epList = new ArrayList<>();
		for(Record record : eps){
			epList.add(record.getColumns());
		}
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("dataList", epList);
		return resMap;
	}
	
	public Map<String, Object> queryEp1List(){
		String sql = "select a.ep_id,a.ep_name from A_LAB_USER A  where a.type = '1' and a.status = '1' and a.ep_id != ''  ";
		List<Record> eps = Db.find(sql);
		List<Map<String, Object>> epList = new ArrayList<>();
		for(Record record : eps){
			epList.add(record.getColumns());
		}
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("dataList", epList);
		return resMap;
	}
	
	public int updateEpStatus(String epId){
		return Db.update("update A_LAB_USER set status = '2' where ep_id = ? " , epId);
	}
	
	public boolean checkCar(Map<String,Object> map){
		Record record = Db.findFirst("select count(1) num from CAR_INFO where plate_number = ? ",map.get("PLATE_NUM"));
		boolean flag = false;
		if(record!=null){
			if(record.getInt("num") > 0){
				flag = true;
			}
		}
		return flag;
	}
}
