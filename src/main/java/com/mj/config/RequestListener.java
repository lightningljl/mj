package com.mj.config;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component //此注解千万千万不要忘记，它的主要作用就是将这个监听器纳入到Spring容器中进行管理,相当于注册监听吧
@Slf4j
public class RequestListener implements ServletRequestListener {  
    @Override  
    public void requestInitialized(ServletRequestEvent sre)  {  
        //将所有request请求都携带上httpSession  
        HttpSession session = ((HttpServletRequest) sre.getServletRequest()).getSession();  
        log.info("将所有request请求都携带上httpSession {}",session.getId());
    }  
    public RequestListener() {}  

    @Override  
    public void requestDestroyed(ServletRequestEvent arg0)  {}  
}  