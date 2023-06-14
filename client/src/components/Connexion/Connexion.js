import React, { useState, useContext } from 'react';
import './Connexion.css';
import Profile from '../Profile/Profile2';
import { useNavigate } from 'react-router-dom';
import { AppContext } from '../../AppContext';

const Connexion = () => {
  const { setIsConnected, setIdentifiant, setCommentaires, setEvaluations } = useContext(AppContext);
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  function handleSubmit(event) {
    event.preventDefault();
    const data = new FormData(event.target);
    fetch('http://localhost:8080/pc3r-projet/connexion?identifiant=' + data.get("identifiant") + "&password=" + data.get("password"), {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title: 'React POST Request Example' })
    })
    .then((res) => res.json())
    .then((data) => {
      if (data.status === true) {
        setIdentifiant(data.identifiant)
        setCommentaires(data.commentaires)
        setEvaluations(data.evaluations)
        setIsConnected(true)
        navigate("/", { state: { connexion: data.status } });
      } else {
        setErrorMessage(data.message);
      }
    });
  }

  return (
    <div className="auth-form-container">
      <form className="register-form" onSubmit={handleSubmit}>
        <label htmlFor="identifiant">Identifiant</label>
        <input id="identifiant" type="text" placeholder="Identifiant" name="identifiant" />
        <label htmlFor="password">Mot de passe</label>
        <input id="password" type="password" name="password" placeholder="********" />
        <br />
        <button type="submit">Connexion</button>
        {errorMessage && <p>{errorMessage}</p>}
      </form>
    </div>
  );
}

export default Connexion;
