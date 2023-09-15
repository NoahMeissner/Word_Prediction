import No_POS_Tagging.Preprocessing.LoadTrainingData;
import No_POS_Tagging.Preprocessing.Test_weights.TestWeightsListNP;
import No_POS_Tagging.Preprocessing.Training.TrainListNP;
import Part_Of_Speech_Tagging.PosTags;
import Part_Of_Speech_Tagging.Test.TestWeightsListP;
import Part_Of_Speech_Tagging.Training.TrainListP;
import Predictor.Loader;
import UI.Chat;
import UI.Tags;
import lingolava.Tuple.Couple;
import lingologs.Script;

import java.util.HashMap;
import java.util.Map;

public class Processor {

    /*
    Diese Klasse erhält die Config und die Links und startet das richtige Learning, was in
     verschiedenen Punkten wichtig ist.
     Danach führt es die Tests durch
     */

    private final String testData = "Henry V";
    private final Map<Tags,Integer> config;
    private final Map<Tags,String> links;
    private boolean postag;
    private boolean preprocessing;
    private boolean howprocess;
    private boolean ngrams;
    private boolean learn;

    private final boolean testConfig;

    private Couple<HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>>,
            HashMap<Script, HashMap<PosTags, Integer>>> CP;

    private Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> MNP;

    private final Map<String, Map<String,Integer>> weightsOPos = new HashMap();

    public Processor(Map<Tags,Integer> config, Map<Tags,String> links, boolean testConfig){
        this.config = config;
        this.links= links;
        this.testConfig = testConfig;
        setConfig(config);
        TrainWeights();
    }

    private void setConfig(Map<Tags,Integer> config)
    {
        postag = config.get(Tags.postag) == 1;
        preprocessing = config.get(Tags.preprocessing) == 1;
        if(postag)
        {
            howprocess = config.get(Tags.howprocess) == 1;
        }
        ngrams = config.get(Tags.ngrams) == 1;
        learn = config.get(Tags.learn) == 1;
        Loader L = new Loader(config);
    }

    private void TrainWeights(){
        if(postag){
            if(!(config.get(Tags.config)==1)) {
                LoadTrainingData LT = new LoadTrainingData();
                TrainListP TP = new TrainListP(LT.getData(), testData, howprocess, ngrams); // TODO Preprocessing fehlt
                Loader L = new Loader(TP.trainWeights());
            }
        }
        else{
            if(!(config.get(Tags.config)==1)) {
                LoadTrainingData LT = new LoadTrainingData();
                TrainListNP TNP = new TrainListNP(LT.getData(), testData); // TODO Preprocessing
                Loader L = new Loader(TNP.getWeights());
            }
        }
        System.out.println(testConfig);
        if(testConfig)
        {
            TestWeights();
        }
        else{
            setUpChat(postag,
                    config.get(Tags.ngrams)==1,
                    (config.get(Tags.howprocess)!= null &&config.get(Tags.howprocess)==1));
        }
    }

    private void setUpChat(boolean postag,boolean UOB, boolean ROP) {
        Loader L = new Loader();
        if(postag )
        {
            Chat C = new Chat(L.getDataP(),UOB,ROP);
        }
        else{
            Chat C = new Chat(L.getDataNP(),UOB);
        }
    }

    private void TestWeights()
    {
        Loader L = new Loader();
        LoadTrainingData LT = new LoadTrainingData();
        if(postag)
        {
            CP = L.getDataP();
            TestWeightsListP TP = new TestWeightsListP(ngrams,CP,howprocess,learn); // Todo Preprocessing
            TP.testWeights(LT.getData(),testData); // TODO Get als TRIPLE
        }
        else{
            MNP = L.getDataNP();
            TestWeightsListNP TNP = new TestWeightsListNP(LT.getData(),testData,MNP, ngrams,learn);
            System.out.println(TNP.testWeights()); // TODO get as triple
        }
    }




}
