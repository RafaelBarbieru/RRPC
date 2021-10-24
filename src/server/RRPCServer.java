package server;

import middleware.RRPCObject;
import middleware.RRPCParser;
import middleware.RRPCType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.Constants.DEFAULT_RPC_PORT;
import static utils.StringUtils.f;
import static utils.StringUtils.title;

/**
 * @author Rafael Barbieru
 */
public class RRPCServer implements AutoCloseable {

    // Attributes
    private int _port = DEFAULT_RPC_PORT;
    private ServerSocket _server;
    private Socket _client;
    private PrintWriter _out;
    private BufferedReader _in;
    private Map<String, Class> handlerClassAssociations;

    // Constructors
    /**
     * Default constructor
     */
    public RRPCServer() {
        handlerClassAssociations = new HashMap<>();
    }

    /**
     * Port-specific constructor
     * @param port
     */
    public RRPCServer(int port) {
        _port = port;
        handlerClassAssociations = new HashMap<>();
    }

    // Methods

    /**
     * Starts the server
     * @throws IOException
     */
    public void startServer() throws IOException, IndexOutOfBoundsException {

        // Starting the server
        _server = new ServerSocket(_port);
        System.out.println(title(f("Server started on port %d", _port)));

        // Waiting for a client to connect
        System.out.println(f("\n-- Listening for requests... --"));
        _client = _server.accept();
        System.out.println(f("\n== Client %s connected to server! ==", _client.getLocalAddress().getHostAddress()));

        // Creating input and output streams
        _out = new PrintWriter(_client.getOutputStream(), true);
        _in = new BufferedReader(new InputStreamReader(_client.getInputStream()));

        // Getting input from client
        String inputLine;
        while(true) {
            while (!(inputLine = _in.readLine()).isEmpty()) {
                System.out.println(f("Line received from %s: %s", _client.getLocalAddress().getHostAddress(), inputLine));
                if (!inputLine.isEmpty()) {

                    // Detecting if it's a sethandler request or a remote execution request
                    if (inputLine.startsWith("sethandler")) {

                        // It's a sethandler request

                        // Associating the handler name with the correspondent class
                        String handlerName = null;
                        String clazz = null;
                        try {
                            List<String> handlerNameAndClass = RRPCParser.getHandlerNameAndClass(inputLine);
                            handlerName = handlerNameAndClass.get(0);
                            clazz = handlerNameAndClass.get(1);
                            String className = "server.implementations." + clazz + "Impl";
                            handlerClassAssociations.put(
                                    handlerName,
                                    Class.forName(className)
                            );

                            // Letting the client know the handler has been set
                            _out.println(f("true;The handler %s has been set for the class %s", handlerName, clazz));
                        } catch (ClassNotFoundException ex) {
                            // Letting the client know there is no class with that name
                            _out.println(f("false;No class called '%s' has been found", clazz));
                        }

                    } else if (inputLine.startsWith("req")) {

                        // It's a remote execution request

                        // Parsing the request string to an RRPCObject
                        RRPCObject obj = RRPCParser.unparse(inputLine);

                        // Searching for the handler name's class
                        Class clazz = handlerClassAssociations.get(obj.getHandlerName().split("\\.")[0]);

                        // Executing the requested method
                        String methodName = obj.getHandlerName().split("\\.")[1];
                        try {
                            Object[] args = obj.getArgs();
                            Class[] argsClasses = new Class[args.length];
                            for (int i = 0; i < args.length; i++) {
                                Object arg = args[i];
                                argsClasses[i] = arg.getClass();
                            }
                            Method method = clazz.getMethod(methodName, argsClasses);

                            // Invoking the method
                            Object result = method.invoke(clazz.getDeclaredConstructor().newInstance(), args);

                            // Creating a response with the result
                            RRPCObject responseObject = new RRPCObject();
                            responseObject.setType(RRPCType.RESPONSE);
                            responseObject.setHandlerName(obj.getHandlerName());
                            responseObject.setArgs(new Object[]{result});

                            // Returning the response to the client
                            String response = RRPCParser.parse(responseObject);
                            _out.println(response);

                        } catch (NoSuchMethodException |
                                IllegalAccessException |
                                InvocationTargetException |
                                InstantiationException ex) {
                            // Letting the client know there is no method with that name
                            _out.println(
                                    f("Internal Server Error while executing method %s for handler %s!",
                                            methodName,
                                            obj.getHandlerName()
                                    )
                            );
                        }

                    }
                }
            }
        }
    }

    /**
     * Closes the server and all the streams
     * @throws Exception
     */
    public void closeServer() throws Exception {
        _in.close();
        _out.close();
        _server.close();
    }

    @Override
    public void close() throws Exception {
        closeServer();
    }
}
