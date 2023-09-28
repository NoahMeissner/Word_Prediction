package Part_Of_Speech_Tagging.Test;

import No_POS_Tagging.Preprocessing.SafeWeightsNP;
import Part_Of_Speech_Tagging.PPos;
import Part_Of_Speech_Tagging.PosTags;
import Part_Of_Speech_Tagging.ProcessPos;
import Part_Of_Speech_Tagging.RPos;
import lingolava.Tuple;
import lingolava.Tuple.Couple;
import lingologs.Script;
import lingologs.Texture;

import java.util.HashMap;
import java.util.List;

/**
 * The TestWeightsListP class is responsible for evaluating the performance of a part-of-speech (POS) tagging system by
 * testing the weights used for tagging. It allows for testing both unigram (UOB) and bigram (ROB) approaches using
 * given training data.
 */

public class TestWeightsListP {

    private final boolean UOB;
    private final boolean ROP;

    private final boolean learn;
    private final boolean preprocessing;
    private final Couple<HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>>,
            HashMap<Script, HashMap<PosTags, Integer>>> C;

    public TestWeightsListP(boolean UOB, Couple<HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>>,
            HashMap<Script, HashMap<PosTags, Integer>>> C, boolean ROP, boolean learn,boolean preprocessing)
    {
        this.UOB = UOB;
        this.C = C;
        this.ROP = ROP;
        this.learn = learn;
        this.preprocessing = preprocessing;
    }

    public Tuple.Quaple<Integer,Integer,Integer,Integer> testWeights(HashMap<String, List<HashMap<Script,Script>>> HS, String testSetName)
    {
        Texture<Script> testSet = getTestSet(HS,testSetName);

        if(UOB)
        {
            SafeWeightsNP S = new SafeWeightsNP();

            //TestCombination T = new TestCombination(UOB,C,learn,makeNgrams(testSet),S.getM().getKey());
            //return T.testSystem();
            TestUnigramWeights T = new TestUnigramWeights(makeNgrams(testSet),learn,C);
            return T.testWeights();
        }
        else {


            TestBigramWeights T = new TestBigramWeights(makeNgrams(testSet),learn,C);
            return T.testWeights();
        }
    }

    private Texture<Script> getTestSet(HashMap<String, List<HashMap<Script, Script>>> S, String testSetName) {
        Texture<Script> res = new Texture<>();
        for(String D : S.keySet())
        {
            if(D.equals(testSetName))
            {
                for(HashMap<Script,Script> H:S.get(D))
                {
                    res = res.add(H.get(Script.of("text_entry")));
                }
            }
        }
        return res;
    }

    private Texture<Texture<Couple<Script,PosTags>>> makeNgrams(Texture<Script> L)

    {
        return processList(L);
    }

    private Texture<Texture<Couple<Script, PosTags>>>processList(Texture<Script> ps) {
        if(ROP)
        {
            PPos PP = new PPos(ps,preprocessing);
            ProcessPos P = new ProcessPos(PP.getPosTags(),ROP);
            return P.getCouples();
        }
        else{
            RPos R = new RPos(ps,preprocessing);
            ProcessPos P = new ProcessPos(ROP,R.getPosTags());
            return P.getCouples();
        }
    }
}
