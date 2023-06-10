package UI;

import lingologs.Script;

import java.util.Scanner;

public class Chat {

    Script S;
    Script DV = new Script("##################");

    public Chat(Script S)
    {
        this.S = S;
    }

    public Chat(Exception e){

    }

    public Script getChat(){
        System.out.println(S +"\n" + DV);
        Scanner SC = new Scanner(System.in);
        Script OUT = new Script(SC.nextLine());
        return OUT;
    }
}
