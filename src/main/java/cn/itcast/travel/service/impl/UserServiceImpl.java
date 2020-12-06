package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao dao = new UserDaoImpl();
/**
 * 注册用户
 */
    @Override
    public Boolean regist(User user) {
//        1.根据用户名查询用户信息
        User u = dao.findUserByUsername(user.getUsername());
//      判断 u 是否为空
        if (u != null) {
//            用户名存在  保存失败
            return false;
        }
//        2.如果用户名不存在---保存 用户信息
//        2.1设置激活码，唯一字符串
        user.setCode(UuidUtil.getUuid());
//        2.2设置激活状态  N表示未激活  Y表示激活
        user.setStatus("N");

        dao.save(user);
//        3.激活邮件发送  链接  邮件正文
        String content = "<a href='http://localhost/travel/user/active?code=" + user.getCode() + "'>点击激活【黑马旅游网】</a>";
        MailUtils.sendMail(user.getEmail(), content, "激活邮件");

        return true;
    }

    /**
     * 激活邮件用户
     */
    @Override
    public Boolean active(String code) {
//        1.根据激活码查询用户对象
        User user = dao.findUserByCode(code);
//        2.判断用户是否存在  即是否为空,存在返回true，不存在则返回false
        if(user != null){
//            该用户存在
//            3.调用dao修改激活状态的方法
            dao.updateStatus(user);
            return true;
        }else {
            return false;
        }
    }

    /**
     * 登录方法
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return dao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
