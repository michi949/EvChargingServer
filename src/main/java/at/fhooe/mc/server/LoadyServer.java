package at.fhooe.mc.server;

import at.fhooe.mc.server.Data.LoadingPort;
import at.fhooe.mc.server.Data.LoadingStation;
import at.fhooe.mc.server.Repository.LoadingStationRepository;
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
    Optimizer optimizer;
    WeatherService weatherService;

    @Autowired
    LoadingStationRepository loadingStationRepository;


    public static void main(String[] args) {
        LoadyServer server =  new LoadyServer();
        SpringApplication.run(LoadyServer.class, args);
        System.out.println("Rest API Started!");
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
            LoadingPort port1 = new LoadingPort(1);
            LoadingPort port2 = new LoadingPort(2);
            LoadingPort port3 = new LoadingPort(3);
            LoadingPort port4 = new LoadingPort(4);
            LoadingPort port5 = new LoadingPort(5);
            LoadingPort port6 = new LoadingPort(6);

            loadingStationRepository.save(new LoadingStation(1, "FH-OOE", new ArrayList<LoadingPort>(List.of(port1, port2))));
            loadingStationRepository.save(new LoadingStation(2, "FH-OOE", new ArrayList<LoadingPort>(List.of(port3, port4))));
            loadingStationRepository.save(new LoadingStation(3, "FH-OOE", new ArrayList<LoadingPort>(List.of(port5, port6))));
        };
    }


}
