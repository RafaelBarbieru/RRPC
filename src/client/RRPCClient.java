package client;

import middleware.RRPCObject;
import middleware.RRPCParser;
import middleware.RRPCProxy;
import middleware.RRPCType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static utils.Constants.*;
import static utils.StringUtils.f;

/**
 * @author Rafael Barbieru
 */
public class RRPCClient implements AutoCloseable {

    // Attributes
    private String _hostname;
    private int _port;
    private BufferedReader _in;
    private BufferedReader _stdin;
    private PrintWriter _out;
    private Socket _client;
    private RRPCParser _parser;

    // Constructors
    public RRPCClient() {
        _hostname = LOCALHOST;
        _port = DEFAULT_RPC_PORT;
    }
    public RRPCClient(String hostname, int port) {
        _hostname = hostname;
        _port = port;
    }

    // Methods

    /**
     * Starts the client socket
     * @throws IOException
     */
    public void startClient() throws IOException {
        // Initializing the client socket
        _client = new Socket(_hostname, _port);

        // Creating the in and out streams
        _in = new BufferedReader(new InputStreamReader(_client.getInputStream()));
        _stdin = new BufferedReader(new InputStreamReader(System.in));
        _out = new PrintWriter(_client.getOutputStream(), true);

        /*String userInput;
        System.out.print("Your message: ");
        while(!(userInput = _stdin.readLine()).isEmpty()) {
            _out.println(userInput);
            System.out.println(f("Line sent to server: %s", _in.readLine()));
            System.out.print("Your message: ");
        }*/
    }

    /**
     * Closes the client socket and all the streams
     * @throws IOException
     */
    private void closeClient() throws IOException {
        _in.close();
        _stdin.close();
        _out.close();
        _client.close();
    }

    public Object setHandler(String handlerName, Class clazz) throws IOException {
        // Sending the handler and class association to the server
        _out.println("sethandler:" + handlerName + ";" + clazz.getSimpleName());

        // Wait for the server's response
        String response;
        while(!(response = _in.readLine()).isEmpty()) {
            if (response.startsWith("true")) {
                System.out.println(response.split(";")[1]);
                return clazz;
            } else {
                System.err.println(f("Server has responded with an error: %s", response.split(";")[1]));
                return null;
            }
        }
        return null;
    }

    public Object execute(String handler, Object... args) throws IOException {
        System.out.println("RRPCClient execution in progress...");

        // Parsing the request
        String request = RRPCParser.parse(RRPCType.REQUEST, handler, args);

        // Sending the request to the server
        System.out.println(f("Sending the request to the server:\n{\n%s\n}", request));
        _out.println(request);

        // Wait for the server's response
        String response;
        while(!(response = _in.readLine()).isEmpty()) {
            RRPCObject objResponse = RRPCParser.unparse(response);

            // Extracting the result from the RRPCObject response
            Object pureResponse = objResponse.getArgs()[0];

            return pureResponse;
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        closeClient();
    }
}
