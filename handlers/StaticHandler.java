package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;

public class StaticHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String html = "<!DOCTYPE html>" +
                "<html><head>" +
                "<title>Budget Travel Planner</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 20px;background-color:rgb(205, 164, 17)}" +
                "form { max-width: 500px; margin: 0 auto; }" +
                "input, select { width: 100%; padding: 8px; margin: 5px 0; }" +
                "button { background: #4CAF50; color: white; padding: 10px; border: none; width: 100%; }" +
                "#result { margin-top: 20px; padding: 10px; border: 1px solid #ddd; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h1>Indian Cities Budget Travel Planner</h1>" +
                "<form id='travelForm'>" +
                "<input type='text' name='origin' placeholder='Origin (Delhi, Mumbai, Bangalore)' required>" +
                "<input type='text' name='destination' placeholder='Destination (Delhi, Mumbai, Bangalore)' required>" +
                "<input type='number' name='budget' placeholder='Total Budget (₹)' required>" +
                "<input type='number' name='days' placeholder='Number of Days (default: 3)'>" +
                "<input type='number' name='people' placeholder='Number of People (default: 1)'>" +
                "<button type='submit'>Plan My Trip</button>" +
                "</form>" +
                "<div id='result'></div>" +
                "<script>" +
                "document.getElementById('travelForm').addEventListener('submit', function(e) {" +
                "e.preventDefault();" +
                "const formData = new FormData(e.target);" +
                "const params = new URLSearchParams(formData);" +
                "fetch('/plan', { method: 'POST', body: params })" +
                ".then(res => res.json())" +
                ".then(data => {" +
                "const resultDiv = document.getElementById('result');" +
                "if (data.error) {" +
                "resultDiv.innerHTML = `<p style='color:red'>${data.error}</p>`;" +
                "} else {" +
                "resultDiv.innerHTML = " +
                "`<h3>Trip from ${data.origin} to ${data.destination}</h3>" +
                "<p>Duration: ${data.days} days for ${data.people} people</p>" +
                "<p>Transport: ₹${data.transportCost}</p>" +
                "<p>Hotel: ₹${data.hotelCost}</p>" +
                "<p>Food: ₹${data.foodCost}</p>" +
                "<h4>Total Cost: ₹${data.totalCost}</h4>" +
                "<h3 style='color:${data.withinBudget?'green':'red'}'>" +
                "${data.withinBudget?'Within budget':'Over budget'}</h3>" +
                "<p>Your budget: ₹${data.budget}</p>`;" +
                "}});});</script>" +
                "</body></html>";

        exchange.sendResponseHeaders(200, html.length());
        OutputStream os = exchange.getResponseBody();
        os.write(html.getBytes());
        os.close();
    }
}