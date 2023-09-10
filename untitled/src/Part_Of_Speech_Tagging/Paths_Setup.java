package Part_Of_Speech_Tagging;

public abstract class Paths_Setup {

    /**
     * The abstract class `Paths_Setup` provides a centralized setup for paths and configurations related to the Python interpreter.
     * It defines a constant that holds the path to the Python interpreter executable, facilitating easy access
     * and maintenance of this information across the application.
     */


    public static final String PATH_TO_PYTHON_INTERPRETER = "/Users/noahmeissner/Documents/Data/University/Informartionswissenschaften/SS23/Bernd_Ludwig/bin/Python";


    public static String PATH_TO_PYTHON_INTERPRETER() {
        return PATH_TO_PYTHON_INTERPRETER;
    }
}
