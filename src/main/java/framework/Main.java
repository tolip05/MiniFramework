package framework;

import framework.injectors.CrazyInjector;
import framework.injectors.Injector;
import framework.interfaces.Engine;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Injector injector = CrazyInjector.createInjector(new Module());
        Engine engine = injector.inject(EngineImpl.class);
        engine.getMessage().add("Pesho");
        engine.getMessage().add("Goshosho");
        engine.getMessage().add("Milen");

        engine.writeMessage();
    }
}
