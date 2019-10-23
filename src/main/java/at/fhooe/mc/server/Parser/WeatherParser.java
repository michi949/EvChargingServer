package at.fhooe.mc.server.Parser;

import at.fhooe.mc.server.Data.Weather;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

import java.util.Map;

/**
 * Parse the Data from JSON String.
 */
public class WeatherParser {

    public static Weather parseWeather(String jsonWeather) {

        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map<String, Object> map = springParser.parseMap(jsonWeather);

        String[] mapArray = new String[map.size()];
        System.out.println("Items found: " + mapArray.length);


        return new Weather();
    }

}
