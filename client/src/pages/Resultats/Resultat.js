import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Header from '../../components/Header/Header';

import "./Resultat.css";



function Resultats() {
  const navigate = useNavigate();
  const { state } = useLocation();

  const showdetails = (searchTerm)=>{ 
    if(searchTerm !=null){
     searchTerm = {...searchTerm, first_air_date : searchTerm.date, release_date : searchTerm.date
      , title : searchTerm.titre, 
      overview : searchTerm.resume,
       poster_path :searchTerm.poster , type: 'Film'}
     navigate("/FilmDetail", { state: { data: searchTerm} });
  } 
  }
 
  if (!state.searchResult.status) {
    return (
      <div>
        <Header />
        <h2>Résultats de recherche</h2>
        <p>Aucun résultat trouvé.</p>
      </div>
    );
  } else {
    const films = state.searchResult.films.map((film) => JSON.parse(film));
    const series = state.searchResult.series.map((serie) => JSON.parse(serie));
    return (
      <div>
        <Header />
        <h2>Résultats de recherche</h2>
        <div>
          <h3>Films</h3>
          {films.map((film) => (
            <div key={film.id} className="film">
              <div className="film-image">
                <img className= "container poster-hover" src={`https://image.tmdb.org/t/p/w500${film.poster}`} alt={film.titre} onClick={() =>showdetails(film)} />
              </div>
              <div className="film-details">
                <h4>{film.titre}</h4>
                <div className="film-meta">
                  <div className="film-evaluations">{`Evaluations : ${film.evaluations}`}</div>
                  <div className="film-commentaires">{`Commentaires : ${film.commentaires}`}</div>
                </div>
              </div>
            </div>
          ))}
        </div>
        <div>
          <h3>Séries</h3>
          {series.map((serie) => (
            <div key={serie.id} className="serie">
              <div className="serie-image">
                <img className = "container poster-hover" src={`https://image.tmdb.org/t/p/w500${serie.poster}`} alt={serie.titre} onClick={() =>showdetails(serie)} />
              </div>
              <div className="serie-details">
                <h4>{serie.titre}</h4>
                <div className="serie-meta">
                  <div className="serie-evaluations">{`Evaluations : ${serie.evaluations}`}</div>
                  <div className="serie-commentaires">{`Commentaires : ${serie.commentaires}`}</div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    );
  }
}

export default Resultats;
