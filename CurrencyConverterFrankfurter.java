import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CurrencyConverterFrankfurter {

    public static void main(String[] args) throws IOException {
        int port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new HomeHandler());
        server.createContext("/convert", new ConversionHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server running on http://localhost:" + port);
    }

    static class HomeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                StringBuilder currencyOptions = new StringBuilder();
                Set<String> currencyCodes = CurrencyUtils.getCurrencyCodes();
                for (String code : currencyCodes) {
                    currencyOptions.append("<option value='").append(code).append("'>")
                            .append(code).append("</option>");
                }
    
                String response = """
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Currency Converter</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            body {
                font-family: Arial, sans-serif;
                display: flex;
                height: 100vh;
                background-color: #f4b400;
                justify-content: center;
                align-items: center;
            }
            .container {
                background: #fff;
                padding: 40px;
                border-radius: 10px;
                box-shadow: 0 0 20px rgba(0,0,0,0.2);
                max-width: 400px;
                width: 100%;
            }
            h1 {
                font-size: 32px;
                margin-bottom: 20px;
                color: #333;
            }
            p {
                font-size: 14px;
                margin-bottom: 25px;
                color: #555;
            }
            form {
                display: flex;
                flex-direction: column;
            }
            label {
                margin: 10px 0 5px;
                font-weight: bold;
            }
            input, select {
                padding: 10px;
                margin-bottom: 15px;
                border: 1px solid #ccc;
                border-radius: 5px;
                font-size: 14px;
            }
            button {
                padding: 12px;
                background-color: #000;
                color: #fff;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                font-weight: bold;
                transition: background 0.3s ease;
            }
            button:hover {
                background-color: #333;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>Currency converter</h1>
            <p>How Digital Currencies Are Changing the World</p>
            <form action="/convert" method="get">
                <label for="amount">Amount</label>
                <input type="number" name="amount" id="amount" step="any" required>
    
                <label for="from">From</label>
                <select name="from" id="from">
    """ + currencyOptions + """
                </select>
    
                <label for="to">To</label>
                <select name="to" id="to">
    """ + currencyOptions + """
                </select>
    
                <button type="submit">Convert</button>
            </form>
        </div>
    </body>
    </html>
    """;
    
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
    }
    

    static class ConversionHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            URI requestURI = exchange.getRequestURI();
            Map<String, String> params = queryToMap(requestURI.getQuery());

            String response;

            try {
                double amount = Double.parseDouble(params.get("amount"));
                String from = params.get("from").toUpperCase();
                String to = params.get("to").toUpperCase();

                if (amount <= 0) {
                    throw new IllegalArgumentException("Amount must be positive");
                }

                double convertedAmount = getConvertedAmount(from, to, amount);

                response = "<html><body style='font-family:Arial;text-align:center;padding:40px;'>" +
                        "<h2>Conversion Result</h2>" +
                        "<p style='font-size:20px;'>" + amount + " " + from + " = " +
                        String.format("%.2f", convertedAmount) + " " + to + "</p>" +
                        "<a href='/'>Back</a>" +
                        "</body></html>";
            } catch (Exception e) {
                response = "<html><body><p>Error: " + e.getMessage() + "</p><a href='/'>Back</a></body></html>";
            }

            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

        private double getConvertedAmount(String from, String to, double amount) throws IOException, InterruptedException {
            String url = "https://api.frankfurter.app/latest?amount=" + amount + "&from=" + from + "&to=" + to;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            String marker = "\"" + to + "\":";
            int start = body.indexOf(marker);
            if (start == -1) throw new IOException("Currency not found");

            start += marker.length();
            int end = body.indexOf(",", start);
            if (end == -1) {
                end = body.indexOf("}", start);
            }

            String rateStr = body.substring(start, end).trim();
            return Double.parseDouble(rateStr);
        }

        private Map<String, String> queryToMap(String query) {
            Map<String, String> result = new HashMap<>();
            if (query == null) return result;
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) result.put(entry[0], entry[1]);
            }
            return result;
        }
    }
}

class CurrencyUtils {
    public static Set<String> getCurrencyCodes() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.frankfurter.app/currencies"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            body = body.replaceAll("[{}\"]", "");
            String[] entries = body.split(",");
            Set<String> codes = new java.util.HashSet<>();
            for (String entry : entries) {
                String[] parts = entry.split(":");
                if (parts.length > 0) {
                    codes.add(parts[0].trim());
                }
            }
            return codes;
        } catch (Exception e) {
            return Set.of("USD", "EUR", "INR", "GBP", "JPY"); // fallback
        }
    }
}