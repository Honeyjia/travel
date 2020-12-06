package cn.itcast.travel.domain;

import java.util.Date;

public class Favorites {
    private int rid;
    private Date Date;
    private int uid;

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Favorites{" +
                "rid=" + rid +
                ", Date=" + Date +
                ", uid=" + uid +
                '}';
    }
}
