package Preprocessing;
import lingologs.Script;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PosTagging {

/*
Dieses Skript wandelt einen Text in POS Tags um
 */

    Script S;

    public PosTagging(Script S){
        this.S = S;
        long startTime = System.currentTimeMillis(); // Startzeit erfassen
        List<String> posTags = runPythonScript(S);
        System.out.println(posTags);
        long endTime = System.currentTimeMillis(); // Endzeit erfassen
        long executionTime = endTime - startTime; // Ausführungszeit berechnen
        System.out.println("Ausführungszeit: " + executionTime + " Millisekunden");
    }


    private final String PythonPOSTagging = """
            try:
                import nltk
            except ImportError:
                print("nltk not found. Installing nltk...")
                subprocess.check_call(["python", "-m", "pip", "install", "nltk"])
                import nltk
                nltk.download('punkt')
                nltk.download('averaged_perceptron_tagger')
                
            def get_pos_tags(sentence):
               tokens = nltk.word_tokenize(sentence)
               pos_tags = nltk.pos_tag(tokens)
               return pos_tags
            print(get_pos_tags(
            """;

    private List<String> runPythonScript(Script sentence) {
        List<String> posTags = new ArrayList<>();

        try {
            String SC = PythonPOSTagging+"\""+sentence+"\""+"))";
            String PATHTOPYTHONINTERPRETER = "/opt/homebrew/bin/python3";
            ProcessBuilder PB  = new ProcessBuilder(PATHTOPYTHONINTERPRETER, "-c",SC);
            Process process = PB.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                posTags.add(line);
            }

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return posTags;
    }
}


