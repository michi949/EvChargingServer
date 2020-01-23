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
        return sessionRepository.findOne(id);
    }


    @GetMapping(value = "/getAllSessionFromCar", produces = "application/json")
    public Session getAllSession(@RequestParam(name="id") int id){

        Car car = carRepository.findOne(id);

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
            String sessionMode = sessionNode.get("mode").asText();
            String endTime = sessionNode.get("endTime").asText();
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

            Session session = new Session();

        } catch (IOException e) {
            e.printStackTrace();
            return "{success: false}";
        }

        return "{success: true}";
    }

    @PutMapping(value = "/updateSession", produces = "application/json")
    public String updateSession() {

        return "success";
    }

    @PutMapping(value = "/stopSession", produces = "application/json")
    public String stopSession() {

        return "success";
    }
}
