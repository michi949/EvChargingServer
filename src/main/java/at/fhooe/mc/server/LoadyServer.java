package at.fhooe.mc.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoadyServer {
    public static void main(String[] args) {
        SpringApplication.run(LoadyServer.class, args);
        System.out.println("Server started!");
    }
}
