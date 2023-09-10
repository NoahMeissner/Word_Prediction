package Part_Of_Speech_Tagging.Test;

import Part_Of_Speech_Tagging.*;
import lingolava.Tuple.Couple;
import lingolava.Tuple.Triple;
import lingologs.Script;

import java.util.*;

public class TestBigramWeights {

    private final List<Script> LS;
    private final boolean ROP;
    private Couple<HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>>,
            HashMap<Class<?>, HashMap<PosTags, Integer>>> C;

    private final boolean learn;

    public TestBigramWeights(List<Script> LS, boolean ROP,boolean learn, Couple<HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>>,
            HashMap<Class<?>, HashMap<PosTags, Integer>>> C )
    {
        this.learn = learn;
        this.ROP = ROP;
        this.LS = LS;
        this.C = C;
    }

    private List<List<Couple<Script,PosTags>>> makeBigrams(List<Script> L)

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


    public Triple<Integer,Integer,Integer> testWeights()
    {
        int positive = 0;
        int negative = 0;
        int notFound = 0;
        HashMap<Class<?>, HashMap<PosTags, Integer>> tags = C.getValue();
        HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>> text_weights = C.getKey();
        List<List<Couple<Script,PosTags>>> L = makeBigrams(LS);
        System.out.println(L);

        for(List<Couple<Script,PosTags>> POSList: L)
        {
            for(int i = 0; i<POSList.size(); i++)
            {
                if(i+2<POSList.size())
                {
                    Script tagB = Script.of(POSList.get(i).getValue() + " "+POSList.get(i+1).getValue());
                    Script textB = Script.of(POSList.get(i).getKey() + " "+POSList.get(i+1).getKey());
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
                                        if(R.equals(POSList.get(i+2).getKey()))
                                        {
                                            positive++;
                                            System.out.println("positive");
                                        }
                                        else {
                                            negative++;

                                            System.out.println("negative"+R+":"+POSList.get(i+2));

                                        }
                                    }
                                }
                            }
                        }
                        else{
                            HashMap<PosTags, HashMap<Script,Integer>> HM = text_weights.get(textB);
                            if(POSList.get(i+2).getKey().equals(findScriptWithMaxValue(HM)))
                            {
                                positive++;
                                System.out.println("positiv");

                            }
                            else{
                                negative++;
                                System.out.println("negative"+POSList.get(i+2)+":"+findScriptWithMaxValue(HM));
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

    private HashMap<Script, HashMap<PosTags, Integer>> convertTags
            (HashMap<Class<?>, HashMap<PosTags, Integer>> T)
    {
        try{
            HashMap<Script, HashMap<PosTags, Integer>> res = new HashMap<>();
            for (Map.Entry<Class<?>, HashMap<PosTags, Integer>> entry : T.entrySet()) {
                Class<?> scriptClass = entry.getKey();
                HashMap<PosTags, Integer> posTagsMap = entry.getValue();
                res.put((Script) scriptClass.newInstance(),posTagsMap);
            }
            return res;
        }catch (Exception e)
        {
            System.out.println("Convert funktioniert nicht"+ e);
            return null;
        }
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


    private Couple<HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>>,
            HashMap<Class<?>, HashMap<PosTags, Integer>>> learn (Couple<HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>>,
            HashMap<Class<?>, HashMap<PosTags, Integer>>> C)
    {
        return C;
    }
}
