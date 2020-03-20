package at.fhooe.mc.server.Simulation;


import ChargingEnviroment.Enums.EvSimChargingType;
import ChargingEnviroment.EvSimBattery;
import ChargingEnviroment.EvSimChargingPoint;
import ChargingEnviroment.EvSimChargingStation;
import ChargingEnviroment.EvSimVehicle;
import EnergySources.EvSimSolar;
import Factory.HagenbergSimulationFactory;
import at.fhooe.mc.server.Data.*;
import at.fhooe.mc.server.Repository.*;
import at.fhooe.mc.server.Services.Optimizer.Optimizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Service
public class Simulation {
    ArrayList<EvSimChargingStation> chargingStations = new ArrayList<>();
    EvSimSolar solar;

    @Autowired
    WeatherForecastRepository weatherForecastRepository;

    @Autowired
    WeatherRepository weatherRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    Optimizer optimizer;

    @Autowired
    LoadingPortRepository loadingPortRepository;

    @Autowired
    CarRepository carRepository;

    public Simulation() {
        chargingStations = HagenbergSimulationFactory.setupEnvironmentHagenberg();
        solar = new EvSimSolar(53000, 0.45, 26.0, true);
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

    public double getFakeSolar(){
       return Math.random() * (33000.0 - 3700.0 + 1.0) + 3700.0;
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
        LoadingPort port = session.getLoadingPort();
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

    /**
     * Commend out when in produktiv use.
     */
    @Scheduled(cron = "0 40 7 * * ?")
    private void addFirstSession() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 15);
        c.set(Calendar.MINUTE, 45);

        LoadingPort loadingPort = loadingPortRepository.findLoadingPortById(1);
        User user = userRepository.findUserById(1);

        Car car = new Car("GM-456WL", "Tesla Model3", HagenbergSimulationFactory.setupTeslaModel3().getEvSimBattery().getCapacity(), false, 80, false, user);
        carRepository.save(car);

        Session session = new Session(c.getTime(), percentToDouble(80, car), false, car, loadingPort);
        sessionRepository.save(session);
        optimizer.addSession(session);
    }

    @Scheduled(cron = "0 14 8 * * ?")
    private void addSecondSession() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 14);
        c.set(Calendar.MINUTE, 50);

        LoadingPort loadingPort = loadingPortRepository.findLoadingPortById(2);
        User user = userRepository.findUserById(1);
        Car car = new Car("GM-456WL", "Nissan Leaf", HagenbergSimulationFactory.setupNissanLeaf().getEvSimBattery().getCapacity(), false, 80, false, user);
        carRepository.save(car);
        Session session = new Session(c.getTime(), percentToDouble(80, car), false, car, loadingPort);
        sessionRepository.save(session);
        optimizer.addSession(session);
    }

    @Scheduled(cron = "0 33 9 * * ?")
    private void addThirdSession() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 16);
        c.set(Calendar.MINUTE, 32);

        LoadingPort loadingPort = loadingPortRepository.findLoadingPortById(3);
        User user = userRepository.findUserById(1);
        Car car = new Car("GM-456WL", "Audio Etron", HagenbergSimulationFactory.setupAudiETron().getEvSimBattery().getCapacity(), false, 80, false, user);
        carRepository.save(car);
        Session session = new Session(c.getTime(), percentToDouble(75, car), false, car, loadingPort);
        //sessionRepository.save(session);
        optimizer.addSession(session);
    }

    @Scheduled(cron = "0 02 10 * * ?")
    private void addFourthSession() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 18);
        c.set(Calendar.MINUTE, 33);

        LoadingPort loadingPort = loadingPortRepository.findLoadingPortById(4);
        User user = userRepository.findUserById(1);
        Car car = new Car("GM-456WL", "BMW i3", HagenbergSimulationFactory.setupBMWi3().getEvSimBattery().getCapacity(), true, 80, false, user);
        carRepository.save(car);
        Session session = new Session(c.getTime(), percentToDouble(60, car), false, car, loadingPort);
        //sessionRepository.save(session);
        optimizer.addSession(session);
    }

    @Scheduled(cron = "0 20 10 * * ?")
    private void addFifthSession() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 13);
        c.set(Calendar.MINUTE, 24);

        LoadingPort loadingPort = loadingPortRepository.findLoadingPortById(5);
        User user = userRepository.findUserById(1);
        Car car = new Car("GM-456WL", "E Golf", HagenbergSimulationFactory.setupVwEGolf().getEvSimBattery().getCapacity(), false, 80, false, user);
        carRepository.save(car);
        Session session = new Session(c.getTime(), percentToDouble(60, car), false, car, loadingPort);
        //sessionRepository.save(session);
        optimizer.addSession(session);
    }

    private double percentToDouble(int percent, Car car){
        double capacity = car.getCapacity();
        double factor = percent/100.0;

        double value = capacity * factor;

        return value;
    }
}
