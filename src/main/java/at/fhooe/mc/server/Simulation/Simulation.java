package at.fhooe.mc.server.Simulation;


import Components.ChargingPoint;
import Components.ChargingStation;
import EnergySources.Solar;
import Factory.HagenbergSimulationFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class Simulation {
    ArrayList<ChargingStation> chargingStations = new ArrayList<>();
    Solar solar;

    public Simulation() {
        chargingStations = HagenbergSimulationFactory.setupEnvironmentHagenberg();
        solar = new Solar(560000, 0.45, 26.0);
    }

    public ArrayList<ChargingStation> getChargingStations() {
        return chargingStations;
    }

    public void setChargingStations(ArrayList<ChargingStation> chargingStations) {
        this.chargingStations = chargingStations;
    }

    public Solar getSolar() {
        return solar;
    }

    public void setSolar(Solar solar) {
        this.solar = solar;
    }

    public ChargingPoint getChargingPoint(int point){
        for(ChargingStation station : chargingStations){
           for(ChargingPoint attachedPoint : station.getChargingPoints()){
               if(attachedPoint.getId() == point){
                   return attachedPoint;
               }
           }
        }
        return null;
    }
}
