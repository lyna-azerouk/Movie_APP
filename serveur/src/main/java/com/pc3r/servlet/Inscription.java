package com.pc3r.servlet;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebServlet("/inscription")
public class Inscription extends HttpServlet {

    private static final long serialVersionUID = 1L;

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
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonString = new Document()
                    .append("status",  false)
                    .append("message", "L'identifiant existe déjà.").toJson();
            response.getWriter().write(jsonString);

        } else {
            String hashedPassword = hashPassword(password);
            Document nouveauUtilisateur = new Document("identifiant", identifiant)
                    .append("mdp", hashedPassword)
                    .append("commentaires", 0)
                    .append("evaluations", 0);

            collection.insertOne(nouveauUtilisateur);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonString = new Document()
                    .append("status", true)
                    .toJson();

            response.getWriter().write(jsonString);
        }

        mongoClient.close();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Impossible de hacher le mot de passe", e);
        }
    }
}
