package at.fhooe.mc.server.Connector;

import at.fhooe.mc.server.Data.Session;
import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.msg.request.ReadHoldingRegistersRequest;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadHoldingRegistersResponse;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import java.net.InetAddress;

public class ModbusConnector {


    public ModbusConnector() {

        try {
            TcpParameters tcpParameters = new TcpParameters();

            //tcp parameters have already set by default as in example
            tcpParameters.setHost(InetAddress.getLocalHost());
            tcpParameters.setKeepAlive(true);
            tcpParameters.setPort(Modbus.TCP_PORT);

            //if you would like to set connection parameters separately,
            // you should use another method: createModbusMasterTCP(String host, int port, boolean keepAlive);
            ModbusMaster m = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
            Modbus.setAutoIncrementTransactionId(true);

            int slaveId = 1;
            int offset = 0;
            int quantity = 10;

            try {
                // since 1.2.8
                if (!m.isConnected()) {
                    m.connect();
                }

                // at next string we receive ten registers from a slave with id of 1 at offset of 0.
                int[] registerValues = m.readHoldingRegisters(slaveId, offset, quantity);

                for (int value : registerValues) {
                    System.out.println("Address: " + offset++ + ", Value: " + value);
                }
                // also since 1.2.8.4 you can create your own request and process it with the master
                offset = 0;
                ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest();
                request.setServerAddress(1);
                request.setStartAddress(offset);
                request.setTransactionId(0);
                ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) m.processRequest(request);
                // you can get either int[] containing register values or byte[] containing raw bytes.
                for (int value : response.getRegisters()) {
                    System.out.println("Address: " + offset++ + ", Value: " + value);
                }
            } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException e) {
                e.printStackTrace();
            } finally {
                try {
                    m.disconnect();
                } catch (ModbusIOException e) {
                    e.printStackTrace();
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stopSessionModBus(Session session){

    }

    public void adjustSessionModBus(Session session){

    }

    public void pauseSessionModBus(Session session){

    }

    public void readCardReader(Session session){

    }

    public void checkSessions(Session session){

    }

}