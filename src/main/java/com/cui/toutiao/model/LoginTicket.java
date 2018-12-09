package com.cui.toutiao.model;

import java.util.Date;

/**
 * 登录凭证实体类
 * author:CuiWJ
 * Date:2018/11/7
 * Created with IDEA
 */
public class LoginTicket {
    private int id;

    private int userId;

    private String ticket;

    private Date expired;

    private int status;//0代表本凭证有效，1无效

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
