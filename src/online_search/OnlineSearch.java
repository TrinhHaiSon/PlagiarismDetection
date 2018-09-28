/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package online_search;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static online_search.MyString.safeString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import plagiarismdetection.KeywordDetection;
import static plagiarismdetection.KeywordDetection.EnvirInitialize;
import java.net.URLEncoder;

/**
 *
 * @author trinhhaison
 */
public class OnlineSearch {
    
//    public static void getPDF(String keyWord ,int limit , String folderName) throws MalformedURLException, IOException{
//       
//        keyWord = keyWord.replaceAll(" ", "+");
//        keyWord += ".html";
//        
//        String url = "https://tailieu.vn/tim-kiem/" + keyWord + "?typesearch=1&type1=3&type2=1";
//        
//        Document doc = Jsoup.parse(new URL(url), 10000);
//        
//        Elements tags = doc.select("#loaddoc>li>div>h3>a");
//        
//        
//        String storingDirPath = System.getProperty("user.dir") + "/OnlinePDF/" + folderName;
//        
//        File storingDir = new File(storingDirPath);
//        
//        storingDir.mkdir();
//        
//        String tempPageUrl;
//        String tempPDFUrl;
//        File tempFile;
//        URL tempURL;
//        int length;
//        byte[] buffer;
//        InputStream in;
//        FileOutputStream fos;
//        Elements scriptTags;
//        
//        int count = 0;
//        for(Element tag : tags){
//            
//            //System.out.println(tag.attr("href"));
//            
//            
//            
//            if(count > limit){
//                break;
//            }
//            
//            tempPageUrl = tag.attr("href");
//            doc = Jsoup.parse(new URL(tempPageUrl), 10000);
//            
//            
//            tempPDFUrl = null;
//            scriptTags = doc.select("script");
//            for(Element scriptTag : scriptTags){
//                
//                if((scriptTag.dataNodes().size() >= 1) && scriptTag.dataNodes().get(0).getWholeData().contains("loaddocdetail2")){
//                    tempPDFUrl = getPath(scriptTag.dataNodes().get(0).getWholeData());
//                }
//                
//                
//            }
//            
//            
//            if(tempPDFUrl != null){
//                
//                tempURL = new URL(tempPDFUrl);
//                in = tempURL.openStream();
//
//                tempFile = new File(storingDirPath + "/pdfFile" + count + ".pdf");
//                tempFile.createNewFile();
//
//                fos = new FileOutputStream(tempFile);
//
//                length = -1;
//                buffer = new byte[1024];// buffer for portion of data from connection
//                while ((length = in.read(buffer)) > -1) {
//                    fos.write(buffer, 0, length);
//                }
//                fos.close();
//                in.close();
//
//                System.out.println(tempPDFUrl);
//            }
//            
//            count++;
//        }
//        
//    }
    
//    public static String getPath(String script){
//        
//        int indexBegin = script.indexOf("src=\"") + 5;
//        
//        int indexEnd = indexBegin;
//        
//        char tempChar = script.charAt(indexEnd);
//        
//        while(tempChar != '"'){
//            indexEnd++;
//            tempChar = script.charAt(indexEnd);
//        }
//        
//        indexEnd--;
//        
//        return script.substring(indexBegin, indexEnd);
//        
//    }
    
    
    public static List<ThesisInfo> getLinksFromLuanvannet(int page) throws IOException{
        String url = "http://luanvan.net.vn/luan-van/cong-nghe-thong-tin/?page="+page;
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select("#documents>.box>ul>li>a");
        
        String link;
        List<ThesisInfo> result = new ArrayList<>();
        
        for(Element element : elements){
            link = "http://luanvan.net.vn"+element.attr("href");
            result.add(new ThesisInfo(link));
        }
        
        return result;
    }
    
    public static String getThesisContentFromLuanvannet(String link) throws IOException{
        String cssLocation = getCSSLocation("http://luanvan.net.vn");
        Document doc = Jsoup.connect(link).get();
        Element dataNode = doc.selectFirst(cssLocation);
        if(dataNode == null){
            return "";
        }
        
        return safeString(dataNode.text());
    }
    
    public static List<ThesisInfo> getLinksFromWikipedia() throws IOException{
        List<ThesisInfo> docList = new ArrayList<>();
        Document doc = Jsoup.connect("https://vi.wikipedia.org/wiki/Tin_h%E1%BB%8Dc").get();
        Elements elements = doc.select("td.navbox-list a");
        String link;
        Set<String> linkSet = new HashSet<>();
        for(Element element : elements){
            if(!element.className().equals("new") && !element.parent().className().equals("nv-edit")){
                link = "https://vi.wikipedia.org"+element.attr("href");
                if(!linkSet.contains(link)){
                    docList.add(new ThesisInfo(link));
                    linkSet.add(link);
                }
            }
        }
        
        elements = doc.select("th.navbox-group>a");
        for(Element element : elements){
            if(!element.className().equals("new") && !element.parent().className().equals("nv-edit")){
                link = "https://vi.wikipedia.org"+element.attr("href");
                if(!linkSet.contains(link)){
                    docList.add(new ThesisInfo(link));
                    linkSet.add(link);
                }
            }
        }
        
        return docList;
    }
    
    public static String getContentFromWikipedia(String link) throws IOException{
        String cssLocation = getCSSLocation("https://vi.wikipedia.org");
        Document doc = Jsoup.connect(link).get();
        Element dataNode = doc.selectFirst(cssLocation);
        dataNode.select("span.mwe-math-element").remove();
        dataNode.select("span.mw-editsection").remove();
        dataNode.select("table.navbox").remove();
        dataNode.select("#footer").remove();
        
        return safeString(dataNode.text());
    }
    
    public static List<String> getLinks(String keyWord ,int limit, String site) throws IOException{
        
        List<String> links = new ArrayList<>();
        
        keyWord = keyWord.trim();
        keyWord = keyWord.replaceAll(" ", "+");
        
        String url = "https://www.google.com/search?q=" + keyWord + "+site:"+site+"&num=" + limit;
        
        Document doc = Jsoup.connect(url).get();
        
        Elements results = doc.select("h3.r>a");
        
        String link;
        
        for(Element result : results){
            link = result.attr("href");
            links.add(link);
        }
        
        return links;
    }
    
    public static void writeResultToFile(String site, Element dataNode, FileWriter fileWriter) throws IOException{
        switch(site){
            case "http://doantotnghiep.vn" : {
                Elements childNodes = dataNode.children();
                for(Element childNode : childNodes){
                    fileWriter.write(childNode.text());
                    fileWriter.write("\n");
                }
                
                break;
            }
            
            case "http://doan.edu.vn" : {
                fileWriter.write(dataNode.text());
            }
            
            case "http://luanvan.net.vn" : {
                fileWriter.write(dataNode.text());
            }
            
            case "https://vi.wikipedia.org" :{
                dataNode.select("span.mwe-math-element").remove();
                dataNode.select("span.mw-editsection").remove();
                fileWriter.write(dataNode.text());
            }
        }
    }
    
    public static String getCSSLocation(String site){
        switch(site){
        	case "http://luanvan.net.vn": return "div.doc-content";
            case "http://doantotnghiep.vn": return "#doc-content>.bg-white";
            case "http://doan.edu.vn": return "div.doc-content";
            case "https://vi.wikipedia.org" : return "div.mw-parser-output";
            default: return "";
        }
    }
    
    public static void getOnlineData(String keyWord ,int limit, String storingDirPath, String site) throws IOException{
        
        List<String> links = getLinks(keyWord, limit, site);
        Document doc;
//        
        File storingDir = new File(storingDirPath);
        
        storingDir.mkdir();
        
        File tempFile;
        
        FileWriter fileWriter;
        
        
        Element dataNode;
        
        int count = 0;
        
        String cssLocation = getCSSLocation(site);
        
        for(String link : links){
            
            doc = Jsoup.connect(link).get();
            
            dataNode = doc.selectFirst(cssLocation);
            
            if(dataNode == null){
                continue;
            }
            
            tempFile = new File(storingDirPath + "/result" + count);
            
            tempFile.createNewFile();
            
            fileWriter = new FileWriter(tempFile);
            

            writeResultToFile(site, dataNode, fileWriter);
            
            fileWriter.close();
            
            count++;
            
            System.out.println(link);
        }
        
    }
    
    public static String streamToString(InputStream inputStream) {
        String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
        return text;
    }
    
    public static String getJSON(String urlStr) throws MalformedURLException, IOException{
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("charset", "utf-8");
        connection.connect();
        InputStream inStream = connection.getInputStream();
        String json = streamToString(inStream);
        return json;
    }
    
    public static void main(String[] args) {
        
           
        
            ////        System.out.println(System.getProperty("user.dir"));
//        try {
////            getPDF("hệ thống thông tin", 10, "HTTT");
//            Document doc = Jsoup.parse(new URL("https://tailieu.vn/doc/seo-flash-website-cho-google-phan-i--520165.html"), 10000);
//            Elements scriptTags = doc.select("script");
//            
////            System.out.println(scriptTags.size());
//            for(Element tag : scriptTags){
//                
//                if((tag.dataNodes().size() >= 1) && tag.dataNodes().get(0).getWholeData().contains("loaddocdetail2")){
////                    System.out.println(getPath(tag.dataNodes().get(0).getWholeData()));
//                    System.out.println(tag.dataNodes().get(0).getWholeData());
//                }
//                
////                for (DataNode node : tag.dataNodes()) {
////                    System.out.println(node.getWholeData());
////                }
////                System.out.println(tag.text());
//                
//            }
//            
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }

        try {
//            getOnlineData("học máy", 20, "HocMay", "http://doantotnghiep.vn", "#doc-content>.bg-white");
                
//            getOnlineData("học máy", 20, "HocMay", "http://doan.edu.vn", "div.doc-content");
//            getOnlineData("học máy", 20, "/home/trinhhaison/NetBeansProjects/PlagiarismDetection/OnlineData/HocMayWiki", "https://vi.wikipedia.org", "div.mw-parser-output");

//            EnvirInitialize();
//            KeywordDetection keywordDetect = new KeywordDetection("/home/trinhhaison/Documents/NPL/TomTat/25.docx.txt", 50, 10, 2, 2);
//            ArrayList<KeywordDetection.PhraseInfo> phraseList = keywordDetect.getKeyPhraseList(false);
//            
//            for(KeywordDetection.PhraseInfo phrase : phraseList){
//                 getOnlineData(phrase.getPhrase(), 20, "/home/trinhhaison/NetBeansProjects/PlagiarismDetection/OnlineData/"+phrase.getPhrase().replaceAll(" ", "_")+"_doantotnghiep", "http://doantotnghiep.vn");
//                 getOnlineData(phrase.getPhrase(), 20, "/home/trinhhaison/NetBeansProjects/PlagiarismDetection/OnlineData/"+phrase.getPhrase().replaceAll(" ", "_")+"_doan", "http://doan.edu.vn");
//                 getOnlineData(phrase.getPhrase(), 20, "/home/trinhhaison/NetBeansProjects/PlagiarismDetection/OnlineData/"+phrase.getPhrase().replaceAll(" ", "_")+"_wikipedia", "https://vi.wikipedia.org");
//            }



//            List<ThesisInfo> thesisList = getLinksFromWikipedia();
//            
//            for(ThesisInfo thesisInfo : thesisList){
//                System.out.println(thesisInfo.getLink() + " " + thesisInfo.getName());
//            }
//            
////            System.out.println(getThesisContentFromLuanvannet(thesisList.get(0).getLink()));
////            System.out.println(getContentFromWikipedia(thesisList.get(3).getLink()));
//            System.out.println(thesisList.get(3).getName());

//
//            List<ThesisInfo> docList = new ArrayList<>();
//            Document doc = Jsoup.connect("https://vi.wikipedia.org/wiki/Tin_h%E1%BB%8Dc").get();
//            Elements elements = doc.select("li.nv-edit>a");
//            System.out.println(elements.get(0).parent().className());



//            URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&list=categorymembers&cmtitle=Category:Physics&cmlimit=5000&cmtype=subcat|page&format=json");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoOutput(true);
//            connection.setInstanceFollowRedirects(false);
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("charset", "utf-8");
//            connection.connect();
//            InputStream inStream = connection.getInputStream();
//            String json = streamToString(inStream);
//            
////            System.out.println(json);
//            JSONObject jo = (JSONObject) new JSONParser().parse(json);
//            JSONArray cats = (JSONArray)((Map)jo.get("query")).get("categorymembers");
//            Map cat;
//            for(Object o : cats){
//                cat = (Map) o;
//                System.out.println(cat.get("title"));
//            }
            

            String json = getJSON("https://vi.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&titles="+URLEncoder.encode("Bảo vệ dữ liệu cá nhân", "UTF-8"));
            JSONObject jo = (JSONObject) new JSONParser().parse(json);
            String content = (String)((Map)((Map)((Map)jo.get("query")).get("pages")).get("27614")).get("extract");
            System.out.println(content);
            
//            new JSONParser().parse(string)
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
}
