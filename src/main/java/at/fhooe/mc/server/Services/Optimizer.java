package at.fhooe.mc.server.Services;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class Optimizer implements Runnable {


    @Override
    public void run() {
        System.out.println("Optimizer Started!");
    }
}
