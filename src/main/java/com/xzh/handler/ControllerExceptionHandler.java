package com.xzh.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

//拦截所有被Controller标注的类
@ControllerAdvice
public class ControllerExceptionHandler {
//获得一个slf4j日志对象
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //声明为一个异常处理方法 所有异常都会被我们捕获
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request,Exception e) throws Exception {
        //打印发生错误的URL 和 错误信息{}可以用于占位
        logger.error("Request URL : {},Exception : {}",request.getRequestURL(),e );

       //抛出带有状态码的错误 如404 500
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class)!=null) {
            throw e;
        }


        //将错误信息 和URL 显示到页面上
        ModelAndView modelAndView =new ModelAndView();
        modelAndView.addObject("url",request.getRequestURL());
        modelAndView.addObject("exception",e);
        modelAndView.setViewName("error/error");
        return modelAndView;
    }
}
