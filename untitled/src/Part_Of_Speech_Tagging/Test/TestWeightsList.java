package Part_Of_Speech_Tagging.Test;

import Part_Of_Speech_Tagging.*;
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
            HashMap<Script, HashMap<PosTags, Integer>>> C;

    public TestWeightsList(boolean UOB, Couple<HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>>,
            HashMap<Script, HashMap<PosTags, Integer>>> C,boolean ROP, boolean learn)
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
            TestUnigramWeights T = new TestUnigramWeights(makeNgrams(testSet),ROP,learn,C);
            return T.testWeights();
        }
        else {


            TestBigramWeights T = new TestBigramWeights(makeNgrams(testSet), ROP,learn,C);
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

    private List<List<Couple<Script,PosTags>>> makeNgrams(List<Script> L)

    {
        StringBuilder SB = new StringBuilder();
        for(Script S: L)
        {
            SB.append(S);
            SB.append(" $ ");
        }

        List<Couple<Script,PosTags>> LC = processList(Script.of(SB.toString()));

        List<List<Couple<Script,PosTags>>> res = new ArrayList<>();
        List<Couple<Script,PosTags>> ZR = new ArrayList<>();

        for(int i = 0; i < LC.size();i++)
        {
            if(LC.get(i).getKey().equals(Script.of('$')))
            {
                res.add(ZR);
                ZR = new ArrayList<>();
            }
            else {
                ZR.add(LC.get(i));
            }
        }
        return res;
    }

    private List<Couple<Script, PosTags>> processList(Script ps) {
        if(ROP)
        {
            PPos PP = new PPos(ps);
            ProcessPos P = new ProcessPos(PP.getPosTags().get(0));
            return P.getCouples();
        }
        else{
            RPos R = new RPos(ps);
            ProcessPos P = new ProcessPos(R.getPosTags());
            return P.getCouples();
        }
    }
}
