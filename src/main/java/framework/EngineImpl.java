package framework;

import framework.annotations.CrazyInjections;
import framework.interfaces.Engine;

import java.util.List;

public class EngineImpl implements Engine {
    @CrazyInjections
    private List<String> list;


    public EngineImpl() {

    }

    public List<String> getList() {
        return this.list;
    }

    @Override
    public void doSomething() {

    }

    @Override
    public void writeMessage() {
        System.out.println(String.join(", ",this.getList()));
    }

    @Override
    public List<String> getMessage() {
        return this.list;
    }
}
