package cn.itcast.travel.web.servlet;

import com.alibaba.druid.pool.PreparedStatementPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println("BaseServlet中的Servlet方法被执行了");

//        完成方法分发  获取请求路径
        String uri = request.getRequestURI();//travel/user/add
        System.out.println("请求uri"+uri);
//        1.获取方法名称
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);  //add
        System.out.println("方法名称"+methodName);
//        2.获取方法对象method
//        this 代表调用的对象 谁调用我 我代表谁
        try {
//            忽略访问权限修饰符protected  获取方法
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
//            暴力反射法
//            method.setAccessible(true);
            //执行方法
            method.invoke(this,request,response);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

//    直接将输入的对象 序列化json  写回客户端
    public void writeValue(Object o,HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=uft-8");
        mapper.writeValue(response.getOutputStream(),o);
    }
//    将传入的对象  序列化为json  返回字符串格式
    public String writeValueAsString(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }
}
