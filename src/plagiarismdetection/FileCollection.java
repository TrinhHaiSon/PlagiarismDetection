/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plagiarismdetection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author trinhhaison
 */
public class FileCollection {
    private ArrayList<File> docxFiles;
    private ArrayList<File> docFiles;
    private ArrayList<File> textFiles;
    
    public FileCollection(String pathToRoot){
        
        File root = new File(pathToRoot);
        ArrayList<File> queue = new ArrayList<>();
        queue.add(root);
        docxFiles = new ArrayList<>();
        docFiles = new ArrayList<>();
        textFiles = new ArrayList<>();
        dirPassingRecursive(queue, docxFiles, docFiles, textFiles);
    }
    
    public static ArrayList<String> readtxtFile(File file) throws FileNotFoundException, IOException{
        ArrayList<String> result = new ArrayList<>();
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr); 
        String sCurrentLine;
        
        while ((sCurrentLine = br.readLine()) != null){
            result.add(sCurrentLine);
        }
        
        fr.close();
        br.close();
        
        return result;
    }
    
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
    
    private void dirPassingRecursive(List<File> queue, List<File> docxFiles, List<File> docFiles, List<File> textFiles){
        
        while(queue.size() > 0){
            File currentNode = queue.remove(0);
            if(currentNode.isFile() == true){
                
                if(getFileExtension(currentNode).equals("docx")){
                    docxFiles.add(currentNode);
                }
                else if(getFileExtension(currentNode).equals("doc")){
                    docFiles.add(currentNode);
                }
                else{
                    textFiles.add(currentNode);
                }
//                result.add(currentNode);
            }
            else{
                queue.addAll(0, Arrays.asList(currentNode.listFiles()));
            }
        }
        
    }
    
    public static void dirPassingRecursive(List<File> queue, List<File> textFiles){
        
        while(queue.size() > 0){
            File currentNode = queue.remove(0);
            if(currentNode.isFile() == true){               
                    textFiles.add(currentNode);
//                result.add(currentNode);
            }
            else{
                queue.addAll(0, Arrays.asList(currentNode.listFiles()));
            }
        }
        
    }
    
    
    public void printFileNames(){
        System.out.println("DocxFile: ");
        for (File file : docxFiles){
            System.out.println(file.getAbsolutePath());
        }
        System.out.println("DocFile: ");
        for (File file : docFiles){
            System.out.println(file.getAbsolutePath());
        }
    }
    
    public void printNumberOfFiles(){
        System.out.println("DocFile: "+docFiles.size());
        System.out.println("DocxFile: "+docxFiles.size());
    }

    public ArrayList<File> getDocxFiles() {
        return docxFiles;
    }

    public ArrayList<File> getDocFiles() {
        return docFiles;
    }

    public ArrayList<File> getTextFiles() {
        return textFiles;
    }

    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File root = new File("/media/trinhhaison/01D22CD594D997B0/School/NLP/BaoMoi/BaoMoi");
        List<File> baomoiFiles = new ArrayList<>();
        List<File> queu = new ArrayList<>();
        queu.add(root);
        dirPassingRecursive(queu, baomoiFiles);
        FileReader fr;
        BufferedReader br; 
        String sCurrentLine;
        int i = 0;
        File tempFile;
        PrintWriter writer;
        for(File file : baomoiFiles){
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            sCurrentLine = br.readLine();
            
            while ((sCurrentLine = br.readLine()) != null){
                tempFile = new File("/media/trinhhaison/01D22CD594D997B0/School/NLP/BaoMoi/BaoMoi2/" + i + ".txt");
                tempFile.createNewFile();
                writer = new PrintWriter(tempFile, "UTF-8");
                do{
                    writer.println(sCurrentLine);
                    sCurrentLine = br.readLine();
                }while((sCurrentLine != null) && (!sCurrentLine.equals("#")));
                writer.close();
                i++;
                System.out.println(tempFile.getAbsolutePath());
                if(sCurrentLine == null){
                    break;
                }
            }
            br.close();
            fr.close();
        }
    }
    
}
