import React, { useState } from 'react';
import './Inscription.css';
import Profile from '../Profile/Profile2';
import { useNavigate } from 'react-router-dom';


const Inscription = () => {
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();


  function handleSubmit(event) {
    event.preventDefault();
    const data = new FormData(event.target);
    // Change ici ton port pour tester sur ta machine // 
    fetch('http://localhost:8080/pc3r-projet/inscription?identifiant=' + data.get("identifiant") + "&password=" + data.get("password"), {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title: 'React POST Request Example' })
    })
      .then((res) => res.json())
      .then((data) => {
        console.log(data);
        if (data.status === true) {
          navigate("/connexion");
        } else {
          setErrorMessage(data.message);
        }
      });
  }

  return (
    <div className="auth-form-container">
      <h2>Inscription</h2>
      <form className="register-form" onSubmit={handleSubmit}>
        <label htmlFor="identifiant">
          Veuillez choisir un identifiant
        </label>
        <input id="identifiant" type="text" placeholder="Votre identifiant" name="identifiant" />

        <label htmlFor="password">
          Veuillez choisir un mot de passe
        </label>
        <input id="password" type="password" name="password" placeholder="********" />
        <br />
        <button type="submit">Inscription</button>
        {errorMessage && <p>{errorMessage}</p>}
      </form>
    </div>
  );
}

export default Inscription;
