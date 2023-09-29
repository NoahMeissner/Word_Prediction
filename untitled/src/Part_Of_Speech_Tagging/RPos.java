package Part_Of_Speech_Tagging;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lingolava.Tuple.Couple;
import lingologs.Script;
import lingologs.Texture;

import java.util.Properties;

/*
 * This class, `RPos`, manages Rule Based Part-Of-Speech (POS) Tagging utilizing the Stanford Core NLP Library.
 * POS tagging is applied to text data, assigning grammatical categories to each word in a sentence.
 * The class provides methods to perform POS tagging on a given script and obtain the POS-tagged words.
 * The execution time for the tagging process is measured and displayed.
 * INPUT: Script S
 * Output: Texture<Couple<Script,Script>>
 */
public class RPos {

    private final Script AS;


    public RPos(Texture<Script> S, boolean preprocessing)
    {
        PreProcessing P = new PreProcessing(S,preprocessing,true);
        this.AS = P.getText();
    }

    public RPos(Script S, boolean preprocessing)
    {
        PreProcessing P = new PreProcessing(S,preprocessing,true);
        this.AS = P.getSentence();
    }

    public Texture<Couple<String,String>> getPosTags()
    {
        return runRbPOS();
    }

    private Texture<Couple<String,String>> runRbPOS() {
        Texture<Couple<String,String>> res = new Texture<>();
        try{
            String A = AS.toString();
            Properties props = new Properties();
            props.put("annotators","tokenize, pos");
            StanfordCoreNLP ST = new StanfordCoreNLP(props);

            CoreDocument coreDocument = ST.processToCoreDocument(A);
            for (CoreLabel tok : coreDocument.tokens()) {
                res = res.add(new Couple<>(tok.word(),tok.tag()));
            }
        }catch (Exception e){
            System.out.println("Rule based Pos Tagging do not work: Exception: "+e);
        }
        return res;
    }
}
