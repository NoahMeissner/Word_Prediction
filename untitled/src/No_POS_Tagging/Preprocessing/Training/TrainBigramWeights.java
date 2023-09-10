package No_POS_Tagging.Preprocessing.Training;
import lingologs.Script;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * The `TrainBig-ramWeights` class is responsible for training bigram models on a collection of scripts.
    * It takes a list of lists of `Script` objects and calculates the bigram weights between consecutive scripts.
    * The class provides a method to obtain the calculated bigram weights.
 * Methods:
    * getWeights():
    * Returns the calculated bigram weights as a hashmap of script pairs with associated frequencies.
 **/


public class TrainBigramWeights {
    private final List<List<Script>> LS;

    // Constructor
    public TrainBigramWeights(List<List<Script>> LS)
    {
        this.LS = LS;
    }

    // This method makes Bigrams
    private List<Script> makeBigrm(List<Script> L)
    {
        List<Script> result = new ArrayList<>();
        for (int i = 0; i<L.size();i++)
        {
            if(i+1<L.size())
            {
                result.add(Script.of(L.get(i)+" "+L.get(i+1)));
            }
        }
        return result;
    }

    public HashMap<Script, HashMap<Script,Integer>> getWeights()
    {
        return setWeights(LS);
    }

    /*
     * The `setWeights` method calculates and returns the bigram weights between consecutive `Script` objects
     * in a collection of script lists. It generates a hashmap where the keys are individual scripts, and the
     * values are hashmaps containing other scripts as keys and their corresponding frequency as values.
     */
    private HashMap<Script, HashMap<Script,Integer>> setWeights(List<List<Script>> LS)
    {
        HashMap<Script, HashMap<Script,Integer>> HM = new HashMap<>();
        for(List<Script> L:LS)
        {
            L = makeBigrm(L);
            for(int i = 0; i< L.size();i++)
            {
                if(i+1<L.size())
                {
                    if(HM.get(L.get(i))!= null)
                    {
                        Script res = L.get(i+1).split(" ").get(1);
                        HashMap<Script,Integer> HT = HM.get(L.get(i));
                        if(HT.get(res)!= null && i+1 <L.size())
                        {
                            int v = HT.get(res)+1;
                            HT.put(res,v);
                            HM.put(L.get(i),HT);
                        }
                        else{
                            if(i+1<L.size())
                            {
                                HT.put(res,1);
                                HM.put(L.get(i),HT);
                            }
                        }
                    }
                    else{
                        if(HM.get(L.get(i))== null)
                        {
                            Script res = L.get(i+1).split(" ").get(1);
                            HM.put(L.get(i), new HashMap<>());
                            HashMap<Script,Integer> HT = HM.get(L.get(i));
                            HT.put(res,1);
                            HM.put(L.get(i),HT);
                        }
                    }
                }
            }
        }
        return HM;
    }
}
