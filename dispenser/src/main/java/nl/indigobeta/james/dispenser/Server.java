package nl.indigobeta.james.dispenser;

import nl.indigobeta.james.dispenser.Controllers.ArduinoController;
import nl.indigobeta.james.dispenser.Controllers.Calculator;
import nl.indigobeta.james.dispenser.Controllers.Database;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

/**
 * Created by jelle on 06/10/2016.
 */
public class Server
{
    private int port = 8001;
    private static Database DB_INSTANCE;
    private static ArduinoController ARD_CONTROLLER;
    private static Calculator CALCULATOR;

    public static void main(String[] args)
    {
        //Create the connection to the arduino
        ARD_CONTROLLER = new ArduinoController();
        //Create the database connection
        DB_INSTANCE = new Database();
        //Create a calculator
        CALCULATOR = new Calculator();

        //Try to start the server
        try
        {
            new Server();
        }
        catch(Exception e)
        {
            System.out.println("Failed to start server. " +e);
        }
    }

    public Server() throws Exception
    {
        //Create a new server and start it
        HttpServer server = initWebserver();
        server.start();
        System.out.println("The server is running on port: " + port);

        while(true)
        {
            Thread.sleep(1000);
        }
    }

    /*
     * Initialises the webserver.
     * Returns the created HTTPserver.
     */
    private HttpServer initWebserver()
    {
        ResourceConfig config = new ResourceConfig(DispenserEndpoint.class);
        config.register(JacksonJaxbJsonProvider.class);
        //Set the server IP and port
        URI uri = URI.create("http://0.0.0.0:" + port);

        return GrizzlyHttpServerFactory.createHttpServer(uri, config, true);
    }

    public static ArduinoController getArduinoController()
    {
        return ARD_CONTROLLER;
    }

    public static Database getDatabase()
    {
        return DB_INSTANCE;
    }

    public static Calculator getCalculator()
    {
        return CALCULATOR;
    }
}
