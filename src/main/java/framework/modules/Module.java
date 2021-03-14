package framework.modules;

public interface Module {
    void configure();

    <T> Class<? extends T> getMapping(Class<T> type,Object annotations);

    Object getInstance(Class<?> implementation);

    void setInstance(Class<?> implementation,Object instance);
}
