package Training;

import lingologs.Script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrainBigramWeightsList {
    private List<List<Script>> LS;
    public TrainBigramWeightsList(List<List<Script>> LS)
    {
        this.LS = LS;
    }

    private List<Script> mB(List<Script> L)
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

    private HashMap<Script, HashMap<Script,Integer>> setWeights(List<List<Script>> LS)
    {
        HashMap<Script, HashMap<Script,Integer>> HM = new HashMap<>();
        for(List<Script> L:LS)
        {
            L = mB(L);
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
