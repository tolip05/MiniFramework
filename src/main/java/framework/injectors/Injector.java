package framework.injectors;

import framework.annotations.CrazyInjections;
import framework.modules.Module;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Injector {
    private Module module;

    public Injector(Module module) {
        this.module = module;
    }
    public <T> T inject(Class<T> desireClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            boolean hasConstructorAnnotation = this.checkForConstructorInjection(desireClass);
            boolean hasFieldAnnotation = this.checkForFieldInjection(desireClass);
            if (hasConstructorAnnotation && hasFieldAnnotation){
                throw new IllegalArgumentException("There must be only field or constructor have annotation CrazyInjection!");
            }
            if(hasConstructorAnnotation){
                return desireClass.cast(this.createConstructorInjection(desireClass));
            }else if(hasFieldAnnotation){
                return desireClass.cast(this.createFieldInjection(desireClass));
            }
            return null;
    }

    private <T> Object createFieldInjection(Class<T> desireClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
       Object desireClassInstance = this.module.getInstance(desireClass);
       if (desireClassInstance == null){
           desireClassInstance = desireClass.getConstructor().newInstance();
           this.module.setInstance(desireClass,desireClassInstance);
       }
       if (desireClass != null){
           Field[] toBeInject = desireClass.getDeclaredFields();
           for (Field field : toBeInject) {
               if (!field.isAnnotationPresent(CrazyInjections.class)) continue;
               field.setAccessible(true);
               CrazyInjections crazyInjections = field.getAnnotation(CrazyInjections.class);
               Class type = field.getType();
               Class dependency = this.module.getMapping(type,crazyInjections);
               Class objectInstance = null;
               if (type.isAssignableFrom(dependency)){
                   objectInstance = this.module.getMapping(type,crazyInjections);
               }
               Object dependencyInstance = objectInstance.getConstructor().newInstance();
               field.set(desireClassInstance,dependencyInstance);
           }
       }
       return desireClassInstance;
    }

    private <T> boolean checkForFieldInjection(Class<T> desireClass) {
        for (Field declaredField : desireClass.getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(CrazyInjections.class)){
                return true;
            }
        }
        return false;
    }

    private <T> boolean checkForConstructorInjection(Class<T> desireClass) {
        for (Constructor<?> constructor : desireClass.getConstructors()) {
            if(constructor.isAnnotationPresent(CrazyInjections.class)){
                return true;
            }
        }
        return false;
    }

    private <T> Object createConstructorInjection(Class<T> desireClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (desireClass != null){
            for (Constructor<?> constructor : desireClass.getConstructors()) {
                if (constructor.isAnnotationPresent(CrazyInjections.class)){
                    CrazyInjections injection =
                            constructor.getAnnotation(CrazyInjections.class);
                    Class[] parameters = constructor.getParameterTypes();
                    Object[] argObjects = new Object[parameters.length];

                    int i = 0;

                    for (Class parameter : parameters) {
                        Class dependency = this.module.getMapping(parameter,injection);
                        this.inject(dependency);
                        if (parameter.isAssignableFrom(dependency)){
                            Object dependencyInstance = this.module.getInstance(dependency);
                            if (dependencyInstance == null){
                                Object ins = dependency.getConstructor()
                                        .newInstance();
                                this.module.setInstance(dependency,ins);
                                argObjects[i++] = ins;
                            }else{
                                argObjects[i++] = dependencyInstance;
                            }
                        }

                    }
                    return desireClass.getConstructor(parameters).newInstance(argObjects);
                }
            }
        }
        return desireClass;
    }
}
