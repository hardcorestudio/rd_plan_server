package com.mine.rd.websocket;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.jfinal.json.Jackson;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.controller.BaseController;

@ServerEndpoint(value="/mywebsocket")
public class MyWebSocket {
	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    public static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    
    public String userId;
    
    public String IWBSESSION;
    
	public Map<String,Object> wsMap=null;
    
    public BaseController controller;
    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
	@OnOpen
    public void onOpen(Session session){
    	this.session = session;
        Map<String, List<String>> paramMap =session.getRequestParameterMap();
        String sessionId = paramMap.get("IWBSESSION") == null ? "" : paramMap.get("IWBSESSION").get(0);
        if(sessionId != null && !"".equals(sessionId)){
        	 Map<String, Object> mySession = CacheKit.get("mySession", sessionId);
        	 boolean flag = true;
        	 for(MyWebSocket item: webSocketSet){
        		 String userId = item.wsMap.get("userId").toString();
        		 if(userId.equals(mySession.get("userId").toString())){
        			 flag = false;
        		 }
        	 }
        	 if(flag){
        		 wsMap = mySession;
            	 webSocketSet.add(this); 
            	 addOnlineCount();           //在线数加1
            	 System.out.println("连接建立成功调用的方法,"+"当前在线人数为" + getOnlineCount());
        	 }else{
        		 System.out.println("连接建立成功调用的方法,"+"但是有重复的数据，不增加队列,当前在线人数为" + getOnlineCount());
        	 }	 
        }else{
       	 	System.out.println("连接建立成功调用的方法,"+"有新连接加入！但IWBSESSION为空，不增加队列,当前在线人数为" + getOnlineCount());
        }
        System.out.println(this);
    }
    /**
     * 连接关闭调用的方法
     */
	@OnClose
    public void onClose(Session session){
        webSocketSet.remove(this); 
        this.session = session;
        Map<String, List<String>> paramMap =session.getRequestParameterMap();
        String sessionId = paramMap.get("IWBSESSION") == null ? "" : paramMap.get("IWBSESSION").get(0);
        if(sessionId != null && !"".equals(sessionId)){
        	Map<String, Object> mySession = CacheKit.get("mySession", sessionId);
        	if(mySession == null){
        		subOnlineCount();           //在线数减1    
        		System.out.println("连接关闭调用的方法,但该请求没有缓存"+"当前在线人数为" + getOnlineCount()); 
        	}
        	else{
        		subOnlineCount();           //在线数减1    
    	        System.out.println("连接关闭调用的方法,"+"有一连接关闭！当前在线人数为" + getOnlineCount());
        	}
        }else{
        	System.out.println("连接关闭调用的方法,"+"有一连接关闭！但传参为空即IWBSESSION为空,当前在线人数为" + getOnlineCount());
        }
    }
    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息   key-区分交互类型（1：获取手机位置）  res-返回结果（0:成功，1:失败）
     * @param session 可选的参数
     */
    @OnMessage
    @SuppressWarnings("unchecked")
    public void onMessage(String message, Session session) {
		System.out.println("来自客户端的消息:" + message);
		if(message.equals("ping"))
		{
			try {
				sendMessage("{key:'ping',value:'success'}");
			} catch (IOException e) {
				e.printStackTrace();
			}  
			return;
		}
		Map<String, Object> map = Jackson.getJson().parse(message, Map.class);
        if(map.get("key") != null && "1".equals(map.get("key"))){
        	if(map.get("res") != null && "0".equals(map.get("res"))){
				Map<String, Object> info = (Map<String, Object>) map.get("info");
        		if(info.isEmpty()){
            		System.out.println("============>false");
            	}else{
            		System.out.println("============>true");
            	}
        	}
        }
        System.out.println("=======================================");
   
    }
    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        String fillInStackTrace = error.fillInStackTrace() + "";
        if(fillInStackTrace.indexOf("java.net.SocketException") > -1){
        	System.err.println("客户端网络异常！");
        	System.err.println(fillInStackTrace);
        }else if(fillInStackTrace.indexOf("java.net.SocketTimeoutException") > -1){
//        	this.onClose(this.session);
       	 	System.err.println("socket连接超时！");
        }else if(fillInStackTrace.indexOf("java.io.EOFException") > -1){
       	 	System.err.println("客户端断开连接！");
        }else{
         	error.printStackTrace();
        }
    }


    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
    	MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
    	MyWebSocket.onlineCount--;
    }
	public Session getSession() {
		return session;
	}
}
