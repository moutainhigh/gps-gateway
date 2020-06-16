package com.zkkj.gps.gateway.jt808tcp.monitor.mrsubscribe;

import com.zkkj.gps.gateway.jt808tcp.monitor.mrsubscribe.event.TerminalConnChannelEvent;
import com.zkkj.gps.gateway.jt808tcp.monitor.session.Session;
import com.zkkj.gps.gateway.jt808tcp.monitor.session.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 记录终端IP端口订阅服务
 * 线程安全对象可能导致的性能问题
 * @author suibozhuliu
 */
@Slf4j
@Component
public class TerminalChannelSubscribe {

    private final SessionManager sessionManager = SessionManager.getInstance();

    public TerminalChannelSubscribe() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void subscribe(TerminalConnChannelEvent arg) {
        Session session = Session.buildSession(arg.getCtx().channel());
        if (!ObjectUtils.isEmpty(session)) {
            sessionManager.put(session.getId(), session);
        }
    }
}
