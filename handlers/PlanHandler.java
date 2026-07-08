package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.util.*;
import java.net.URLDecoder;

public class PlanHandler implements HttpHandler {
    private static final Map<String, City> cities = new HashMap<>();
    static {
        cities.put("delhi", new City("Delhi", 28.61, 77.23, 3500, 600));
        cities.put("mumbai", new City("Mumbai", 19.07, 72.87, 4000, 700));
        cities.put("bangalore", new City("Bangalore", 12.97, 77.59, 3800, 650));
        cities.put("hyderabad", new City("Hyderabad", 17.38, 78.48, 3200, 550));
        cities.put("chennai", new City("Chennai", 13.08, 80.27, 3300, 500));
        cities.put("kolkata", new City("Kolkata", 22.57, 88.36, 3000, 450));
        
        // Tourist Destinations
        cities.put("jaipur", new City("Jaipur", 26.91, 75.78, 2800, 500));
        cities.put("udaipur", new City("Udaipur", 24.58, 73.68, 3000, 550));
        cities.put("goa", new City("Goa", 15.29, 74.12, 3500, 600));
        cities.put("kochi", new City("Kochi", 9.93, 76.26, 2500, 450));
        cities.put("shimla", new City("Shimla", 31.10, 77.17, 3200, 500));
        cities.put("manali", new City("Manali", 32.23, 77.18, 3000, 500));
        
        // Other Major Cities
        cities.put("pune", new City("Pune", 18.52, 73.85, 3000, 500));
        cities.put("ahmedabad", new City("Ahmedabad", 23.02, 72.57, 2500, 400));
        cities.put("surat", new City("Surat", 21.17, 72.83, 2200, 350));
        cities.put("lucknow", new City("Lucknow", 26.84, 80.94, 2000, 300));
        cities.put("kanpur", new City("Kanpur", 26.44, 80.33, 1800, 250));
        cities.put("nagpur", new City("Nagpur", 21.14, 79.08, 2000, 300));
        cities.put("indore", new City("Indore", 22.71, 75.85, 2200, 350));
        cities.put("bhopal", new City("Bhopal", 23.25, 77.41, 2000, 300));
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        // Parse form data
        String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines().reduce("", (accumulator, actual) -> accumulator + actual);
        
        Map<String, String> params = parseFormData(requestBody);
        String response = generateTravelPlan(params);
        
        sendResponse(exchange, 200, response);
    }

    private Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            String value = URLDecoder.decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }

    private String generateTravelPlan(Map<String, String> params) {
        String origin = params.get("origin").toLowerCase();
        String destination = params.get("destination").toLowerCase();
        int budget = Integer.parseInt(params.get("budget"));
        int days = Integer.parseInt(params.getOrDefault("days", "3"));
        int people = Integer.parseInt(params.getOrDefault("people", "1"));

        if (!cities.containsKey(origin) || !cities.containsKey(destination)) {
            return "{\"error\":\"Invalid city selection\"}";
        }

        City originCity = cities.get(origin);
        City destCity = cities.get(destination);

        // Calculate costs
        int transportCost = calculateTransportCost(originCity, destCity);
        int hotelCost = destCity.hotelPrice * days * people;
        int foodCost = destCity.foodCost * days * people;
        int totalCost = transportCost + hotelCost + foodCost;

        // Generate response
        return String.format("{" +
                "\"origin\":\"%s\"," +
                "\"destination\":\"%s\"," +
                "\"days\":%d," +
                "\"people\":%d," +
                "\"budget\":%d," +
                "\"transportCost\":%d," +
                "\"hotelCost\":%d," +
                "\"foodCost\":%d," +
                "\"totalCost\":%d," +
                "\"withinBudget\":%b" +
                "}",
                originCity.name, destCity.name, days, people, budget,
                transportCost, hotelCost, foodCost, totalCost,
                totalCost <= budget);
    }

    private int calculateTransportCost(City origin, City destination) {
        // Simplified distance calculation
        double distance = Math.sqrt(Math.pow(origin.lat - destination.lat, 2) + 
                         Math.pow(origin.lon - destination.lon, 2));
        return (int)(distance * 1000); // 1000 per unit distance
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}