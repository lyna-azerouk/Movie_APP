import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function SearchBar(props) {
  const [searchTerm, setSearchTerm] = useState("");
  const navigate = useNavigate();

  const handleInputChange = (event) => {
    setSearchTerm(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    if (searchTerm !== "") {
      // Change ici le port // 
      fetch(`http://localhost:8080/pc3r-projet/recherche?recherche=${searchTerm}`)
        .then((res) => res.json())
        .then((data) => {
          if (data !== null) {
            navigate("/resultats", { state: { searchResult: data } });
          }
        })
        .catch((error) => console.error(error));
    }
  };

  return (
    <form className="form-inline my-2 my-lg-0 mx-auto d-flex" onSubmit={handleSubmit}>
      <input
        className="form-control mr-sm-2"
        type="search"
        placeholder="Rechercher un film, une série TV"
        aria-label="Rechercher"
        value={searchTerm}
        onChange={handleInputChange}
      />
      <button className="btn btn-outline-light my-2" type="submit">
        Rechercher
      </button>
    </form>
  );
}

export default SearchBar;
