package at.fhooe.mc.server;

import at.fhooe.mc.server.Connector.ModbusConnector;
import at.fhooe.mc.server.Logging.ActionLogger;
import org.springframework.boot.SpringApplication;

public class ModbusTest {

    public static void main(String[] args) {
        System.out.println("Test");

        ModbusConnector modbusConnector = new ModbusConnector();
    }

}
