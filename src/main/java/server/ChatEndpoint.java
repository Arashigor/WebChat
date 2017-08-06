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
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/chat/{room}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ChatEndpoint {

    private final Logger log = Logger.getLogger(getClass().getName());
    private static volatile Set<Session> peers = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("room") String room) throws IOException {
        log.info(">> new session \"" + session.getId() + "\" bound to room " + room);
        session.getUserProperties().put("room", room);
        peers.add(session);
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        String room = (String) session.getUserProperties().get("room");
        for (Session peer : peers) {
            if (room.equals(peer.getUserProperties().get("room"))) {
                peer.getAsyncRemote().sendObject(message);
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String room = (String) session.getUserProperties().get("room");
        log.info(">> session closed for user: " + session.getId());

        peers.remove(session);

        Message message = new Message();
        message.setSender("Server");
        message.setMessage(session.getId() + " has left " + room + ".");
        for (Session peer : peers) {
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
