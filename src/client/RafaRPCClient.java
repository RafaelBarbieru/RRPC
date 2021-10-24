package client;

import rrpcinterfaces.Adder;
import middleware.RRPCProxy;
import rrpcinterfaces.Greeter;

import java.io.IOException;

import static utils.StringUtils.f;


/**
 * @author Rafael Barbieru
 */
public class RafaRPCClient {

    /**
     * CLIENT MAIN FUNCTION
     *
     * @param args
     */
    public static void main(String[] args) throws NullPointerException {

        try(RRPCClient client = new RRPCClient()) {

            // Starting the client
            client.startClient();

            // Using RRPC to add two numbers
            System.out.println("\n--- Using RRPC to add two numbers ---\n");
            Adder adder = RRPCProxy.getProxy("adder", Adder.class, client);
            int sum = adder.add(1, 2);
            System.out.println(f("\n*** Result of 1 + 2 = %s ***", sum));

            // Using RRPC to add two more numbers
            System.out.println("\n--- Using RRPC to add two more numbers ---\n");
            int sum2 = adder.add(15, 16);
            System.out.println(f("\n *** Result of 15 + 16 = %s ***", sum2));

            // Using RRPC to greet the user and tell their age
            System.out.println("\n--- Using RRPC to greet the user and tell their age ---\n");
            Greeter greeter = RRPCProxy.getProxy("greeter", Greeter.class, client);
            String greeting = greeter.greetAndTellAge("Rafael", 21);
            System.out.println(f("\n *** The server says: %s ***", greeting));

        } catch (IOException ex) {
            System.err.println(f("Error while starting the client: %s", ex.getMessage()));
        } catch (Exception ex) {
            System.err.println(f("An unexpected error has occurred: %s", ex.getMessage()));
            ex.printStackTrace();
        }

    }

}
