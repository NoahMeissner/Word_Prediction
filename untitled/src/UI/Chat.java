package UI;

import lingologs.Script;

import java.util.List;
import java.util.Scanner;

public class Chat {

    public Chat()
    {
        List<Script> T = List.of(Script.of("test1"),Script.of("test2"),Script.of("test3"));
        write("Hallo",T);
    }

    private String write(String S, List<Script> T){
        System.out.println("################");
        System.out.println(S);
        System.out.println("(1):"+ T.get(0) + " (2):" + T.get(1)+" (3):" + T.get(2));
        String A = new Scanner(System.in).nextLine();
        switch (A) {
            case "exit()":
                return S;
            case "1":
                S += " " + T.get(0);
                break;
            case "2":
                S += " " + T.get(1);
                break;
            case "3":
                S += " " + T.get(2);
                break;
            default:
                S += " " + A;
                break;
        }

        return write(S,T);
    }

    private void safe(){

    }


}
