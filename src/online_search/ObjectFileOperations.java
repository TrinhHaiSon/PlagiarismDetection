/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package online_search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author trinhhaison
 */
public class ObjectFileOperations {
    
    public static void writeObjectToFile(String pathToFile, Object obj) throws FileNotFoundException, IOException{
        FileOutputStream f = new FileOutputStream(new File(pathToFile), false);
        ObjectOutputStream o = new ObjectOutputStream(f);
        
        o.writeObject(obj);
        
        o.close();
        f.close();
    }
    
    public static Object readObjectFromFile(String pathToFile) throws FileNotFoundException, IOException, ClassNotFoundException{
        FileInputStream fi = new FileInputStream(new File(pathToFile));
        ObjectInputStream oi = new ObjectInputStream(fi);
        
        Object obj = oi.readObject();
        
        oi.close();
        fi.close();
        
        return obj;
    }
    
}
