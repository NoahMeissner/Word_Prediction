package Part_Of_Speech_Tagging;

import lingologs.Script;

public class Preprocessing {

    private Script S;
    public Preprocessing(Script S){
        this.S = S;
    }



    private Script Normalise (Script S)
    {
        S = S.replace("[\\r|\\n]+", "");
        S = S.replace(","," , ");
        S = S.replace("\\."," \\. ");
        S = S.replace(":","");
        S = S.toLower();
        S = S.replace("'s"," is");
        S = S.replace("\"","");
        S = S.replace("'re"," are");
        return S;
    }

    public Script getS() {
         return Normalise(S);
    }
}
