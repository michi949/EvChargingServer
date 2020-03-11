package at.fhooe.mc.server.Controller;


import at.fhooe.mc.server.Data.*;
import at.fhooe.mc.server.Repository.CarRepository;
import at.fhooe.mc.server.Repository.LoadingPortRepository;
import at.fhooe.mc.server.Repository.SessionRepository;
import at.fhooe.mc.server.Repository.UserRepository;
import at.fhooe.mc.server.Services.Optimizer.Optimizer;
import at.fhooe.mc.server.Simulation.Simulation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

@RestController
public class SessionController {
    @Autowired
    LoadingPortRepository loadingPortRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    Optimizer optimizer;

    @Autowired
    Simulation simulation;


    @GetMapping(value = "/getSession", produces = "application/json")
    public Session getSession(@RequestParam(name="id") int id) {
        return sessionRepository.findSessionById(id);
    }


    @GetMapping(value = "/getAllSessionFromCar", produces = "application/json")
    public Session getAllSession(@RequestParam(name="id") int id){

        Car car = carRepository.findCarById(id);

        if(car == null) {
            return null;
        }

        return car.getSession();
    }


    /**
     * curl --header "Content-Type: application/json" \ --request POST \ --data {"username":"xyz","password":"xyz"} \ http://localhost:8080/setSession
     * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST \ -d '{"name":"value"}' http://localhost:8080/setSession
     * application/json
     * @param payload
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/setSession", method = RequestMethod.POST, consumes = "application/json")
    public String setSession(@RequestBody String payload) throws Exception {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payload);
            JsonNode userNode = rootNode.get("user");
            JsonNode vehicleNode = rootNode.get("vehicle");
            JsonNode sessionNode = rootNode.get("session");

            int userID = userNode.get("id").asInt();
            int vehicleID = vehicleNode.get("id").asInt();
            boolean isSlowMode = sessionNode.get("isSlowMode").asBoolean();
            long endTime = sessionNode.get("endTime").asLong();
            int endPercent = sessionNode.get("endPercent").asInt();
            int loadingPort = sessionNode.get("loadingPort").asInt();

            User user = userRepository.findUserById(userID);

            if(user == null){
                return "{success: false}";
            }

            Car car = user.getCarFromUserWithID(vehicleID);
            LoadingPort port = loadingPortRepository.findLoadingPortById(loadingPort);

            if(car == null || port == null){
                return "{success: false}";
            }

            if(port.isOccupied()){
                return "{success: false}";
            }

            Date endDate = new Date(endTime);

            Session session = new Session(endDate, percentToDouble(endPercent, car), isSlowMode, car, port);
            sessionRepository.save(session);
            optimizer.addSession(session);

        } catch (IOException e) {
            e.printStackTrace();
            return "{success: false}";
        }

        return "{success: true}";
    }

    @RequestMapping(value = "/pauseSession", method = RequestMethod.PUT, consumes = "application/json")
    public String pauseSession(@RequestBody String payload) throws Exception {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payload);
            JsonNode sessionNode = rootNode.get("session");

            int sessionId = sessionNode.get("id").asInt();
            Session session = sessionRepository.findSessionById(sessionId);

            if(session == null){
                return "{success: false}";
            }

            optimizer.pauseSession(session);

        } catch (IOException e) {
            e.printStackTrace();
            return "{success: false}";
        }

        return "{success: true}";
    }

    @RequestMapping(value = "/stopSession", method = RequestMethod.DELETE, consumes = "application/json")
    public String stopSession(@RequestBody String payload) throws Exception {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payload);
            JsonNode sessionNode = rootNode.get("session");

            int sessionId = sessionNode.get("id").asInt();
            Session session = sessionRepository.findSessionById(sessionId);

            if(session == null){
                return "{success: false}";
            }

            optimizer.stopSession(session, true);

        } catch (IOException e) {
            e.printStackTrace();
            return "{success: false}";
        }

        return "{success: true}";
    }

    @RequestMapping(value = "/restartSession", method = RequestMethod.PUT, consumes = "application/json")
    public String restartSession(@RequestBody String payload) throws Exception {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payload);
            JsonNode sessionNode = rootNode.get("session");

            int sessionId = sessionNode.get("id").asInt();
            Session session = sessionRepository.findSessionById(sessionId);

            if(session == null){
                return "{success: false}";
            }

            optimizer.restartSession(session);

        } catch (IOException e) {
            e.printStackTrace();
            return "{success: false}";
        }

        return "{success: true}";
    }

    private double percentToDouble(int percent, Car car){
        double capacity = car.getCapacity();
        double factor = percent/100.0;

        double value = capacity * factor;

        return value;
    }
}
