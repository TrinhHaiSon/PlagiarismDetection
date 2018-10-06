/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocabulary;

//import static K_Means.DocumentRepresentation.compressDictionary;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
//import opennlp.tools.sentdetect.SentenceDetectorME;
//import opennlp.tools.sentdetect.SentenceModel;
//import opennlp.tools.tokenize.Tokenizer;
//import opennlp.tools.tokenize.TokenizerME;
//import opennlp.tools.tokenize.TokenizerModel;
//import opennlp.tools.util.InvalidFormatException;
//import opennlp.tools.stemmer.PorterStemmer;

/**
 *
 * @author trinhhaison
 */
public class DemoNPL {

    /**
     * @param args the command line arguments
     */
    
//    public ArrayList<HashSet<String>> docSet;
//    public ArrayList<HashSet<String>> docxSet;
    public HashMap<String, Integer> wordMap; 
    public FileCollection fileCollect;
    public HashMap<String, Double> wordIDFs;
//    public static int fileNumber = 20000;
//    public static int appearanceThreshold = 3;
    
//    public static String[] stopWordsofwordnet;
//    public static HashSet<String> stopWords;
    
//    public static HashMap<String, Double> compressDictionary(HashMap<String, Double> wordIDFs){
//        HashMap<String, Double> compression = new HashMap<>();
//        double threshold = Math.log(fileNumber / appearanceThreshold);
//        
//        for(Map.Entry<String, Double> entry : wordIDFs.entrySet()){
//            if(entry.getValue() <= threshold){
//                compression.put(entry.getKey(), entry.getValue());
//            }
//        }
//        
//        return compression;
//    }
    
    public DemoNPL() throws IOException, IOException {
        
        wordMap = new HashMap<>();
//        setStopWords();
        fileCollect = new FileCollection("/home/huong/BaoMoi2", 1);
        wordIDFs = new HashMap<>();
        
    }
    
    
    public static void setStopWords(Set stopWordSet) throws FileNotFoundException, IOException{
//        stopWords = new HashSet<>();
        File file = new File("/home/huong/JavaCode/PlagiarismDetection/vetnamese_stopwords");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr); 
        String sCurrentLine;
        while((sCurrentLine = br.readLine()) != null){
            stopWordSet.add(sCurrentLine);
        }
    }
    
//    public static void setWordIDFs(String pathToDictionary) throws FileNotFoundException, IOException, ClassNotFoundException{
//        FileInputStream fileIn = new FileInputStream(pathToDictionary);
//        ObjectInputStream in = new ObjectInputStream(fileIn);
//        wordIDFs = compressDictionary((HashMap<String, Double>) in.readObject());
//        in.close();
//        fileIn.close();
//    }
    
    public static boolean containLetter(String s){
        for (int i = 0; i < s.length(); i++){
            if (Character.isAlphabetic(s.charAt(i))){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isProperWord(String s){
        for (int i = 0; i < s.length(); i++){
            if (!Character.isLetter(s.charAt(i))){
                return false;
            }
        }
        return true;
    }
    
    public static String stripSeparateCharacter(String s){
        
        char begin = s.charAt(0);
        char end = s.charAt(s.length() - 1);
        String result = s;
        StringBuilder sb; 
        
        while ((!(Character.isAlphabetic(begin))) && (!(Character.isDigit(begin))))
            {
                sb = new StringBuilder(result);
                sb.deleteCharAt(0);
                result = sb.toString();
                begin = result.charAt(0);
            }
        
        
        while ((!(Character.isAlphabetic(end))) && (!(Character.isDigit(end))))
            {
                sb = new StringBuilder(result);
                sb.deleteCharAt(sb.length() - 1);
                result = sb.toString();
                end = result.charAt(result.length() - 1);
            }
        
        
        return result;
    }
    
    public static int findApostrophe(String s){
        char apostrophe = '\'';
        int result = s.indexOf(apostrophe);
        if (result >= 0){
            return result;
        }
        
        apostrophe = '’';
        result = s.indexOf(apostrophe);
        if (result >= 0){
            return result;
        }
        
        apostrophe = '`';
        result = s.indexOf(apostrophe);
        return result;
    }
    
    public static String removeAbbreviatedStyle(String s){
        StringBuilder sb = new StringBuilder(s);
        if (sb.length() >= 3){
            
            int apoLocation = findApostrophe(s);
            
            if(apoLocation >= 0){
                
                //remove n't
                if((sb.charAt(sb.length() - 1) == 't') && 
                    ((sb.charAt(sb.length() - 2) == '\'') || (sb.charAt(sb.length() - 2) == '’') || (sb.charAt(sb.length() - 2) == '`')) &&
                    (sb.charAt(sb.length() - 3) == 'n') &&
                    !(s.toLowerCase().equals("shan't") || s.toLowerCase().equals("shan’t") || s.toLowerCase().equals("shan`t") || s.toLowerCase().equals("won't") || s.toLowerCase().equals("won’t") || s.toLowerCase().equals("won`t"))){
                    sb.delete(sb.length() - 3, sb.length());
                }
                else{ //remove he'll, she've, it's,...
                    
                    sb.delete(apoLocation, sb.length());
                    
                }
                
            }
      
        }
        return sb.toString();
    }
    
    public static boolean isAlphabeticUppercase(char c){
        if (Character.isAlphabetic(c) && Character.isUpperCase(c)){
            return true;
        }
        return false;
    }
    
    public static boolean isAbbreviation(String s){
        int count = 0;
        if(s.length() >= 2){
            for(int i = 0; i < s.length(); i++){
                if(isAlphabeticUppercase(s.charAt(i))){
                    count++;
                    if(count == 2){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isInList(String str, ArrayList<String> list){
        for(String s : list){
            if(s.equals(str)){
                return true;
            }
        }
        return false;
    }
    
//    public static void addWordsInSourceToDB(String rootName, MySQLCon connect) throws SQLException{
//        FileCollection source = new FileCollection(rootName);
//        ArrayList<File> fileList = source.getFiles();
//        for(File file : fileList){
//            System.out.println(file.getAbsolutePath());
//            addWordsInFileToDB(file, connect);
//        }
//        connect.calculateIdf(fileList.size());
//    }
    
    
    public static File getTrainingFile(){
        String separator = System.getProperty("file.separator");
        File trainingFile = new File(DemoNPL.class.getClassLoader().getResource("Files" + separator + "en-token.bin").getFile());
        return trainingFile;
    }
    
    public static String wordProcess(String word, Set<String> stopWord){
        if(containLetter(word)){
            word = word.toLowerCase();
            if(!stopWord.contains(word)){
                return word;
            }
        }
        return null;
    }
    
//    public static String wordProcess(String word, Set<String> stopWord){
//        
//        PorterStemmer stemmer = new PorterStemmer();
//        
//        if (containLetter(word)){
//                            
//            //separate character elimination
//            word = stripSeparateCharacter(word);
//
//            //Abbreviated style detection
//            word = removeAbbreviatedStyle(word);
//            word = word.toLowerCase();
//            //Abbreviation detection
//            if (isProperWord(word) && (!stopWord.contains(word))){
////                                if(isAbbreviation(a)){
//
//                    //Stemming
//                    word = stemmer.stem(word);
//
//                    return word;
//            }
//            
//            return null;
//        }
//        
//        return null;
//    }
    
    public void createVocab() throws SQLException, InterruptedException, IOException{
        
        int fileListSize1 = fileCollect.getFiles().get(0).size();
//        int fileListSize2 = fileCollect.getFiles().get(1).size();
//        int fileListSize3 = fileCollect.getFiles().get(2).size();
        
        
        HashSet<String> stopWords1 = new HashSet<>();
        setStopWords(stopWords1);
//        HashSet<String> stopWords2 = new HashSet<>();
//        setStopWords(stopWords2);
//        HashSet<String> stopWords3 = new HashSet<>();
//        setStopWords(stopWords3);
        
        ArrayList<HashSet<String>> wordSet1 = new ArrayList<>();
        for(int i = 0; i < fileListSize1; i++){
            wordSet1.add(new HashSet<>());
        }
        
//        ArrayList<HashSet<String>> wordSet2 = new ArrayList<>();
//        for(int i = 0; i < fileListSize2; i++){
//            wordSet2.add(new HashSet<>());
//        }
//        
//        ArrayList<HashSet<String>> wordSet3 = new ArrayList<>();
//        for(int i = 0; i < fileListSize3; i++){
//            wordSet3.add(new HashSet<>());
//        }
        
        FeedWordSetList feedWordSetList1 = new FeedWordSetList(wordSet1, 3, fileCollect.getFiles().get(0), stopWords1);
//        FeedWordSetList feedWordSetList2 = new FeedWordSetList(wordSet2, 3, fileCollect.getFiles().get(1), stopWords2);
//        FeedWordSetList feedWordSetList3 = new FeedWordSetList(wordSet3, 3, fileCollect.getFiles().get(2), stopWords3);
        
        FeedWordMap feedWordMap1 = new FeedWordMap(wordMap, wordSet1);
//        FeedWordMap feedWordMap2 = new FeedWordMap(wordMap, wordSet2);
//        FeedWordMap feedWordMap3 = new FeedWordMap(wordMap, wordSet3);
        
        feedWordSetList1.start();
//        feedWordSetList2.start();
//        feedWordSetList3.start();
        
        feedWordMap1.start();
//        feedWordMap2.start();
//        feedWordMap3.start();
        
        
        feedWordSetList1.join();
//        feedWordSetList2.join();
//        feedWordSetList3.join();
        
        feedWordMap1.join();
//        feedWordMap2.join();
//        feedWordMap3.join();
        
        double tempIdf;
        for(Map.Entry<String, Integer> entry : wordMap.entrySet()){
            tempIdf = Math.log(((double)fileCollect.getCorpusSize()) / entry.getValue());
            wordIDFs.put(entry.getKey(), tempIdf);
        }
        
    }
    
    public static Object readObjectFromFile(String filePath) throws FileNotFoundException, IOException, ClassNotFoundException{
        FileInputStream fi = new FileInputStream(filePath);
        ObjectInputStream oi = new ObjectInputStream(fi);
        
        Object obj = oi.readObject();
        
        oi.close();
        fi.close();
        
        return obj;

    }
    
    public static void writeObjectToFile(String filePath, Object obj) throws FileNotFoundException, IOException{
        FileOutputStream fileOut = new FileOutputStream(filePath);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(obj);
        out.close();
        fileOut.close();
    }
    
    public void pushWordMapToFile(String filePath) throws FileNotFoundException, IOException{
//        "/home/trinhhaison/NetBeansProjects/DemoNPL/Demo/Data"
        FileOutputStream fileOut = new FileOutputStream(filePath);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(wordMap);
        out.close();
        fileOut.close();
    }
    
    public void pushWordIDFsToFile(String filePath) throws FileNotFoundException, IOException{
//        "/home/trinhhaison/NetBeansProjects/DemoNPL/Demo/Data"
        FileOutputStream fileOut = new FileOutputStream(filePath);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(wordIDFs);
        out.close();
        fileOut.close();
    }
    
    public static void main(String[] args) throws
		IOException {
        
       
            //        try {
            //        String separator = System.getProperty("file.separator");
//        String paragraph = "Hi. How are you? This is Mike.";
//        
//        DemoNPL demo = new DemoNPL();
//        File file = new File(demo.getClass().getClassLoader().getResource("Files" + separator + "en-sent.bin").getFile());
//        
//	// always start with a model, a model is learned from training data
//	InputStream is = new FileInputStream(file);
//	SentenceModel model = new SentenceModel(is);
//	SentenceDetectorME sdetector = new SentenceDetectorME(model);
// 
//	String sentences[] = sdetector.sentDetect(paragraph);
// 
//	System.out.println(sentences[0]);
//	System.out.println(sentences[1]);
//	is.close();




//        PorterStemmer stemmer = new PorterStemmer();
//        stemmer.setCurrent("originality"); //set string you need to stem
//        stemmer.stem();  //stem the word
////        stemmer.getCurrent();//get the stemmed word
//        System.out.println(stemmer.getCurrent());
//        System.out.println(stemmer.getCurrent());
//
//        String rootName = "/home/trinhhaison/Documents/NPL/20_newsgroups";
//        MySQLCon connect = new MySQLCon();
//        
//        addWordsInSourceToDB(rootName, connect);
//        
//        connect.close();
//        
//            System.out.println("done");

//        PrintWriter writer = new PrintWriter("/media/trinhhaison/01D22CD594D997B0/School/Project2/Demo/Demofile.txt", "UTF-8");
//        writer.println("The first line");
//        writer.println("The second line");
//        writer.close();

// TODO code application logic here
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(DemoNPL.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(DemoNPL.class.getName()).log(Level.SEVERE, null, ex);
//        }
        



        try {
            
            DemoNPL demo = new DemoNPL();
            demo.createVocab();
            demo.pushWordIDFsToFile("/home/huong/JavaCode/PlagiarismDetection/vietnamese_news_tf_idf");
            
        } catch (SQLException ex) {
            Logger.getLogger(DemoNPL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(DemoNPL.class.getName()).log(Level.SEVERE, null, ex);
        }


        
        



//        try {
//            
//            FileInputStream fileIn = new FileInputStream("/home/trinhhaison/NetBeansProjects/DemoNPL/Demo/Data");
//            ObjectInputStream in = new ObjectInputStream(fileIn);
//            
//            wordIDFs = new HashMap<>();
//            HashMap<String, Integer> myMap = (HashMap<String, Integer>) in.readObject();
//            in.close();
//            fileIn.close();
//            
//            for(Map.Entry<String, Integer> entry : myMap.entrySet()){
////                System.out.println(":"+entry.getKey() + "   " + entry.getValue());
//                wordIDFs.put(entry.getKey(), new Double(Math.log(20000 / entry.getValue())));
//            }
//            
//            FileOutputStream fileOut = new FileOutputStream("/home/trinhhaison/NetBeansProjects/DemoNPL/Demo/Data2");
//            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            out.writeObject(wordIDFs);
//            out.close();
//            fileOut.close();
//            
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Demo4.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(Demo4.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(Demo4.class.getName()).log(Level.SEVERE, null, ex);
//        }





//        HashSet<String> stopWords = new HashSet<>(Arrays.asList(stopWordsofwordnet));
//        String word = wordProcess("'willing", stopWords);
//        System.out.println(word);

//        HashSet<String> stopWords1 = new HashSet<>(Arrays.asList(stopWordsofwordnet));
//        HashSet<String> stopWords2 = new HashSet<>(Arrays.asList(stopWordsofwordnet));
//        HashSet<String> stopWords3 = new HashSet<>(Arrays.asList(stopWordsofwordnet));
//        
        
        
//        try {
//            
//            FileInputStream fileIn = new FileInputStream("/home/trinhhaison/NetBeansProjects/DemoNPL/Demo/Data2");
//            ObjectInputStream in = new ObjectInputStream(fileIn);
//            
//            wordIDFs = (HashMap<String, Double>) in.readObject();
//            in.close();
//            fileIn.close();
//            
//            for(Map.Entry<String, Double> entry : wordIDFs.entrySet()){
//                System.out.println(":"+entry.getKey() + "   " + entry.getValue());
//            }
//            
//           
//        } catch (Exception ex) {
//           ex.printStackTrace();
//        }
//        
//        System.out.println(Math.exp(2));
    }
    
    
}
