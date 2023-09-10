package Part_Of_Speech_Tagging.Test;

import Part_Of_Speech_Tagging.PosTags;
import Part_Of_Speech_Tagging.Preprocessing;
import lingolava.Tuple.Couple;
import lingolava.Tuple.Triple;
import lingologs.Script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestWeightsList {

    private final boolean UOB;
    private final boolean ROP;

    private final boolean learn;
    private Couple<HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>>,
            HashMap<Class<?>, HashMap<PosTags, Integer>>> C;

    public TestWeightsList(boolean UOB, Couple<HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>>,
            HashMap<Class<?>, HashMap<PosTags, Integer>>> C,boolean ROP, boolean learn)
    {
        this.UOB = UOB;
        this.C = C;
        this.ROP = ROP;
        this.learn = learn;
    }

    public Triple<Integer,Integer,Integer> testWeights(HashMap<String, List<HashMap<Script,Script>>> HS, String testSetName)
    {
        List<Script> testSet = getTestSet(HS,testSetName);

        if(UOB)
        {
            //TODO Unigrams
            return null;
        }
        else {


            TestBigramWeights T = new TestBigramWeights(testSet, ROP,learn,C);
            return T.testWeights();
        }
    }

    private List<Script> getTestSet(HashMap<String, List<HashMap<Script, Script>>> S, String testSetName) {
        List<Script> res = new ArrayList<>();
        for(String D : S.keySet())
        {
            if(D.equals(testSetName))
            {
                for(HashMap<Script,Script> H:S.get(D))
                {
                    Preprocessing P = new Preprocessing(H.get(Script.of("text_entry")));
                    res.add(P.getS());
                }
            }
        }
        return res;
    }
}
