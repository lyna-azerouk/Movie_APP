package com.pc3r.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@WebServlet("/recherche")

public class RechercheServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        String recherche = request.getParameter("recherche");
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("Movie");
        MongoCollection<Document> moviecollection = database.getCollection("Movies");
        MongoCollection<Document> seriecollection = database.getCollection("Series");

        
        BasicDBObject requete = new BasicDBObject();
        
        requete.put("titre", new BasicDBObject("$regex", ".*" + recherche + ".*").append("$options", "i"));
        
        FindIterable<Document> movies = moviecollection.find(requete);
        FindIterable<Document> series = seriecollection.find(requete);


        List<String> moviesF = new ArrayList<>();
        List<String> seriesF = new ArrayList<>();

        
        for (Document doc : movies) {
        	moviesF.add(doc.toJson());
        }
        
        for(Document doc : series) {
        	seriesF.add(doc.toJson());
        }
        
        if (moviesF.isEmpty() && seriesF.isEmpty()) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonString = new Document().
            		append("status", false).
            		append("message", "Aucun film ou série trouvé pour ce nom.").toJson();
            response.getWriter().write(jsonString);
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonString = new Document().
            		append("status", true).
            		append("films", moviesF)
            		.append("series", seriesF).toJson();
            response.getWriter().write(jsonString);
        }

        mongoClient.close();
    }
}


	


