package middleware;

import client.RRPCClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static utils.StringUtils.f;

public class RRPCProxy<T> implements InvocationHandler {

    private String _handler;
    private RRPCClient _client;

    private RRPCProxy(String handler, RRPCClient client) {
        _handler = handler;
        _client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String fullHandler = _handler + "." + method.getName();
        System.out.println(
                f("Method %s captured! Executing RPC protocol with handler %s..."
                        , method.getName()
                        , fullHandler
                )
        );
        Object result = _client.execute(fullHandler, args);
        System.out.println(f("Received server response for handler %s!", fullHandler));
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(String handler, Class<? super T> clazz, RRPCClient client) throws IOException {
        // Setting the handler in the server
        Object response = client.setHandler(handler, clazz);
        if (response == null) {
            System.err.println("Aborting RRPC call...");
            return null;
        }
        RRPCProxy proxy = new RRPCProxy(handler, client);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz}, proxy
        );
    }
}

