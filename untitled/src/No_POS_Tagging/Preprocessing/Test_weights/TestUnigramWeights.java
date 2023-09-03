package No_POS_Tagging.Preprocessing.Test_weights;

import lingologs.Script;

import java.util.*;

public class TestUnigramWeights {

    private HashMap<Script, HashMap<Script, Integer>> HM;
    private List<List<Script>> testSet;

    private int simi;
    public TestUnigramWeights(HashMap<Script, HashMap<Script, Integer>> HM, List<List<Script>> testSet,int simi)
    {
        this.HM = HM;
        this.simi = simi;
        this.testSet = testSet;
    }

    public Script getResults(boolean learn)
    {
        return calculate_any_value(testSet,HM,learn);
    }

    private Script calculate_any_value(List<List<Script>> scripts,
                                       HashMap<Script, HashMap<Script,Integer>> hm,
                                       boolean learn) {
        List<Integer> result = new ArrayList<>();

        for(List<Script> L : scripts)
        {
            for (int i = 0; i < L.size(); i++) {
                if (hm.get(L.get(i)) != null && (i + 1) < L.size()) {
                    HashMap<Script, Integer> H = hm.get(L.get(i));
                    boolean t = false;
                    if(H!=null)
                    {
                        List<Script> LR = getMaxValues(H, simi);
                        for(int a = 0; a<LR.size();a++)
                        {
                            if (L.get(i + 1).equals(LR.get(a))) {
                                result.add(1);
                                if(learn)
                                {
                                    int e = H.get(L.get(i+1));
                                    H.put(L.get(i+1),e+1);
                                }
                                t = true;
                                break;
                            }
                        }
                        if(!t)
                        {
                            if(learn)
                            {
                                int e = 0;
                                if(H.get(L.get(i+1))!= null)
                                {
                                    e = H.get(L.get(i+1));
                                }
                                H.put(L.get(i+1),e+1);
                            }

                            result.add(0);
                        }
                        if(learn)
                        {
                            hm.put(L.get(i),H);
                        }
                    }
                    else{
                        result.add(2);
                    }
                }

            }
        }
        int tr = 0; // 1
        int fa = 0; // 0
        int no = 0; // 2
        for (int i = 0; i< result.size();i++)
        {

            if(result.get(i)== 1)
            {
                tr++;
            }
            if(result.get(i)==0)
            {
                    fa++;
            }
            if(result.get(i)==2)
            {
                no++;
            }

        }
        return Script.of("Wir haben"+ tr+"positiv"+":"+ (float)tr/(tr+fa+no)+"Succes Rate :"+"Wir haben "+fa+"falsch vorhersagen und "+ no+"nicht gefunden");
    }

    public List<Script> getMaxValues(HashMap<Script, Integer> hashMap, int n) {
        List<Map.Entry<Script, Integer>> sortedEntries = new ArrayList<>(hashMap.entrySet());

        Collections.sort(sortedEntries, (e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        List<Script> maxValues = new ArrayList<>();

        for (int i = 0; i < n && i < sortedEntries.size(); i++) {
            maxValues.add(sortedEntries.get(i).getKey());
        }

        return maxValues;
    }



}
