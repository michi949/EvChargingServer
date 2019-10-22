package at.fhooe.mc.server.Connector;

import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;
import de.re.easymodbus.modbusclient.gui.EasyModbusTcpClient;

import java.io.IOException;

public class ModbusConnector {
    private ModbusClient modbusClient;

    public ModbusConnector(String ip, int port) {
        modbusClient = new ModbusClient(ip, port);
    }

    public String readRegister() {
        try {
            modbusClient.Connect();
            System.out.println(modbusClient.ReadCoils(0, 1)[0]);
        } catch (ModbusException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}
