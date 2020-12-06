package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    UserService service = new UserServiceImpl();
//    regist login find exit active
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //        1.获取验证码
        String check = request.getParameter("check");
//        2.从session中获取验证
        HttpSession session = request.getSession();
        String checkCode = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码只使用一次
//        3.比较
//        验证码为空（因为在之前用session把checkCode移除了） 或者 和用户输入的验证码相同（忽略大小写）
        if(checkCode == null || !checkCode.equalsIgnoreCase(check)){
//            验证码错误
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
//      将info对象序列化json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
//      将json数据写回客户端
            response.setContentType("application/json;charset=utf-8");  //设置编码
            response.getWriter().write(json);
            return;//返回
        }

//        1.获取数据   map集合获取
        Map<String, String[]> map =  request.getParameterMap();
//        2.封装数据
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

//        3.调用service完成注册
//        UserService service = new UserServiceImpl();
        Boolean flag = service.regist(user);
        ResultInfo info = new ResultInfo();

//        4.响应结果  判断flag的值
        if(flag){
//            注册成功
            info.setFlag(true);
        }else {
//            注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败");
        }
//        5.将info对象序列化json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
//        6.将json数据写回客户端
        response.setContentType("application/json;charset=utf-8");  //设置编码
        response.getWriter().write(json);
    }

    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        1.验证码检验
//            1.1先获取用户输入的验证码
        String check = request.getParameter("check");
//            1.2获取生成的验证码信息
        HttpSession session = request.getSession();
        String checkCode = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
//            1.3比较 check和checkCode  如果验证码正确，则执行if下面语句，判断用户名和密码信息
        if(checkCode == null || !checkCode.equalsIgnoreCase(check)){
//            验证码错误
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码输入错误");
//            将对象序列化为json
            ObjectMapper mapper = new ObjectMapper();
            response.setContentType("application/json;charset=utf-8");
            mapper.writeValue(response.getOutputStream(),info);
            return; //返回
        }

//        1.获取用户名和密码数据
        Map<String, String[]> map = request.getParameterMap();
//        2.封装User对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
//        3.调用Service查询
//        UserService service = new UserServiceImpl();
        User u = service.login(user);

        ResultInfo info = new ResultInfo();

//        4.判断用户是否为空null
        if(u == null){
//            用户名或密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
//        5.判断用户是否激活  Status
        if(u != null && !"Y".equals(u.getStatus())){
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请激活");
        }
//        6.登录成功的判断
        if(u != null && "Y".equals(u.getStatus())){
            info.setFlag(true);
            request.getSession().setAttribute("user",u);//登录成功的标记
        }
//        7.响应数据 转化为json格式
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),info);
    }

    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        1.从session中获取user
        Object user = request.getSession().getAttribute("user");

//        2.将user协会客户端
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),user);
    }

    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        1.销毁session
        request.getSession().invalidate();

//        2.跳转登录页面
        response.sendRedirect(request.getContextPath()+"/login.html");
    }

    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //        1.获取激活码
        String code = request.getParameter("code");
//        2.判断是否为空
        if(code != null) {
//        3.调用service激活
//            UserService service = new UserServiceImpl();
            Boolean flag = service.active(code);
//        3.1判断返回值
            String msg = null;
            if(flag){
//                激活成功
                msg = "激活成功，请<a href='login.html'>登录</a>";
            }else {
//                激活失败
                msg = "激活失败，请联系管理员";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);//把提示信息展示到页面上显示
        }
    }
}
