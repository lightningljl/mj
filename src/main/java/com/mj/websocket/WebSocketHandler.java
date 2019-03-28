package com.mj.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.dao.RoomDao;
import com.entity.Room;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.Response;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	
	public static Map<String, ChannelGroup> clientList = new ConcurrentHashMap<>();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
        String uid    = maps.get("uid").toString();
        //获取data中的数据
	    Map data = (Map)maps.get("data");
        int code = Integer.parseInt(thisCode);
        switch(code){
            //存储uuid
	        case(1):
                 Response resp = new Response(1, "同步成功");
                 ctx.writeAndFlush(new TextWebSocketFrame(resp.toString()));
                 break;
               //创建房间
 	       case(2):
 	    	   //先在数据库中创建房间
 	    	   
	               logger.info("操作步骤二");
 	    	   break;
    	}
        String resp= "(" +ctx.channel().remoteAddress() + ") ：" ;
        ctx.writeAndFlush(new TextWebSocketFrame(resp));
	}
}
