/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package online_search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static online_search.OnlineSearch.getJSON;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tree_structure.Node;

/**
 *
 * @author trinhhaison
 */
public class WikiPageNode extends Node<WikiPageInfo>{

    public WikiPageNode(WikiPageInfo data, Node<WikiPageInfo> parent) {
        super(data, parent);
    }

    @Override
    public boolean isLeaf() {
        if(data.getType() == 0){
            return true;
        }
        return false;
    }

    @Override
    public List<Node<WikiPageInfo>> listChildren(Set<WikiPageInfo> existNode) {
        List<Node<WikiPageInfo>> results = new LinkedList<>();
        try {
            String json = getJSON("https://vi.wikipedia.org/w/api.php?action=query&list=categorymembers&cmtitle="+URLEncoder.encode(data.getTitle(), "UTF-8")+"&cmlimit=5000&cmtype=subcat|page&format=json");
            JSONObject jo = (JSONObject) new JSONParser().parse(json);
            JSONArray subPages = (JSONArray)((Map)jo.get("query")).get("categorymembers");
            Map page;
            long pageId;
            String pageTitle;
            long pageType;
            WikiPageInfo tempPage;
            Node<WikiPageInfo> tempNode;
            
            for(Object obj : subPages){
                page = (Map) obj;
                pageId = (long) page.get("pageid");
    //            System.out.println(pageId);
                pageTitle = (String) page.get("title");
                pageType = (long) page.get("ns");
                
                if((pageType == 0) || (pageType == 14)){
                    tempPage = new WikiPageInfo(pageTitle, pageId, pageType);
                     
                    if(!existNode.contains(tempPage)){
                        tempNode = new WikiPageNode(tempPage, this);
                        results.add(tempNode);
                        existNode.add(tempPage);
                    }
                }
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return results;
    }
    
}
