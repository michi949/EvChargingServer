package at.fhooe.mc.server.Connector;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.ModbusHoldingRegisters;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.msg.request.ReadHoldingRegistersRequest;
import com.intelligt.modbus.jlibmodbus.msg.request.ReadInputRegistersRequest;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadHoldingRegistersResponse;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadInputRegistersResponse;
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlaveFactory;
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlaveTCP;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import com.intelligt.modbus.jlibmodbus.utils.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observer;

public class ExampleTcp{

    static public void main(String[] argv) {

        //
        try {
            Modbus.setLogLevel(Modbus.LogLevel.LEVEL_DEBUG);
            TcpParameters tcpParameters = new TcpParameters();
            //listening on localhost
            InetAddress addr = InetAddress.getByName("10.23.99.24");
            tcpParameters.setHost(addr);
            tcpParameters.setPort(Modbus.TCP_PORT);
            tcpParameters.setKeepAlive(true);

            ModbusSlaveTCP slave = (ModbusSlaveTCP) ModbusSlaveFactory.createModbusSlaveTCP(tcpParameters);
            ModbusMaster master = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);


            master.setResponseTimeout(1000);
            slave.setServerAddress(Modbus.TCP_DEFAULT_ID);
            slave.setBroadcastEnabled(true);
            slave.setReadTimeout(1000);

            FrameEventListener listener = new FrameEventListener() {
                @Override
                public void frameSentEvent(FrameEvent event) {
                    System.out.println("frame sent " + DataUtils.toAscii(event.getBytes()));
                }

                @Override
                public void frameReceivedEvent(FrameEvent event) {
                    System.out.println("frame recv " + DataUtils.toAscii(event.getBytes()));
                }
            };

            master.addListener(listener);
            slave.addListener(listener);
            Observer o = new ModbusSlaveTcpObserver() {
                @Override
                public void clientAccepted(TcpClientInfo info) {
                    System.out.println("Client connected " + info.getTcpParameters().getHost());
                }

                @Override
                public void clientDisconnected(TcpClientInfo info) {
                    System.out.println("Client disconnected " + info.getTcpParameters().getHost());
                }
            };
            slave.addObserver(o);

            ModbusHoldingRegisters holdingRegisters = new ModbusHoldingRegisters(1000);

            //place the number PI at offset 0
            holdingRegisters.setFloat64At(0, Math.PI);

            slave.getDataHolder().setHoldingRegisters(holdingRegisters);

            Modbus.setAutoIncrementTransactionId(true);

            //slave.listen();

            master.connect();

            //prepare request
            /**
             ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest();
             request.setServerAddress(255);
             request.setStartAddress(2);
             request.setQuantity(2); //1 is one register with a size of 16bit
             ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) request.getResponse();

             master.processRequest(request);
             ModbusHoldingRegisters registers = response.getHoldingRegisters();
             for (int r : registers) {
             System.out.println(r);
             } */

            ///Read Input register requet
            ReadInputRegistersRequest inputRegistersRequest = new ReadInputRegistersRequest();
            inputRegistersRequest.setServerAddress(255);
            inputRegistersRequest.setStartAddress(108);
            inputRegistersRequest.setQuantity(2);
            ReadInputRegistersResponse response = (ReadInputRegistersResponse) inputRegistersRequest.getResponse();

            master.processRequest(inputRegistersRequest);
            ModbusHoldingRegisters registers = response.getHoldingRegisters();
            for (int r : registers) {
                System.out.println(r);
            }

            //get float
            //000100000006FF0300000020
            /*
            System.out.println("PI is approximately equal to " + registers.getFloat64At(0));
            System.out.println();  */

            master.disconnect();
            slave.shutdown();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (ModbusProtocolException e) {
            e.printStackTrace();
        } catch (ModbusIOException e) {e.printStackTrace();
            e.printStackTrace();
        } catch (ModbusNumberException e) {
            e.printStackTrace();
        }
    }
}