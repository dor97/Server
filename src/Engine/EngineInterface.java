package Engine;

import org.omg.CORBA.DynAnyPackage.InvalidValue;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.nio.file.NoSuchFileException;

public interface EngineInterface {

    public void loadSimulation(String fileName) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException;
    public void activeSimulation()throws InvalidValue;
    public void saveSystemState();
    public void loadSystemState();
}
