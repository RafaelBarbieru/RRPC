package server.implementations;

import rrpcinterfaces.Greeter;

import static utils.StringUtils.f;

public class GreeterImpl implements Greeter {
    @Override
    public String greetAndTellAge(String name, Integer age) {
        return f("Hi, %s! Your age is %d", name, age);
    }
}
