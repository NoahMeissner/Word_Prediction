import Predictor.Loader;

import UI.SetUp;
import lingologs.Script;

public class Main {

    /*
    Start of the Program
     */

    private static final Script BP = new Script("""
        /Users/noahmeissner/Documents/Data/projectreischer/untitled/src/test.json
    """);


    public static void main(String[] args) {
        SetUp S = new SetUp();
        Loader L = null;
        while(L == null)
        {
            L = S.start();
        }
    }





}