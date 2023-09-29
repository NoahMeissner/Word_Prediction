import UI.Configuration;
import UI.Tags;

import java.util.Map;

/**
 * The Main class serves as the entry point for the program, orchestrating the initialization
 * of critical components, including configuration, links, and the Processor. It is the starting
 * point for the execution of the project.
 */
public class Main {

    private static final String info = """
            ###########
            INFORMATION
            PLEASE SET THE LINKS IN THE PART_OF_SPEECH_TAGGING/PATHS_SETUP FILE
            Work of Noah Meißner
            ###########
            """;

    public static void main(String[] args) {
        Configuration C = new Configuration();
        System.out.println(info);
        System.out.println("###########################");
        System.out.println("Step 1: Configuration");
        Map<Tags,Integer> config = C.getConfiguration();
        System.out.println("###########################");
        System.out.println("Step 2: Links");
        Map<Tags,String> links = C.getLinks();
        System.out.println("Step 3: Test System");
        boolean T = C.getTest();
        boolean TA = false;
        if(T)
        {
            TA = C.testAll();
        }
        System.out.println("All Configured");
        Processor P = new Processor(config, links, T,TA);
    }

}






