
package UI;

import Part_Of_Speech_Tagging.PPos;
import Part_Of_Speech_Tagging.PosTags;
import Part_Of_Speech_Tagging.ProcessPos;
import Part_Of_Speech_Tagging.RPos;
import lingolava.Tuple.Couple;
import lingologs.Script;
import lingologs.Texture;

import java.util.*;

/**
 * The Chat class facilitates text-based interaction with the user, providing functionality for generating
 * and suggesting text predictions based on the project's language model. It supports both rule-based and
 * probabilistic approaches for generating suggestions and allows users to save generated text to files.
 */
public class Chat {

    private final String exitName = "exit()";
    private final String delimiter = "#################################";
    private final boolean preprocessing;
    private final boolean UOB;

    public Chat(Couple<HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>>,
            HashMap<Script, HashMap<PosTags, Integer>>> C,
                Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> MNP,
                boolean UOB, boolean ROP, Map<Tags,String> links, boolean preprocessing)
    {
        this.preprocessing = preprocessing;
        this.UOB = UOB;
        if(UOB)
        {
            safeText(Script.of(chatPU(C,MNP.getKey(), "",ROP)), links);

        }
        else {
            chatPB(C,MNP.getValue(), "",ROP);
            safeText(Script.of(chatPB(C,MNP.getValue(), "",ROP)), links);

        }
    }



    public Chat(Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> MNP, boolean
            UOB, Map<Tags,String> links, boolean preprocessing)
    {
        this.UOB = UOB;
        this.preprocessing = preprocessing;
        if(UOB)
        {
            safeText(Script.of(chatNPU(MNP.getKey(), "")), links);
        }
        else {
            safeText(Script.of(chatNPB(MNP.getValue(), "")), links);

        }
    }

    private String chatPB(Couple<HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>>,
            HashMap<Script, HashMap<PosTags, Integer>>> C,
                          HashMap<Script, HashMap<Script, Integer>> MNP, String S,boolean ROP) {
        List<String> LS = List.of(S.split(" "));
        Script Bigr;
        if(S.contains(exitName)){
            return S.substring(0, S.length() - exitName.length());
        }
        else{
            System.out.println(S);
            Script[] SUG = new Script[3];
            System.out.println(delimiter);
            if(LS.size()>1 && !S.equals(""))
            {
                Bigr = Script.of(" "+LS.get(LS.size()-2)+" "+LS.get(LS.size()-1));
                SUG = findMaxPOS(C,MNP,Bigr,ROP);
                if(SUG != null)
                {
                    System.out.println("(1) "+ SUG[0] + "(2) "+SUG[1] +"(3) "+SUG[2]+"Bigr");
                }
            }
            else{
                System.out.println("Start Bigram");
            }
            return chatPB(C,MNP, write(S,SUG),ROP);
        }
    }

    private String chatNPB(HashMap<Script, HashMap<Script, Integer>> key, String S) {
        List<String> LS = List.of(S.split(" "));
        Script Bigr;
        if(S.contains(exitName)){
            return S.substring(0, S.length() - exitName.length());
        }
        else{
            System.out.println(S);
            Script[] SUG = new Script[3];
            if(LS.size()>1 && !S.equals(""))
            {
                Bigr = Script.of(LS.get(LS.size()-2)+" "+LS.get(LS.size()-1));
                SUG = findMaxNP(Bigr,key,new Script[3]);
            }
            else{
                System.out.println("Start");
            }
            assert SUG != null;
            return chatNPB(key, write(S,SUG));
        }
    }

    private String chatNPU(HashMap<Script, HashMap<Script, Integer>> key, String S) {
        List<String> LS = List.of(S.split(" "));
        if(LS.get(LS.size()-1).equals(exitName)){
            return S.substring(0, S.length() - exitName.length());
        }
        else{
            System.out.println(S);
            Script[] SUG = new Script[3];
            if(!S.equals(""))
            {
                SUG = findMaxNP(Script.of(LS.get(LS.size()-1)),key,new Script[3]);
            }
            else{
                System.out.println("Start");
            }



            return chatNPU(key, write(S,SUG));
        }
    }

    private String chatPU(
            Couple<HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>>,
                    HashMap<Script, HashMap<PosTags, Integer>>> C, HashMap<Script, HashMap<Script, Integer>> MNP,
            String S, boolean ROP) {
        if (S.contains(exitName)) {
            return S.substring(0, S.length() - exitName.length());
        }

        List<String> LS = List.of(S.split(" "));
        System.out.println(S);
        Script[] SUG = new Script[3];
        System.out.println(delimiter);

        if (LS.size() > 1 && !S.equals("")&& C != null) {
            Script UGR = Script.of(LS.get(LS.size()-1));
            SUG = findMaxPOS(C,MNP, UGR, ROP);
        } else {
            System.out.println("Start Unigram");
        }

        return chatPU(C,MNP, write(S, SUG), ROP);
    }

    private Script[] findMaxPOS(
            Couple<HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>>, HashMap<Script, HashMap<PosTags, Integer>>> c,
            HashMap<Script, HashMap<Script, Integer>> MNP,
            Script S, boolean ROP) {
        Couple<Script, PosTags> NGram;
        Texture<Couple<Script, PosTags>> LC;
        if(ROP)
        {
            PPos pPos = new PPos(S,preprocessing);
            ProcessPos PR = new ProcessPos(pPos.getSentences(),ROP);
            LC = PR.getSentence();
        }
        else{
            RPos R = new RPos(S,preprocessing);
            ProcessPos PR = new ProcessPos(ROP,R.getPosTags());
            LC = PR.getSentence();
        }
        if(UOB)
        {
            System.out.println(LC.toList().size());
            if(LC.toList().size()>0)
            {
                NGram = LC.at(LC.toList().size()-1);
                PosTags P;
                if(NGram != null) {
                    P = findPos(c.getValue(), Script.of(NGram.getValue().name()));
                    if(P != null)
                    {
                        HashMap<PosTags,HashMap<Script, Integer>> ZR = c.getKey().get(Script.of(" "+NGram.getKey()));
                        HashMap<Script,Integer> M = ZR.get(P);
                        Script[] res = findMaxSPOS(M);
                        if(res != null)
                        {
                            return findMaxSPOS(M);
                        }
                        else{
                            return findMaxNP(NGram.getKey(),MNP,new Script[3]);
                        }
                    }
                    else {
                        System.out.println("nicht POS");
                        return findMaxNP(NGram.getKey(),MNP,new Script[3]);
                    }
                }
            }
            else{
                System.out.println("no prediction");
            }
        }
        else{
            try{
                String BiNgrm = LC.at(0).getValue().name()+" "+ LC.at(1).getValue().name();
                if(c.getValue()!= null )
                {
                    String textB = " "+LC.at(0).getKey()+ " "+LC.at(1).getKey();
                    PosTags posRes = findPos(c.getValue(), Script.of(BiNgrm));
                    if(posRes != null)
                    {
                        HashMap<PosTags,HashMap<Script, Integer>> ZR = c.getKey().get(Script.of(textB));
                        HashMap<Script,Integer> M = ZR.get(posRes);
                        return findMaxSPOS(M);
                    }

                }
                else {
                    System.out.println("C gleich null");
                    return findMaxNP(new Script(BiNgrm),MNP,new Script[3]);
                }
            }catch (Exception e)
            {
                return null;
            }
        }
        return null;
    }

    private static Script[] findMaxSPOS(HashMap<Script, Integer> key) {
        Script[] topThreeScripts = new Script[3];
        PriorityQueue<Script> priorityQueue = new PriorityQueue<>(3, (s1, s2) -> key.get(s2).compareTo(key.get(s1)));

        if (key != null) {
            for (Script script : key.keySet()) {
                priorityQueue.offer(script);

                if (priorityQueue.size() > 3) {
                    priorityQueue.poll();
                }
            }
        }

        for (int i = 2; i >= 0; i--) {
            topThreeScripts[i] = priorityQueue.poll();
        }
        for (int i = 0; i<topThreeScripts.length;i++)
        {
            if(topThreeScripts[i] != null)
            {
                Script S = topThreeScripts[i].replace(" ","");
                if(S.equals(new Script("$")))
                {
                    topThreeScripts[i] = new Script(" ");
                }
            }
        }

        return topThreeScripts;
    }

    private PosTags findPos(HashMap<Script, HashMap<PosTags, Integer>> value, Script S) {
        HashMap<PosTags, Integer> HM = value.get(S);
        PosTags maxPosTag = null;
        int maxValue = Integer.MIN_VALUE;
        if(HM!= null)
        {
            for (Map.Entry<PosTags, Integer> entry : HM.entrySet()) {
                if (entry.getValue() > maxValue) {
                    maxValue = entry.getValue();
                    maxPosTag = entry.getKey();
                }
            }
        }
        return maxPosTag;
    }



    private Script[] findMaxNP(Script S, HashMap<Script, HashMap<Script, Integer>> key, Script[] LS) {
        S = Script.of(S.toString().toLowerCase());
        try{
            if(key.get(S)!= null)
            {
                HashMap<Script, Integer> HM = key.get(S);
                Integer highestValue = Integer.MIN_VALUE;
                Script ZR = Script.of("");
                for (Script SK : HM.keySet()) {
                    if (HM.get(SK) > highestValue && (LS[0]== null || !LS[0].equals(SK)) && (LS[1]==null||!LS[1].equals(SK))) {
                        highestValue = HM.get(SK);
                        ZR = SK;
                    }

                }

                if(LS[2] == null && LS[1] != null && LS[0] != null) {
                    LS[2] = ZR;
                }

                if (LS[1] == null && LS[0] != null) {
                    LS[1] = ZR;
                }

                if (LS[0]== null) {
                    LS[0] = ZR;
                }

                if (LS[2] != null) {
                    return LS;
                } else {
                    return findMaxNP(S, key, LS);

                }

            }
            else {
                return findMax(key);
            }

        } catch (Exception e) {
            return null;
        }
    }

    private static Script[] findMax(HashMap<Script, HashMap<Script, Integer>> V) {
        HashMap<Script, Integer> keyCount = new HashMap<>();

        for (HashMap<Script, Integer> innerMap : V.values()) {
            for (Script script : innerMap.keySet()) {
                keyCount.put(script, keyCount.getOrDefault(script, 0) + 1);
            }
        }

        List<Map.Entry<Script, Integer>> entryList = new ArrayList<>(keyCount.entrySet());

        entryList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        Script[] mostFrequentKeys = new Script[3];

        for (int i = 0; i < 3 && i < entryList.size(); i++) {
            mostFrequentKeys[i] = entryList.get(i).getKey();
        }

        return mostFrequentKeys;
    }

    private String write (String S, Script[] T){
        if(T != null)
        {
            System.out.println("(1):"+ T[0] + " (2):" + T[1]+" (3):" + T[2]);
        }
        else{
            System.out.println("No Predictions");
        }
        String A = new Scanner(System.in).nextLine();
        switch (A) {
            case "1" -> S += " " + Objects.requireNonNull(T)[0].replace(" ","");
            case "2" -> S += " " + Objects.requireNonNull(T)[1].replace(" ","");
            case "3" -> S += " " + Objects.requireNonNull(T)[2].replace(" ","");
            default -> S += " " + A;
        }
        return S;
    }

    private void safeText(Script S, Map<Tags, String> links){
        String L = links.get(Tags.text);
        System.out.println("Do you want to save your Text");
        String A = new Scanner(System.in).nextLine();
        if(A.equals("true"))
        {
            SafeConfig SC = new SafeConfig(S,L);
        }
        else{
            System.out.println("System closed");
        }
    }
}
