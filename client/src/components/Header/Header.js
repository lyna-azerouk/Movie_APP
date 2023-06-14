import React, { useContext} from "react";
import SearchBar from "../SearchBar/SearchBar";
import { Link } from 'react-router-dom';
import { AppContext } from "../../AppContext";
import { useNavigate} from 'react-router-dom';


const Header = () => {
  const { isConnected, setIdentifiant, setIsConnected } = useContext(AppContext);
  const navigate = useNavigate()

  function handleDeconnexion() {
    fetch('/deconnexion')
      .then((response) => {
        if (response.status === 200) {
          setIdentifiant('');
          setIsConnected(false);
          navigate('/');
        } else {
          console.error(response);
        }
      })
      .catch((error) => {
        console.error(error);
      });
  }
  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
      <div className="container-fluid">
        <Link to="/" className="navbar-brand">Apanyan Series & Movies</Link>
        <div className="d-flex justify-content-center">
          <SearchBar />
        </div>
        <div>
          {isConnected ? (
            <>
              <Link to="/profil" className="btn btn-outline-light me-2">Profil</Link>
              <button onClick={handleDeconnexion}>Déconnexion</button> 
           </>
          ) : (
            <>
              <Link to="/inscription" className="btn btn-outline-light me-2">Inscription</Link>
              <Link to="/connexion" className="btn btn-outline-light">Connexion</Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Header;
