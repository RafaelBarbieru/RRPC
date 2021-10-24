package middleware;

import java.util.Arrays;
import java.util.Objects;

public class RRPCObject {

    private RRPCType _type;
    private String _handlerName;
    private Object[] _args;

    public RRPCType getType() {
        return _type;
    }

    public void setType(RRPCType _type) {
        this._type = _type;
    }

    public String getHandlerName() {
        return _handlerName;
    }

    public void setHandlerName(String _handlerName) {
        this._handlerName = _handlerName;
    }

    public Object[] getArgs() {
        return _args;
    }

    public void setArgs(Object[] _args) {
        this._args = _args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RRPCObject that = (RRPCObject) o;
        return _type == that.getType() &&
                _handlerName.equals(that.getHandlerName())
                && Arrays.equals(_args, that._args);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(_type, _handlerName);
        result = 31 * result + Arrays.hashCode(_args);
        return result;
    }
}
