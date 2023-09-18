package com.chinesecheckers.serverside.websocket;

import com.chinesecheckers.serverside.entity.User;
import com.chinesecheckers.serverside.repository.UserRepository;
import com.chinesecheckers.serverside.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

@Controller
@ServerEndpoint("/chat/lobby/{lobbyid}/{userid}")
public class LobbyChatReceiver {

  // Store all socket session and their corresponding username.
  private static Map < Session, Integer > sessionLobbyIdMap = new Hashtable < > ();
  private static Map <Integer, Session > lobbyIdSessionMap = new Hashtable < > ();

  private static Map < Session, Integer > sessionUserIdMap = new Hashtable < > ();
  private static Map <Integer, Session > userIdSessionMap = new Hashtable < > ();

  private final Logger logger = LoggerFactory.getLogger(LobbyChatReceiver.class);

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
  public void onOpen(Session session, @PathParam("lobbyid") int lobbyId, @PathParam("userid") Integer userId)
  throws IOException {
    //logger.info("Entered into Open");

    sessionLobbyIdMap.put(session, lobbyId);
    lobbyIdSessionMap.put(lobbyId, session);

    //logger.info("lobby:" + lobbyId + "added to map");

    sessionUserIdMap.put(session, userId);
    userIdSessionMap.put(userId, session);

    //logger.info("user:" + username + "added to map");
//    logger.info( userService==null? "user serv null" : "user serv not null");
//    logger.info( userRepository==null? "user repo null" : "user repo not null");

    try {
      User joinuser = userService.fetchUserById(userId.longValue());
      String message = joinuser.getUsername() + " has Joined the Lobby";
      broadcastToLobby(message,lobbyId);
    }catch(Exception e){ e.printStackTrace();}

  }

  @OnMessage
  public void onMessage(Session session, String message) throws IOException {
    // Handle new messages
    logger.info("Entered into Message: Got Message:" + message);
    int lobbyId = sessionLobbyIdMap.get(session);
    User user = userService.fetchUserById(sessionUserIdMap.get(session).longValue());
    if(user.getMuted() != 0) {
      session.getBasicRemote().sendText("You are muted");
    }else {
      String username = user.getUsername();
      broadcastToLobby(username + ": " + message, lobbyId);
    }
  }

  @OnClose
  public void onClose(Session session) throws IOException {
    logger.info("Entered into Close");

    Integer lobbyId = sessionLobbyIdMap.get(session);
    sessionLobbyIdMap.remove(session);
    lobbyIdSessionMap.remove(lobbyId);

    Integer userId = sessionUserIdMap.get(session);
    sessionUserIdMap.remove(session);
    userIdSessionMap.remove(userId);

    try {
      String message = userService.fetchUserById(userId.longValue()).getUsername() + " has Left the Lobby";
      broadcastToLobby(message,lobbyId);
    }catch(Exception e){ e.printStackTrace();}
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    // Do error handling here
    logger.info("Entered into Error");
  }

  private void broadcastToLobby(String message, int receiverlobbyId) {
    sessionLobbyIdMap.forEach((session, lobbyId) -> {
      try {
        if(lobbyId.equals(receiverlobbyId)) {
          session.getBasicRemote().sendText(message);
        }
      } catch (IOException e) {
        logger.info("Exception: " + e.getMessage().toString());
        e.printStackTrace();
      }

    });

  }
}