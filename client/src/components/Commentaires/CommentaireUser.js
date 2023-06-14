import React, { useContext, useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';

import { AppContext } from "../../AppContext";

const CommentaireUser = () => {
  const { identifiant,commentaires, setCommentaires } = useContext(AppContext);
  const [contenuCommentaires, setContenuCommentaires] = useState([]);
  const [shouldRefresh, setShouldRefresh] = useState(false); 
  const navigate = useNavigate();


  useEffect(() => {
    fetch(
      `http://localhost:8080/pc3r-projet/commentairesutilisateurs?identifiant=${identifiant}`
    )
      .then((res) => res.json())
      .then((data) => setContenuCommentaires(data.commentaires))
      .catch((err) => console.log(err))
      .finally(() => setShouldRefresh(false));

  }, [identifiant, shouldRefresh]); 
  const contenu = contenuCommentaires.map((commentaire) =>
    JSON.parse(commentaire)
  );

  const handleSupprimerCommentaire = (commentaireId, commentaireTypeId, commentaireType, identif) => {
    const requestOptions = {
      method: "DELETE",
      headers: { "Content-Type": "application/json" },
    };
    fetch(`http://localhost:8080/pc3r-projet/commentaires?typeId=${commentaireTypeId}&commentaireId=${commentaireId}&identifiant=${identif}&type=${commentaireType}`, requestOptions)
      .then((response) => response.json())
      .then((data) => {
        if (data.status){
          setCommentaires(commentaires-1)
          setShouldRefresh(true); 
        }
      })
      .catch((error) => {
        console.error("Erreur lors de la suppression du commentaire :", error);
      });
  };

  return (
    <div className="profile-body">
      {contenu.length > 0 ? (
        <ul className="commentaires-list">
          {contenu.map((commentaire) => (
            <li key={commentaire.id} className="commentaire">
              <div className="commentaire-header">
                <span className="film-nom">{commentaire.nom}</span>
                <span className="commentaire-date">
                  {new Date(commentaire.date.$date).toLocaleDateString()}
                </span>
                <button onClick={() => handleSupprimerCommentaire(commentaire.id, commentaire.typeId, commentaire.type, identifiant)}>Supprimer</button>
              </div>
              <div className="commentaire-contenu">{commentaire.contenu}</div>
            </li>
          ))}
        </ul>
      ) : (
        <p>Pas de commentaires pour le moment.</p>
      )}
    </div>
  );
};

export default CommentaireUser;
