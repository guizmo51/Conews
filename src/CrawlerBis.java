import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerBis {
	
	static String baseURL="http://web.archive.org/web/";
	static ArrayList<String> dateOfCrawl=new ArrayList<String>();
	
	public static String splitFilePath(String url){
		String[] tokens = url.split("/",11);
		
		return tokens[(tokens.length)-1];
		
		
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
	
	
	public static String getNameOfpage(String url){
		
		String[] tokens = url.split("/",11);
			System.out.println(tokens[(tokens.length)-1]);
			return tokens[(tokens.length)-1];
			
			//return url;
			
			
			
		}

	
	
	public static boolean internURL(String url){
		
		if(url.indexOf("http://forum.aceboard.net/")>=0){
			
			return true;
			
		}else{
			
			return false;
		}
		
	}
	
	
	
	public static String nameOfFile(String url){
		String[] parse=url.split("/",4);
		
		return parse[2];
	}
	
	
	public static boolean isHtmFile(String url){
		
		
			
		String ext=url.substring(url.lastIndexOf('.'));
		
			if(ext.equals(".htm")){
				
				return true;
			}else{
				return false;
			}
			
	
	}
	
	
	
	 public static Connection getConnection() throws Exception {
		    // load the Oracle JDBC Driver
		    Class.forName("com.mysql.jdbc.Driver");
		    // define database connection parameters
		    return DriverManager.getConnection("jdbc:mysql://localhost/projet_conews", "USER",
		        "PASSWORD");
		    
		  }
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		
		// TODO Auto-generated method stub
		 Connection conn = null; // connection object
		    Statement stmt = null; // statement object
		    Statement stmtbis = null;
		    ResultSet rs = null; // result set object
		try{
			conn = getConnection(); 
			String query="SELECT * FROM erreurs";
			
			 // create the java statement
		      stmt = conn.createStatement();
		      
		      // execute the query, and get a java resultset
		      rs = stmt.executeQuery(query);
		      
		      // iterate through the java resultset
		      while (rs.next())
		      {
		        int id = rs.getInt("id");
		       
		        String requested_url = rs.getString("requested_url");
		        
		        	
		        	
			        if(isHtmFile(requested_url)){
			        	
			        	String filename=nameOfFile(requested_url);
			        	
			        		
			        		//String completeUrl=baseURL+date_crawl+"/"+filename;
			        		String searchUrl="http://web.archive.org/web/*/http://forum.aceboard.net/"+filename;
			        		
			        			System.out.println("On essaie "+searchUrl);
								Response reponse =Jsoup.connect(searchUrl).ignoreHttpErrors(true).execute();
								int statusCode = reponse.statusCode();
								if(statusCode == 200) {
									
									try{
										Document page_reponses=Jsoup.connect(searchUrl).get();
										Element sujet=page_reponses.select("div.day > a").first();
										System.out.println(sujet.attr("href"));
										
										
										Document doc = Jsoup.parse(sujet.attr("href"));
										
										
										
										Elements images = doc.getElementsByTag("img");
										
										for (Element image : images) {
											  String imgsrc = image.attr("src");
											  try{
												  
													  image.attr("src", saveImage(imgsrc));
													 
													  
												
												 
													
												  //splitFilePath(imgsrc);
												 
												  
											  }catch(Exception e){
												  
												  
											  }
											  
										
									}
										
										
										
										Elements ahrefs=doc.getElementsByTag("a");
										for (Element ahref : ahrefs) {
										
												if(internURL(ahref.attr("href"))){
													 ahref.attr("href", getNameOfpage(ahref.attr("href")));
													;
												}
										}
										
										 FileWriter fstream = new FileWriter(filename);
										  BufferedWriter out = new BufferedWriter(fstream);
										  out.write(doc.html());
										  //Close the output stream
										  out.close();
										
										
										
										
									}catch(Exception e){
										
										
									}
									
									//On retire l'URL
									String query_delete="DELETE FROM erreurs WHERE id='"+id+"' ";
									  // execute the query, and get a java resultset
									// create the java statement
								      stmtbis = conn.createStatement();
								      
								      // execute the query, and get a java resultset
								      //rs = stmt.executeQuery(query);
								      int delete = stmtbis.executeUpdate(query_delete);
								      if(delete == 1){
								    	  System.out.println("Row is deleted.");
								    	  }
								    	  else{
								    	  System.out.println("Row is not deleted.");
								    	  }
								}
								else{
									
									
								}
							
			        		
			        		
			       
			        	
			        }
		        
		        
		        
		        
		        
		        
		      }
		      stmt.close();
		      
		      
		      
		      
		      
		      
			
			
		}catch(SQLException e){
			  // something went wrong, we are handling the exception here
		      if (conn != null) {
		        conn.rollback();
		        conn.setAutoCommit(true);
		      }

		      System.out.println("--- SQLException caught ---");
		      // iterate and get all of the errors as much as possible.
		      while (e != null) {
		        System.out.println("Message   : " + e.getMessage());
		        System.out.println("SQLState  : " + e.getSQLState());
		        System.out.println("ErrorCode : " + e.getErrorCode());
		        System.out.println("---");
		        e = e.getNextException();
		      }
			
		}finally{
			
			
		}
		//On va ouvrir db et table

	}

}
