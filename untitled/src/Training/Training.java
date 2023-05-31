package Training;

import lingologs.Script;

import java.util.List;

public class Training {
    private final List<Script> uniGrams;
    private final List<Script> biGrams;

    /*
    This Script will Calculate the probabilities of each N - Gram and will
    Safe these in a JSON File
     */


    public Training(List<Script> uniGrams,List<Script> biGrams )
    {
        this.uniGrams = uniGrams;
        this.biGrams = biGrams;
    }

    private void calculate()
    {

    }

    // This Method will set a List which will be safed in the JSON File
    private void setList(){

    }

    private void collectPairs()
    {

    }


}
