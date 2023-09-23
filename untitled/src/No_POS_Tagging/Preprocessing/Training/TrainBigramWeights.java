package No_POS_Tagging.Preprocessing.Training;

import lingologs.Script;
import lingologs.Texture;

import java.util.HashMap;


/**
 * The `TrainBig-ramWeights` class is responsible for training bigram models on a collection of scripts.
    * It takes a list of lists of `Script` objects and calculates the bigram weights between consecutive scripts.
    * The class provides a method to obtain the calculated bigram weights.
 * Methods:
    * getWeights():
    * Returns the calculated bigram weights as a hashmap of script pairs with associated frequencies.
 **/


public class TrainBigramWeights {
    private final Texture<Texture<Script>> LS;

    // Constructor
    public TrainBigramWeights(Texture<Texture<Script>> LS)
    {
        this.LS = LS;
    }

    // This method makes Bigram
    private Texture<Script> makeBigram(Texture<Script> L)
    {
        Texture<Script> result = new Texture<>();
        for (int i = 0; i<L.toList().size();i++)
        {
            if(i+1<L.toList().size())
            {
                result = result.add(Script.of(L.at(i)+" "+L.at(i+1)));
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
    private HashMap<Script, HashMap<Script,Integer>> setWeights(Texture<Texture<Script>> LS)
    {
        HashMap<Script, HashMap<Script,Integer>> HM = new HashMap<>();
        for(Texture<Script> L:LS)
        {
            L = makeBigram(L);
            for(int i = 0; i< L.toList().size();i++)
            {
                if(i+1<L.toList().size())
                {
                    if(HM.get(L.at(i))!= null)
                    {
                        Script res = L.at(i+1).split(" ").get(1);
                        HashMap<Script,Integer> HT = HM.get(L.at(i));
                        if(HT.get(res)!= null && i+1 <L.toList().size())
                        {
                            int v = HT.get(res)+1;
                            HT.put(res,v);
                            HM.put(L.at(i),HT);
                        }
                        else{
                            if(i+1<L.toList().size())
                            {
                                HT.put(res,1);
                                HM.put(L.at(i),HT);
                            }
                        }
                    }
                    else{
                        if(HM.get(L.at(i))== null)
                        {
                            Script res = L.at(i+1).split(" ").get(1);
                            HM.put(L.at(i), new HashMap<>());
                            HashMap<Script,Integer> HT = HM.get(L.at(i));
                            HT.put(res,1);
                            HM.put(L.at(i),HT);
                        }
                    }
                }
            }
        }
        return HM;
    }
}
