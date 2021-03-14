package framework.modules;

import framework.annotations.CrazyInjections;
import framework.injectors.CrazyInjector;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractModule implements Module {
    private Map<Class<?>, Map<String, Class<?>>> implementationMap;
    private Map<Class<?>, Object> instanceMap;

    public AbstractModule() {
        this.implementationMap = new HashMap<>();
        this.instanceMap = new HashMap<>();
    }

    @Override
    public abstract void configure();

    @SuppressWarnings("unchecked")
    @Override
    public <T> Class<? extends T> getMapping(Class<T> type, Object annotations) {
        Map<String, Class<?>> implementations = this.implementationMap.get(type);
        Class someClass = null;
        if (annotations instanceof CrazyInjections) {
            CrazyInjections crazyInjection = (CrazyInjections) annotations;
            for (String classNames : crazyInjection.value()) {
                someClass = implementations.get(classNames);
                if (someClass != null) {
                    for (Class anInterface : someClass.getInterfaces()) {
                        if (anInterface.getSimpleName().equals(type.getSimpleName())) {
                            return someClass.asSubclass(type);
                        }
                    }
                }
            }

        }
        if (someClass == null) {
            if (implementations.size() == 1) {
                someClass = implementations.values().parallelStream().findFirst().orElseGet(null);
            } else {
                throw new IllegalArgumentException("No avialable mapsing for class" + type.getSimpleName());
            }
        }
        return someClass.asSubclass(type);
    }

    @Override
    public Object getInstance(Class<?> implementation) {
        return this.instanceMap.get(implementation);
    }

    @Override
    public void setInstance(Class<?> implementation, Object instance) {
        this.instanceMap.putIfAbsent(implementation, instance);
    }

    protected <T> void createMapping(Class<T> interfaceClass, Class<? extends T> classImplementation) {
        this.implementationMap.putIfAbsent(interfaceClass, new HashMap<>());
        this.implementationMap.get(interfaceClass).put(classImplementation.getSimpleName(), classImplementation);

    }
}
