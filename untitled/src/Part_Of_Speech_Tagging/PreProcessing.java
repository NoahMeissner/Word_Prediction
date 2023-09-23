package Part_Of_Speech_Tagging;

import lingologs.Script;
import lingologs.Texture;

public class PreProcessing {

    private Script S;
    private final boolean preprocessing;
    private final boolean POS;
    private Texture<Script> LS;

    public PreProcessing(Script S, boolean preprocessing){
        this.S = S;
        this.preprocessing = preprocessing;
        this.POS = false;
    }

    public PreProcessing(Texture<Script> LS, boolean preprocessing)
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

    private Texture<Script> deleteWhiteSpace(Script S) {
        Texture<Script> LS = new Texture<>(Normalise(S).split(" "));
        LS.filter(A -> !A.equals(Script.of(" ")) ||!A.equals(Script.of("")) );
        return LS;
    }

    public Script getText() {
        return Normalise(S);
    }

    public Texture<Texture<Script>> getListText()
    {
        Texture<Texture<Script>> result= new Texture<>();

        for (Script l : LS) {
            if(deleteWhiteSpace(l).toList().size()>1)
            {
                result = result.add(deleteWhiteSpace(l));
            }
        }
        return result;
    }
}