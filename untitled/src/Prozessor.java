import UI.Tags;

import java.util.HashMap;
import java.util.Map;

public class Prozessor {

    /*
    Diese Klasse erhält die Config und die Links und startet das richtige Learning, was in
     verschiedenen Punkten wichtig ist.
     Danach führt es die Tests durch
     */

    private Map<Tags,Integer> config;
    private Map<Tags,String> links;

    private boolean testConfig;

    private Map<String, Map<String,Integer>> weightsOPos = new HashMap();

    public Prozessor(Map<Tags,Integer> config, Map<Tags,String> links, boolean testConfig){
        this.config = config;
        this.links= links;
        this.testConfig = testConfig;
    }

    public void TrainWeights(){
        if(config.get(Tags.postag) == 1){
            // TODO Hier muss ich dann POS Tagging Training einbauen
        }
        else{
            // TODO Hier muss ich dann dass nich POS Tagging einbauen
        }
    }




}
