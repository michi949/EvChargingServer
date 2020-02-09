package at.fhooe.mc.server.Controller;

import at.fhooe.mc.server.Data.Car;
import at.fhooe.mc.server.Data.LoadingPort;
import at.fhooe.mc.server.Data.Session;
import at.fhooe.mc.server.Data.User;
import at.fhooe.mc.server.Repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;


    @GetMapping(value = "/getUserFromID", produces = "application/json")
    public User getUserID(@RequestParam(name="id") int id) {
        return userRepository.findUserById(id);
    }

    @GetMapping(value = "/getUserFromCard", produces = "application/json")
    public User getUserFromCard(@RequestParam(name="card") int card) {
        return userRepository.findUserByCard(card);
    }

    @RequestMapping(value = "/setUser", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public String setUser(@RequestBody String payload) throws Exception {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payload);
            String name = rootNode.get("name").asText();
            String email = rootNode.get("email").asText();
            int card = rootNode.get("card").asInt();

            if(userRepository.findUserByCard(card) != null && userRepository.findUserByEmail(email) != null){
                return "{success: false}";
            }

            if(name == "" || card == 0 || email == ""){
                return "{success: false}";
            }
            User user = new User(name, email, card);
            userRepository.save(user);
            return "{success: true, id: "+ user.getId() + "}";

        } catch (IOException e) {
            e.printStackTrace();
            return "{success: false}";
        }

    }


    @PutMapping(value = "/updateUser", produces = "application/json")
    public String updateUser() {
        return "success";
    }


    @RequestMapping(value = "/deleteUser", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    public String deleteUser(@RequestBody String payload) throws Exception {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payload);
            JsonNode userNode = rootNode.get("user");
            int id = userNode.get("id").asInt();

            User user = userRepository.findUserById(id);
            if(user == null){
                return "{success: false}";
            }

            userRepository.delete(user);
            return "{success: true}";

        } catch (IOException e) {
            e.printStackTrace();
            return "{success: false}";
        }

    }
}
