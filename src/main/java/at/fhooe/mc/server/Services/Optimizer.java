package at.fhooe.mc.server.Services;

import org.springframework.stereotype.Component;

@Component
public class Optimizer implements Runnable {



    @Override
    public void run() {
        System.out.println("Optimizer Started!");
    }
}
