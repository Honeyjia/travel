package cn.itcast.travel.web.servlet;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
//    创建service对象
    private RouteService service = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();
    /**
     * 分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        1.接收参数
        String cidStr = request.getParameter("cid");
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
//        接收rname参数
        String rname = request.getParameter("rname");
//        避免rname乱码
        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");
//        2.处理参数
        int cid = 0; // 类别id
//        避免空指针异常
        if(cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;//当前页码  如果不传递  则默认为第一页
        if(currentPageStr != null && currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        }else {
            currentPage = 1;
        }
        int pageSize = 0;//每页显示的条数  如果不传递 则默认显示5条记录
        if(pageSizeStr != null && pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else {
            pageSize = 5;
        }
        if(rname == null || "null".equals(rname) || rname.length() <= 0){
            rname = "";
        }
//        3.调用service查询PageBean对象
        PageBean<Route> pb = service.pageQuery(cid, currentPage, pageSize, rname);
//        4.将PageBean对象序列化为json  返回
        writeValue(pb,response);
    }

    /**
     * 根据rid 查询一个旅游项目的详细信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        1.接收rid参数
        String rid = request.getParameter("rid");
//        2.调用service查询route对象
        Route route = service.findOne(rid);
//        3.转化为json 写回客户端
        writeValue(route,response);
    }

    /**
     * 判断是否线路收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        1.获取rid
        String rid = request.getParameter("rid");
//        2.获取当前登录的user用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
//        3.判断user是否为空
        if(user == null){
            uid = 0;
        }else {
            uid = user.getUid();
        }
//        4.调用faviconService查询是否收藏
        Boolean flag = favoriteService.isFavorite(rid, uid);
//        写回客户端
        writeValue(flag,response);
    }

    /**
     * 添加线路
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        1.获取当前线路rid
        String rid = request.getParameter("rid");
//        2.获取用户和判断用户是否存在
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
//        3.判断user是否为空
        if(user == null){
            return;
        }else {
            uid = user.getUid();
        }
//        3.调用service添加
        favoriteService.add(rid,uid);
    }

    /**
     * 展示 我的收藏 信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void myFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        1.接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
//        1。1.获取用户信息 得到uid
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
//        2.判断user是否为空
        if(user == null){
            return;
        }else {
            uid = user.getUid();
        }
//        2.1判断参数
        int currentPage = 0;//当前页码  如果不传递  则默认为第一页
        if(currentPageStr != null && currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        }else {
            currentPage = 1;
        }
        int pageSize = 0;//每页显示的条数  如果不传递 则默认显示8条记录
        if(pageSizeStr != null && pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else {
            pageSize = 8;
        }

//        4.根据所有的rid查询tab_route表  得到route对象的集合
        PageBean<Route> pb = favoriteService.myFavorite(uid,currentPage,pageSize);
//        5.把list序列化为json
        writeValue(pb,response);
    }
}
/*
//        1.接收参数
        String cidStr = request.getParameter("cid");
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
//        接收rname参数
        String rname = request.getParameter("rname");
//        避免rname乱码
        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");
//        2.处理参数
        int cid = 0; // 类别id
//        避免空指针异常
        if(cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;//当前页码  如果不传递  则默认为第一页
        if(currentPageStr != null && currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        }else {
            currentPage = 1;
        }
        int pageSize = 0;//每页显示的条数  如果不传递 则默认显示5条记录
        if(pageSizeStr != null && pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else {
            pageSize = 5;
        }
        if(rname == null || "null".equals(rname) || rname.length() <= 0){
            rname = "";
        }
//        3.调用service查询PageBean对象
        PageBean<Route> pb = service.pageQuery(cid, currentPage, pageSize, rname);
//        4.将PageBean对象序列化为json  返回
        writeValue(pb,response);
 */