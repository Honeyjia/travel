package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {

    /**
     * 根据route的rid 查询tab_route_img表中对应的图片
     * @param rid
     * @return
     */
    public List<RouteImg> findByRid(int rid);
}
