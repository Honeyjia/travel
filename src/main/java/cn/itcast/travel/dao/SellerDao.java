package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {
    /**
     * 根据sid查询 商家信息
     * @param sid
     * @return
     */
    public Seller findBySid(int sid);
}
