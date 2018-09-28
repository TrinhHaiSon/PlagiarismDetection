/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocabulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author trinhhaison
 */
public class FeedWordMap extends Thread {
    public HashMap<String, Integer> wordMap;
    public ArrayList<HashSet<String>> wordSetList;

    public FeedWordMap(HashMap<String, Integer> wordMap, ArrayList<HashSet<String>> wordSetList) {
        this.wordMap = wordMap;
        this.wordSetList = wordSetList;
    }
    
    
    
    
    @Override
    public void run(){
        
        int setListSize = wordSetList.size();
        HashSet<String> setTemp;
        Iterator<String> ite;
        String stringTemp;
        int count; 
        try{
            
            for(int i = 0; i < setListSize; i++){
                setTemp = wordSetList.get(i);
                synchronized(setTemp){
                    while(setTemp.size() == 0){
                        setTemp.wait();
                    }
                    
                    ite = setTemp.iterator();
                    while(ite.hasNext()){
                        stringTemp = ite.next();
                        synchronized(wordMap){
                            if(wordMap.containsKey(stringTemp)){
                                count = wordMap.get(stringTemp);
                                wordMap.put(stringTemp, count + 1);
                            }
                            else{
                                wordMap.put(stringTemp, new Integer(1));
                            }
                        }
                    }
                }    
                
            }
            
        }
        catch (InterruptedException ex){
            System.err.println(ex);
        }
    }
}
