package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface FavoriteService {
    /**
     * 判断用户是否收藏
     */
    public Boolean isFavorite(String rid,int uid);

    /**
     * 天剑收藏
     * @param rid
     * @param uid
     */
    void add(String rid, int uid);


    PageBean<Route> myFavorite(int uid, int currentPage, int pageSize);
}
