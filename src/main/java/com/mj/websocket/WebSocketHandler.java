package com.mj.websocket;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	
	Map<String, ChannelHandlerContext> clientList = new ConcurrentHashMap<>();
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端建立连接，通道开启！");
    }
 
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端断开连接，通道关闭！");
    }
 
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		System.out.println("客户端收到服务器数据:" + msg.text());
		ObjectMapper mapper = new ObjectMapper(); 
    	mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);   
    	mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    	Map<String, Object> maps = mapper.readValue(msg.text(), Map.class);
    	String code   = maps.get("code").toString();
        String uid    = maps.get("uid").toString();
    	
        String resp= "(" +ctx.channel().remoteAddress() + ") ：" ;
        ctx.writeAndFlush(new TextWebSocketFrame(resp));
	}
}
