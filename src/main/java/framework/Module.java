package framework;

import framework.interfaces.Engine;
import framework.io.ConsoleReader;
import framework.io.ConsoleWriter;
import framework.io.Reader;
import framework.io.Writer;
import framework.modules.AbstractModule;

import java.util.ArrayList;
import java.util.List;


public class Module extends AbstractModule {
    @Override
    public void configure() {
      super.createMapping(Reader.class, ConsoleReader.class);
      super.createMapping(Writer.class, ConsoleWriter.class);
      super.createMapping(Engine.class,EngineImpl.class);
      super.createMapping(List.class, ArrayList.class);
    }
}
