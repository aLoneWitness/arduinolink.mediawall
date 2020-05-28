package arduinolink;

import com.fazecast.jSerialComm.SerialPort;
import arduinolink.listeners.ArduinoListener;
import arduinolink.websocket.WebsocketHandler;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // SETUP WEBSOCKET LISTENING (AND WRITING OBVIOUSLY)
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        WebsocketHandler websocketHandler = new WebsocketHandler();
        stompClient.connect("ws://localhost:8080/ws", websocketHandler);

        // SETUP SERIAL LISTENING
        ArduinoListener arduinoListener = new ArduinoListener(websocketHandler);
        SerialPort comPort = SerialPort.getCommPorts()[1];
        comPort.openPort();
        comPort.addDataListener(arduinoListener);

        new Scanner(System.in).nextLine();
    }
}
