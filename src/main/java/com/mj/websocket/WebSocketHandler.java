package com.mj.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.service.Operate;
import com.tools.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.ImmediateEventExecutor;

public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	
	public static Map<String, ChannelGroup> clientList = new ConcurrentHashMap<>();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private Operate operate;
	
	@Autowired
	public JdbcTemplate jdbcTemplate;
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端建立连接，通道开启！");
    }
 
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端断开连接，通道关闭！");
    }
 
	//@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		logger.info("接收到来自客户端的数据:"+msg.text());
		ObjectMapper mapper = new ObjectMapper(); 
    	mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);   
    	mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    	//将接受到的数据转化为map
    	Map<String, Object> maps = mapper.readValue(msg.text(), Map.class);
    	String thisCode   = maps.get("code").toString();
    	//用户ID
        String uid    = maps.get("uid").toString();
        //房间ID
        String rid    = maps.get("rid").toString();
        //获取data中的数据
	    //Map data = (Map)maps.get("data");
        int code = Integer.parseInt(thisCode);
        Response resp = new Response(1, "操作成功");
        //初始化操作类
        switch(code){
            //获取当前房间用户的信息
	        case(1):
//	        	 logger.info("action,用户:"+uid+"初始化连接");
//	        	 resp = operate.initUser(uid);
//                 break;
               //创建房间
 	        case(2):
// 	        	logger.info("action,用户:"+uid+"创建房间");
// 	    	    //先在数据库中创建房间
// 	             resp = operate.create(data, uid);
// 	             //如果创建房间成功，则将房间ID作为key,加入chaanelGroup
// 	             ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
// 	             group.add(ctx.channel());
// 	             clientList.put(resp.getData().toString(), group);
 	    	   break;
 	    	   //进入房间
 	        case(3):
 	        	 //先在数据库中创建房间
 	        	resp = operate.enterRoom(rid, uid);
 	    	    break;
 	    	   //准备
 	        case(4):
 	        	 //先在数据库中创建房间
 	        	resp = operate.reader(rid, uid);
 	    	    break;
    	}
        ctx.writeAndFlush(new TextWebSocketFrame(resp.toString()));
	}
}
