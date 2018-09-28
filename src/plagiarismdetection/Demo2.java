/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plagiarismdetection;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author trinhhaison
 */
public class Demo2 {
    
    
    
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, InterruptedException{
        
        String docxFilePath = "/media/trinhhaison/01D22CD594D997B0/School/NLP/LVTN/17.docx";
        String txtInputPath = "/home/trinhhaison/NetBeansProjects/PlagiarismDetection/src/vnTagger-master/Demo/Input.txt";
        
//        Demo1.readDocxFile(docxFilePath, txtInputPath, true);
        
        Process p = Runtime.getRuntime().exec("src/vnTagger-master/vnTagger.sh -i src/vnTagger-master/Demo/Input.txt -o src/vnTagger-master/Demo/Output.txt");
        p.waitFor();
//        BufferedReader reader =
//            new BufferedReader(new InputStreamReader(p.getErrorStream()));
//
//        String line = "";
//        while ((line = reader.readLine())!= null) {
////                    output.append(line + "\n");
//            System.out.println(line);
//        }
        p.destroy();
        
        File inputFile = new File("/home/trinhhaison/NetBeansProjects/PlagiarismDetection/src/vnTagger-master/Demo/Output.txt");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        
        Node nNode;
        Element eElement;
        
        NodeList nList = doc.getElementsByTagName("w");
        
        for (int temp = 0; temp < nList.getLength(); temp++) {
            nNode = nList.item(temp);
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE){
                eElement = (Element) nNode;
                if(eElement.getAttribute("pos").equals("Np")){
                    System.out.println(eElement.getTextContent());
                }
            }
        } 
    }
}
