package arduinolink.websocket;

import arduinolink.websocket.messages.TriggerMessage;
import arduinolink.websocket.messages.enums.TriggerType;
import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;



public class WebsocketHandler extends StompSessionHandlerAdapter {
    private Logger logger = LogManager.getLogger(WebsocketHandler.class);

    @Getter
    private List<StompSession> sessions = new ArrayList<>();

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        logger.info("New session established : " + session.getSessionId());
        session.subscribe("/topic/website", this);
        TriggerMessage message = new TriggerMessage();
        message.setTriggerType(TriggerType.TWITTER);
        session.send("/app/website", message);
        sessions.add(session);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        sessions.remove(session);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        sessions.remove(session);
        session.disconnect();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return TriggerMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        TriggerMessage msg = (TriggerMessage) payload;
    }
}
