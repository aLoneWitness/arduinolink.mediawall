package arduinolink.listeners;

import arduinolink.websocket.WebsocketHandler;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;
import arduinolink.websocket.messages.TriggerMessage;
import arduinolink.websocket.messages.enums.TriggerType;
import org.springframework.messaging.simp.stomp.StompSession;

public class ArduinoListener implements SerialPortMessageListener {
    private WebsocketHandler websocketHandler;

    public ArduinoListener(WebsocketHandler websocketHandler) {
        this.websocketHandler = websocketHandler;
    }

    @Override
    public byte[] getMessageDelimiter() {
        return ";".getBytes();
    }

    @Override
    public boolean delimiterIndicatesEndOfMessage() {
        return true;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        String delimitedMessage = new String(event.getReceivedData());

        TriggerType triggerType = TriggerType.ERROR;
        String triggerTypeIndicator = delimitedMessage.substring(delimitedMessage.length() - 2, delimitedMessage.length() - 1);
        switch (triggerTypeIndicator) {
            case "T":
                triggerType = TriggerType.TWITTER;
                break;
            case "I":
                triggerType = TriggerType.INSTAGRAM;
                break;
            case "V":
                triggerType = TriggerType.VIDEO;
                break;
            default:
                break;
        }

        String result = delimitedMessage.substring(0, delimitedMessage.length() - 2);
        int distance = Integer.parseInt(result);
        if(distance < 50) {
            System.out.println("Trigger happened of type " + triggerType.name());
            TriggerMessage triggerMessage = new TriggerMessage();
            triggerMessage.setTriggerType(triggerType);
            for (StompSession stompSession : this.websocketHandler.getSessions()) {
                stompSession.send("/app/website", triggerMessage);
            }
        }
    }
}