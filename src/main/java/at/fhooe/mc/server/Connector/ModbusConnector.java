package at.fhooe.mc.server.Connector;

import at.fhooe.mc.server.Data.LoadingPort;
import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.ModbusHoldingRegisters;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.msg.request.ReadHoldingRegistersRequest;
import com.intelligt.modbus.jlibmodbus.msg.request.ReadInputRegistersRequest;
import com.intelligt.modbus.jlibmodbus.msg.request.WriteMultipleRegistersRequest;
import com.intelligt.modbus.jlibmodbus.msg.request.WriteSingleRegisterRequest;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadHoldingRegistersResponse;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadInputRegistersResponse;
import com.intelligt.modbus.jlibmodbus.msg.response.WriteSingleRegisterResponse;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import com.intelligt.modbus.jlibmodbus.utils.DataUtils;
import com.intelligt.modbus.jlibmodbus.utils.FrameEvent;
import com.intelligt.modbus.jlibmodbus.utils.FrameEventListener;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

@Service
public class ModbusConnector {
    LoadingPort loadingPort;
    TcpParameters parameters;
    ModbusMaster master;
    int serverAddr = 255;
    int transactionID;

    public ModbusConnector() {
    }

    public ModbusConnector(LoadingPort port) {
        this.loadingPort = port;
        InetAddress addr = null;

        try {
            addr = InetAddress.getByName(port.getIp());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        parameters.setHost(addr);
        parameters.setPort(Modbus.TCP_PORT);
        parameters.setKeepAlive(true);
        master = ModbusMasterFactory.createModbusMasterTCP(parameters);
        master.setResponseTimeout(1000);
        transactionID = 0;
        attachListener();
    }

    private void attachListener(){
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
    }

    /**
     * Connecnts to station
     * @return if the connection was success
     */
    public boolean connectToStation(){

        if(master.isConnected()){
            return false;
        }

        try {
            master.connect();
        } catch (ModbusIOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean endConnectionToStation(){

        if(master.isConnected()){
            try {
                master.disconnect();
            } catch (ModbusIOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * Read the input register of a charging point.
     * @param offset The starting adress of the registers.
     * @param quantity The count of the register 16 bit is one register mostyl are 32 bit wich means 2 registers.
     * @return Array of int and the valeus.
     */
    public ArrayList<Integer> readInputRegister(int offset, int quantity){
        ArrayList<Integer> dataList = new ArrayList<>();

        ReadInputRegistersRequest inputRegistersRequest = new ReadInputRegistersRequest();
        try {
            inputRegistersRequest.setServerAddress(serverAddr);
            inputRegistersRequest.setStartAddress(offset);
            inputRegistersRequest.setQuantity(quantity);
            ReadInputRegistersResponse response = (ReadInputRegistersResponse) inputRegistersRequest.getResponse();

            master.processRequest(inputRegistersRequest);
            ModbusHoldingRegisters registers = response.getHoldingRegisters();
            for (int r : registers) {
                dataList.add(r);
            }
        } catch (ModbusNumberException e) {
            e.printStackTrace();
        } catch (ModbusIOException e) {
            e.printStackTrace();
        } catch (ModbusProtocolException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    /**
     * Read the holding register of a charging point.
     * @param offset The starting adress of the registers.
     * @param quantity The count of the register 16 bit is one register mostyl are 32 bit wich means 2 registers.
     * @return Array of int and the valeus.
     */
    public ArrayList<Integer> readHoldingRegister(int offset, int quantity){
        ArrayList<Integer> dataList = new ArrayList<>();

        ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest();
        try {
            request.setServerAddress(serverAddr);
            request.setStartAddress(offset);
            request.setQuantity(quantity);

            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) request.getResponse();

            master.processRequest(request);
            ModbusHoldingRegisters registers = response.getHoldingRegisters();
            for (int r : registers) {
                dataList.add(r);
            }
        } catch (ModbusNumberException e) {
            e.printStackTrace();
        } catch (ModbusIOException e) {
            e.printStackTrace();
        } catch (ModbusProtocolException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    /**
     * Write to a register and gets then the message if sucess.
     * @param offset starting address
     * @param value the value which has to be writen
     * @return the value if it was success.
     */
    public int writeToHoldingRegisters(int offset, int value){
        int result = 0;

        WriteSingleRegisterRequest request = new WriteSingleRegisterRequest();
        try {
            request.setServerAddress(serverAddr);
            request.setStartAddress(offset);
            request.setTransactionId(transactionID);
            request.setValue(value);

            WriteSingleRegisterResponse response = (WriteSingleRegisterResponse) request.getResponse();

            master.processRequest(request);
            transactionID += 1;

            return response.getValue();
        } catch (ModbusNumberException e) {
            e.printStackTrace();
        } catch (ModbusIOException e) {
            e.printStackTrace();
        } catch (ModbusProtocolException e) {
            e.printStackTrace();
        }

        transactionID += 1;
        return result;
    }

}
