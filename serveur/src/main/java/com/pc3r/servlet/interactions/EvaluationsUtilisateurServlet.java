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

@WebServlet("/evaluationsutilisateurs")
public class EvaluationsUtilisateurServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final int MAX_EVAL = 20; 

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
	    String utilisateur = request.getParameter("identifiant");
	        
	    @SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient("localhost", 27017);
	    MongoDatabase database = mongoClient.getDatabase("Movie");
	    MongoCollection<Document> collection = database.getCollection("Evaluations");
	        
	    BasicDBObject requete = new BasicDBObject();
        requete.put("userId", utilisateur);
        
        FindIterable<Document> evaluations = collection.find(requete);
	        
        List<String> evalsF = new ArrayList<>();

        
        for (Document doc : evaluations) {
        	evalsF.add(doc.toJson());
        }
        
	   if (evalsF.size() > MAX_EVAL) {
	        evalsF = evalsF.subList(0, MAX_EVAL);
	    } 
	        
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    String jsonString = new Document().
        		append("evaluations", evalsF).toJson();
        response.getWriter().write(jsonString);

	    mongoClient.close();

    }
    
}
