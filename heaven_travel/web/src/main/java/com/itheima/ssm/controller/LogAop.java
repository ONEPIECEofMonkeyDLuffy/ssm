package com.itheima.ssm.controller;

import com.itheima.ssm.domain.SysLog;
import com.itheima.ssm.service.ISysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by tanshuai on 2018/10/28.
 */
@Component
@Aspect
public class LogAop {


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ISysLogService logService;

    @Pointcut("execution(* com.itheima.ssm.controller.*.*(..))")
    public void pt1(){}

    /**
     *  执行时长
     *  客户端ip
     *  方法描述：类名:xxx方法名:xxx
     *  请求url
     *  当前用户
     *  访问时间
     * @param pjp
     * @return
     * @throws Exception
     */
    @Around("pt1()")
    public Object around(ProceedingJoinPoint pjp){
        //定义个方法的返回值
        Object returnValue = null;
        //拿到被增强的对象-----也就是controller对象
        Object target = pjp.getTarget();
        //拿到被增强的方法名称
        String methodName = pjp.getSignature().getName();
        //拿到方法的执行时的参数列表
        Object[] args = pjp.getArgs();
        //获取执行时长
        long begin = 0L;
        long end = 0L;
        //获取客户端ip地址
        String ip = getIpAddr();
        //获取方法的描述
        String methodDesc = getMethodDesc(target,methodName);
        //获取请求的url地址
        String url = getUrl(target,methodName,args);
        //获取当前的用户
        String username = getUserName();
        //获取当前的访问时间
        Date vistiTime = new Date();
        try {
            begin = System.currentTimeMillis();
            // 相当于执行了被增强的方法----controller中的方法执行了
            returnValue = pjp.proceed(args);
            end = System.currentTimeMillis();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }finally {
            //日志的记录
            SysLog log = new SysLog();
            log.setUsername(username);
            log.setVisitTime(vistiTime);
            log.setUrl(url);
            log.setMethod(methodDesc);
            log.setExecutionTime(end-begin);
            log.setIp(ip);
            try {
                logService.save(log);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returnValue;
    }

    /**
     * 获取请求的url地址：
     *  controller类上的RequestMapping注解里面的地址+方法上的RequestMapping注解里面的地址
     *  1:拿到controller类上的RequestMapping注解
     *      1.1:获得代表该类的Class对象 字节码对象
     *  2:拿到方法上的RequestMapping注解
     *      2.1:拿到代表方法的Method对象(反射中的Method对象)
     * @param target     controller对象
     * @param methodName controller对象中的方法名称
     * @param args       controller对象中方法执行时的参数列表
     * @return
     */
    private String getUrl(Object target, String methodName, Object[] args) {
        //定义一个url
        String url="";
        Class<?> controllerClass = target.getClass();
        if(controllerClass == null || controllerClass == this.getClass()){
            return url;
        }
        //获取类上的RequestMapping注解
        RequestMapping requestMapping_class = controllerClass.getAnnotation(RequestMapping.class);
        if(requestMapping_class!=null){
            url +=requestMapping_class.value()[0];
        }
        //获取方法上的RequestMapping注解
        Method method = null;
        // 通过类的字节码对象.getMethod方法可以获取代表方法的Method对象
        //区分方法有无参数  方法有参数和无参数获取Method对象的方法不一样
        if(args==null|| args.length<1){
            //没有参数  直接根据方法名称获取Method对象
            try {
                method = controllerClass.getMethod(methodName);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }else{
            //方法有参数  除了根据方法名称还要根据方法参数的Class数组来获取Method对象
            Class[] params = new Class[args.length];
            for(int i=0;i<args.length;i++){
                params[i]= args[i].getClass();
            }
            try {
                method = controllerClass.getMethod(methodName,params);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        if(method!=null){
            RequestMapping requestMapping_method = method.getAnnotation(RequestMapping.class);
            if(requestMapping_method!=null){
                url += requestMapping_method.value()[0];
            }
        }

        return url;
    }

    /**
     * 获取方法的描述：类名:xxx方法名:xxx
     * @param target      controller对象
     * @param methodName  对象中的方法名称
     * @return
     */
    private String getMethodDesc(Object target, String methodName) {
        StringBuilder sb = new StringBuilder("类名:");
        sb.append(target.getClass().getName()).append("方法名:").append(methodName);
        return  sb.toString();
    }

    /**
     * 获取客户端的ip地址
     * @return
     */
    public String getIpAddr() {
        return request.getRemoteAddr();
    }

    /**
     * 获取当前登陆的用户
     * @return
     */
    public String getUserName() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return username;
    }
}
