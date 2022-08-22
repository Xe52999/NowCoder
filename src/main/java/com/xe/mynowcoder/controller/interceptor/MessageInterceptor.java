package com.xe.mynowcoder.controller.interceptor;

import com.xe.mynowcoder.entity.User;
import com.xe.mynowcoder.service.MessageService;
import com.xe.mynowcoder.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MessageInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    //每次请求前，处理头部栏的未读消息数量
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            int letterUnreadCount = messageService.findLetterUnreadCount(user.getUserId(), null);
            int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getUserId(), null);
            modelAndView.addObject("allUnreadCount", letterUnreadCount + noticeUnreadCount);
        }
    }
}
