package Part_Of_Speech_Tagging.Test;

import Part_Of_Speech_Tagging.PosTags;
import lingolava.Tuple;
import lingolava.Tuple.Couple;
import lingologs.Script;
import lingologs.Texture;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Key Functionalities:
 * - Testing Weights: Evaluates the performance of a POS tagging system using unigram-based weights on a provided test set.
 * - Learning: Optionally updates the weights during testing if the 'learn' parameter is set to true.
 * - Evaluation Metrics: Computes metrics including true positives, false positives, not found tags, and correctly tagged positions.
 */
public class TestUnigramWeights {

    private final boolean learn;
    private final Texture<Texture<Couple<Script,PosTags>>> LS;
    private Couple<HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>>,
            HashMap<Script, HashMap<PosTags, Integer>>> C;


    public TestUnigramWeights(Texture<Texture<Couple<Script,PosTags>>> LS, boolean learn, Couple<HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>>,
            HashMap<Script, HashMap<PosTags, Integer>>> C )
    {
        this.learn = learn;
        this.LS = LS;
        this.C = C;
    }
    public Tuple.Quaple<Integer,Integer,Integer,Integer> testWeights()
    {
        int positive = 0;
        int negative = 0;
        int notFound = 0;
        int posright = 0;
        HashMap<Script, HashMap<PosTags, Integer>> tags = C.getValue();
        HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>> text_weights = C.getKey();

        for(Texture<Couple<Script,PosTags>> POSList: LS)
        {
            for(int i = 0; i<POSList.toList().size(); i++)
            {
                if(i+1<POSList.toList().size())
                {
                    PosTags tagB = POSList.at(i).getValue();
                    Script textB = POSList.at(i).getKey();
                    if(text_weights.get(textB)!= null)
                    {
                        if(tags.get(Script.of(tagB.name()))!= null) // TODO name geändert
                        {
                            HashMap<PosTags, Integer> HMP = tags.get(Script.of(tagB.name()));
                            Optional<Map.Entry<PosTags, Integer>> entryWithMaxPos = HMP.entrySet().stream()
                                    .max(Comparator.comparingInt(Map.Entry::getValue));
                            if (entryWithMaxPos.isPresent())
                            {
                                Map.Entry<PosTags, Integer> maxPos = entryWithMaxPos.get();
                                PosTags keyMaxPos = PosTags.fromInt(maxPos.getKey().ordinal());
                                HashMap<PosTags, HashMap<Script,Integer>> HMT = text_weights.get(textB);
                                if(HMT.get(keyMaxPos)!= null)
                                {
                                    if(keyMaxPos == POSList.at(i+1).getValue()){
                                        posright++;
                                    }
                                    HashMap<Script,Integer> HMS = HMT.get(keyMaxPos);
                                    Optional<Map.Entry<Script, Integer>> KeyMaxScript = HMS.entrySet().stream()
                                            .max(Comparator.comparingInt(Map.Entry::getValue));
                                    if (KeyMaxScript.isPresent()) {
                                        Map.Entry<Script, Integer> MRS = KeyMaxScript.get();
                                        Script R = Script.of(MRS.getKey());
                                        if(R.equals(POSList.at(i+1).getKey()))
                                        {
                                            positive++;
                                            if(learn)
                                            {
                                                HMP.put(keyMaxPos,HMP.get(keyMaxPos)+1);
                                                tags.put(Script.of(tagB.name()),HMP);
                                                HMS.put(R,HMS.get(R)+1);
                                                HMT.put(keyMaxPos,HMS);
                                                text_weights.put(textB,HMT);
                                            }
                                        }
                                        else {
                                            negative++;
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            HashMap<PosTags, HashMap<Script,Integer>> HM = text_weights.get(textB);
                            Script S = findScriptWithMaxValue(HM);
                            if(POSList.at(i+1).getKey().equals(S))
                            {
                                positive++;
                                if(learn)
                                {
                                    PosTags P = POSList.at(i+1).getValue();
                                    HashMap<PosTags,Integer> HP = new HashMap<>(); // PosTags HashMap
                                    HashMap<Script,Integer> HS = new HashMap<>(); // Inner Inner Text Hashmap
                                    HashMap<PosTags,HashMap<Script,Integer>> HR = new HashMap<>(); // Text PosTags HashMap
                                    // Fill Text HashMap
                                    HS.put(S,1);
                                    HR.put(P,HS);
                                    text_weights.put(textB,HR);
                                    // Fill PosTags Hashmap
                                    HP.put(P,1);
                                    tags.put(Script.of(tagB.name()),HP);
                                }

                            }
                            else{
                                negative++;
                            }
                        }
                    }
                    else{
                        notFound++;
                    }
                }
            }
        }
        return new Tuple.Quaple<>(positive,negative,notFound,posright);
    }

    private Script findScriptWithMaxValue(HashMap<PosTags, HashMap<Script, Integer>> dataMap) {
        return dataMap.values()
                .stream()
                .flatMap(scriptMap -> scriptMap.entrySet().stream())
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
