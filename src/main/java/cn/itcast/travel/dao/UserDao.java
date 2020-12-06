package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * 根据用户名查找用户信息
     * @return
     */
    public User findUserByUsername(String username);

    /**
     * 保存用户信息
     * @param user
     */
    public void save(User user);

    User findUserByCode(String code);

    void updateStatus(User user);

    User findByUsernameAndPassword(String username, String password);
}
