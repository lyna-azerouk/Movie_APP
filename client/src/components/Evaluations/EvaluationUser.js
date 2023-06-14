import React, { useContext, useState, useEffect } from "react";
import { AppContext } from "../../AppContext";

function noteEnEtoiles(note) {
  const etoilesRemplies = '★'.repeat(note);
  const etoilesVides = '☆'.repeat(5 - note);
  return etoilesRemplies + etoilesVides;
}

const EvaluationUser = () => {
  const { identifiant, evaluations, setEvaluations} = useContext(AppContext);
  const [contenuEvaluations, setContenuEvaluations] = useState([]);
  const [shouldRefresh, setShouldRefresh] = useState(false); 


  useEffect(() => {
    fetch(
      `http://localhost:8080/pc3r-projet/evaluationsutilisateurs?identifiant=${identifiant}`
    )
      .then((res) => res.json())
      .then((data) => setContenuEvaluations(data.evaluations))
      .catch((err) => console.log(err))
      .finally(() => setShouldRefresh(false));
  }, [identifiant,shouldRefresh]);

  const contenu = contenuEvaluations.map((evaluation) =>
    JSON.parse(evaluation)
  );

  const handleSupprimerEvaluation = (evalId, evalTypeId, evalType, identif) => {
    const requestOptions = {
      method: "DELETE",
      headers: { "Content-Type": "application/json" },
    };
    fetch(`http://localhost:8080/pc3r-projet/evaluations?typeId=${evalTypeId}&evaluationId=${evalId}&identifiant=${identif}&type=${evalType}`, requestOptions)
      .then((response) => response.json())
      .then((data) => {
        if (data.status){
          setEvaluations(evaluations-1)
          setShouldRefresh(true); 
        }
      })
      .catch((error) => {
        console.error("Erreur lors de la suppression de l'évaluation :", error);
      });
  };
  
  return (
      <div className="profile-body">
        {contenu.length > 0 ? (
          <ul className="commentaires-list">
          {contenu.map((evaluation) => (
            <li key={evaluation.id} className="commentaire">
              <div className="commentaire-header">
                <span className="film-nom">{evaluation.nom}</span>
                <span className="commentaire-date">{new Date(evaluation.date.$date).toLocaleDateString()}</span>
                <button onClick={() => handleSupprimerEvaluation(evaluation.id, evaluation.typeId, evaluation.type, identifiant)}>Supprimer</button>
              </div>
              <div className="commentaire-contenu">{noteEnEtoiles(evaluation.note)}</div>
            </li>
          ))}
        </ul>
        ) : (
          <p>Pas d'évaluations pour le moment.</p>
        )}
      </div>
  );
};

export default EvaluationUser;
