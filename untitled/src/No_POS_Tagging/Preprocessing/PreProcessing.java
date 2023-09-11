package No_POS_Tagging.Preprocessing;

import lingologs.Script;

import java.util.ArrayList;
import java.util.List;

public class PreProcessing {
    private final List<Script> LS;

    public PreProcessing(List<Script> LS)
    {
        this.LS = LS;
    }

    private List<Script> Normalise (Script S)
    {
        if(S.includes(Script.of("ACT"))||S.includes(Script.of("SCENE")))
        {
            S = Script.of(" ");
        }
        S = S.replace("[\\r\\n]+", " ");
        S = S.replace(","," , ");
        S = S.replace("\\.", " . ");
        S = S.replace(":","");
        S = S.toLower();
        S = S.replace("\\?"," \\?");
        S = S.replace("!"," !");
        S = S.replace("'s"," is");
        S = S.replace("'re"," are");
        return S.split(" ");
    }

    private List<Script> deleteWhiteSpace(Script S) {
        List<Script> LS = Normalise(S);
        for (int i = 0; i<LS.size(); i++) {
            if(LS.get(i).equals(Script.of(" "))||LS.get(i).equals(Script.of("")))
            {
                LS.remove(LS.get(i));
            }
           // LS.set(i,LS.get(i).replace(" ", ""));
        }
        return LS;
    }
    public List<List<Script>> getListText()
    {
        List<List<Script>> result= new ArrayList<>();

        for (Script l : LS) {
            result.add(deleteWhiteSpace(l));
        }
        return result;
    }
}
