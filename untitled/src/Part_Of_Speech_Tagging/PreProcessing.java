package Part_Of_Speech_Tagging;

import lingologs.Script;
import lingologs.Texture;

/**
 * Key Functionalities:
 * - Text Normalization: Normalizes input text by handling punctuation, capitalization, and whitespace.
 * - Preprocessing Options: Allows for different levels of preprocessing
 * - Handling Whitespace: Provides methods to split and filter text into meaningful units, such as sentences or paragraphs.
 */
public class PreProcessing {

    private final boolean preprocessing;
    private final boolean POS;
    private Script S;
    private Texture<Script> LS;



    public PreProcessing(Texture<Script> LS, boolean preprocessing, boolean POS)
    {
        this.LS = LS;
        this.preprocessing = preprocessing;
        this.POS = POS;
    }

    public PreProcessing(Script S, boolean preprocessing, boolean POS)
    {
        this.S = S;
        this.preprocessing = preprocessing;
        this.POS = POS;
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

    public Script getSentence(){
        return Normalise(S);
    }

    private Texture<Script> deleteWhiteSpace(Script S) {
        Texture<Script> LS = new Texture<>(Normalise(S).split(" "));
        LS.filter(A -> !A.equals(Script.of(" ")) ||!A.equals(Script.of("")) );
        return LS;
    }

    public Script getText() {
        StringBuilder SB = new StringBuilder();
        for (Script A : LS)
        {
            SB.append(Normalise(A));
            SB.append("$");
        }
        return Script.of(SB.toString());
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