package com.pc3r.servlet;


import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@WebServlet("/connexion")

public class Connexion extends HttpServlet {
	
    private static final long serialVersionUID = 1L;

	
	private static String hashPassword(String password){
	    MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
		    byte[] digest = md.digest();
		    StringBuilder sb = new StringBuilder();
		    for (byte b : digest) {
		        sb.append(String.format("%02x", b & 0xff));
		    }
		    return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    String identifiant = request.getParameter("identifiant");
    String password = request.getParameter("password");

    MongoClient mongoClient = new MongoClient("localhost", 27017);
    MongoDatabase database = mongoClient.getDatabase("Movie");
    MongoCollection<Document> collection = database.getCollection("Utilisateurs");

    BasicDBObject requete = new BasicDBObject();
    requete.put("identifiant", identifiant);
    Document resultat = collection.find(requete).first();
    
    if (resultat != null) {
        // on compare le hash du mot de passe entré avec le hash stocké dans la base de données
        String hashedPassword = hashPassword(password);
        if (!hashedPassword.equals(resultat.getString("mdp"))) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonString = new Document().
                    append("status", false).
                    append("message", "Mot de passe incorrect.").toJson();
            response.getWriter().write(jsonString);
          
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("identifiant", identifiant);
            session.setMaxInactiveInterval(86400); // 24 heures en secondes
            
            Cookie cookie = new Cookie("session", identifiant + "|" + resultat.get("commentaires").toString() + "|" + resultat.get("evaluations").toString());
            cookie.setMaxAge(86400); // temps en secondes avant expiration
            response.addCookie(cookie);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonString = new Document()
                .append("status", true)
                .append("identifiant", resultat.getString("identifiant"))
                .append("commentaires", resultat.get("commentaires"))
                .append("evaluations", resultat.get("evaluations"))
                .toJson();
            response.getWriter().write(jsonString);
        }
    } else {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonString = new Document().
                append("status", false).
                append("message", "Identifiant inconnu.").toJson();
        response.getWriter().write(jsonString);
    }

    mongoClient.close();
}
}

