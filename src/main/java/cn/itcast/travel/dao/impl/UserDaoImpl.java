package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {
//    1.定义template
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User findUserByUsername(String username) {
//        为避免空指针异常
        User user = null;
        try{
//        1.定义sql语句
            String sql = "select * from tab_user where username = ?";
//        2.执行sql
         user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        }catch(Exception e){
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void save(User user) {
//        1.定义sql
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code)" +
                " values(?,?,?,?,?,?,?,?,?)";
//        2.执行sql
        template.update(sql,user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode());
    }
/**
 * 查找激活码为code的用户信息
 */
    @Override
    public User findUserByCode(String code) {
        User user = null;
        try{
            //        1.定义sql语句
            String sql = "select * from tab_user where code = ?";
//        2.执行sql
          user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);

        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void updateStatus(User user) {
//        1.定义sql
        String sql = "update tab_user set status = 'Y' where uid = ?";
//        2.执行sql
        template.update(sql,user.getUid());
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        User user = null;
        try{
            //        1.定义sql语句
            String sql = "select * from tab_user where username = ? and password = ?";
//        2.执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class),username,password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
}
