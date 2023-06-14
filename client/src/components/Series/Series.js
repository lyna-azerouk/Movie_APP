import React, { useState, useEffect  } from 'react';
import './Series.css'
import { useNavigate} from 'react-router-dom';


const Series = () => {
  const [series, setSeries] = useState([]);
  const navigate = useNavigate();

  useEffect( () =>  {
    fetch('http://localhost:8080/pc3r-projet/series')
      .then(response => response.json())
      .then(data => setSeries(data.results.slice(0, 6)))
      .catch(error => console.error(error));
  }, []);


  const showdetails = (searchTerm)=>{ 
    if(searchTerm !=null){
     searchTerm = {...searchTerm, type: 'Serie'}
     navigate("/FilmDetail", { state: { data: searchTerm} });
  } 
}
    
  return (
      <div className="container">
        <div className="row">
          {series.map(serie => (
            <div key={serie.id} className="col-2">
              <div className="card mb-4">
                <img className="card-img-top series-container" src={`https://image.tmdb.org/t/p/w500${serie.poster_path}`} alt={serie.name} onClick={() =>showdetails(serie)}  />
                <div className="card-body">
                  <h5 className="card-title">{serie.name}</h5>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    );
  }


export default Series;
