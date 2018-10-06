/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocabulary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
//import opennlp.tools.tokenize.Tokenizer;
//import opennlp.tools.tokenize.TokenizerME;
//import opennlp.tools.tokenize.TokenizerModel;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import static plagiarismdetection.PlagiarismDetection.POS;
import vn.pipeline.Word;

/**
 *
 * @author trinhhaison
 */
public class FeedWordSetList extends Thread {
    
    public ArrayList<HashSet<String>> wordSetList;
//    public VietnameseMaxentTagger tagger;
    public HashSet<String> stopWords;
    public int fileType; // 1: doc; 2: docx 3:txt
    public ArrayList<File> fileList;
    
    
    
    
    
    
    public FeedWordSetList(ArrayList<HashSet<String>> wordSetList, int fileType, ArrayList<File> fileList, HashSet<String> stopWords) {
        this.wordSetList = wordSetList;
        this.fileType = fileType;
        this.fileList = fileList;
        this.stopWords = stopWords;
    }
    
    public void extractWordFromTxtFile(File file, HashSet<String> result) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr); 
        String sCurrentLine;
        List<Word> words;
        String wordTemp;
        
        while ((sCurrentLine = br.readLine()) != null){
            words = POS(sCurrentLine);
            
            for (Word word : words){
                wordTemp = DemoNPL.wordProcess(word.getForm().replaceAll("_", " "), stopWords);
                if(wordTemp != null){
                    result.add(wordTemp);
                }
            }
        }
        
        br.close();
        fr.close();
    }
    
//    public void extractWordFromDocxFile(File file, HashSet<String> result) throws FileNotFoundException, IOException{
//        
//        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
//
//        XWPFDocument document = new XWPFDocument(fis);
//
//        List<XWPFParagraph> paragraphs = document.getParagraphs();
//        
//        List<WordTag> wordTagList;
//        
//        for (XWPFParagraph para : paragraphs) {
//            
//            wordTagList = tagger.tagText2(para.getText());
//            for (WordTag wordTag : wordTagList){
//                if(wordTypes.contains(wordTag.tag()) && containLetter(wordTag.word())){
//                    result.add(wordTag.word().toLowerCase());
//                }
//            }
//        }
//        fis.close();
//    }
    
//    public void extractWordFromDocFile(File file, HashSet<String> result) throws FileNotFoundException, IOException{
//        
//        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
//
//        HWPFDocument doc = new HWPFDocument(fis);
//
//        WordExtractor we = new WordExtractor(doc);
//
//        String[] paragraphs = we.getParagraphText();
//        
//        List<WordTag> wordTagList;
//        
//        for (String para : paragraphs) {
//            
//            wordTagList = tagger.tagText2(para);
//            for (WordTag wordTag : wordTagList){
//                if(wordTypes.contains(wordTag.tag()) && containLetter(wordTag.word())){
//                    result.add(wordTag.word().toLowerCase());
//                    System.out.println(wordTag.word());
//                }
//            }
//        }
//        fis.close();
//    }
    
    @Override
    public void run(){
        
        try{
            int i = 0;
            HashSet<String> temp;
            FileReader fr;
            BufferedReader br; 
            String sCurrentLine;
            List<Word> words;
            String wordTemp;
            
            if(fileType == 3){
                for(File file : fileList){
                    
                    fr = new FileReader(file);
                    br = new BufferedReader(fr); 
                    sCurrentLine = br.readLine();
                    while ((sCurrentLine = br.readLine()) != null){
                        temp = wordSetList.get(i);
                         synchronized(temp){
                             do{
                                 System.out.println(sCurrentLine);
                                 words = POS(sCurrentLine);
                                for (Word word : words){
                                    wordTemp = DemoNPL.wordProcess(word.getForm().replaceAll("_", " "), stopWords);
                                    if(wordTemp != null){
                                        temp.add(wordTemp);
                                    }
                                }
                                
                                temp.notify();
                                System.out.println(file.getAbsolutePath() + " " + i);
                                sCurrentLine = br.readLine();
                                
                             }while((sCurrentLine != null) && (!sCurrentLine.equals("#")));
                             
                             i++;
                             
                             if(sCurrentLine == null) break;
                         }
//                        if(sCurrentLine.equals("#")){
//                            i++;
//                            while((sCurrentLine = br.readLine()) != "#"){
//                                
//                            }
//                        }
//                        else{
//                            
//                            temp = wordSetList.get(i - 1);
//                            synchronized(temp){
//
//                                words = POS(sCurrentLine);
//
//                                for (Word word : words){
//                                    wordTemp = DemoNPL.wordProcess(word.getForm().replaceAll("_", " "), stopWords);
//                                    if(wordTemp != null){
//                                        temp.add(wordTemp);
//                                    }
//                                }
//                                temp.notify();
//                                System.out.println(file.getAbsolutePath());                        
//                            }
//                            
//                        }
                        
                    }

                    br.close();
                    fr.close();
                    
                    
                }
            }
            
//            else if(fileType == 1){
//            
//                
//                for(File file : fileList){
//                    temp = wordSetList.get(i);
//                    synchronized(temp){
//                        synchronized(tagger){
//                            System.out.println(file.getAbsolutePath());
//                            extractWordFromDocFile(file, temp);
//                            temp.notify();
////                            System.out.println(file.getAbsolutePath());
//                        }
//                    }
//                    i++;
//                }
//            }
//            else if(fileType == 2){
//                
//                for(File file : fileList){
//                    temp = wordSetList.get(i);
//                    synchronized(temp){
//                        synchronized(tagger){
//                            extractWordFromDocxFile(file, temp);
//                            temp.notify();
//                            System.out.println(file.getAbsolutePath());
//                        }
//                    }
//                    i++;
//                }
//            }
        }
        catch(IOException ex){
            System.out.println(ex);
        }
        
    }
    
}
