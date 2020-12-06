package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Favorites;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        Favorite favorite = null;
        try {
            String sql = "select * from tab_favorite where rid = ? and uid = ?";
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
        }catch (Exception e){
            e.printStackTrace();
        }
        return favorite;
    }

    @Override
    public int findCountByRid(String rid) {
        String sql = "select count(*) from tab_favorite where rid = ?";
        return template.queryForObject(sql,Integer.class,rid);
    }

    @Override
    public void add(int rid, int uid) {
        String sql = "insert into tab_favorite values(?,?,?)";
        template.update(sql,rid,new Date(),uid);
    }

    @Override
    public List findRidByUid(int uid) {
        List arr = new ArrayList();
        String sql = "select * from tab_favorite WHERE uid = ?";
        List<Favorites> list = template.query(sql, new BeanPropertyRowMapper<Favorites>(Favorites.class), uid);
        for(int i = 0; i < list.size(); i ++){
            Favorites f = list.get(i);
            int rid = f.getRid();
            arr.add(i,rid);
        }
      return arr;
    }

    @Override
    public int findTotalCountByCid(List rids) {
        String sql ="select count(*) from tab_route where 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        List lis = new ArrayList();
        for(int i = 0; i < rids.size();i ++){
            if(i == 0){
                sb.append(" and rid = ? ");
            }else {
                sb.append(" or rid = ? ");
            }
            lis.add(rids.get(i));
        }
        sql = sb.toString();
        return template.queryForObject(sql, Integer.class,lis.toArray());
    }

    @Override
    public List<Route> getRouteByRid(List rid, int start, int pageSize) {
//        1.定义sql模板
        String sql = "select * from tab_route where 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();
            if(rid != null && rid.size() > 0){
                for(int i = 0 ; i < rid.size(); i ++){
                if(i == 0){
                    sb.append(" and rid = ? ");
                }else {
                    sb.append(" or rid = ? ");
                }
                params.add(rid.get(i));//添加对应的值
            }
        }
        sb.append(" limit ?,? ");
        params.add(start);
        params.add(pageSize);
        sql = sb.toString();
        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), params.toArray());
    }
}
