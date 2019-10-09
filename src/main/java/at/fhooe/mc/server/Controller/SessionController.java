package at.fhooe.mc.server.Controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    @RequestMapping("/")
    public String getSession() {
        return "Congratulations from BlogController.java";
    }
}
