import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class BudgetTravelPlanner {
    public static void main(String[] args) throws IOException {
        int port = 9000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        // Set up handlers
        server.createContext("/", new handlers.StaticHandler());
        server.createContext("/plan", new handlers.PlanHandler());
        
        server.setExecutor(null);
        System.out.println("Server started on port " + port);
        System.out.println("Open http://localhost:" + port + " in your browser");
        server.start();
    }
}