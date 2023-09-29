package Part_Of_Speech_Tagging.Training;

import Part_Of_Speech_Tagging.PosTags;
import lingolava.Tuple.Couple;
import lingologs.Script;
import lingologs.Texture;

import java.util.HashMap;

/**
 * The TrainUnigramWeightsList class is responsible for training unigram weights and generating tag relationships
 */

public class TrainUnigramWeightsList {

    private final Texture<Texture<Couple<Script, PosTags>>> LS;

    public TrainUnigramWeightsList(Texture<Texture<Couple<Script, PosTags>>> LS)
    {
        this.LS = LS;
    }


    public HashMap<Script, HashMap<PosTags,HashMap<Script,Integer>>> getWeights()
    {
        return setWeights(LS);
    }

    public HashMap<Script,HashMap<PosTags,Integer>> getTags()
    {
        return setTags(LS);
    }

    private HashMap<Script, HashMap<PosTags, Integer>> setTags(Texture<Texture<Couple<Script, PosTags>>> LA) {
        HashMap<Script, HashMap<PosTags, Integer>> res = new HashMap<>();

        for(Texture<Couple<Script, PosTags>> L : LA)
        {
            for(int i = 0; i<L.toList().size();i++)
            {
                PosTags currentP = L.at(i).getValue();

                if(res.get(Script.of(currentP.name()))!= null)
                {
                    if(i+1<L.toList().size()){
                        HashMap<PosTags,Integer> HP = res.get(Script.of(currentP.name()));
                        PosTags nextP = L.at(i+1).getValue();
                        int number = 1;
                        if(HP.get(nextP)!=null)
                        {
                            number += HP.get(nextP) ;
                        }
                        HP.put(nextP,number);
                        res.put(Script.of(currentP.name()),HP);
                    }
                }
                else{
                    if(i+1<L.toList().size())
                    {
                        HashMap<PosTags,Integer> HP = new HashMap<>();
                        PosTags nextP = L.at(i+1).getValue();
                        HP.put(nextP,1); // one because first time
                        res.put(Script.of(currentP.name()),HP);
                    }
                }
            }
        }
        return res;
    }

    private HashMap<Script, HashMap<PosTags,HashMap<Script,Integer>>>  setWeights(Texture<Texture<Couple<Script, PosTags>>> LA) {
        HashMap<Script, HashMap<PosTags,HashMap<Script,Integer>>> res = new HashMap<>();

        for(Texture<Couple<Script, PosTags>> L : LA)
        {
            for(int i = 0; i< L.toList().size();i++)
            {
                Script currentS = L.at(i).getKey();

                if(i+1<L.toList().size())
                {
                    PosTags nextP = L.at(i+1).getValue();
                    Script nextS = L.at(i+1).getKey();

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
