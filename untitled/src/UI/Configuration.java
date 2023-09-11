package UI;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Configuration {


    private final Map<Tags,String> questions = Map.of(
            Tags.postag, "With POS-TAGGING(1) or not(2)",
            Tags.preprocessing,"With Pre-Processing(1) or not(2)?",
            Tags.howprocess,"Rule Based(1) or Probablistic POS-Tagging(2)?",
            Tags.ngrams,"Bigrams(1) or Unigrams(2) or Both(3)?",
            Tags.learn,"Learning during Processing(1) or not(2)?"
    );

    private Map<Tags,Integer> answer = Map.of(
            Tags.postag, 0,
            Tags.preprocessing,0,
            Tags.howprocess,0,
            Tags.ngrams,0,
            Tags.learn,0
    );

    public Configuration(){
    }

    public Map<Tags,Integer> getConfiguration(){
        answer = setup();
        return answer;
    }

    public Map<Tags,String> getLinks(){
        return links();
    }

    public boolean getTest(){
        return askTest("Would you like to test the system? (true/false)");
    }


    private Map<Tags,String> links(){
        Map<Tags,String> res = new HashMap<>();
        res.put(Tags.text,askLink("If you want to save the text, type in the file link"));
        res.put(Tags.result,askLink("If you want to save the result, type in the file link"));
        return res;
    }

    private String askLink(String S){
        System.out.println(S);
        String res = new Scanner(System.in).nextLine();
        if(!res.equals("")){
            if(!isFileExists(res)){
                System.out.println("This is not a Link try again");
                return askLink(S);
            }
        }
        System.out.println("Link: "+ res+ " was saved");
        return res;
    }

    private boolean isFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    private Map<Tags,Integer> setup() {
        Map<Tags,Integer> res = new HashMap<>();
        for(Tags T: questions.keySet())
        {
            System.out.println("###########################");
            if(T == Tags.howprocess && !res.containsKey(Tags.postag)){
                int I = askQuestion(questions.get(Tags.postag));
                res.put(Tags.postag,I);
                System.out.println("Number "+ I +" was set");
            }
            if(T == Tags.howprocess && res.containsKey(Tags.postag) && res.get(Tags.postag)==2){
                break;
            }
            else{
                if(!res.containsKey(T)){
                    int I = askQuestion(questions.get(T));
                    res.put(T,I);
                    System.out.println("Number "+ I +" was set");
                }
            }
        }
        System.out.println("All declarations were set");
        return res;
    }

    private int askQuestion(String S)
    {
        System.out.println(S);
        try{
            int I = Integer.parseInt(new Scanner(System.in).nextLine());
            if(I<=3 && I>0)
            {
                return I;
            }
            else{
                System.out.println("This is not a correct Number try again");
                return askQuestion(S);
            }

        }
        catch(Exception e){
            System.out.println("This is not a Number try again");
            System.out.println("Error"+e);
            return askQuestion(S);
        }
    }

    private boolean askTest(String S){
        String A = new Scanner(System.in).nextLine();
        if(A.equals("true")){
            return true;
        }
        if(A.equals("false")){
            return false;
        }
        else{
            System.out.println("Try Again");
            return askTest(S);
        }
    }


}
