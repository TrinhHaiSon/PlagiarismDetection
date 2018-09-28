/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package online_search;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static online_search.MyString.safeString;
import static online_search.OnlineSearch.getJSON;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author trinhhaison
 */
public class WikiPageInfo implements Serializable{
    private String url;
    private String title;
    private long pageId;
    private long type;
    private static final long serialVersionUID = 340599236730340919L;
    
    
    
    public WikiPageInfo(String title, long pageId, long type) throws IOException, ParseException {
        this.title = title;
        this.pageId = pageId;
        this.type = type;
        
        if(type == 0){
            String infoUrl = "https://vi.wikipedia.org/w/api.php?action=query&prop=info&pageids="+pageId+"&inprop=url&format=json";
            String json = getJSON(infoUrl);
            JSONObject jo = (JSONObject) new JSONParser().parse(json);
            this.url = (String)((Map)((Map)((Map)jo.get("query")).get("pages")).get(String.valueOf(pageId))).get("fullurl");
        }
        else{
            this.url = "";
        }
        
    }
    
    public String getContent() throws IOException, ParseException{
        
        if(type == 0){
            String json = getJSON("https://vi.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&titles="+URLEncoder.encode(title, "UTF-8"));
            JSONObject jo = (JSONObject) new JSONParser().parse(json);
            String htmlContent = (String)((Map)((Map)((Map)jo.get("query")).get("pages")).get(String.valueOf(pageId))).get("extract");
            Document doc = Jsoup.parse(htmlContent);
            doc.select("span.mwe-math-element").remove();
            doc.select("span.mw-editsection").remove();
            return safeString(doc.text());
        }
        else{
            return "";
        }
    
    }
    
    public static List<WikiPageInfo> getSubPages(WikiPageInfo categoryPage, Set<Long> pageIdSet) throws IOException, ParseException{
        List<WikiPageInfo> results = new LinkedList<>();
        String json = getJSON("https://vi.wikipedia.org/w/api.php?action=query&list=categorymembers&cmtitle="+URLEncoder.encode(categoryPage.getTitle(), "UTF-8")+"&cmlimit=5000&cmtype=subcat|page&format=json");
        JSONObject jo = (JSONObject) new JSONParser().parse(json);
        JSONArray subPages = (JSONArray)((Map)jo.get("query")).get("categorymembers");
        Map page;
        long pageId;
        String pageTitle;
        long pageType;
        
        for(Object obj : subPages){
            page = (Map) obj;
            pageId = (long) page.get("pageid");
//            System.out.println(pageId);
            pageTitle = (String) page.get("title");
            pageType = (long) page.get("ns");
            if((!pageIdSet.contains(pageId)) && ((pageType == 0) || (pageType == 14))){
                results.add(new WikiPageInfo(pageTitle, pageId, pageType));
                pageIdSet.add(pageId);
            }
        }
        
        return results;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WikiPageInfo other = (WikiPageInfo) obj;
        if (this.pageId != other.pageId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WikiPageInfo{" + "title=" + title + '}';
    }
    
    
    
    public String getName(){
        return title.replaceAll(" ", "_");
    }
    
    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public long getPageId() {
        return pageId;
    }

    public long getType() {
        return type;
    }
    
    public static void main(String[] args) {
        try {
//            WikiPageInfo page = new WikiPageInfo("Bảo vệ dữ liệu cá nhân", "27614", 0);
//            System.out.println(page.getContent());
//            System.out.println(page.getUrl());
            
            WikiPageInfo rootPage = new WikiPageInfo("Thể loại:Tin học", 27614, 14);
            Set<Long> pageIdSet = new HashSet<>();
            List<WikiPageInfo> pageList = getSubPages(rootPage, pageIdSet);
            for(WikiPageInfo page : pageList){
                System.out.println(page.getTitle());
                System.out.println(page.getUrl());
            }
            

//            String infoUrl = "https://vi.wikipedia.org/w/api.php?action=query&prop=info&pageids=4784&inprop=url&format=json";
//            String json = getJSON(infoUrl);
//            JSONObject jo = (JSONObject) new JSONParser().parse(json);
//            String url = (String)((Map)((Map)((Map)jo.get("query")).get("pages")).get("4784")).get("fullurl");
//            System.out.println(url);
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
