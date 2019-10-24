package at.fhooe.mc.server.Connector;

import at.fhooe.mc.server.Data.Weather;
import at.fhooe.mc.server.Parser.WeatherParser;
import at.fhooe.mc.server.Utilitys;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Make an Request to an Weather Server and Store it in weather data.
 * @see at.fhooe.mc.server.Data.Weather
 */
@Service
public class WeatherConnector {
    private String path;
    private final RestTemplate restTemplate;

    public WeatherConnector() {
        this.restTemplate = new RestTemplate();
        this.path = "https://samples.openweathermap.org/data/2.5/weather?";
    }

    /**
     * Query the data from the Weather API and return a weather data.
     * @return Weather Data.
     * @throws UnsupportedEncodingException
     */
    public Weather peformRequest() {

        String url = null;
        try {
            url = path + getParamsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        String jsonWeather =  this.restTemplate.getForObject(url, String.class);

        return WeatherParser.parseWeather(jsonWeather);
    }

    /**
     * Build the request String.
     * @return GET Request String for WeatherAPI
     * @throws UnsupportedEncodingException
     */
    private String getParamsString() throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        Map<String, String> params = setupParameters();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
    }

    /**
     * Setups the parametes for an request.
     * @return HashMap with the parameters.
     */
    private Map<String, String> setupParameters() {
        Map<String, String> params = new HashMap<>();

        params.put("APPID", Utilitys.WEATHERAPI);
        params.put("q", "Hagenberg");

        return params;
    }


}
