package No_POS_Tagging.Preprocessing.Test_weights;

import Part_Of_Speech_Tagging.PreProcessing;
import lingolava.Tuple;
import lingologs.Script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestWeightsListNP {


    private final List<List<Script>> HS;

    private final int simi = 1;
    private final boolean UOB;

    private final boolean learn;
    private final boolean preprocessing;
    private final Map.Entry<
            HashMap<Script, HashMap<Script, Integer>>,
            HashMap<Script, HashMap<Script, Integer>>> Weights;
    public TestWeightsListNP(HashMap<String, List<HashMap<Script,Script>>> HS,
                             String test,
                             Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> Weights,
                             boolean UOB, boolean learn, boolean preprocessing)
    {
        this.HS = reduceMap(HS,test);
        this.Weights = Weights;
        this.UOB = UOB;
        this.learn = learn;
        this.preprocessing = preprocessing;
    }

    private List<List<Script>> reduceMap(HashMap<String, List<HashMap<Script,Script>>> HS, String test)
    {
        List<List<Script>> result = new ArrayList<>();
        for(String D : HS.keySet())
        {
            if(D.equals(test))
            {
                List<Script> btw = new ArrayList<>();
                for(HashMap<Script,Script> H:HS.get(D))
                {
                    btw.add(H.get(Script.of("text_entry")));
                }
                PreProcessing P = new PreProcessing(btw,preprocessing);
                result.addAll(P.getListText());
            }
        }
        return result;
    }


    public Tuple.Quaple<Integer,Integer,Integer,Integer> testWeights (){
        if(UOB)
        {
            HashMap<Script, HashMap<Script, Integer>> UGRM = Weights.getKey();
            TestUnigramWeights T = new TestUnigramWeights(UGRM,HS,simi);
            return T.getResults(learn);

        }
        else {
            HashMap<Script, HashMap<Script, Integer>> Bigram = Weights.getValue();
            TestBigramWeights T = new TestBigramWeights(Bigram,HS,simi);
            return T.getResults(learn);
        }
    }
}
