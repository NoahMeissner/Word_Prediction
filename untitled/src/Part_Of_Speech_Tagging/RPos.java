package Part_Of_Speech_Tagging;
import lingologs.*;

import java.util.List;

public class RPos {
    /*
    This class will handle Rule Based Pos Tagging with the help of the Stanford Core NLP Libary
     */

    private final Script S;


    public RPos(Script S)
    {
        this.S = S;
    }


    public List<String> getPosTags()
    {
        long startTime = System.currentTimeMillis(); // start time
        List<String> posTags = runRbPOS (S);
        long endTime = System.currentTimeMillis(); // end time
        long executionTime = endTime - startTime; // calculate time
        System.out.println("Probabilistic POS Tagging Time: " + executionTime + " MilliSeconds");
        return posTags;
    }

    private List<String> runRbPOS(Script s) {
        return null;
    }
}
