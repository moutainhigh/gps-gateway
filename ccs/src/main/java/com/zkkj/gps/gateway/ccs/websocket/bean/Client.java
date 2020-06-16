package com.zkkj.gps.gateway.ccs.websocket.bean;

import javax.websocket.Session;
import java.io.Serializable;
import java.util.Objects;

/**
 * 客户端对象
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-05-09 上午 11:35
 */
public class Client implements Serializable {

    /**
     * session
     */
    private Session session;

    /**
     * 空构造
     */
    public Client() {
    }

    /**
     * 代餐构造
     * @param session
     */
    public Client(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "Client{" +
                "session=" + session +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(session, client.session);
    }

    @Override
    public int hashCode() {

        return Objects.hash(session);
    }
}
