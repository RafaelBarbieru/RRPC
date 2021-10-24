package server;

import java.io.IOException;

import static utils.StringUtils.f;

/**
 * @author Rafael Barbieru
 */
public class RafaRPCServer {

    /**
     * SERVER MAIN FUNCTION
     * @param args
     */
    public static void main(String[] args) {

        try(RRPCServer server = new RRPCServer()) {

            // Starting the server
            server.startServer();

        } catch (IOException ex) {
            System.err.println(f("Error while starting the server: %s", ex.getMessage()));
        } catch (Exception ex) {
            System.err.println(f("An unexpected error has occurred: %s", ex.getMessage()));
            ex.printStackTrace();
        }

    }

}
