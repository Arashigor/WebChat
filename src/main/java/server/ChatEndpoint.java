package server;

import model.Message;
import util.MessageDecoder;
import util.MessageEncoder;

import javax.websocket.*;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/chat/{room}/{login}",
        encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ChatEndpoint {

    private final Logger log = Logger.getLogger(getClass().getName());
    private static final Set<Session> PEERS = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("room") String room, @PathParam("login") String login)
            throws IOException {
        log.info(">> new session \"" + session.getId() + "\" bound to room " + room + ". User: " + login);

        PEERS.add(session);
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {

        String sendersRoom = session.getPathParameters().get("room");

        for (Session peer : PEERS) {
            if (sendersRoom.equals(peer.getPathParameters().get("room"))) {
                if (!message.hasReceiver()) {
                    peer.getAsyncRemote().sendObject(message);
                } else if (message.getReceiver().equals(peer.getPathParameters().get("login")) ||
                           message.getSender().equals(peer.getPathParameters().get("login"))) {

                    Message formattedSenderMsg = new Message(message);
                    formattedSenderMsg.setSender(message.getSender() + " to " + message.getReceiver());
                    peer.getAsyncRemote().sendObject(formattedSenderMsg);
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String room = session.getPathParameters().get("room");
        String login = session.getPathParameters().get("login");

        log.info(">> session closed for user: " + session.getId() + "("+ login +")");

        PEERS.remove(session);

        Message message = new Message();
        message.setSender("Server");
        message.setMessage(login + " has left " + room + " room.");
        message.setDate();

        for (Session peer : PEERS) {
            if (room.equals(peer.getPathParameters().get("room"))) {
                peer.getAsyncRemote().sendObject(message);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.log(Level.WARNING, session.getId(), throwable);
    }
}
