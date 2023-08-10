package Test_weights;

import lingologs.Script;

import java.util.*;

public class TestBigramWeights {

    private HashMap<Script, HashMap<Script, Integer>> HM;
    private List<List<Script>> testSet;

    private int simi;
    public TestBigramWeights(HashMap<Script, HashMap<Script, Integer>> HM, List<List<Script>> testSet,int simi)
    {
        this.HM = HM;
        this.simi = simi;
        this.testSet = makeBigrams(testSet);
    }

    private List<List<Script>> makeBigrams(List<List<Script>> L)
    {
        List<List<Script>> result = new ArrayList<>();
        for(List<Script> LS : L)
        {
            List<Script> zwischen = new ArrayList<>();
            for (int i = 0; i<LS.size();i++)
            {
                if(i+1<LS.size())
                {
                    zwischen.add(Script.of(LS.get(i)+" "+LS.get(i+1)));
                }
            }
            result.add(zwischen);
        }
        return result;
    }

    public Script getResults(boolean learn)
    {
        return calculate_any_value(testSet,HM,simi,learn);
    }

    private Script calculate_any_value(List<List<Script>> scripts,
                                       HashMap<Script, HashMap<Script, Integer>> hm,
                                       int simi,
                                       boolean learn) {
        List<Integer> result = new ArrayList<>();

        for(List<Script> L : scripts)
        {
            for (int i = 0; i < L.size(); i++) {
                if (hm.get(L.get(i)) != null && (i + 1) < L.size()) {
                    HashMap<Script, Integer> H = hm.get(L.get(i));
                    if(H!=null)
                    {
                        List<Script> LR = getMaxValues(H, simi);
                        boolean tr = false;
                        for(int a = 0; a<LR.size();a++)
                        {
                            Script res = L.get(i+1).split(" ").get(1);
                            if (res.equals(LR.get(a))) {
                                result.add(1);
                                if(learn)
                                {
                                    int e = H.get(res);
                                    H.put(res,e+1);
                                }
                                tr = true;
                                break;
                            }
                        }
                        if(!tr)
                        {
                            if(learn)
                            {
                                Script res = L.get(i+1).split(" ").get(1);
                                int e = 0;
                                if(H.get(res)!=null)
                                {
                                    e = H.get(res);
                                }
                                H.put(res,e+1);
                            }
                            result.add(0);
                        }
                        if(learn)
                        {
                            hm.put(L.get(i),H);
                        }

                    }
                    else {
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
