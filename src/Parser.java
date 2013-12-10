import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Parser {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		  // Directory path here
		  String path = "."; 
		 
		  String files;
		  File folder = new File(path);
		  File[] listOfFiles = folder.listFiles(); 
		  
		  for (int i = 0; i < listOfFiles.length; i++) 
		  {
		 
		   if (listOfFiles[i].isFile()) 
		   {
		   files = listOfFiles[i].getName();
		       if (files.endsWith(".htm") )
		       {
		    	   File input = new File(files);
		          System.out.println("./"+files);
		          
		      	Document page_sujet=Jsoup.parse(input,"UTF-8","");
		      	
		      	Elements select=page_sujet.getElementsByTag("script");
		      	
		      
		      	for(Element recup: select){
		      		
		      		
		      		//recup.remove();
		      	}
		      	Elements anchors = page_sujet.select("a[href]");
		      	
		      	for(Element anchor : anchors){
		      		
		      		System.out.println(anchor.text());
		      		
		      	}
		      	if(anchors.size()>0){
		      		
		      	   	anchors.remove(0);
			      	anchors.remove(1);
			      	anchors.remove(2);
			      	anchors.remove(3);
			      	anchors.remove(4);
		      	}
		   
		      	
		      	Element body = page_sujet.getElementsByTag("body").first();
		      	body.append("<div id=\"speacial_header\" style=\"position:fixed;top:0;width:100%;height:35px;color:red; opacity:0.5; background-color:#66FF66;\">Vous êtes sur une page du projet \"CONews\" - <a href=\"./index.html\">Retourner à l'accueil du projet</a></div><script type=\"text/javascript\" src=\"ga.js\"></script>");
		      	
		      	FileWriter fstream = new FileWriter(files);
		  	 BufferedWriter out = new BufferedWriter(fstream);
		  	  out.write(page_sujet.html());
		  	  //Close the output stream
		  	  out.close();
		  	
		      	
		      
		      	
		          //On ajouter une top bar
		          // On  ajout Google Analitics
		          //On enleve les merdes 
		        }
		     }
		  }
		
		
	}

}
