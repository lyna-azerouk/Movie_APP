import React, { useContext, useEffect, useState, useRef } from 'react';
import { AppContext } from '../../AppContext';
import { useNavigate } from 'react-router-dom';

import './CommentaireFilm.css'


const CommentaireFilm = ({ valeur_movie, type }) => {
  const { isConnected, identifiant, commentaires, setCommentaires } = useContext(AppContext);
  const [contenuCommentaires, setContenuCommentaires] = useState([]);
  const [shouldRefresh, setShouldRefresh] = useState(false);
  const navigate = useNavigate();
  const formRef = useRef(null);

  const handleSubmitCommentaire = event => {
    event.preventDefault();
    const data = new FormData(event.target);
    if (isConnected) {
      fetch(`http://localhost:8080/pc3r-projet/commentaires?type=${valeur_movie.type}&typeId=${valeur_movie.id}&commentaire=${data.get('commentaire')}&identifiant=${identifiant}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
      })
        .then(res => res.json())
        .then(data => {
          if (data.status) {
            setCommentaires(commentaires + 1);
            setShouldRefresh(true);
            formRef.current.reset(); // réinitialiser le formulaire
          }
        })
        .catch(error => console.error(error));
    } else {
      navigate('/connexion');
    }
  };

  useEffect(() => {
    fetch(`http://localhost:8080/pc3r-projet/commentaires?typeId=${valeur_movie.id}`)
      .then(response => response.json())
      .then(data => setContenuCommentaires(data.commentaires))
      .catch(error => console.error(error))
      .finally(() => setShouldRefresh(false));
  }, [valeur_movie, shouldRefresh]);

  const contenu = contenuCommentaires.map(commentaire => JSON.parse(commentaire));

  return (
    <div>
    <form ref={formRef} className="form" onSubmit={handleSubmitCommentaire}>
        <label htmlFor="commentaire">Ajouter un commentaire</label>
        <textarea id="commentaire" name="commentaire" placeholder="Ecrire un commentaire..." required />
        <button type="submit">Envoyer</button>
    </form>


      {contenu.length > 0 ? (
        <ul className="co">
          {contenu.map(commentaire => (
            <li key={commentaire.id} className="c">
              <div className="commentaire-header">
                <span className="commentaire-pseudo">{commentaire.userId}</span>
                <span className="commentaire-date">{new Date(commentaire.date.$date).toLocaleDateString()}</span>
              </div>
              <div className="commentaire-contenu">{commentaire.contenu}</div>
            </li>
          ))}
        </ul>
      ) : (
        <p>Pas de commentaires pour le moment.</p>
      )}
      </div> 
  )
      }

export default CommentaireFilm; 
     
   
