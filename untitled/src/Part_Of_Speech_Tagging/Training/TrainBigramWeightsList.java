package Part_Of_Speech_Tagging.Training;

import Part_Of_Speech_Tagging.PosTags;
import lingolava.Tuple.Couple;
import lingologs.Script;

import java.util.HashMap;
import java.util.List;

/**
 * The TrainBigramWeightsList class is responsible for training bigram weights and generating tag relationships
 * for a given list of script and part-of-speech tag pairs. It calculates the frequency of co-occurrences between
 * script pairs, part-of-speech tags, and other scripts, and stores this information in nested hash maps.
 */

public class TrainBigramWeightsList {

    private final List<List<Couple<Script, PosTags>>> LS;


    public TrainBigramWeightsList(List<List<Couple<Script, PosTags>>> LS)
    {
        this.LS = LS;
    }

    public HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>> getWeights()
    {
        return setWeights(LS);
    }

    public HashMap<Script,HashMap<PosTags,Integer>> getTags()
    {
        return setTags(LS);
    }

    private HashMap<Script,HashMap<PosTags,Integer>> setTags(List<List<Couple<Script, PosTags>>> LA)
    {
        HashMap<Script, HashMap<PosTags, Integer>> res = new HashMap<>();

        for(List<Couple<Script, PosTags>> L : LA)
        {
            for(int i = 0; i<L.size()-1;i++)
            {
                Script currentP = Script.of(L.get(i).getValue().name() +" " + L.get(i+1).getValue().name());

                if(res.get(currentP)!= null)
                {
                    if(i+2<L.size()){
                        HashMap<PosTags,Integer> HP = res.get(currentP);
                        PosTags nextP = L.get(i+2).getValue();
                        int number = 1;
                        if(HP.get(nextP)!=null)
                        {
                            number += HP.get(nextP) ;
                        }
                        HP.put(nextP,number);
                        res.put(currentP,HP);
                    }
                }
                else{
                    if(i+2<L.size())
                    {
                        HashMap<PosTags,Integer> HP = new HashMap<>();
                        PosTags nextP = L.get(i+2).getValue();
                        HP.put(nextP,1); // one because first time
                        res.put(currentP,HP);
                    }
                }
            }
        }
        return res;
    }

    private HashMap<Script, HashMap<PosTags,HashMap<Script,Integer>>>  setWeights(List<List<Couple<Script, PosTags>>> LA) {
        HashMap<Script, HashMap<PosTags,HashMap<Script,Integer>>> res = new HashMap<>();

        for(List<Couple<Script, PosTags>> L : LA)
        {
            for(int i = 0; i< L.size()-1;i++)
            {
                Script currentS = Script.of(L.get(i).getKey() + " " + L.get(i+1).getKey());

                if(i+2<L.size())
                {
                    PosTags nextP = L.get(i+2).getValue();
                    Script nextS = L.get(i+2).getKey();

                    if(res.get(currentS) != null){
                        HashMap<PosTags,HashMap<Script,Integer>> HP = res.get(currentS);
                        if(HP.get(nextP)!= null)
                        {
                            HashMap<Script,Integer> HS  = HP.get(nextP);
                            int number = 1;
                            if(HS.get(nextS)!= null)
                            {
                                number += HS.get(nextS);
                            }
                            HS.put(nextS,number);
                            HP.put(nextP,HS);
                        }
                        else {
                            HP.computeIfAbsent(nextP, k -> new HashMap<>()).put(nextS, 1);
                        }

                    }
                    else{
                        res.computeIfAbsent(currentS, k -> new HashMap<>())
                                .computeIfAbsent(nextP, k -> new HashMap<>())
                                .merge(nextS, 1, Integer::sum);
                    }
                }

            }
        }
        return res;
    }
}
