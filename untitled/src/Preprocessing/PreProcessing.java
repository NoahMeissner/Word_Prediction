package Preprocessing;

import lingologs.Script;

import java.util.ArrayList;
import java.util.List;

public class PreProcessing {

    private List<Script> LS;

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
       // S = S.replace("is", "be");
       // S = S.replace("are","be");
       // S = S.replace("am","be");

        return S.split(" ");
    }

    private List<Script> deleteWhiteSpace(Script S) {
        List<Script> LS = Normalise(S);
        for (Script A : LS) {
            if(A.equals(" ")||A.equals(""))
            {
                LS.remove(A);
            }
            A = A.replace(" ", "");
        }
        return LS;
    }

    public List<List<Script>> getListText()
    {
        List<List<Script>> result= new ArrayList<>();

        for(int i = 0; i<LS.size();i++)
        {
            result.add(deleteWhiteSpace(LS.get(i)));
        }
        return result;
    }
}
