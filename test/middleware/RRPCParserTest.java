package middleware;

import org.junit.Assert;
import org.junit.Test;

public class RRPCParserTest {

    @Test
    public void testParseStringReq() {
        String expected =   "req " +
                            "hdl greeter.sayHi " +
                            "arg str[Rafael];str[Barbieru] " +
                            "end";
        RRPCObject requestObject = new RRPCObject();
        String req = RRPCParser.parse(RRPCType.REQUEST, "greeter.sayHi", "Rafael", "Barbieru");
        Assert.assertEquals(expected, req);
    }

    @Test
    public void testParseIntegerReq() {
        String expected =   "req " +
                            "hdl adder.add " +
                            "arg int[1];int[2] " +
                            "end";
        String req = RRPCParser.parse(RRPCType.REQUEST, "adder.add", 1, 2);
        Assert.assertEquals(expected, req);
    }

    @Test
    public void testParseMixReq() {
        String expected =   "req " +
                            "hdl greeter.sayNameAndAge " +
                            "arg str[Rafael Barbieru];int[21] " +
                            "end";
        String req = RRPCParser.parse(RRPCType.REQUEST, "greeter.sayNameAndAge", "Rafael Barbieru", 21);
        Assert.assertEquals(expected, req);
    }

    @Test
    public void testParseStringRes() {
        String expected =   "res " +
                            "hdl greeter.sayHi " +
                            "arg str[Hi Rafael Barbieru!] " +
                            "end";
        String res = RRPCParser.parse(RRPCType.RESPONSE, "greeter.sayHi", "Hi Rafael Barbieru!");
        Assert.assertEquals(expected, res);
    }

    @Test
    public void testParseIntegerRes() {
        String expected =   "res " +
                            "hdl adder.add " +
                            "arg int[3] " +
                            "end";
        String res = RRPCParser.parse(RRPCType.RESPONSE, "adder.add", 3);
        Assert.assertEquals(expected, res);
    }

    @Test
    public void testParseMixRes() {
        String expected =   "res " +
                            "hdl greeter.sayNameAndAge " +
                            "arg str[Hi Rafael Barbieru! You're 21!] " +
                            "end";
        String res = RRPCParser.parse(RRPCType.RESPONSE, "greeter.sayNameAndAge", "Hi Rafael Barbieru! You're 21!");
        Assert.assertEquals(expected, res);
    }

    @Test
    public void testUnparseReq() {
        RRPCObject expectedObject = new RRPCObject();
        expectedObject.setType(RRPCType.REQUEST);
        expectedObject.setHandlerName("add");
        expectedObject.setArgs(new Integer[]{1, 2});
        String req = "req hdl add arg int[1];int[2] end";
        RRPCObject actualObject = RRPCParser.unparse(req);
        Assert.assertTrue(expectedObject.equals(actualObject));
    }

    @Test
    public void testUnparseRes() {
        RRPCObject expectedObject = new RRPCObject();
        expectedObject.setType(RRPCType.RESPONSE);
        expectedObject.setHandlerName("greet");
        expectedObject.setArgs(new String[]{"Hi!"});
        String res = "res hdl greet arg str[Hi!] end";
        RRPCObject actualObject = RRPCParser.unparse(res);
        Assert.assertTrue(expectedObject.equals(actualObject));
    }

    @Test
    public void testUnparseMix() {
        RRPCObject expectedObject = new RRPCObject();
        expectedObject.setType(RRPCType.REQUEST);
        expectedObject.setHandlerName("greetAndTellAge");
        expectedObject.setArgs(new Object[]{"Rafael Barbieru", 21});
        String req = "req hdl greetAndTellAge arg str[Rafael Barbieru];int[21] end";
        RRPCObject actualObject = RRPCParser.unparse(req);
        Assert.assertTrue(expectedObject.equals(actualObject));
    }

}
