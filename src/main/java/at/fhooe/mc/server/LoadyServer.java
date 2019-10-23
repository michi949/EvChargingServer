package at.fhooe.mc.server;

import at.fhooe.mc.server.Connector.WeatherConnector;
import at.fhooe.mc.server.Services.Optimizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.UnsupportedEncodingException;

@SpringBootApplication
public class LoadyServer {
    Optimizer optimizer;

    public static void main(String[] args) {
        LoadyServer server =  new LoadyServer();
        SpringApplication.run(LoadyServer.class, args);
        System.out.println("Rest API Started!");
        server.startOptimizer();
    }



    /**
     * Starts the Optimizer
     */
    private void startOptimizer() {
        optimizer = new Optimizer();
        optimizer.run();
    }
}
