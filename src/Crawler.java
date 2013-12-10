import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.ClientProtocolException;
//import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




public class Crawler {
	static Map<String,String> urltoDo = new HashMap<String,String>();
	static Map<String,String> urlDone = new HashMap<String,String>();
	static List<String> urlError = new ArrayList<String>();
	static List<String> savedIcon=new ArrayList<String>();
	static List<String> iconToSave=new ArrayList<String>();
	static List<String> subForum=new ArrayList<String>();
	
	static String baseUrl="http://web.archive.org";
	static String indexUrl="http://web.archive.org/web/20090412232428/http://forum.aceboard.net/index.php?login=29408";
	static String currentUrl=indexUrl;
	
	
public static void main(String[] args) throws ClientProtocolException, IOException{
	
	subForum.add("http://web.archive.org/web/20120126060122/http://forum.aceboard.net/29408-2702-0-Annonces-course.htm");
	
	Document xmldoc=new Document("doc");
	Element root = xmldoc.createElement("forum");
	
		
		
	urltoDo.put(currentUrl, "index");
		
	try{
		
		
		for (String link_sub : subForum) {
			
			
				
				System.out.println("On rentre dans le sous-forum "+link_sub);
				
				
				try{
					//On ouvre un sous-forum
					
					
					
					
				Document doc_sous_fofo = Jsoup.connect(link_sub).get(); 
				ArrayList<String> listUrls=findURLpages(doc_sous_fofo,link_sub );
				for (String url : listUrls){
					
					/*Pour chaque page d'un sous forum */
					
					try{
						Document page_sousfofo=Jsoup.connect(baseUrl+url).get(); 
						Element sujets=page_sousfofo.select("table.master").first();
						//String phrase=zone.html();
						
						Elements links=sujets.getElementsByTag("a");
						for (Element link_sujets : links) {
							//getNameOfpage(link.attr("href"));
							
							if(!hasVbInPath(link_sujets.attr("href"))){
								
								
								
								try{
									Document page_sujet=Jsoup.connect(baseUrl+link_sujets.attr("href")).get(); 
									//On recupere toutes les pages d'un sujet
									ArrayList<String>listUrlsOtherpages=findURLpagesSujet(page_sujet,link_sujets.attr("href"));
									//l'array list contient toutes les pages du thread et m�me l'index
									System.out.println("\t On rentre dans le sujet "+link_sujets.text());
									for(String page_sujet1: listUrlsOtherpages ){
										System.out.println("\t\t"+listUrlsOtherpages.size());
										
										try{
											Document page_reponses=Jsoup.connect(baseUrl+page_sujet1).get();
											savePage(savePictures(page_reponses), getNameOfpage(baseUrl+page_sujet1));
										}catch(Exception e){
											
											
										}
										
										
										
										
									}
									
									
									
								}catch(Exception e){
									System.out.println(e);
								}
								
							
								
								
							}
							
							
						}
						savePage(savePictures(page_sousfofo), getNameOfpage(baseUrl+url));
					}catch(Exception e){
						addUrlInErrorList(url);
						
					}
					
					
				}
				
				
				
				
			
				
				savePage(savePictures(doc_sous_fofo), getNameOfpage(link_sub));
				/* Find recuperation page*/
				
				saveAllImages();
				}catch(Exception e){
					//addUrlInErrorList(link.attr("href"));
					System.out.println(e.getMessage());
					
				}
				
			
			
		}
		
		
		
		//Elements table=doc.select("div.master").first();
		//findURLsujets(doc);
		//findURLpages(doc);


		
		
		//findSubForum(doc);
		//findURLpagesReponses(doc);
		//savePictures(doc);
		//savePage(savePictures(doc), getNameOfpage("http://web.archive.org/web/20110922072658/http://forum.aceboard.net/29408-11-0-news.htm"));
		//savePage(savePictures(doc), getNameOfpage(currentUrl));
		
		
		readErrorList();
		
	}catch(Exception e){
		System.out.println("toto");
		//urlError.put(indexUrl, "index");
	}
	
	
	
}

public static void saveAllImages() throws IOException{
	
	for (String img : iconToSave) {
		saveImage(img);
	}
}



public  static void readErrorList(){
	
	for (String link : urlError) {
		System.out.println("Erreur "+link);
	}
}

public static void addValueInMap(String url){
	urlError.add(url);
	
}

public static boolean notDoneUrl(String url){
	
	
	if((!urltoDo.containsKey(url))&&(!urlDone.containsKey(url))&&(!urlError.contains(url)) ) {
	    return true;
	}
	else{
		
		return false;
	}
}

public static String localPath(String url){
	
	String[] tokens = url.split("/",4);
	
	return tokens[(tokens.length)-1];
}
public static boolean findSubForum(Document doc){
	Element zone=doc.select("table.master").get(1);
	Elements links=zone.getElementsByTag("a");
	for (Element link : links) {
		
		if(isSubForumLink(link.attr("href"))){
			System.out.println(link.attr("href"));
		}
		
	}
	return true;
}

public static boolean isSubForumLink(String url){
	
if(url.indexOf("http://forum.aceboard.net/29408")>=0){
		
		return true;
		
	}else{
		
		return false;
	}
	
}
public static void addUrlInErrorList(String url){
	
	urlError.add(url);
	
}

public static boolean hasWayBackPrefix(String url){
	
if(url.indexOf("web.archive.org")>=0){
		
		return true;
		
	}else{
		
		return false;
	}
	
}

public static ArrayList<String> findURLpages(Document doc, String current_url){
	
	ArrayList<String> listUrl = new ArrayList<String>();
	listUrl.add(current_url);
	
	
	Element zone=doc.select("div.petit").get(1);
	//String phrase=zone.html();
	
	Elements links=zone.getElementsByTag("a");
	for (Element link : links) {
String url=link.attr("href");
		
		try{
			Integer.parseInt(link.text());
			System.out.println(link.text()+" -> "+url);
			listUrl.add(url);
			
		}catch(Exception e){
			
			
		}
		
		
	}
	
	return listUrl;
	
}
public static ArrayList<String> findURLpagesSujet(Document doc, String current_url){
	
	ArrayList<String> listUrl = new ArrayList<String>();
	
	listUrl.add(current_url);
	Element zone=doc.select("div.petit").get(2);
	
	Elements ahrefs=zone.getElementsByTag("a");
	for (Element ahref : ahrefs) {
		
		String url=ahref.attr("href");
		
		try{
			Integer.parseInt(ahref.text());
			
			listUrl.add(url);
			
		}catch(Exception e){
			
			
		}
		
		
	}
	
	return listUrl;
	
}

public static void findURLsujets(Document doc) {
	
	
	Element zone=doc.select("table.master").first();
	//String phrase=zone.html();
	System.out.println(zone);
	Elements links=zone.getElementsByTag("a");
	for (Element link : links) {
		getNameOfpage(link.attr("href"));
		System.out.println(link.attr("href"));
		
	}
}


public static String splitFilePath(String url){
	String[] tokens = url.split("/",11);
	
	return tokens[(tokens.length)-1];
	
	
}

public static void findURL(Document doc) {
	
	// /html/body/div[2]/div/table[2]/tbody/tr/td/table
	Elements links = doc.getElementsByTag("a");
	
	for (Element link : links) {
		  String linkHref = link.attr("href");
		  String linkText = link.text();
		  
		  if(internURL(linkHref)){
			  System.out.println(localPath(linkHref));
			  //System.out.println(linkHref);}
		  
		}
	
}
}

public static boolean internURL(String url){
	
	if(url.indexOf("http://forum.aceboard.net/")>=0){
		
		return true;
		
	}else{
		
		return false;
	}
	
}
public static String saveImage(String imageUrl) throws IOException {
	System.out.println(imageUrl);
	URL url = new URL(imageUrl);
	InputStream is = url.openStream();
	String nameoffile=splitFilePath(imageUrl);
	OutputStream os = new FileOutputStream("img/"+nameoffile);

	byte[] b = new byte[2048];
	int length;

	while ((length = is.read(b)) != -1) {
		os.write(b, 0, length);
	}

	is.close();
	os.close();
	
	return "img/"+nameoffile;
}


public static String savePictures(Document html) throws IOException{
	
Elements images = html.getElementsByTag("img");
	
	for (Element image : images) {
		  String imgsrc = image.attr("src");
		  try{
			  if(!(savedIcon.contains(splitFilePath(imgsrc)))){
				  image.attr("src", "img/"+splitFilePath(imgsrc));
				  System.out.println(image.attr("src"));
				 if(!iconToSave.contains(imgsrc)){
					 iconToSave.add(imgsrc);
				 }
				  
				  //savedIcon.add(splitFilePath(imgsrc));
			  }else{
				  
				  image.attr("src", "img/"+splitFilePath(imgsrc));
			  }
			 
				
			  //splitFilePath(imgsrc);
			 
			  
		  }catch(Exception e){
			  
			  
		  }
		  
	
}
	
	return (html.html());
	
}


public static void removeWayBackBar(String html){
	
	
	//wm-ipp-inside
}

public static String getNameOfpage(String url){
	
String[] tokens = url.split("/",11);
	System.out.println(tokens[(tokens.length)-1]);
	return tokens[(tokens.length)-1];
	
	//return url;
	
	
	
}








public static void findURLpagesReponses(Document doc){
	
	Element zone=doc.select("div.petit").get(2);
	
	Elements ahrefs=zone.getElementsByTag("a");
	for (Element ahref : ahrefs) {
		
		String url=ahref.attr("href");
		
		try{
			Integer.parseInt(ahref.text());
			
			
		}catch(Exception e){
			
			
		}
		
		
	}
	
}



public static boolean hasVbInPath(String url){
if(url.indexOf("#vb")>=0){
		
		return true;
		
	}else{
		
		return false;
	}
	
	
}


public static boolean savePage(String html, String url) throws IOException{
	
	
	/* On va changer les liens locaux */
	/*  */
	Document doc = Jsoup.parse(html);
	
	Elements ahrefs=doc.getElementsByTag("a");
	for (Element ahref : ahrefs) {
	
			if(internURL(ahref.attr("href"))){
				 ahref.attr("href", getNameOfpage(ahref.attr("href")));
				;
			}
	}
	
	 FileWriter fstream = new FileWriter(url);
	  BufferedWriter out = new BufferedWriter(fstream);
	  out.write(doc.html());
	  //Close the output stream
	  out.close();
	
	return false;
	
}

	
}
