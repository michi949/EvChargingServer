package at.fhooe.mc.server;

import at.fhooe.mc.server.Connector.ModbusConnector;
import org.springframework.boot.SpringApplication;

public class Test {
    public static void main(String[] args) {
        ModbusConnector connector = new ModbusConnector("127.0.0.1", 500);
        connector.readRegister();
    }
}
