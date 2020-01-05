import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

public class ControllerTest  {
    private String path = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    public void getCar() throws Exception {
        String url = path + "/getCarFromId?id=5";
        String result = restTemplate.getForObject(url, String.class);
        System.out.println(result);
    }


    public void postCar() throws Exception {

        
    }

    @Test
    public void getWeather() throws Exception {
        String url = path + "/getCurrentWeather";
        String result = restTemplate.getForObject(url, String.class);
        System.out.println(result);
    }

}
