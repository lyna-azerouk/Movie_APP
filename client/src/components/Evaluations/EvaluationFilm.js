import React, { useContext, useEffect, useState } from 'react';
import { AppContext } from '../../AppContext';
import { useNavigate } from 'react-router-dom';



function noteEnEtoiles(note) {
    const etoilesRemplies = '★'.repeat(note);
    const etoilesVides = '☆'.repeat(5 - note);
    return etoilesRemplies + etoilesVides;
  }

const EvaluationFilm = ({ valeur_movie}) => {
  const { isConnected, identifiant, evaluations, setEvaluations } = useContext(AppContext);
  const [contenuEvaluations, setContenuEvaluations] = useState([]);
  const [shouldRefresh, setShouldRefresh] = useState(false);
  const navigate = useNavigate();

  const handleSubmitevaluation = event => {
    event.preventDefault();
    const data = new FormData(event.target);
    console.log(data.get('evaluation'))
    if (isConnected) {
      fetch(`http://localhost:8080/pc3r-projet/evaluations?typeId=${valeur_movie.id}&evaluation=${data.get('evaluation')}&identifiant=${identifiant}&type=${valeur_movie.type}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title: 'React POST Request Example' })
      })
        .then(res => res.json())
        .then(data => {
            if(data.status){
                setEvaluations(evaluations +1);
                setShouldRefresh(true)
            }
         })
        .catch(error => console.error(error));
    } else {
      navigate('/connexion');
    }
  };

  useEffect(() => {
    fetch(`http://localhost:8080/pc3r-projet/evaluations?typeId=${valeur_movie.id}`)
      .then(response => response.json())
      .then(data => setContenuEvaluations(data.evaluations))
      .catch(error => console.error(error))
      .finally(() => setShouldRefresh(false))
  }, [valeur_movie, shouldRefresh]);


  const contenu = contenuEvaluations.map(evaluation => JSON.parse(evaluation));

  return (
    <div>
      <form className="f" onSubmit={handleSubmitevaluation}>
        <label htmlFor="evaluation">Ajouter une évaluation</label>
        <select id="evaluation" name="evaluation" required>
          <option value="">-- Choisir une note --</option>
          <option value="1">1 étoile</option>
          <option value="2">2 étoiles</option>
          <option value="3">3 étoiles</option>
          <option value="4">4 étoiles</option>
          <option value="5">5 étoiles</option>
        </select>
        <button type="submit">Envoyer</button>
      </form>

      {contenu.length > 0 ? (
        <ul className="co">
        {contenu.map(evaluation => (
          <li key={evaluation.id} className="b">
            <div className="commentaire-header">
              <span className="commentaire-pseudo">{evaluation.userId}</span>
              <span className="commentaire-date">{new Date(evaluation.date.$date).toLocaleDateString()}</span>
            </div>
            <div className="eval-contenu">{noteEnEtoiles(evaluation.note)}</div>
          </li>
        ))}
      </ul>
    ) : (
      <p>Pas d'évaluation pour le moment.</p>
    )}
    </div>
  );
};

export default EvaluationFilm;
