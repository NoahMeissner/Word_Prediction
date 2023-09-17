package Part_Of_Speech_Tagging;

import lingologs.Script;

import java.util.ArrayList;
import java.util.List;

public class PreProcessing {

    private Script S;
    private final boolean preprocessing;
    private List<Script> LS;

    public PreProcessing(Script S, boolean preprocessing){
        this.S = S;
        this.preprocessing = preprocessing;
    }

    public PreProcessing(List<Script> LS, boolean preprocessing)
    {
        this.LS = LS;
        this.preprocessing = preprocessing;
    }



    private Script Normalise (Script S)
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
        if(preprocessing){
            S = S.replace("'s"," is");
            S = S.replace("'re"," are");
        }
        return S;
    }

    private List<Script> deleteWhiteSpace(Script S) {
        List<Script> LS = Normalise(S).split(" ");
        for (int i = 0; i<LS.size(); i++) {
            if(LS.get(i).equals(Script.of(" "))||LS.get(i).equals(Script.of("")))
            {
                LS.remove(LS.get(i));
            }
        }
        return LS;
    }

    public Script getText() {
        return Normalise(S);
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