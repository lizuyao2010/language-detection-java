import java.io.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by lizuyao on 2/19/16.
 */
public class LanguageDetector {
    private List <LangProfile> langProfiles;

    public LanguageDetector(String dirName) throws IOException, ParseException {
        langProfiles = new ArrayList<LangProfile>();
        JSONParser parser = new JSONParser();
        File directory = new File(dirName);
        File[] listOfFiles = directory.listFiles();
        for (File file : listOfFiles) {
            String filePath = dirName + '/' + file.getName();
            Object obj = parser.parse(new FileReader(filePath));
            JSONObject profile = (JSONObject) obj;
            String name = (String) profile.get("name");
            HashMap<String, Long> freq = (HashMap<String, Long>) profile.get("freq");
            List<Long> n_words = (List<Long>) profile.get("n_words");
            LangProfile langProfile = new LangProfile(name, freq, n_words);
            langProfiles.add(langProfile);
        }
    }
    private List<List<String>> getFeatures(String text)
    {
        int n = text.length();
        List<List<String>> grams = new ArrayList<List<String>>();
        for (int order=1; order<4; order++) {
            List<String> gram = new ArrayList<String>();
            for (int i = 0; i + order - 1 < n; i++) {
                gram.add(text.substring(i, i + order));
            }
            grams.add(gram);
        }
        return grams;

    }
    public static void train(String folderPath, String modelPath)
    {
        final long threshold=10;
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(folderPath+"/"+listOfFiles[i].getName()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String line=null;
                JSONObject obj=new JSONObject();

                Map<String,Long> counter = new HashMap<>();
                long[] n_words=new long[3];
                try {
                    while ( (line=br.readLine()) != null) {
                        int n = line.length();
                        for (int order=1; order<4; order++) {
                            for (int j = 0; j + order - 1 < n; j++) {
                                String gram = line.substring(j, j + order);
                                if (!counter.containsKey(gram))
                                    counter.put(gram, (long) 0);
                                counter.put(gram,counter.get(gram)+1);
                            }
                        }
                    }
                    for(Iterator<Map.Entry<String, Long>> it = counter.entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry<String, Long> entry = it.next();
                        if(entry.getValue()<threshold) {
                            it.remove();
                        }
                        else
                        {
                            n_words[entry.getKey().length()-1]+=entry.getValue();
                        }
                    }
                    obj.put("freq",counter);
                    obj.put("name",listOfFiles[i].getName());
                    JSONArray n_words_json = new JSONArray();
                    for (int j=0;j<3;j++)
                        n_words_json.add(j,n_words[j]);
                    obj.put("n_words",n_words_json);
                    try (FileWriter file = new FileWriter(modelPath+"/"+listOfFiles[i].getName())) {
                        file.write(obj.toJSONString());
                        System.out.println("Successfully dumped model File "+listOfFiles[i].getName());
//                        System.out.println("\nJSON Object: " + obj);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
    }

    public String predict(String text,int order)
    {
        List<List<String>> grams=getFeatures(text);
        long vocabularySize = 0;
        for (LangProfile lp : langProfiles)
        {
            vocabularySize+=lp.getN_words().get(order-1);
        }
        //List<Pair> predictList=new ArrayList<Pair>();
        double bestScore=-Double.MAX_VALUE;
        String bestLabel="";
        for (LangProfile lp : langProfiles)
        {
            double totalScore=0.0;
            for (String feature : grams.get(order-1))
            {
                HashMap<String,Long> freq=lp.getFreq();
                if (!freq.containsKey(feature))
                    totalScore+=Math.log(1.0/vocabularySize);
                else {
                    totalScore += Math.log( (double)(freq.get(feature))/ (lp.getN_words().get(order - 1)));
                }
            }
            if (totalScore > bestScore) {
                bestScore = totalScore;
                bestLabel = lp.getName();
            }
            //predictList.add(new Pair(totalScore, lp.getName()));
        }
        //Collections.sort(predictList);
        //return predictList.get(0).getLabel();
        return bestLabel;
    }
    public static void test(String modelfile, String folderPath, int order){
        LanguageDetector languageDetector = null;
        try {
            languageDetector = new LanguageDetector(modelfile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();
        int overall_correct=0;
        int overall_total=0;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(folderPath+"/"+listOfFiles[i].getName()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String line=null;
                int correct=0;
                int total=0;
                try {
                    while ( (line=br.readLine()) != null) {
                        String predict_label = languageDetector.predict(line, order);
                        if (predict_label.equals(listOfFiles[i].getName())) {
                            correct++;
                            overall_correct++;
                        }
                        else {
//                            System.out.println(line);
//                            System.out.println(predict_label);
                        }
                        total++;
                        overall_total++;
                    }
                    System.out.println("Accuracy: "+(double)correct/total);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
        System.out.println("Total Accuracy: "+(double)overall_correct/overall_total);
    }
    public void testWithStdin(String modelPath)
    {
        LanguageDetector languageDetector = null;
        try {
            languageDetector = new LanguageDetector(modelPath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        String s;
        s = "";
        try {
            while ((line = br.readLine()) != null) {
                s += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String predict_label = languageDetector.predict(s, 2);
        System.out.println("predicted language: " + predict_label);
    }
    public static void main(String[] args) throws IOException, ParseException {
        String modelPath="nlp/model";
        String phase="test";
        String trainFolder="nlp/TrainData";
        String testFolder="nlp/TestData";
        int order=2;
        if (args.length == 0) {
            System.out.println("run default setting to train!");
        } else {
            for (int i=0;i<args.length;i++)
            {
                if (i==0)
                    phase=args[i];
                else if(i==1)
                    modelPath=args[i];
                else if(i==2) {
                    if (phase.equals("train"))
                        trainFolder = args[i];
                    else
                        testFolder = args[i];
                }
                else if(i==3 && phase.equals("test")) {
                    order=Integer.parseInt(args[i]);
                }
            }
        }
        if (phase.equals("test")) {
            try {
                test(modelPath,testFolder,order);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (phase.equals("train")){
            train(trainFolder, modelPath);
        }
    }
}
