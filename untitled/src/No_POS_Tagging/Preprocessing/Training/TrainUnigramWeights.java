package No_POS_Tagging.Preprocessing.Training;

import lingologs.Script;
import lingologs.Texture;

import java.util.HashMap;

/**
 * The `TrainUnigramWeights` class is responsible for training unigram models on a collection of scripts.
 * It takes a list of lists of `Script` objects and calculates the unigram weights between consecutive scripts.
 * Methods:
 * getWeights():
 * Returns the calculated unigram weights as a hashmap of script pairs with associated frequencies.
 **/
public class TrainUnigramWeights {

    private final Texture<Texture<Script>> LS;

    public TrainUnigramWeights(Texture<Texture<Script>> LS)
    {
        this.LS = LS;
    }


    public HashMap<Script, HashMap<Script,Integer>> getResults()
    {
        return setWeights(LS);
    }

    private HashMap<Script, HashMap<Script,Integer>> setWeights(Texture<Texture<Script>> Ls){
        HashMap<Script, HashMap<Script,Integer>> HM = new HashMap<>();

        for(Texture<Script> L : Ls)
        {
            for(int i = 0; i< L.toList().size();i++)
            {
                if(i+1<L.toList().size())
                {
                    if(HM.get(L.at(i))!= null)
                    {
                        HashMap<Script,Integer> HA = HM.get(L.at(i));
                        if(HA.get(L.at(i+1))!= null && i+1 <L.toList().size())
                        {
                            int v = HA.get(L.at(i+1))+1;
                            HA.put(L.at(i+1),v);
                            HM.put(L.at(i),HA);
                        }
                        else{
                            if(i+1<L.toList().size())
                            {
                                HA.put(L.at(i+1),1);
                                HM.put(L.at(i),HA);
                            }
                        }
                    }
                    else{
                        if(HM.get(L.at(i))== null)
                        {
                            HM.put(L.at(i), new HashMap<>());
                            HashMap<Script,Integer> HC = HM.get(L.at(i));
                            HC.put(L.at(i+1),1);
                            HM.put(L.at(i),HC);
                        }
                    }
                }
            }
        }
        return HM;
    }
}
