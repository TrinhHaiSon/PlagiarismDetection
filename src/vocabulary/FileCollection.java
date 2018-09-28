/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocabulary;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author trinhhaison
 */
public class FileCollection {
    private ArrayList<ArrayList<File>> files;
    private int corpusSize;
    
    public FileCollection(String pathToRoot, int listNumber){
        
        ArrayList<File> fileList = new ArrayList<>();
        
        File root = new File(pathToRoot);
        ArrayList<File> queue = new ArrayList<>();
        queue.add(root);
        files = new ArrayList<>();
        dirPassingRecursive(queue, fileList);
        corpusSize = fileList.size();
        
        int sizeOfEachList = corpusSize / listNumber;
        int surplus = corpusSize % listNumber;
        
        ArrayList<File> temp;
        for(int i = 0; i < listNumber; i++){
            temp = new ArrayList<>();
            for(int j = i * sizeOfEachList; j < (i+1) * sizeOfEachList; j++){
                temp.add(fileList.get(j));
            }
            files.add(temp);
        }
        
        if(surplus > 0){
            int a = listNumber * sizeOfEachList;
            for(int i = a; i < a + surplus; i++){
                files.get(listNumber - 1).add(fileList.get(i));
            }
        }
        
    }
    
    private void dirPassingRecursive(ArrayList<File> queue, ArrayList<File> result){
        
        while(queue.size() > 0){
            File currentNode = queue.remove(0);
            if(currentNode.isFile() == true){
                result.add(currentNode);
            }
            else{
                queue.addAll(0, Arrays.asList(currentNode.listFiles()));
            }
        }
        
    }
    
//    public void printFileNames(){
//        for (File file : files){
//            System.out.println(file.getAbsolutePath());
//        }
//    }
//    
//    public void printNumberOfFiles(){
//        System.out.println(files.size());
//    }
//
//    public ArrayList<File> getFiles() {
//        return files;
//    }

    public ArrayList<ArrayList<File>> getFiles() {
        return files;
    }

    public int getCorpusSize() {
        return corpusSize;
    }
    
    
}
