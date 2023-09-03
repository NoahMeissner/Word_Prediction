package No_POS_Tagging.Preprocessing.Test_weights;

import No_POS_Tagging.Preprocessing.PreProcessing;
import lingologs.Script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestWeightsList {


    private List<List<Script>> HS;
    private Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> Weights;
    public TestWeightsList(HashMap<String, List<HashMap<Script,Script>>> HS,
                           String test,
                           Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> Weights)
    {
        this.HS = reduceMap(HS,test);
        this.Weights = Weights;
    }

    private List<List<Script>> reduceMap(HashMap<String, List<HashMap<Script,Script>>> HS, String test)
    {
        List<List<Script>> result = new ArrayList<>();
        for(String D : HS.keySet())
        {
            if(D.equals(test))
            {
                List<Script> zwischen = new ArrayList<>();
                for(HashMap<Script,Script> H:HS.get(D))
                {
                    zwischen.add(H.get(Script.of("text_entry")));
                }
                PreProcessing P = new PreProcessing(zwischen);
                result.addAll(P.getListText());
            }
        }
        return result;
    }


    public Script calculateProbability (boolean U,int simi,boolean learn){
        if(U)
        {
            HashMap<Script, HashMap<Script, Integer>> Unigram = Weights.getKey();
            TestUnigramWeights T = new TestUnigramWeights(Unigram,HS,simi);
            return T.getResults(learn);

        }
        else {
            HashMap<Script, HashMap<Script, Integer>> Bigram = Weights.getValue();
            TestBigramWeights T = new TestBigramWeights(Bigram,HS,simi);
            return T.getResults(learn);
        }
    }
}
