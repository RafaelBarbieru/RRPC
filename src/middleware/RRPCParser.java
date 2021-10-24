package middleware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static utils.StringUtils.f;

public class RRPCParser {

    public static String parse(RRPCType type, String handler, Object... args) {
        RRPCObject requestObject = new RRPCObject();
        requestObject.setType(type);
        requestObject.setHandlerName(handler);
        requestObject.setArgs(args);
        return parse(requestObject);
    }

    public static String parse(RRPCObject object) {

        String parsed = null;

        if (object.getType() == RRPCType.REQUEST) {
            // Building the request
            StringBuilder req = new StringBuilder();
            req.append("req ");
            req.append(f("hdl %s ", object.getHandlerName()));
            req.append(f("arg %s ", serializeArguments(object.getArgs())));
            req.append("end");
            parsed = req.toString();
        } else if (object.getType() == RRPCType.RESPONSE) {
            // Building the response
            StringBuilder res = new StringBuilder();
            res.append("res ");
            res.append(f("hdl %s ", object.getHandlerName()));
            res.append(f("arg %s ", serializeArguments(object.getArgs())));
            res.append("end");
            parsed = res.toString();
        }

        return parsed;
    }

    public static RRPCObject unparse(String string) {
        RRPCObject obj = new RRPCObject();

        // Detecting whether this is a request or a response
        if (string.startsWith("req")) {
            obj.setType(RRPCType.REQUEST);
        } else if (string.startsWith("res")) {
            obj.setType(RRPCType.RESPONSE);
        }

        // Detecting the handler name
        String handlerName = string.substring(string.indexOf("hdl") + "hdl".length() + 1, string.indexOf("arg") - 1);
        obj.setHandlerName(handlerName);

        // Detecting the arguments
        String argsString = string.substring(string.indexOf("arg") + 4, string.indexOf("end") - 1);
        String[] argsStringArray = argsString.split(";");
        Object[] argsObjects = new Object[argsStringArray.length];
        for (int i = 0; i < argsStringArray.length; i++) {
            String arg = argsStringArray[i];
            if (arg.startsWith("int")) {
                argsObjects[i] = Integer.parseInt(arg.substring(4, arg.indexOf(']')));
            } else if (arg.startsWith("str")) {
                argsObjects[i] = arg.substring(4, arg.indexOf(']'));
            }
        }
        obj.setArgs(argsObjects);

        return obj;
    }

    public static List<String> getHandlerNameAndClass(String string) throws IndexOutOfBoundsException {
        String payload = string.split(":")[1];
        String handlerName = payload.split(";")[0];
        String className = payload.split(";")[1];
        return List.of(handlerName, className);
    }

    private static String serializeArguments(Object... args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                stringBuilder.append(f("str[%s]", args[i]));
            } else if (args[i] instanceof Integer) {
                stringBuilder.append(f("int[%d]", args[i]));
            }
            if (i != args.length - 1) {
                stringBuilder.append(";");
            }
        }
        return stringBuilder.toString();
    }

}
