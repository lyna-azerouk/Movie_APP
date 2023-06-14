package com.pc3r.servlet.interactions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/commentaires")
public class CommentairesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final int MAX_COMMENTS = 20; 

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
	    int typeId = Integer.parseInt(request.getParameter("typeId"));
	        
	    @SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient("localhost", 27017);
	    MongoDatabase database = mongoClient.getDatabase("Movie");
	    MongoCollection<Document> collection = database.getCollection("Commentaires");
	        
	    BasicDBObject requete = new BasicDBObject();
        requete.put("typeId", typeId);
        
        FindIterable<Document> comments = collection.find(requete);
	    
	    List<String> commentsF = new ArrayList<>(); 
	    
	    for (Document c : comments )
	    {
	    	commentsF.add(c.toJson());
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
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    	/* Marche pas 
    	HttpSession session = request.getSession(false);
 	    if (session == null || session.getAttribute("identifiant") == null) {
 	    	System.out.println(session.getAttribute("identifiant"));
 	    	System.out.println("non");
 	    	response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        
	        String jsonString = new Document().
            		append("status", false).
            		append("message", "Vous devez vous connecté "
        	        		+ "pour ajouter un commentaire }").toJson();
            response.getWriter().write(jsonString);
 	        return;
 	    }*/
    	int typeId = Integer.parseInt(request.getParameter("typeId"));
        String type = request.getParameter("type");
        String identifiant = request.getParameter("identifiant");
        String commentaire = request.getParameter("commentaire");
        
        if (commentaire == null) {
        	response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        String jsonString = new Document().
            		append("status", false).
            		append("message", "Veuillez écrire un commentaire").toJson();
            response.getWriter().write(jsonString);
 	        return;
        }

        @SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("Movie");
        MongoCollection<Document> collection = database.getCollection("Commentaires");
        
        MongoCollection<Document> typeCollection;
        if (type.equals("Serie")) {
        	typeCollection = database.getCollection("Series");
        }else {
        	typeCollection = database.getCollection("Movies");
        }
        
        MongoCollection<Document> userCollection = database.getCollection("Utilisateurs");
        
        BasicDBObject requete = new BasicDBObject();
        requete.put("id", typeId);
        Document nommovieorserie = typeCollection.find(requete).first();

        Document comment = new Document("id", UUID.randomUUID().toString())
        		.append("type", type)
        		.append("typeId", typeId)
        		.append("userId", identifiant)
        		.append("nom", nommovieorserie.getString("titre"))
                .append("contenu", commentaire)
                .append("date", new Date());
        
        Document movieorserie = new Document("id", typeId);
        Document incrementation = new Document("$inc", new Document("commentaires", 1));
        
        Document user = new Document("identifiant", identifiant);
        Document incrementation2 = new Document("$inc", new Document("commentaires",1));
        
        
        collection.insertOne(comment);
        typeCollection.updateOne(movieorserie, incrementation);
        userCollection.updateOne(user, incrementation2);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonString = new Document()
                .append("status", true)
                .append("message", "commentaire ajouté ")
                .toJson();
        response.getWriter().write(jsonString);
	    mongoClient.close();

    }
    
    
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
 	    
        String commentaireId = request.getParameter("commentaireId");
        String identifiant = request.getParameter("identifiant");
        int typeId = Integer.parseInt(request.getParameter("typeId"));
        String type = request.getParameter("type");

        
        @SuppressWarnings("resource")
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("Movie");
        MongoCollection<Document> collection = database.getCollection("Commentaires");
        MongoCollection<Document> userCollection = database.getCollection("Utilisateurs");
        
        MongoCollection<Document> typeCollection;
        if (type.equals("Serie")) {
        	typeCollection = database.getCollection("Series");
        }else {
        	typeCollection = database.getCollection("Movies");
        }
        
        Document movieorserie = new Document("id", typeId);
        Document decrementation = new Document("$inc", new Document("commentaires", -1));
        
        Document user = new Document("identifiant", identifiant);
        Document decrementation2 = new Document("$inc", new Document("commentaires",-1));
        
        
        BasicDBObject requete = new BasicDBObject();
        requete.put("id", commentaireId);
        
        DeleteResult resultat = collection.deleteOne(requete);
        typeCollection.updateOne(movieorserie, decrementation);
        userCollection.updateOne(user, decrementation2);

        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (resultat.getDeletedCount() == 1) {
        	String jsonString = new Document()
                    .append("status", true)
                    .append("message", "Commentaire supprimé")
                    .toJson();
            response.getWriter().write(jsonString);
        } else {
        	String jsonString = new Document()
                    .append("status", false)
                    .append("message", "Commentaire non trouvé")
                    .toJson();
            response.getWriter().write(jsonString);
        }
    }
}
