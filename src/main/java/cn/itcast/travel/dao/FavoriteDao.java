package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface FavoriteDao {
    /**
     * 根据rid和uid查询收藏信息
     * @param rid
     * @param uid
     * @return
     */
    public Favorite findByRidAndUid(int rid,int uid);

    /**
     * 根据rid查询收藏次数  查询tab_favorite表张红rid出现得次数
     * @param rid
     * @return
     */

    int findCountByRid(String rid);

    /**
     * 添加收藏
     * @param rid
     * @param uid
     */
    void add(int rid, int uid);

    /**
     * 根据uid查询tab_favorite表 得到所有的rid
     * @param uid
     * @return
     */
    List findRidByUid(int uid);

    /**
     * 根据所有的rid查询tab_route表  得到route对象的集合
     * @param rid
     * @param start
     * @param pageSize
     * @return
     */
    List<Route> getRouteByRid(List<Integer> rid, int start, int pageSize);

    int findTotalCountByCid(List rids);

    /**
     * 根据cid 查询 收藏线路的数量
     * @param cid
     * @return
     */

}
