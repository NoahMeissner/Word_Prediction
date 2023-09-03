package Part_Of_Speech_Tagging;

import lingologs.Script;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PPos {
    /**
     * This class, `PPos`, manages Probabilistic Part-Of-Speech (POS) Tagging utilizing the NLTK Python Library.
     * POS tagging is applied to text data, assigning grammatical categories to each word in a sentence.
     * The class provides methods to perform POS tagging on a given script and obtain the POS-tagged words.
     * It interfaces with a Python script to achieve the tagging using NLTK's capabilities.
     * The execution time for the tagging process is measured and displayed.
     * INPUT: Script S
     * Output: List<Script>
     */

    private final Script S;

    // Constructor
    public PPos(Script S){
        this.S = S;
    }

    // Method to Start Pos-Tagging Process
    public List<String> getPosTags()
    {
        long startTime = System.currentTimeMillis(); // start time
        List<String> posTags = runPythonScript(S);
        long endTime = System.currentTimeMillis(); // end time
        long executionTime = endTime - startTime; // calculate time
        System.out.println("Probabilistic POS Tagging Time: " + executionTime + " MilliSeconds");
        return posTags;
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

    private List<String> runPythonScript(Script sentence)
    {
        List<String> posTags = new ArrayList<>();
        try {
            String SC = PythonPOSTagging+"\""+sentence+"\""+"))";
            String PATHTOPYTHONINTERPRETER = Paths_Setup.PATH_TO_PYTHON_INTERPRETER();
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
