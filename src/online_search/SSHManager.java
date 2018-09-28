package online_search;

import com.jcraft.jsch.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static online_search.WikiPageInfo.getSubPages;
import org.json.simple.parser.ParseException;
import tree_structure.Node;
import tree_structure.Tree;

public class SSHManager
{
	private static final Logger LOGGER = 
	    Logger.getLogger(SSHManager.class.getName());
	private JSch jschSSHChannel;
	private String strUserName;
	private String strConnectionIP;
	private int intConnectionPort;
	private String strPassword;
	private Session sesConnection;
	private int intTimeOut;
	
	private void doCommonConstructorActions(String userName, 
	     String password, String connectionIP, String knownHostsFileName)
	{
	   jschSSHChannel = new JSch();
	
	   try
	   {
	      jschSSHChannel.setKnownHosts(knownHostsFileName);
	   }
	   catch(JSchException jschX)
	   {
	      logError(jschX.getMessage());
	   }
	
	   strUserName = userName;
	   strPassword = password;
	   strConnectionIP = connectionIP;
	}
	
	public SSHManager(String userName, String password, 
	   String connectionIP, String knownHostsFileName)
	{
	   doCommonConstructorActions(userName, password, 
	              connectionIP, knownHostsFileName);
	   intConnectionPort = 22;
	   intTimeOut = 60000;
	}
	
	public SSHManager(String userName, String password, String connectionIP, 
	   String knownHostsFileName, int connectionPort)
	{
	   doCommonConstructorActions(userName, password, connectionIP, 
	      knownHostsFileName);
	   intConnectionPort = connectionPort;
	   intTimeOut = 60000;
	}
	
	public SSHManager(String userName, String password, String connectionIP, 
	    String knownHostsFileName, int connectionPort, int timeOutMilliseconds)
	{
	   doCommonConstructorActions(userName, password, connectionIP, 
	       knownHostsFileName);
	   intConnectionPort = connectionPort;
	   intTimeOut = timeOutMilliseconds;
	}
	
	public String connect()
	{
	   String errorMessage = null;
	
	   try
	   {
	      sesConnection = jschSSHChannel.getSession(strUserName, 
	          strConnectionIP, intConnectionPort);
	      sesConnection.setPassword(strPassword);
	      // UNCOMMENT THIS FOR TESTING PURPOSES, BUT DO NOT USE IN PRODUCTION
	       sesConnection.setConfig("StrictHostKeyChecking", "no");
	      sesConnection.connect(intTimeOut);
	   }
	   catch(JSchException jschX)
	   {
	      errorMessage = jschX.getMessage();
	   }
	
	   return errorMessage;
	}
	
	private String logError(String errorMessage)
	{
	   if(errorMessage != null)
	   {
	      LOGGER.log(Level.SEVERE, "{0}:{1} - {2}", 
	          new Object[]{strConnectionIP, intConnectionPort, errorMessage});
	   }
	
	   return errorMessage;
	}
	
	private String logWarning(String warnMessage)
	{
	   if(warnMessage != null)
	   {
	      LOGGER.log(Level.WARNING, "{0}:{1} - {2}", 
	         new Object[]{strConnectionIP, intConnectionPort, warnMessage});
	   }
	
	   return warnMessage;
	}
	
	public String sendCommand(String command)
	{
	   StringBuilder outputBuffer = new StringBuilder();
	
	   try
	   {
	      Channel channel = sesConnection.openChannel("exec");
	      ((ChannelExec)channel).setCommand(command);
	      InputStream commandOutput = channel.getInputStream();
	      channel.connect();
	      int readByte = commandOutput.read();
	
	      while(readByte != 0xffffffff)
	      {
	         outputBuffer.append((char)readByte);
	         readByte = commandOutput.read();
	      }
	
	      channel.disconnect();
	   }
	   catch(IOException ioX)
	   {
	      logWarning(ioX.getMessage());
	      return null;
	   }
	   catch(JSchException jschX)
	   {
	      logWarning(jschX.getMessage());
	      return null;
	   }
	
	   return outputBuffer.toString();
	}
	
	public void close()
	{
	   sesConnection.disconnect();
	}
	
        public void storeWikiPage(WikiPageInfo wikipage, String dirPath) throws IOException, ParseException{
            String url = wikipage.getUrl();
            String[] parts = url.split("/");
            String fileName = parts[parts.length - 1];
            sendCommand("touch " + dirPath + "/" + fileName);
            sendCommand("echo \"" + url +"\n"+ wikipage.getContent() +"\" > " + dirPath + "/" + fileName);
//            sendCommand("echo \"" + wikipage.getContent() + "\" > " + dirPath + "/" + wikipage.getName());
        }
        
        public void expandWikiPageStorage(int extraDepth, Tree<WikiPageInfo> wikiPageTree, String dirPath) throws IOException, ParseException{
            List<Node<WikiPageInfo>> leaves = new ArrayList<>();
            wikiPageTree.expand(extraDepth, leaves);
            for(Node<WikiPageInfo> leaf : leaves){
                storeWikiPage(leaf.getData(), dirPath);
                System.out.println("Store: " + leaf.getData().toString());
            }
        }
        
	public static void main(String[] args) {
		
            try{
		String command = "ls";
		String userName = "huong";
		String password = "huonghttt";
		String connectionIP = "202.191.57.85";
		SSHManager instance = new SSHManager(userName, password, connectionIP, "");
		String errorMessage = instance.connect();
		 
		if(errorMessage != null)
		{
			System.out.println(errorMessage);
			
		}
		else {
			
//			instance.sendCommand("touch ~/InternetData/demo");
//                      instance.sendCommand("echo \"this is double quote \\\" \" > ~/InternetData/demo");
//                        
////                        String result = instance.sendCommand(command);
////                        System.out.println(result);
////                        
//			instance.close();
                    

                        
                        String dirPath = "~/InternetData/Luanvannet2";
                        instance.sendCommand("mkdir " + dirPath);
                        System.out.println("Created directory");
                        
                        List<ThesisInfo> tempThesisList;
                        for(int page = 1; page <= 488; page++){
                            tempThesisList = OnlineSearch.getLinksFromLuanvannet(page);
                            for(ThesisInfo thesis : tempThesisList){
                                instance.sendCommand("touch "+ dirPath + "/" +thesis.getName());
                                instance.sendCommand("echo \"" + thesis.getLink() +"\n"+ OnlineSearch.getThesisContentFromLuanvannet(thesis.getLink()) + "\" > "+ dirPath +"/"+thesis.getName());
                                System.out.println(thesis.getName());
                            }
                        }
                        
                        instance.close();
                        System.out.println("Done");
                        
                        
                        
                        
//                        instance.sendCommand("mkdir ~/InternetData/Wikipedia");
//                        System.out.println("Created directory");
//                        
//                        List<ThesisInfo> contentList;
//                        
//                        contentList = OnlineSearch.getLinksFromWikipedia();
//                        for(ThesisInfo thesis : contentList){
//                            instance.sendCommand("touch ~/InternetData/Wikipedia/"+thesis.getName());
//                            instance.sendCommand("echo \"" + OnlineSearch.getContentFromWikipedia(thesis.getLink()) + "\" > ~/InternetData/Wikipedia/"+thesis.getName());
//                            System.out.println(thesis.getName());
//                        }
//                        
//                        
//                        instance.close();
//                        System.out.println("Done");

//                    String dirPath = "~/InternetData/Wikipedia2";
//                    instance.sendCommand("mkdir " + dirPath);
//                    System.out.println("Created directory");
//                    
//                    List<WikiPageInfo> pageList = new LinkedList<>();
//                    WikiPageInfo rootPage = new WikiPageInfo("Thể loại:Tin học", 27614, 14);
//                    pageList.add(rootPage);
//                    Set<Long> pageIdSet = new HashSet<>();
//                    pageIdSet.add(rootPage.getPageId());
//                    WikiPageInfo headPage;
//                    List<WikiPageInfo> subPages;
//                    while(!pageList.isEmpty()){
//                        headPage = pageList.get(0);
//                        pageList.remove(0);
//                        if(headPage.getType() == 0){
//                            instance.storeWikiPage(headPage, dirPath);
//                            System.out.println(headPage.getTitle());
//                        }
//                        else if(headPage.getType() == 14){
//                            subPages = getSubPages(headPage, pageIdSet);
//                            pageList.addAll(0, subPages);
//                        }
//                    }
//                    


//                    Node<WikiPageInfo> rootNode = new WikiPageNode(rootPage, null);
//                    Tree<WikiPageInfo> wikiPageTree = new Tree<>(rootNode);
//                    instance.expandWikiPageStorage(4, wikiPageTree, dirPath);
//                    
//                    ObjectFileOperations.writeObjectToFile("/home/trinhhaison/NetBeansProjects/PlagiarismDetection/OnlineData/wiki_page_tree", wikiPageTree);
//                    
//
//
////                    WikiPageInfo testPage = new WikiPageInfo("Máy truy tìm dữ liệu", 5764, 0);
////                    instance.storeWikiPage(testPage, dirPath);
//                    instance.close();
//                    System.out.println("Done");
                    
		}
            }
            catch(Exception e){
                e.printStackTrace();
            }
	}

}