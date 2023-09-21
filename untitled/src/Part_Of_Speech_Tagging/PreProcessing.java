package Part_Of_Speech_Tagging;

import lingologs.Script;

import java.util.ArrayList;
import java.util.List;

public class PreProcessing {

    private Script S;
    private final boolean preprocessing;
    private final boolean POS;
    private List<Script> LS;

    public PreProcessing(Script S, boolean preprocessing){
        this.S = S;
        this.preprocessing = preprocessing;
        this.POS = false;
    }

    public PreProcessing(List<Script> LS, boolean preprocessing)
    {
        this.LS = LS;
        this.preprocessing = preprocessing;
        this.POS = true;
    }



    private Script Normalise (Script S)
    {

        if(POS && (S.includes(Script.of("ACT"))||S.includes(Script.of("SCENE"))))
        {
            S = Script.of(" ");
        }
        //TODO nochmal anschauen

        S = S.replace("[\\r\\n]+", " ")
            .replace(","," , ")
            .replace("\\.", " . ")
            .replace(":","")
            .toLower()
            .replace("\\?"," \\?")
            .replace("\\?"," \\?")
            .replace("!"," !");
        if(preprocessing){
            S = S.replace(" hes ", "he is")
                    .replace("youre","you are")
                    .replace("theyre","they are");
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
            if(deleteWhiteSpace(l).size()>1)
            {
                result.add(deleteWhiteSpace(l));
            }
        }
        System.out.println("res"+result.size());
        return result;
    }
}