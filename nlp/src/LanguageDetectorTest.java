
import org.junit.Test;
import java.io.*;


public class LanguageDetectorTest {

//    @Test
//    public void testPredictLang() throws Exception {
//        final String modelfile="nlp/lang"; // directory contains pretrained model
//        LanguageDetector languageDetector =new LanguageDetector(modelfile);
//        BufferedReader br = new BufferedReader(new FileReader("nlp/test/europarl.test"));
//        String line=null;
//        int correct=0;
//        int total=0;
//        while ( (line=br.readLine()) != null) {
//            String[] example = line.trim().split("\t");
//            String predict_label = languageDetector.predict(example[1], 2);
//            if (predict_label.equals(example[0]))
//                    correct++;
//            total++;
//        }
//        System.out.println("Accuracy: "+(double)correct/total);
//    }
//    @Test
//    public void testPredictModel() throws Exception {
//        final String modelfile="nlp/model"; // directory contains pretrained model
//        LanguageDetector languageDetector =new LanguageDetector(modelfile);
//        String folderPath="nlp/testData";
//        File folder = new File(folderPath);
//        File[] listOfFiles = folder.listFiles();
//
//        for (int i = 0; i < listOfFiles.length; i++) {
//            if (listOfFiles[i].isFile()) {
//                System.out.println("File " + listOfFiles[i].getName());
//                BufferedReader br = null;
//                try {
//                    br = new BufferedReader(new FileReader(folderPath+"/"+listOfFiles[i].getName()));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                String line=null;
//                int correct=0;
//                int total=0;
//                try {
//                    while ( (line=br.readLine()) != null) {
//                        String predict_label = languageDetector.predict(line, 2);
//                        if (predict_label.equals(listOfFiles[i].getName()))
//                            correct++;
//                        else {
//                            System.out.println(line);
//                            System.out.println(predict_label);
//                        }
//                        total++;
//                    }
//                    System.out.println("Accuracy: "+(double)correct/total);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else if (listOfFiles[i].isDirectory()) {
//                System.out.println("Directory " + listOfFiles[i].getName());
//            }
//        }
//    }
}