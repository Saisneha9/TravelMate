import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WeatherServer {
    private static final String[] INDIAN_CITIES = {
        "Mumbai", "Delhi", "Bangalore", "Hyderabad", "Chennai",
        "Kolkata", "Pune", "Jaipur", "Ahmedabad", "Surat",
        "Lucknow", "Kanpur", "Nagpur", "Patna", "Indore",
        "Thane", "Bhopal", "Visakhapatnam", "Vadodara", "Coimbatore"
    };
    
    private static final String BASE_URL = "https://api.open-meteo.com/v1/forecast";

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        server.createContext("/api/weather", new WeatherHandler());
        server.createContext("/api/cities", new CityHandler());
        server.createContext("/", new StaticFileHandler());
        
        server.setExecutor(null);
        System.out.println("Server started on port " + port);
        server.start();
    }

    static class WeatherHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
                String city = params.getOrDefault("city", "Delhi");
                
                try {
                    String weatherData = fetchWeatherData(city);
                    sendResponse(exchange, weatherData, 200);
                } catch (Exception e) {
                    System.err.println("Error fetching weather: " + e.getMessage());
                    sendResponse(exchange, "{\"error\":\"Weather data unavailable\"}", 500);
                }
            } else {
                sendResponse(exchange, "{\"error\":\"Method not allowed\"}", 405);
            }
        }

        private String fetchWeatherData(String city) throws IOException {
            double[] coordinates = getCityCoordinates(city);
            double latitude = coordinates[0];
            double longitude = coordinates[1];
            
            String urlString = BASE_URL + 
                "?latitude=" + latitude + 
                "&longitude=" + longitude + 
                "&current_weather=true" + 
                "&hourly=relativehumidity_2m,pressure_msl,visibility" +
                "&daily=weathercode,temperature_2m_max,temperature_2m_min" + 
                "&windspeed_unit=kmh" +
                "&timezone=auto";
            
            System.out.println("Fetching weather from: " + urlString);
            
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            
            try {
                int status = conn.getResponseCode();
                if (status != 200) {
                    throw new IOException("HTTP error: " + status);
                }
                
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString();
                }
            } finally {
                conn.disconnect();
            }
        }

        private double[] getCityCoordinates(String city) {
            Map<String, double[]> cityCoordinates = new HashMap<>();
            cityCoordinates.put("Mumbai", new double[]{19.0760, 72.8777});
            cityCoordinates.put("Delhi", new double[]{28.6139, 77.2090});
            cityCoordinates.put("Bangalore", new double[]{12.9716, 77.5946});
            cityCoordinates.put("Hyderabad", new double[]{17.3850, 78.4867});
            cityCoordinates.put("Chennai", new double[]{13.0827, 80.2707});
            cityCoordinates.put("Kolkata", new double[]{22.5726, 88.3639});
            cityCoordinates.put("Pune", new double[]{18.5204, 73.8567});
            cityCoordinates.put("Jaipur", new double[]{26.9124, 75.7873});
            cityCoordinates.put("Ahmedabad", new double[]{23.0225, 72.5714});
            cityCoordinates.put("Surat", new double[]{21.1702, 72.8311});
            cityCoordinates.put("Lucknow", new double[]{26.8467, 80.9462});
            cityCoordinates.put("Kanpur", new double[]{26.4499, 80.3319});
            cityCoordinates.put("Nagpur", new double[]{21.1458, 79.0882});
            cityCoordinates.put("Patna", new double[]{25.5941, 85.1376});
            cityCoordinates.put("Indore", new double[]{22.7196, 75.8577});
            cityCoordinates.put("Thane", new double[]{19.2183, 72.9781});
            cityCoordinates.put("Bhopal", new double[]{23.2599, 77.4126});
            cityCoordinates.put("Visakhapatnam", new double[]{17.6868, 83.2185});
            cityCoordinates.put("Vadodara", new double[]{22.3072, 73.1812});
            cityCoordinates.put("Coimbatore", new double[]{11.0168, 76.9558});

            return cityCoordinates.getOrDefault(city, new double[]{28.6139, 77.2090});
        }
    }

    static class CityHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < INDIAN_CITIES.length; i++) {
                    sb.append("\"").append(INDIAN_CITIES[i]).append("\"");
                    if (i < INDIAN_CITIES.length - 1) sb.append(",");
                }
                sb.append("]");
                
                sendResponse(exchange, sb.toString(), 200);
            } else {
                sendResponse(exchange, "{\"error\":\"Method not allowed\"}", 405);
            }
        }
    }

    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) path = "/weather.html";
            
            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                sendResponse(exchange, "File not found", 404);
                return;
            }
            
            String contentType = "text/html";
            if (path.endsWith(".css")) contentType = "text/css";
            else if (path.endsWith(".js")) contentType = "application/javascript";
            else if (path.endsWith(".png")) contentType = "image/png";
            
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, 0);
            
            try (OutputStream os = exchange.getResponseBody()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }
        }
    }

    private static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null) return result;
        
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }

    private static void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}