import React, { useState, useEffect } from 'react';
import './Movies.css';
import { useNavigate} from 'react-router-dom';

const Movies = () => {
  const [movies, setMovies] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch('http://localhost:8080/pc3r-projet/movies')
      .then(response => response.json())
      .then(data => setMovies(data.results.slice(0, 6)))
      .catch(error => console.error(error));
  }, []);

const showdetails = (searchTerm)=>{ 
       if(searchTerm !=null){
        searchTerm = {...searchTerm, type: 'Film'}
        navigate("/FilmDetail", { state: { data: searchTerm} });
     } 
  }

  return (
    <div className="container">
    <div className="row">
      {movies.map(movie => (
        <div key={movie.id} className="col-2"    >
          <div className="card mb-4">
      
            <img className="card-img-top movies-container" src={`https://image.tmdb.org/t/p/w500${movie.poster_path}`} alt={movie.title}  onClick={() =>showdetails(movie)} />
            <div className="card-body">
              <h5 className="card-title">{movie.title}</h5>
            </div>
          </div>
        </div>
      ))}
    </div>


  </div>


);
};

export default Movies;
