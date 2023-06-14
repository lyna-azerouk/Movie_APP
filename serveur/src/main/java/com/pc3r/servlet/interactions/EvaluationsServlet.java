package com.pc3r.servlet.interactions;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

@WebServlet("/evaluations")
public class EvaluationsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final int MAX_EVAL = 20;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	int typeId = Integer.parseInt(request.getParameter("typeId"));
        
	    @SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient("localhost", 27017);
	    MongoDatabase database = mongoClient.getDatabase("Movie");
	    MongoCollection<Document> collection = database.getCollection("Evaluations");
	        
	    BasicDBObject requete = new BasicDBObject();
        requete.put("typeId", typeId);
        
	    
        FindIterable<Document> evaluations = collection.find(requete);
        
	    List<String> evaluationsF = new ArrayList<>(); 

	    for (Document c : evaluations )
	    {	evaluationsF.add(c.toJson());
	    }
	    
	    if (evaluationsF.size() > MAX_EVAL) {
	    	evaluationsF = evaluationsF.subList(0, MAX_EVAL);
	    }
	    
	    	         
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    String jsonString = new Document().
        		append("evaluations", evaluationsF).toJson();
        response.getWriter().write(jsonString);
        
	    mongoClient.close();

    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    	// récupérer l'identifiant de l'utilisateur
    	String identifiant = request.getParameter("identifiant");

    	// récupérer l'identifiant du film
    	int typeId = Integer.parseInt(request.getParameter("typeId"));

    	// se connecter à la base de données
    	MongoClient mongoClient = new MongoClient("localhost", 27017);
    	MongoDatabase database = mongoClient.getDatabase("Movie");
    	MongoCollection<Document> collection = database.getCollection("Evaluations");

    	// vérifier si l'utilisateur a déjà une évaluation pour ce film
    	BasicDBObject requete = new BasicDBObject();
    	requete.put("typeId", typeId);
    	requete.put("userId", identifiant);
    	FindIterable<Document> evaluations = collection.find(requete);
    	int nbEvaluations = 0;
    	for (Document c : evaluations) {
    	    nbEvaluations++;
    	    // mettre à jour la note de l'utilisateur s'il a déjà évalué le film
    	    int nouvelleNote = Integer.parseInt(request.getParameter("evaluation"));
    	    BasicDBObject update = new BasicDBObject();
    	    update.put("$set", new BasicDBObject("note", nouvelleNote));
    	    collection.updateOne(requete, update);
    	    response.setContentType("application/json");
    	    response.setCharacterEncoding("UTF-8");
    	    String jsonString = new Document()
    	            .append("status", true)
    	            .append("message", "Votre évaluation a été mise à jour.")
    	            .toJson();
    	    response.getWriter().write(jsonString);
    	}

    	if (nbEvaluations == 0) {
    	    // l'utilisateur n'a pas encore évalué ce film, on ajoute la nouvelle évaluation
    	    String type = request.getParameter("type");
    	    int evaluation = Integer.parseInt(request.getParameter("evaluation"));

    	    MongoCollection<Document> userCollection = database.getCollection("Utilisateurs");
    	    MongoCollection<Document> typeCollection;
    	    if (type.equals("Serie")) {
    	        typeCollection = database.getCollection("Series");
    	    } else {
    	        typeCollection = database.getCollection("Movies");
    	    }

    	    BasicDBObject requete2 = new BasicDBObject();
    	    requete2.put("id", typeId);
    	    Document nommovieorserie = typeCollection.find(requete2).first();

    	    Document comment = new Document("id", UUID.randomUUID().toString())
    	            .append("type", type)
    	            .append("typeId", typeId)
    	            .append("userId", identifiant)
    	            .append("nom", nommovieorserie.getString("titre"))
    	            .append("note", evaluation)
    	            .append("date", new Date());

    	    Document movieorserie = new Document("id", typeId);
    	    Document incrementation = new Document("$inc", new Document("evaluations", 1));

    	    Document user = new Document("identifiant", identifiant);
    	    Document incrementation2 = new Document("$inc", new Document("evaluations", 1));

    	    collection.insertOne(comment);
    	    typeCollection.updateOne(movieorserie, incrementation);
    	    userCollection.updateOne(user, incrementation2);

    	    response.setContentType("application/json");
    	    response.setCharacterEncoding("UTF-8");
    	    String jsonString = new Document()
    	            .append("status", true)
    	            .append("message", "Votre évaluation a été ajoutée.")
    	            .toJson();
    	    response.getWriter().write(jsonString);
    	}

    	mongoClient.close();

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String evaluationId = request.getParameter("evaluationId");
        String identifiant = request.getParameter("identifiant");
        int typeId = Integer.parseInt(request.getParameter("typeId"));
        String type = request.getParameter("type");

        
        @SuppressWarnings("resource")
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("Movie");
        MongoCollection<Document> collection = database.getCollection("Evaluations");
        MongoCollection<Document> userCollection = database.getCollection("Utilisateurs");
        
        MongoCollection<Document> typeCollection;
        if (type.equals("Serie")) {
        	typeCollection = database.getCollection("Series");
        }else {
        	typeCollection = database.getCollection("Movies");
        }
        
        
        Document movieorserie = new Document("id", typeId);
        Document decrementation = new Document("$inc", new Document("evaluations", -1));
        
        Document user = new Document("identifiant", identifiant);
        Document decrementation2 = new Document("$inc", new Document("evaluations",-1));
        
        
        BasicDBObject requete = new BasicDBObject();
        requete.put("id", evaluationId);
        
        DeleteResult resultat = collection.deleteOne(requete);
        typeCollection.updateOne(movieorserie, decrementation);
        userCollection.updateOne(user, decrementation2);

        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (resultat.getDeletedCount() == 1) {
        	String jsonString = new Document()
                    .append("status", true)
                    .append("message", "Evaluation supprimé")
                    .toJson();
            response.getWriter().write(jsonString);
        } else {
        	String jsonString = new Document()
                    .append("status", false)
                    .append("message", "Evaluation non trouvé")
                    .toJson();
            response.getWriter().write(jsonString);        }
    }
    
    
}
