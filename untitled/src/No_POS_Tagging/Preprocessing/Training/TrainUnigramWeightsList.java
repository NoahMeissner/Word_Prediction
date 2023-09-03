package No_POS_Tagging.Preprocessing.Training;

import lingologs.Script;

import java.util.HashMap;
import java.util.List;

public class TrainUnigramWeightsList {

    private List<List<Script>> LS;

    public TrainUnigramWeightsList(List<List<Script>> LS)
    {
        this.LS = LS;
    }


    public HashMap<Script, HashMap<Script,Integer>> getWeights()
    {
        return setWeights(LS);
    }

    private static HashMap<Script, HashMap<Script,Integer>> setWeights(List<List<Script>> Ls){
        HashMap<Script, HashMap<Script,Integer>> HM = new HashMap<>();

        for(List<Script> L : Ls)
        {
            for(int i = 0; i< L.size();i++)
            {
                if(i+1<L.size())
                {
                    if(HM.get(L.get(i))!= null)
                    {
                        HashMap<Script,Integer> HA = HM.get(L.get(i));
                        if(HA.get(L.get(i+1))!= null && i+1 <L.size())
                        {
                            int v = HA.get(L.get(i+1))+1;
                            HA.put(L.get(i+1),v);
                            HM.put(L.get(i),HA);
                        }
                        else{
                            if(i+1<L.size())
                            {
                                HA.put(L.get(i+1),1);
                                HM.put(L.get(i),HA);
                            }
                        }
                    }
                    else{
                        if(HM.get(L.get(i))== null)
                        {
                            HM.put(L.get(i), new HashMap<>());
                            HashMap<Script,Integer> HC = HM.get(L.get(i));
                            HC.put(L.get(i+1),1);
                            HM.put(L.get(i),HC);
                        }
                    }
                }
            }
        }
        return HM;
    }
}
