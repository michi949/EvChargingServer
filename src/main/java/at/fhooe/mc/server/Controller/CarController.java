package at.fhooe.mc.server.Controller;

import at.fhooe.mc.server.Data.Car;
import at.fhooe.mc.server.Data.User;
import at.fhooe.mc.server.Repository.CarRepository;
import at.fhooe.mc.server.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        return carRepository.findOne(id);
    }


    /**
     * Query with user id.
     * @param id of user.
     * @return list of cars.
     */
    @GetMapping(value = "/getCarFromUser", produces = "application/json")
    public ArrayList<Car> getCarsFormUser(@RequestParam(name="id") Integer id) {
        User user = userRepository.findOne(id);

        if(user == null) {
            return null;
        }

        return new ArrayList<>(user.getCar());
    }


    /**
     * Creats an Car element.
     * @return An information if the put was sucess full or not.
     */
    @PutMapping(value = "/putCarForUser", produces="application/json", consumes="application/json")
    public String putCarForUser(@RequestParam(name = "id") Integer id, @RequestBody String json){

        User user = userRepository.findOne(id);

        if(user == null) {
            return "400";
        }

        Car car = new Car();
        user.getCar().add(car);

        userRepository.save(user);
        return "200";
    }
}
