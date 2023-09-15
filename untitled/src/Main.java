import UI.Configuration;
import UI.Tags;

import java.util.Map;


public class Main {

    /*
    Start of the Program
     */
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
        System.out.println("All Configured");
        Processor P = new Processor(config, links, T);
    }

}






