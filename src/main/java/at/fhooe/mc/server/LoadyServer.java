package at.fhooe.mc.server;

import at.fhooe.mc.server.Services.Optimizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoadyServer {
    Optimizer optimizer;

    public static void main(String[] args) {
        LoadyServer server =  new LoadyServer();
        SpringApplication.run(LoadyServer.class, args);
        System.out.println("Rest API Started!");

        server.startOptimizer();
    }


    private void startOptimizer() {
        optimizer = new Optimizer();
        optimizer.run();
    }


}
