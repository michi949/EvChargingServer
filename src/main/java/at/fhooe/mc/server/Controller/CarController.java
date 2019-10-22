package at.fhooe.mc.server.Controller;

import at.fhooe.mc.server.Data.Car;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class CarController {

    /**
     * Provide one car from an given id.
     * @param id the id of the car.
     * @return the car with the matching id.
     */
    @RequestMapping(value = "/getCarFromId", method = RequestMethod.GET)
    public Car getCarFromId(@RequestParam(name="id") Integer id) {
        System.out.println("Request for car with Id: " + id);
        return new Car();
    }

    /**
     *
     * @param user
     * @return
     */
    @GetMapping(value = "/getCarFromUser")
    public ArrayList<Car> getCarsFormUser(@RequestParam(name="user") String user) {

        return  new ArrayList<Car>();
    }


    @PutMapping(value = "/putCarForUser")
    public String putCarForUser(){

        return "success";
    }
}
