import React, { useContext, useEffect, useState } from 'react';
import CommentaireFilm from '../Commentaires/CommentaireFilm'
import EvaluationFilm from '../Evaluations/EvaluationFilm';
import { useLocation } from 'react-router-dom';
import './SingleMovie.css';

const sleep = ms => new Promise(r => setTimeout(r, ms));

const SingleMovie = () => {
  const location = useLocation();
  const valeur_movie = location.state.data;

  return (
    <div className="single-movie">
      <div className="poster">
        <img src={`https://image.tmdb.org/t/p/w500${valeur_movie.poster_path}`} alt={valeur_movie.title} />
        <p>Type: {valeur_movie.type}</p>
        {valeur_movie.type === "Serie" ? (
          <p>Date premier épisode : {valeur_movie.first_air_date}</p>
        ) : (
          <p>Date de sortie : {valeur_movie.release_date}</p>
        )}
      </div>
      <div className="details">
        <h5 className="card-title">{valeur_movie.title}</h5>
        <p> Description: {valeur_movie.overview}</p>
        <CommentaireFilm valeur_movie={valeur_movie} />
        <EvaluationFilm valeur_movie={valeur_movie}/>
      </div>
    </div>
  );
};



export default SingleMovie ;