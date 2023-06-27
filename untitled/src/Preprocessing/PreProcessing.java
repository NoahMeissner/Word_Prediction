package Preprocessing;

import lingologs.Script;

import java.util.List;

public class PreProcessing {

    List<Script> L;

    public PreProcessing(Script text){
        this.L = cutText(text);
    }

    /*
        Goal is to split text into to parts to start multiprocessing part
     */
    private List<Script> cutText(Script S){
        int length = S.length();
        int mid = length /2;

        Script firstHalf = S.part(0,mid);
        Script secondHalf = S.part(mid,length);
        System.out.println(firstHalf);
        System.out.println(secondHalf);
        return List.of(firstHalf, secondHalf);
    }



    public void getText(){

    }
}
