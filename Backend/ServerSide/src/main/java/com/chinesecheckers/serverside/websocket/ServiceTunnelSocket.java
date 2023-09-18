package com.chinesecheckers.serverside.websocket;

import com.chinesecheckers.serverside.entity.User;
import com.chinesecheckers.serverside.repository.UserRepository;
import com.chinesecheckers.serverside.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

@Controller
@ServerEndpoint("/servicetunnel/{userid}")
public class ServiceTunnelSocket {

  // Store all socket session and their corresponding username.
  private static Map < Session, Integer > sessionUserIdMap = new Hashtable < > ();
  private static Map <Integer, Session > userIdSessionMap = new Hashtable < > ();

  private final Logger logger = LoggerFactory.getLogger(ServiceTunnelSocket.class);

  private static UserRepository userRepository;
  private static UserService userService;

  @Autowired
  public void setUserRepository(UserRepository repo) {
    userRepository = repo;
  }

  @Autowired
  public void setUserService(UserService serv) {
    userService = serv;
  }

  @OnOpen
  public void onOpen(Session session, @PathParam("userid") Integer userId)
  throws IOException {
    logger.info("Open");

    sessionUserIdMap.put(session, userId);
    userIdSessionMap.put(userId, session);

    try {
      broadcast("%join%"+userId.toString());
    }catch(Exception e){ e.printStackTrace();}

  }

  @OnMessage
  public void onMessage(Session session, String message) throws IOException {
    // Handle new messages
    logger.info("Message: Got Message:" + message);

    broadcast(message);
  }

  @OnClose
  public void onClose(Session session) throws IOException {
    logger.info("Close");

    Integer userId = sessionUserIdMap.get(session);
    sessionUserIdMap.remove(session);
    userIdSessionMap.remove(userId);

    try {
      broadcast("%left%"+userId);
    }catch(Exception e){ e.printStackTrace();}
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    // Do error handling here
    logger.info("Error");
  }

  private void broadcast(String message) {
    sessionUserIdMap.forEach((session, userId) -> {
      try {
          session.getBasicRemote().sendText(message);
      } catch (IOException e) {
        logger.info("Exception: " + e.getMessage().toString());
        //e.printStackTrace();
      }

    });

  }
}