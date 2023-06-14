import React, { useState, useEffect } from 'react';
import { useLocation } from "react-router-dom";

import Movies from '../../components/Movies/Movies';
import Series from '../../components/Series/Series';
import Header from '../../components/Header/Header';

const Home = () => {
  const { state } = useLocation();
  const [isConnected, setIsConnected] = useState(false)

  useEffect(() => {
    if (state && state.connexion){
      setIsConnected(true)
    }
  }, [state]);

  return (
    <div>
      <Header isConnected={isConnected}/>
      <h1 className="text-center my-5">Top films du moments</h1>  
      <Movies />
      <h1 className="text-center my-5"> Top séries populaires</h1>
      <Series />
    </div>
  );
};

export default Home;
