/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plagiarismdetection;

//import edu.stanford.nlp.ling.WordTag;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.poi.*;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.hwpf.HWPFDocument;
//import org.apache.poi.hwpf.extractor.WordExtractor;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import static plagiarismdetection.KeywordDetection.stopWords2;
//import vn.hus.nlp.tagger.VietnameseMaxentTagger;
import vn.pipeline.*;

/**
 *
 * @author trinhhaison
 */
public class PlagiarismDetection {
    
    /**
     * @param args the command line arguments
     */
    
    public ArrayList<HashSet<String>> docSet;
    public ArrayList<HashSet<String>> docxSet;
    public ArrayList<HashSet<String>> textSet;
    public HashMap<String, Integer> wordMap; 
    public FileCollection fileCollect;
    
    public PlagiarismDetection(String pathToSource) {
        docSet = new ArrayList<>();
        docxSet = new ArrayList<>();
        textSet = new ArrayList<>();
        wordMap = new HashMap<>();
        fileCollect = new FileCollection(pathToSource);
    }
    
    
    
    public static boolean containLetter(String s){
        for (int i = 0; i < s.length(); i++){
            if (Character.isAlphabetic(s.charAt(i))){
                return true;
            }
        }
        return false;
    }
    
//    public void createVocabulary3() throws InterruptedException{
//        ArrayList<File> textFileList = fileCollect.getTextFiles();
//        int textFileListSize = textFileList.size();
//        
//        for (int i = 0; i < textFileListSize; i++){
//            textSet.add(new HashSet<>());
//        }
//        
//        HashSet<String> wordTypes = new HashSet<>();
//        wordTypes.add("Np");
//        wordTypes.add("Nu");
//        wordTypes.add("N");
//        wordTypes.add("V");
//        wordTypes.add("A");
//        wordTypes.add("R");
//        wordTypes.add("Y");
//        
//        FeedWordSetList feedWordSetListThread1 = new FeedWordSetList(textSet, wordTypes, 3, textFileList);
//        FeedWordMap feedWordMapThread1 = new FeedWordMap(wordMap, textSet);
//        
//        feedWordSetListThread1.start();
//        feedWordMapThread1.start();
//        
//        feedWordSetListThread1.join();
//        feedWordMapThread1.join();
//    }
    
//    public void createVocabulary() throws InterruptedException{
//        
//        ArrayList<File> docFileList = fileCollect.getDocFiles();
//        ArrayList<File> docxFileList = fileCollect.getDocxFiles();
//        
//        int docFileListSize = docFileList.size();
//        int docxFileListSize = docxFileList.size();
//        
//        for (int i = 0; i < docFileListSize; i++){
//            docSet.add(new HashSet<>());
//        }
//        
//        for (int i = 0; i < docxFileListSize; i++){
//            docxSet.add(new HashSet<>());
//        }
//        
////        VietnameseMaxentTagger tagger = new VietnameseMaxentTagger();
////        VietnameseMaxentTagger tagger2 = new VietnameseMaxentTagger();
//        
//        HashSet<String> wordTypes = new HashSet<>();
//        wordTypes.add("Np");
//        wordTypes.add("Nu");
//        wordTypes.add("N");
//        wordTypes.add("V");
//        wordTypes.add("A");
//        wordTypes.add("R");
//        wordTypes.add("Y");
//        
//        
//        FeedWordSetList feedWordSetListThread1 = new FeedWordSetList(docSet, wordTypes, 1, docFileList);
//        FeedWordMap feedWordMapThread1 = new FeedWordMap(wordMap, docSet);
//        
//        FeedWordSetList feedWordSetListThread2 = new FeedWordSetList(docxSet, wordTypes, 2, docxFileList);
//        FeedWordMap feedWordMapThread2 = new FeedWordMap(wordMap, docxSet);
//        
//        feedWordSetListThread1.start();
//        feedWordMapThread1.start();
//        
//        feedWordSetListThread1.join();
//        feedWordMapThread1.join();
//        
//        
//        
//        feedWordSetListThread2.start();
//        feedWordMapThread2.start();
//        
//        feedWordSetListThread2.join();
//        feedWordMapThread2.join();
//    }
    
//    public void createVocabulary2() throws IOException, InterruptedException, ParserConfigurationException, SAXException{
//        ArrayList<File> docFileList = fileCollect.getDocFiles();
//        ArrayList<File> docxFileList = fileCollect.getDocxFiles();
//        
//        int docFileListSize = docFileList.size();
//        int docxFileListSize = docxFileList.size();
//        
//        HashSet<String> wordTypes = new HashSet<>();
//        wordTypes.add("Np");
//        wordTypes.add("Nu");
//        wordTypes.add("N");
//        wordTypes.add("V");
//        wordTypes.add("A");
//        wordTypes.add("R");
//        wordTypes.add("Y");
//        
//        HashSet<String> setTemp;
//        
//        for (File docFile : docFileList){
//            setTemp = new HashSet<>();
//            docSet.add(setTemp);
//            
//            extractWordFromFile(docFile.getAbsolutePath(), wordTypes, setTemp, 1);
//            
//            
//        }
//        
//        for (File docxFile : docxFileList){
//            setTemp = new HashSet<>();
//            docSet.add(setTemp);
//            
//            extractWordFromFile(docxFile.getAbsolutePath(), wordTypes, setTemp, 2);
//        }
//        
//        
//        
//        
//    }
//    
    public void pushToDataBase(MySQLCon connect) throws SQLException{
        
        String word;
        int idf;
        
        for (Map.Entry<String, Integer> entry : wordMap.entrySet()){
            word = entry.getKey();
            idf = entry.getValue();
            connect.addWord(word, idf);
            
        }
        
        connect.calculateIdf(fileCollect.getDocFiles().size() + fileCollect.getDocxFiles().size());           
        connect.close();
    }
    
//    public void extractWordFromFile(String pathToFile, HashSet<String> wordTypes, HashSet<String> wordSet, int fileType) throws IOException, InterruptedException, ParserConfigurationException, SAXException{
//        
//        if(fileType == 1){ //doc
//            Demo1.readDocFile(pathToFile, "/home/trinhhaison/NetBeansProjects/PlagiarismDetection/src/vnTagger-master/Demo/Input.txt", true);
//        }
//        
//        else if(fileType == 2){ // docx
//            Demo1.readDocxFile(pathToFile, "/home/trinhhaison/NetBeansProjects/PlagiarismDetection/src/vnTagger-master/Demo/Input.txt", true);
//        }
//        
//        
//        
//        Process p = Runtime.getRuntime().exec("src/vnTagger-master/vnTagger.sh -i src/vnTagger-master/Demo/Input.txt -o src/vnTagger-master/Demo/Output.txt");
//        p.waitFor();
//        
//        p.destroy();
//        
//        File inputFile = new File("/home/trinhhaison/NetBeansProjects/PlagiarismDetection/src/vnTagger-master/Demo/Output.txt");
//        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//        Document doc = dBuilder.parse(inputFile);
//        doc.getDocumentElement().normalize();
//        
//        Node nNode;
//        Element eElement;
//        String stringTemp;
//        
//        NodeList nList = doc.getElementsByTagName("w");
//        
//        for (int temp = 0; temp < nList.getLength(); temp++) {
//            nNode = nList.item(temp);
//            
//            if (nNode.getNodeType() == Node.ELEMENT_NODE){
//                eElement = (Element) nNode;
//                if(wordTypes.contains(eElement.getAttribute("pos"))){
////                    System.out.println(eElement.getTextContent());
//                    stringTemp = eElement.getTextContent();
//                    if(containLetter(stringTemp)){
//                        wordSet.add(stringTemp.toLowerCase());
//                    }
//                    
//                }
//            }
//        } 
//    }
    
//    public static List<WordTag> extractWord(String a){
//        return (new VietnameseMaxentTagger()).tagText2(a);
//    }
    
    
    
    public static List<Word> POS(String a) throws IOException{
        String[] annotators = {"wseg", "pos"};
        VnCoreNLP pipeline = new VnCoreNLP(annotators);
        Annotation annotation = new Annotation(a);
        pipeline.annotate(annotation); 
        return annotation.getWords();
    }
    
//    public static List<WordTag> wordFilter(String str){
////        String temp = str.replaceAll("[,_;!?:-]", " ");
////        String temp = str.replaceAll("\\.", "\\. ");
//        String temp = str.replaceAll("[^0-9a-zA-ZÀàÁáẢảÃãẠạĂăẮắằắẲẳẴẵẶặÂâẦầẤấẨẩẪẫẬậĐđÈèÉéẺẻẼẽẸẹÊêỀềẾếỂểỄễỆệìÌíÍỉỈĩĨịỊÒòÓóỎỏÕõỌọÔôỒồỐốỔổỖỗỘộƠơỜờỚớỞởỠỡỢợÙùÚúỦủŨũỤụƯưỪừỨứỬửỮữỰựỲỳÝýỶỷỸỹỴỵ.\t \n]","");
//        temp = temp.toLowerCase();
//        List<WordTag> wordTagList = PlagiarismDetection.extractWord(temp);
//        return wordTagList;
//    }
    
   
    public void pushToFile(String filePath) throws FileNotFoundException, IOException{
//        "/home/trinhhaison/NetBeansProjects/DemoNPL/Demo/Data"
        FileOutputStream fileOut = new FileOutputStream(filePath);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(wordMap);
        out.close();
        fileOut.close();
    }
    
    public static String readPDFfile(String fileName) throws IOException{
        try (PDDocument document = PDDocument.load(new File(fileName))) {

            document.getClass();

            if (!document.isEncrypted()) {
			
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                PDFTextStripper tStripper = new PDFTextStripper();

                String pdfFileInText = tStripper.getText(document);
//                System.out.println(pdfFileInText);
                return pdfFileInText;

            }

        }
        return "";
    }
    
    public static String readDocxFile(String fileName) throws FileNotFoundException, InvalidFormatException, IOException {
            FileInputStream fis = new FileInputStream(fileName);
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
            XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
//            xdoc.getParagraphs().get(0).
            return extractor.getText();
    }
    
    public static String readDocFile(String fileName) throws FileNotFoundException, IOException, IOException{
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            HWPFDocument document = new HWPFDocument(fis);
            WordExtractor extractor = new WordExtractor(document);
//            xdoc.getParagraphs().get(0).
            return extractor.getText();
    }
    
    public static String getFileExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, InterruptedException {
//        try {
//            // TODO code application logic here
////        FileCollection fileCollect = new FileCollection("/media/trinhhaison/01D22CD594D997B0/School/NLP/LVTN");
////        ArrayList<File> docFileList = fileCollect.getDocFiles();
////        ArrayList<File> docxFileList = fileCollect.getDocxFiles();
//


//            String pathToSource = "/home/trinhhaison/Documents/NPL/TomTat";
////            String pathToSource = "/media/trinhhaison/01D22CD594D997B0/School/NLP/LVTN";
//
//            PlagiarismDetection detect = new PlagiarismDetection(pathToSource);
//
//            detect.createVocabulary3();
//            
//            detect.pushToFile("/home/trinhhaison/NetBeansProjects/PlagiarismDetection/Data3");
      



//            detect.pushToDataBase(new MySQLCon());
//            MySQLCon connect = new MySQLCon();
//            String word;
//            int idf;
//            for (Map.Entry<String, Integer> entry : detect.wordMap.entrySet()){
//                word = entry.getKey();
//                idf = entry.getValue();
//                connect.addWord(word, idf);
//            }
//            connect.close();
//        int docFileListSize = docFileList.size();
//        int docxFileListSize = docxFileList.size();
//        
//        for (int i = 0; i < docFileListSize; i++){
//            docSet.add(new HashSet<>());
//        }
        
//        fileCollect.printFileNames();
//        fileCollect.printNumberOfFiles();
//        } catch (InterruptedException ex) {
//            Logger.getLogger(PlagiarismDetection.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(PlagiarismDetection.class.getName()).log(Level.SEVERE, null, ex);
//        }

//        String a = "Trong thông cáo vừa phát đi chiều 13/3, Bộ Thông tin & Truyền thông cho biết đã chấp thuận chủ trương cho phép MobiFone đàm phán chấm dứt hợp đồng mua 95% cổ phần AVG. ";
//        List<WordTag> wordTagList2 = extractWord(a);
//        for (WordTag wordTag : wordTagList2){
//            System.out.println(wordTag.word() + "  " + wordTag.tag());
//        }







//        FileInputStream fis = new FileInputStream("/media/trinhhaison/01D22CD594D997B0/School/NLP/LVTN/100.doc");
//
//        HWPFDocument doc = new HWPFDocument(fis);
//
//        WordExtractor we = new WordExtractor(doc);
//
//        String[] paragraphs = we.getParagraphText();
//        
//        List<WordTag> wordTagList;
//        
//        HashSet<String> mySet = new HashSet<>();
//        HashSet<String> wordTypes = new HashSet<>();
//        wordTypes.add("Np");
//        wordTypes.add("Nu");
//        wordTypes.add("N");
//        wordTypes.add("V");
//        wordTypes.add("A");
//        wordTypes.add("R");
//        wordTypes.add("Y");
//        
//        for (String para : paragraphs) {
//            
//            para = para.replaceAll("[^0-9.,;!?:a-zA-ZÀàÁáẢảÃãẠạĂăẮằắẲẳẴẵẶặÂâẦầẮắẨẩẴẵẬậĐđÈèÉéẺẻẼẽẸẹÊêỀềẾếỂểỄễỆệìÌíÍỉỈĩĨịỊÒòÓóỎỏÕõỌọÔôỒồỐốỔổỖỗỘộƠơỜờỚớỞởỠỡỢợÙùÚúỦủŨũỤụƯưỪừỨứỬửỮữỰựỲỳÝýỶỷỸỹỴỵ\t ]","");
//            wordTagList = PlagiarismDetection.extractWord(para);
////            System.out.println();
//            for (WordTag wordTag : wordTagList){
//                if(wordTypes.contains(wordTag.tag()) && containLetter(wordTag.word())){
////                    result.add(wordTag.word().toLowerCase());
//                    System.out.println(wordTag.word());
//                    mySet.add(wordTag.word());
//                }
////                System.out.println(wordTag.word());
////                mySet.add(wordTag.word());
//            }
//        }
//        fis.close();
//        System.out.println(mySet.size());
//        
//        FileOutputStream fileOut = new FileOutputStream("/home/trinhhaison/NetBeansProjects/PlagiarismDetection/Data");
//        ObjectOutputStream out = new ObjectOutputStream(fileOut);
//        out.writeObject(mySet);
//        out.close();
//        fileOut.close();
        



        

//        List<WordTag> wordTagList;
//        
//        HashSet<String> mySet = new HashSet<>();
//        HashSet<String> wordTypes = new HashSet<>();
//        wordTypes.add("Np");
//        wordTypes.add("Nu");
//        wordTypes.add("N");
//        wordTypes.add("V");
//        wordTypes.add("A");
//        wordTypes.add("R");
//        wordTypes.add("Y");
//        FileInputStream fis = new FileInputStream("/media/trinhhaison/01D22CD594D997B0/School/NLP/LVTN/1.docx");
//
//        XWPFDocument document = new XWPFDocument(fis);
//
//        List<XWPFParagraph> paragraphs = document.getParagraphs();
//        
//        
//        String paraText;
//        for (XWPFParagraph para : paragraphs) {
//            
//            paraText = para.getText().replaceAll("[^0-9.,;!?:a-zA-ZÀàÁáẢảÃãẠạĂăẮằắẲẳẴẵẶặÂâẦầẮắẨẩẴẵẬậĐđÈèÉéẺẻẼẽẸẹÊêỀềẾếỂểỄễỆệìÌíÍỉỈĩĨịỊÒòÓóỎỏÕõỌọÔôỒồỐốỔổỖỗỘộƠơỜờỚớỞởỠỡỢợÙùÚúỦủŨũỤụƯưỪừỨứỬửỮữỰựỲỳÝýỶỷỸỹỴỵ\t ]","");
//            wordTagList = PlagiarismDetection.extractWord(paraText);
//            for (WordTag wordTag : wordTagList){
//                if(wordTypes.contains(wordTag.tag()) && containLetter(wordTag.word())){
//                    System.out.println(wordTag.word());
//                    mySet.add(wordTag.word());
//                }
//            }
//        }
//        fis.close();
//        
//        FileOutputStream fileOut = new FileOutputStream("/home/trinhhaison/NetBeansProjects/PlagiarismDetection/Data");
//        ObjectOutputStream out = new ObjectOutputStream(fileOut);
//        out.writeObject(mySet);
//        out.close();
//        fileOut.close();
        
        


//        FileInputStream fileIn = new FileInputStream("/home/trinhhaison/NetBeansProjects/PlagiarismDetection/Data");
//        ObjectInputStream in = new ObjectInputStream(fileIn);
//        HashSet<String> mySet = (HashSet<String>) in.readObject();
//        in.close();
//        fileIn.close();
//
//        for(String a : mySet){
//            System.out.println(a);
//        }
        
//        HashMap<String, Double> wordIDF = new HashMap<>();
//        
//        FileInputStream fileIn = new FileInputStream("/home/trinhhaison/NetBeansProjects/PlagiarismDetection/Data");
//        ObjectInputStream in = new ObjectInputStream(fileIn);
//        HashMap<String, Integer> myMap = (HashMap<String, Integer>) in.readObject();
//        in.close();
//        fileIn.close();
//
//        for(Map.Entry<String, Integer> entry : myMap.entrySet()){
////                System.out.println(entry.getKey() + "   " + entry.getValue());
//                wordIDF.put(entry.getKey(), Math.log(11 / entry.getValue()));
//        }
//        
//        FileOutputStream fileOut = new FileOutputStream("/home/trinhhaison/NetBeansProjects/PlagiarismDetection/Data2");
//        ObjectOutputStream out = new ObjectOutputStream(fileOut);
//        out.writeObject(wordIDF);
//        out.close();
//        fileOut.close();
        
//        System.out.println(myMap.size());
        
        


//        HashMap<String, Integer> words = new HashMap<>();
//        FileReader fr = new FileReader("/home/trinhhaison/Downloads/dic.txt");
//        BufferedReader br = new BufferedReader(fr); 
//        String sCurrentLine;
//        String[] temp;
//        
//        while ((sCurrentLine = br.readLine()) != null){
//            temp = sCurrentLine.split("-");
//            words.put(temp[0], Integer.valueOf(temp[1]));
//        }
        
//        for(Map.Entry<String, Integer> entry : words.entrySet()){
//            System.out.println(entry.getKey() + "  " + entry.getValue());
//        }

//        FileOutputStream fileOut = new FileOutputStream("/home/trinhhaison/NetBeansProjects/PlagiarismDetection/Data");
//        ObjectOutputStream out = new ObjectOutputStream(fileOut);
//        out.writeObject(words);
//        out.close();
//        fileOut.close();
//        
//        
//        HashMap<String, Double> wordIDF = new HashMap<>();
//        
//        for(Map.Entry<String, Integer> entry : words.entrySet()){
//            wordIDF.put(entry.getKey(), Math.log(346 / entry.getValue()));
//        }
//        
//        FileOutputStream fileOut2 = new FileOutputStream("/home/trinhhaison/NetBeansProjects/PlagiarismDetection/Data2");
//        ObjectOutputStream out2 = new ObjectOutputStream(fileOut2);
//        out2.writeObject(wordIDF);
//        out2.close();
//        fileOut2.close();

//        String str = "Sau thông tin từ Trung Quốc, giá vàng đã tăng thêm 0,5%, lên sát 1.340 USD một ounce, đồng NDT mất 0,4% tại Hong Kong, yen Nhật bắt đầu tăng giá, còn giá đậu nành giao tháng 5 lao dốc thẳng đứng. Hang Seng Index trên sàn chứng khoán Hong Kong hiện mất 2,2%. Các chỉ số tương lai của Wall Street cũng đi xuống.";
//        List<WordTag> wordTagList = wordFilter(str);
//        for(WordTag wordTag : wordTagList){
//            System.out.println(wordTag.value() + "  " + wordTag.tag());
//        }

//        String a = "AB c_d E_f gH";
//        a = a.toLowerCase();
//        a = a.replaceAll("_", " ");
//        System.out.println(a);

        String a = "xin chào, tôi là Sơn. Tôi sống ở thị xã Bắc Giang (trước là Hà Bắc)\n Tôi có một tình yêu: vật lý";
        List<Word> wordList = POS(a);
        String temp;
        for(Word word : wordList){
            temp = word.getForm().replaceAll("_", " ");
            System.out.println(temp + ": " + word.getPosTag());
        }
        


//        FileInputStream fileIn = new FileInputStream("/home/trinhhaison/NetBeansProjects/PlagiarismDetection/StopWords2");
//        ObjectInputStream in = new ObjectInputStream(fileIn);
//        stopWords2 = (HashSet<String>) in.readObject();
//        fileIn.close();
//        in.close();
//
//        File file = new File("/media/trinhhaison/01D22CD594D997B0/School/Linux/NPL/TomTat/23.docx.txt");
//        ArrayList<String> lines = FileCollection.readtxtFile(file);
//        List<Word> wordList;
//        String temp;
//        for(String line : lines){
//            wordList = POS(line);
//            for(Word word : wordList){
//                temp = word.getForm().replaceAll("_", " ");
//                if(!stopWords2.contains(temp)){
//                    System.out.println(temp + ": " + word.getPosTag());
//                }
//            }
//        }
        
        
//        FileCollection fileCollect = new FileCollection("/home/trinhhaison/Documents/NPL/TomTat");
//        for(File file : fileCollect.getTextFiles()){
//            System.out.println(file.getAbsolutePath());
//        }
//
//        try{
//            ArrayList<String> list = new ArrayList<>();
//            System.out.println(list.get(1));
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            System.err.println(e.getCause());
//        }

//        readDocFile("/media/trinhhaison/01D22CD594D997B0/School/NLP/LVTN/16.doc");
        
    }
    
}
