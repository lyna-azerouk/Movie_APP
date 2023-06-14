package com.pc3r.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/series")
public class SerieDBServlet extends HttpServlet {
	
	/* API REST il faut appeller la méthode GET depuis le client pour récuperer les bonnes donnés du fichier JSON */ 
	private static final long serialVersionUID = 1L;
	private static final String API_KEY = "ded6000884579524d7f23a2993383461";
    private static final String API_URL = "https://api.themoviedb.org/3/tv/top_rated?api_key=" + API_KEY + "&language=fr-FR&page=1";
    
    @SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        

        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("Movie");
        MongoCollection<Document> collection = database.getCollection("Series");
        List<Document> document_a_ajouter = new ArrayList<Document>();
        
        
        /* Vérifie si la BDD a trop de document pour éviter de la surcharger */ 
        int taille = (int)collection.countDocuments();
        
        try {
			JSONObject json = new JSONObject(content.toString());
			JSONArray tabjson = json.getJSONArray("results");
			for (int i = 0 ; i < tabjson.length() ; i++) {
				JSONObject resultat = tabjson.getJSONObject(i);
				int id = resultat.getInt("id");
				
				BasicDBObject requete = new BasicDBObject();
		        requete.put("id", id);
		        Document movie = collection.find(requete).first();
		        
		        if(movie == null) {
		        	movie = new Document("id",id).
		        			append("titre",resultat.getString("name")).
		        			append("resume",resultat.getString("overview")).
		        			append("poster",resultat.getString("poster_path")).
		        			append("date", resultat.getString("first_air_date")).
		        			append("evaluations",0).
		        			append("commentaires",0);
		        	document_a_ajouter.add(movie);
		        }
		        
			}
			/* Si la taille est trop grande, supprimer les films avec le moins de commentaires */ 
			if (taille >= 200 && document_a_ajouter.size() != 0) {
				((FindIterable<Document>) collection.deleteMany(Filters.exists("commentaires"))).limit(taille - document_a_ajouter.size());
			}
			if (document_a_ajouter.size() != 0 ) {
				collection.insertMany(document_a_ajouter);
			}
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(content.toString());
		    
			
		} catch (JSONException e) {
			System.out.println(e);
			
		} finally {
			mongoClient.close();
		}

   	
    }
    
}
