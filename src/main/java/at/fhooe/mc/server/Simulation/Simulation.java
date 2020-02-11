package at.fhooe.mc.server.Simulation;


import ChargingEnviroment.Enums.EvSimChargingType;
import ChargingEnviroment.EvSimBattery;
import ChargingEnviroment.EvSimChargingPoint;
import ChargingEnviroment.EvSimChargingStation;
import ChargingEnviroment.EvSimVehicle;
import EnergySources.EvSimSolar;
import Factory.HagenbergSimulationFactory;
import at.fhooe.mc.server.Data.Car;
import at.fhooe.mc.server.Data.LoadingPort;
import at.fhooe.mc.server.Data.Session;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class Simulation {
    ArrayList<EvSimChargingStation> chargingStations = new ArrayList<>();
    EvSimSolar solar;

    public Simulation() {
        chargingStations = HagenbergSimulationFactory.setupEnvironmentHagenberg();
        solar = new EvSimSolar(560000, 0.45, 26.0);
    }

    public ArrayList<EvSimChargingStation> getChargingStations() {
        return chargingStations;
    }

    public void setChargingStations(ArrayList<EvSimChargingStation> chargingStations) {
        this.chargingStations = chargingStations;
    }

    public EvSimSolar getSolar() {
        return solar;
    }

    public void setSolar(EvSimSolar solar) {
        this.solar = solar;
    }

    public EvSimChargingPoint getChargingPoint(int point){
        for(EvSimChargingStation station : chargingStations){
           for(EvSimChargingPoint attachedPoint : station.getEvSimChargingPoints()){
               if(attachedPoint.getId() == point){
                   return attachedPoint;
               }
           }
        }
        return null;
    }

    public void setVehicleToChargingPoint(Session session){
        LoadingPort port = session.getLoadingport();
        Car car = session.getCar();

        EvSimChargingPoint simulationPoint = getChargingPoint(port.getPort());

        if(simulationPoint == null){
            return;
        }

        double currentCapacity = 0;
        if(session.getCurrentCapacity() ==  null || session.getCurrentCapacity() == 0) {
             currentCapacity = Math.random() * (car.getCapacity() - 3700.0D + 1.0D) + 3700.0D;
        } else {
             currentCapacity = session.getCurrentCapacity();
        }
        session.setCurrentCapacity(currentCapacity);
        EvSimVehicle simulationVehicle =  new EvSimVehicle(new EvSimBattery(currentCapacity, car.getCapacity()), car.getType(), EvSimChargingType.AC);


        simulationPoint.addVehicleToPoint(simulationVehicle);
    }
}
