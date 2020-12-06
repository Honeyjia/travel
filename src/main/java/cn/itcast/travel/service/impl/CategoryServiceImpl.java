package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class CategoryServiceImpl implements CategoryService {
    private CategoryDao dao = new CategoryDaoImpl();
    @Override
    public List<Category> findAll() {
//        1.从redis中查询
//        1.1获取jedis客户端 获取连接
        Jedis jedis = JedisUtil.getJedis();
//        1.2使用sortedset有序集合排序查询
//        Set<String> categorys = jedis.zrange("category", 0, -1);

//        1.3查询sortedset中的分数(cid)和值(cname)
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);
        List<Category> cs = null;
//        2.判断集合是否为空
        if(categorys == null || categorys.size() == 0){
            System.out.println("从数据库中查询");
//        3.如果为空，则需要从数据库中查询，再数据存入redis中
            cs = dao.findAll();
//            遍历cs
            for(int i= 0; i < cs.size(); i ++){
                jedis.zadd("category",cs.get(i).getCid(),cs.get(i).getCname());
            }
        }else {
            System.out.println("从redis中查询");
//        4.如果不为空，则将redis中缓存的数据存入list中
            cs = new ArrayList<Category>();
//            遍历set集合 category  把set中的数据存入 list中
            for(Tuple tuple : categorys){
//                创建一个Category对象
                Category c = new Category();
                c.setCname(tuple.getElement());
                c.setCid((int) tuple.getScore());
                cs.add(c);
            }
        }
        return cs;
    }
}
