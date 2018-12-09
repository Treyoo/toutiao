package com.cui.toutiao.model;

import org.springframework.stereotype.Component;

/**
 * 此类用于保存系统所有已登录用户。
 * 通过ThreadLocal，当前用户只能获取自己所在线程保存的用户信息
 * author:CuiWJ
 * Date:2018/11/8
 * Created with IDEA
 */
@Component
public class HostHolder {
    //由于多用户访问、本类对象是单例的，必须保证多线程访问安全，这里是通过ThreadLocal方式
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
