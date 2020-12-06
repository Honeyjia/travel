package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao dao = new RouteDaoImpl();
    private RouteImgDao img = new RouteImgDaoImpl();
    private SellerDao seller = new SellerDaoImpl();
    private FavoriteDao favorite = new FavoriteDaoImpl();
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
//        封装PageBean对象
        PageBean<Route> pb = new PageBean<>();
//        设置当前页码
        pb.setCurrentPage(currentPage);
//        设置每页显示的条数
        pb.setPageSize(pageSize);
//        设置总记录数
        int totalCount = dao.findTotalCount(cid,rname);
        pb.setTotalCount(totalCount);
//        设置当前页显示的数据集合
//        开始的记录 = （当前页 - 1）*每页显示的条数
        int start = (currentPage - 1) * pageSize;
        List<Route> list = dao.findByPage(cid, start, pageSize, rname);
        pb.setList(list);

//        设置总页数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalPage);
        return pb;
    }

    @Override
    public Route findOne(String rid) {
//        1.根据rid去route表中 查询route对象
        Route route = dao.findOne(Integer.parseInt(rid));
//        2.根据route的rid 获取图片的集合信息
        List<RouteImg> list_img = img.findByRid(route.getRid());
//        2.1将集合设置到 route对象中
        route.setRouteImgList(list_img);
//        3.根据route的sid（商家的sid）来查询商家对象
        Seller seller_sid = seller.findBySid(route.getSid());
        route.setSeller(seller_sid);
//        4.查询收藏次数
        int count = favorite.findCountByRid(rid);
        route.setCount(count);
        return route;
    }
}
