package at.fhooe.mc.server;

import at.fhooe.mc.server.Data.LoadingPort;
import at.fhooe.mc.server.Data.LoadingStation;
import at.fhooe.mc.server.Data.SolarPanels;
import at.fhooe.mc.server.Data.User;
import at.fhooe.mc.server.Logging.ActionLogger;
import at.fhooe.mc.server.Repository.LoadingStationRepository;
import at.fhooe.mc.server.Repository.SolarPanelsRepository;
import at.fhooe.mc.server.Repository.UserRepository;
import at.fhooe.mc.server.Services.Optimizer.Optimizer;
import at.fhooe.mc.server.Services.WeatherService;
import at.fhooe.mc.server.Simulation.Simulation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class LoadyServer {
    WeatherService weatherService;

    @Autowired
    LoadingStationRepository loadingStationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SolarPanelsRepository solarPanelsRepository;

    Optimizer optimizer;


    public static void main(String[] args) {
        LoadyServer server =  new LoadyServer();
        SpringApplication.run(LoadyServer.class, args);
        ActionLogger.creatNewFile();
        ActionLogger.writeLineToFile("Rest Api Started.");
        server.startServices();
    }

    /**
     * Starts the Optimizer and the Weather Service.
     */
    private void startServices() {
        optimizer = new Optimizer();
        optimizer.run();
        weatherService = new WeatherService(optimizer);
    }


    /**
     * Inital setup of Database
     * @return Closure of Setup Data.
     */
    @Bean
    InitializingBean sendDatabase() {
        return () -> {
            LoadingPort port1 = new LoadingPort(1, "10.23.99.21");
            LoadingPort port2 = new LoadingPort(2, "10.23.99.22");
            LoadingPort port3 = new LoadingPort(3, "10.23.99.23");
            LoadingPort port4 = new LoadingPort(4, "10.23.99.24");
            LoadingPort port5 = new LoadingPort(5, "10.23.99.25");
            LoadingPort port6 = new LoadingPort(6, "10.23.99.26");

            loadingStationRepository.save(new LoadingStation(1, "FH-OOE", new ArrayList<LoadingPort>(List.of(port1, port2))));
            loadingStationRepository.save(new LoadingStation(2, "FH-OOE", new ArrayList<LoadingPort>(List.of(port3, port4))));
            loadingStationRepository.save(new LoadingStation(3, "FH-OOE", new ArrayList<LoadingPort>(List.of(port5, port6))));

            User user = userRepository.findUserByCard(123456789);
            if(user == null){
                user = new User("Michael Reder", "reder949@gmail.com", 123456789);
            }
            userRepository.save(user);

            SolarPanels solarPanels = new SolarPanels();
            solarPanelsRepository.save(solarPanels);
        };
    }
}
