import React from 'react';
import Home from './pages/Home/Home';
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Resultats from './pages/Resultats/Resultat'
import Connexion from './pages/Connexion/Connexion'
import Inscription from './pages/Inscription/Inscription'
import Profil from './pages/Profil/ProfilPage';
import FilmDetail from './pages/FilmDetail/FilmDetail'
import { AppContextProvider } from './AppContext';

const App = () => {
  return (
    <BrowserRouter>
      <AppContextProvider>
        <Routes>
          <Route path="/" element={<Home />} /> 
          <Route path="/resultats" element={<Resultats />} />
          <Route path="/connexion" element={<Connexion />} />
          <Route path="/inscription" element ={<Inscription />} />
          <Route path="/profil" element={<Profil />} />
          <Route path="/FilmDetail" element={<FilmDetail />} />
        </Routes>
      </AppContextProvider>
    </BrowserRouter>
  );
};

export default App;
