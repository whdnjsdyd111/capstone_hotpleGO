package com.example.demo.broadcast;

import com.example.demo.config.CustomSpringConfigurator;
import com.example.demo.domain.web.ChatLogVO;
import com.example.demo.service.web.ChatLogService;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Log4j2
@Component
@ServerEndpoint(value = "/admin/websocket", configurator = CustomSpringConfigurator.class)
public class BoardSocket {
    private static final Set<Session> clients = Collections.synchronizedSet(new HashSet<>());
    private static int onlineCount = 0;

    @Setter(onMethod_ = @Autowired)
    private ChatLogService chatLog;

    @OnOpen
    public void onOpen(Session session) {
        onlineCount++;
        clients.add(session);
        log.info("onOpen called, userCount : " + onlineCount);
    }

    @OnClose
    public void onClose(Session session) {
        onlineCount--;
        clients.remove(session);
        log.info("onClose called, userCount : " + onlineCount);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;

        try {
            obj = (JSONObject) parser.parse(message);
        } catch (ParseException e) {
            log.info("파싱 에러");
        }

        log.info(obj);

        if (obj.get("message") != null) {
            broadcast(message, session);
        } else {
            ChatLogVO vo = ChatLogVO.builder()
                    .chtCont(obj.get("chtCont").toString())
                    .uCode(obj.get("uCode").toString())
                    .aName(obj.get("aName").toString()).build();

            if (chatLog.registerChat(vo)) {
                obj.put("chtCode", vo.getChtCode());
                message = obj.toJSONString();
                broadcast(message, session);
            }
        }

        log.info("onMessage called, message:" + message);
    }

    @OnError //의도치 않은 에러 발생
    public void onError(Session session, Throwable throwable) {
        log.warn("onClose called, error:" + throwable.getMessage());
        clients.remove(session);
        onlineCount--;
    }

    public static void broadcast(String message, Session session) {
        try {
            synchronized (clients) {
                for(Session client : clients) {
                    if(!client.equals(session))
                        client.getBasicRemote().sendText(message);
                }
            }
        } catch (IOException e) {
            log.warn("Caught exception while sending message to Session " + session.getId() + "error:" + e.getMessage());
        }
    }
}
