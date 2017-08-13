package server;

import model.Message;
import util.MessageDecoder;
import util.MessageEncoder;

import javax.websocket.*;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/chat/{room}/{login}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ChatEndpoint {

    private final Logger log = Logger.getLogger(getClass().getName());
    private static volatile Set<Session> PEERS = new CopyOnWriteArraySet<>();
    private static final String[] COLORS = {"red","grey","green","orange","coral","crimson","cyan","yellow"};

    @OnOpen
    public void onOpen(Session session, @PathParam("room") String room, @PathParam("login") String login) throws IOException {
        log.info(">> new session \"" + session.getId() + "\" bound to room " + room + ". User: " + login);
        session.getUserProperties().put("room", room);
        session.getUserProperties().put("login", login);

        int c = ThreadLocalRandom.current().nextInt(0,8);
        session.getUserProperties().put("userColor", COLORS[c]);

        PEERS.add(session);
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        String room = (String) session.getUserProperties().get("room");
        String color = (String) session.getUserProperties().get("userColor");

        message.setDate();
        message.setColor(color);

        System.out.println(message);

        for (Session peer : PEERS) {
            if (room.equals(peer.getUserProperties().get("room"))) {
                if (message.getWhom().isEmpty()) {
                    peer.getAsyncRemote().sendObject(message);
                } else {
                    if (message.getWhom().equals(peer.getUserProperties().get("login")) ||
                            message.getSender().equals(peer.getUserProperties().get("login"))) {
                        peer.getAsyncRemote().sendObject(message);
                    }
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String room = (String) session.getUserProperties().get("room");
        String login = (String) session.getUserProperties().get("login");
        log.info(">> session closed for user: " + session.getId() + "("+ login +")");

        PEERS.remove(session);

        Message message = new Message();
        message.setSender("Server");
        message.setMessage(login + " has left " + room + " room.");
        message.setDate();
        for (Session peer : PEERS) {
            if (room.equals(peer.getUserProperties().get("room"))) {
                peer.getAsyncRemote().sendObject(message);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.log(Level.WARNING, session.getId(), throwable);
    }
}
