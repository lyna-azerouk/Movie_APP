package com.pc3r.servlet.interactions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/commentairesutilisateurs")
public class CommentairesUtilisateurServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final int MAX_COMMENTS = 20; 

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
	    String utilisateur = request.getParameter("identifiant");
	        
	    @SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient("localhost", 27017);
	    MongoDatabase database = mongoClient.getDatabase("Movie");
	    MongoCollection<Document> collection = database.getCollection("Commentaires");
	        
	    BasicDBObject requete = new BasicDBObject();
        requete.put("userId", utilisateur);
        
        FindIterable<Document> comments = collection.find(requete);
	        
        List<String> commentsF = new ArrayList<>();

        
        for (Document doc : comments) {
        	commentsF.add(doc.toJson());
        }
        
	   if (commentsF.size() > MAX_COMMENTS) {
	        commentsF = commentsF.subList(0, MAX_COMMENTS);
	    } 
	        
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    String jsonString = new Document().
        		append("commentaires", commentsF).toJson();
        response.getWriter().write(jsonString);

	    mongoClient.close();

    }
    
}
