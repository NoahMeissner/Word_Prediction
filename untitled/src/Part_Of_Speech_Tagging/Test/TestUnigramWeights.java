package Part_Of_Speech_Tagging.Test;

import Part_Of_Speech_Tagging.PosTags;
import lingolava.Tuple.Couple;
import lingolava.Tuple.Triple;
import lingologs.Script;

import java.util.*;

public class TestUnigramWeights {

    private final boolean learn;
    private final List<List<Couple<Script,PosTags>>> LS;
    private Couple<HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>>,
            HashMap<Script, HashMap<PosTags, Integer>>> C;


    public TestUnigramWeights(List<List<Couple<Script,PosTags>>> LS, boolean learn, Couple<HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>>,
            HashMap<Script, HashMap<PosTags, Integer>>> C )
    {
        this.learn = learn;
        this.LS = LS;
        this.C = C;
    }
    public Triple<Integer,Integer,Integer> testWeights()
    {
        int positive = 0;
        int negative = 0;
        int notFound = 0;
        HashMap<Script, HashMap<PosTags, Integer>> tags = C.getValue();
        HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>> text_weights = C.getKey();

        for(List<Couple<Script,PosTags>> POSList: LS)
        {
            for(int i = 0; i<POSList.size(); i++)
            {
                if(i+1<POSList.size())
                {
                    PosTags tagB = POSList.get(i).getValue();
                    Script textB = POSList.get(i).getKey();
                    if(text_weights.get(textB)!= null)
                    {
                        if(tags.get(tagB)!= null)
                        {
                            HashMap<PosTags, Integer> HMP = tags.get(tagB);
                            Optional<Map.Entry<PosTags, Integer>> entryWithMaxPos = HMP.entrySet().stream()
                                    .max(Comparator.comparingInt(Map.Entry::getValue));
                            if (entryWithMaxPos.isPresent())
                            {
                                Map.Entry<PosTags, Integer> maxPos = entryWithMaxPos.get();
                                PosTags keyMaxPos = PosTags.fromInt(maxPos.getKey().ordinal());
                                HashMap<PosTags, HashMap<Script,Integer>> HMT = text_weights.get(textB);
                                if(HMT.get(keyMaxPos)!= null)
                                {
                                    HashMap<Script,Integer> HMS = HMT.get(keyMaxPos);
                                    Optional<Map.Entry<Script, Integer>> KeyMaxScript = HMS.entrySet().stream()
                                            .max(Comparator.comparingInt(Map.Entry::getValue));
                                    if (KeyMaxScript.isPresent()) {
                                        Map.Entry<Script, Integer> MRS = KeyMaxScript.get();
                                        Script R = Script.of(MRS.getKey());
                                        if(R.equals(POSList.get(i+1).getKey()))
                                        {
                                            positive++;
                                            System.out.println("positive");
                                            if(learn)
                                            {
                                                HMP.put(keyMaxPos,HMP.get(maxPos)+1);
                                                tags.put(Script.of(tagB.name()),HMP);
                                                HMS.put(R,HMS.get(R)+1);
                                                HMT.put(keyMaxPos,HMS);
                                                text_weights.put(textB,HMT);
                                            }
                                        }
                                        else {
                                            negative++;

                                            System.out.println("negative"+R+":"+POSList.get(i+1));

                                        }
                                    }
                                }
                            }
                        }
                        else{
                            HashMap<PosTags, HashMap<Script,Integer>> HM = text_weights.get(textB);
                            Script S = findScriptWithMaxValue(HM);
                            if(POSList.get(i+1).getKey().equals(S))
                            {
                                positive++;
                                if(learn)
                                {
                                    PosTags P = POSList.get(i+1).getValue();
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
                        System.out.println("notFound");
                    }
                }
            }
        }
        return new Triple<>(positive,negative,notFound);
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
