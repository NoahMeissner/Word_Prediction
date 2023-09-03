import UI.Chat;
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
        test();
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
        Prozessor P = new Prozessor(config, links, T);
        Chat CH = new Chat();
    }

    private static void test(){
        String s="";
        int[] a = {3,8,5,1,8,5,3,2,7};
        int i =0;
        while (i <a.length){
            if(a[i] %2 !=0){
                s+=a[i]+a[a[i]];
                i+=2;

            }else{
                i-=1;
            }
        }
        System.out.println(s);
    }
}






