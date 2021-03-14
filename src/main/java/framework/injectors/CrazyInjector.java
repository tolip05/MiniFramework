package framework.injectors;

import framework.modules.Module;

public class CrazyInjector {

    public static Injector createInjector(Module module) {
        module.configure();
        return new Injector(module);
    }


}
