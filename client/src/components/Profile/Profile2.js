import React, { useContext, useState, useEffect } from "react";
import { AppContext } from "../../AppContext";

import "./Profile2.css";
import CommentaireUser from "../Commentaires/CommentaireUser";
import EvaluationUser from "../Evaluations/EvaluationUser";


const Profile2 = () => {
  const { identifiant, commentaires, evaluations } = useContext(AppContext);
  const [contenuCommentaires, setContenuCommentaires] = useState([]);

  useEffect(() => {
    fetch(
      `http://localhost:8080/pc3r-projet/commentairesutilisateurs?identifiant=${identifiant}`
    )
      .then((res) => res.json())
      .then((data) => setContenuCommentaires(data.commentaires))
      .catch((err) => console.log(err));
  }, [identifiant]);

  const contenu = contenuCommentaires.map((commentaire) =>
    JSON.parse(commentaire)
  );
  
  return (
    <div className="profil">
      <div className="profile-header">
        <h2 className="identifiant">{identifiant}</h2>
        <div className="stats-container">
          <p className="commentaires">Nombre commentaires: {commentaires}</p>
          <p className="evaluations">Nombre évaluations: {evaluations}</p>
        </div>
      </div>
      <h1> Commentaires </h1>
         <CommentaireUser  />
      <h1> Evaluations </h1>
        < EvaluationUser />
    </div>
  );
};

export default Profile2;
