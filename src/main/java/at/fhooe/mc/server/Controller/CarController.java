package at.fhooe.mc.server.Controller;

import at.fhooe.mc.server.Data.Car;
import at.fhooe.mc.server.Data.User;
import at.fhooe.mc.server.Repository.CarRepository;
import at.fhooe.mc.server.Repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class CarController {

    @Autowired
    CarRepository carRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * Provide one car from an given id.
     * @param id the id of the car.
     * @return the car with the matching id.
     */
    @RequestMapping(value = "/getCarFromId", method = RequestMethod.GET, produces = "application/json")
    public Car getCarFromId(@RequestParam(name="id") Integer id) {
        return carRepository.findCarById(id);
    }


    /**
     * Query with user id.
     * @param id of user.
     * @return list of cars.
     */
    @GetMapping(value = "/getCarFromUser", produces = "application/json")
    public ArrayList<Car> getCarsFormUser(@RequestParam(name="id") Integer id) {
        User user = userRepository.findUserById(id);

        if(user == null) {
            return null;
        }

        return new ArrayList<>(user.getCar());
    }

    @RequestMapping(value = "/deleteCar", method = RequestMethod.DELETE, consumes = "application/json")
    public String deleteCar(@RequestBody String payload) throws Exception{
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payload);
            JsonNode carNode = rootNode.get("vehicle");
            int id = carNode.get("id").asInt();

            Car car = carRepository.findCarById(id);
            if(car == null){
                return "{success: false}";
            } else if (car.getSession() != null){
                return "{success: false, message: \"Car is in Use.\"}";
            }

            carRepository.delete(car);

            return "{success: true}";

        } catch (IOException e) {
            e.printStackTrace();
            return "{success: false}";
        }
    }


    @RequestMapping(value = "/updateCar", method = RequestMethod.PUT, consumes = "application/json")
    public String updateCar(@RequestBody String payload) throws Exception{
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payload);
            JsonNode carNode = rootNode.get("vehicle");
            int id = carNode.get("id").asInt();

            Car car = carRepository.findCarById(id);
            if(car == null){
                return "{success: false}";
            } else if (car.getSession() != null){
                return "{success: false, message: \"Car is in Use.\"}";
            }

            car.setPlate(carNode.get("plate").asText());
            car.setType(carNode.get("type").asText());
            car.setCapacity(carNode.get("capacity").asDouble());
            car.setOnePhase(carNode.get("isOnePhase").asBoolean());
            car.setDefaultPercent(carNode.get("defaultPercent").asInt());
            car.setDefaultIsSlowMode(carNode.get("defaultIsSlowMode").asBoolean());
            carRepository.save(car);

            return "{success: true}";

        } catch (IOException e) {
            e.printStackTrace();
            return "{success: false}";
        }
    }

    /**
     * {"user":{"id":"1","card": "2131231412"}, "vehicle": {"plate":"GM-567FE", "type": "Tesla S", "capacity": "155748", "isOnePhase": "false", "defaultPercent": "80", "defaultIsSlowMode": "false"}}
     * Creats an Car element.
     * @return An information if the put was sucess full or not.
     */
    @RequestMapping(value = "/createCar", method = RequestMethod.POST, consumes = "application/json")
    public String createCar(@RequestBody String payload) throws Exception{

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payload);
            JsonNode userNode = rootNode.get("user");
            int id = userNode.get("id").asInt();

            User user = userRepository.findUserById(id);
            if(user == null){
                return "{success: false}";
            }

            JsonNode carNode = rootNode.get("vehicle");
            String plate = carNode.get("plate").asText();
            String type = carNode.get("type").asText();
            Double capacity = carNode.get("capacity").asDouble();
            boolean isOnePhase = carNode.get("isOnePhase").asBoolean();
            int defaultPercent = carNode.get("defaultPercent").asInt();
            boolean defaultIsSlowMode = carNode.get("defaultIsSlowMode").asBoolean();

            Car car = new Car(plate, type, capacity, isOnePhase, defaultPercent, defaultIsSlowMode, user);
            carRepository.save(car);

            return "{success: true, id: "+ car.getId() + "}";

        } catch (IOException e) {
            e.printStackTrace();
            return "{success: false}";
        }
    }
}
