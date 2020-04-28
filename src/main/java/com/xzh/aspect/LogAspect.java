package com.xzh.aspect;



import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//切入点
    @Pointcut("execution(* com.xzh.web.*.*(..))")
    public void log(){};

    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
//得到request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
// 拿到请求前的url、ip、类名.方法名、参数
        String url =request.getRequestURL().toString();
        String ip =request.getRemoteAddr();
        String classMethod =joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        Object [] args=joinPoint.getArgs();
        RequestLog requestLog =new RequestLog(url,ip,classMethod,args);
        logger.info("Request : {}"+requestLog);

    }

    @After("log()")
    public void doAfter(){};

    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturning(Object result){
        logger.info("Result : {}" +result);

    }

    public  class RequestLog{
        String url;
        String ip;
        String classMethod;
        Object [] args;

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }
    }
}
