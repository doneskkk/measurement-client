    import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainClient {

    public static void main(String[] args) {
        final String sensorName = "sensor from client 1demo";

        registerSensor(sensorName);

        Random rndm = new Random();

        double maxT = 50.0;

        for(int i = 0; i<1000; i++){
            postMeasurements(rndm.nextDouble() * maxT, rndm.nextBoolean(), sensorName);
        }




    }

    private static void registerSensor(String name){
        final String url = "http://localhost:8080/api/v1/sensors/registration";
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);

        makePostRequestWithJSONData(url, data);
    }
    private static void postMeasurements(double value, boolean raining, String name){
        final String url = "http://localhost:8080/api/v1/measurements/add";

        Map<String, Object> data = new HashMap<>();
        data.put("value", value);
        data.put("isRaining", raining);
        data.put("sensor", Map.of("name", name));

        makePostRequestWithJSONData(url, data);
    }
    private static void makePostRequestWithJSONData(String url, Map<String, Object> jsonDataObject) {
        final RestTemplate restTemplate = new RestTemplate();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(jsonDataObject, headers);

        try {
            restTemplate.postForObject(url, request, String.class);

            System.out.println("The measurement was successfully sent to the server!");
        } catch (HttpClientErrorException e) {
            System.out.println("Error!");
            System.out.println(e.getMessage());
        }
    }
}
