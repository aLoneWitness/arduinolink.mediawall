package mediawall.arduinolink;

import com.fazecast.jSerialComm.SerialPort;
import mediawall.arduinolink.listeners.ArduinoListener;

public class Main {
    public static void main(String[] args) {
        SerialPort comPort = SerialPort.getCommPorts()[1];
        comPort.openPort();
        comPort.addDataListener(new ArduinoListener());
    }
}
