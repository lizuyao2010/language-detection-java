import java.io.*;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by lizuyao on 2/22/15.
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
    public String predict(String text,int order)
    {
        List<List<String>> grams=getFeatures(text);
        long vocabularySize = 0;
        for (LangProfile lp : langProfiles)
        {
            vocabularySize+=lp.getN_words().get(order-1);
        }
        List<Pair> predictList=new ArrayList<Pair>();
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
            predictList.add(new Pair(totalScore,lp.getName()));
        }
        Collections.sort(predictList);
        return predictList.get(0).getLabel();
    }
    public static void main(String[] args) throws IOException, ParseException {
        final String modelfile="nlp/lang"; // directory contains pretrained model
        LanguageDetector languageDetector =new LanguageDetector(modelfile);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line=null;
        String s;
        s = "";
        while ( (line=br.readLine()) != null) {
            s += line;
        }
        String predict_label= languageDetector.predict(s,2);
        System.out.println("predicted language: "+predict_label);
    }
}
