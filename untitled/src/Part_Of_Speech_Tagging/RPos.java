package Part_Of_Speech_Tagging;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lingolava.Tuple.*;
import lingologs.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RPos {
    /*
     * This class, `RPos`, manages Rule Based Part-Of-Speech (POS) Tagging utilizing the Stanford Core NLP Library.
     * POS tagging is applied to text data, assigning grammatical categories to each word in a sentence.
     * The class provides methods to perform POS tagging on a given script and obtain the POS-tagged words.
     * The execution time for the tagging process is measured and displayed.
     * INPUT: Script S
     * Output: List<Couple<Script,Script>>
     */

    private final Script S;


    public RPos(Script S, boolean preprocessing)
    {
        PreProcessing P = new PreProcessing(S,preprocessing);
        this.S = P.getText();
    }


    public List<Couple<String,String>> getPosTags()
    {
        long startTime = System.currentTimeMillis(); // start time
        List<Couple<String,String>> posTags = runRbPOS();
        long endTime = System.currentTimeMillis(); // end time
        long executionTime = endTime - startTime; // calculate time
        System.out.println("Ruled-Based POS Tagging Time: " + executionTime + " MilliSeconds");
        return posTags;
    }

    private List<Couple<String,String>> runRbPOS() {
        List<Couple<String,String>> res = new ArrayList<>();
        try{
            String A = S.toString();
            Properties props = new Properties();
            props.put("annotators","tokenize, pos");
            StanfordCoreNLP ST = new StanfordCoreNLP(props);

            CoreDocument coreDocument = ST.processToCoreDocument(A);
            for (CoreLabel tok : coreDocument.tokens()) {
                res.add(new Couple<>(tok.word(),tok.tag()));
            }
        }catch (Exception e){
            System.out.println("Rule based Pos Tagging do not work: Exception: "+e);
        }
        return res;
    }
}
