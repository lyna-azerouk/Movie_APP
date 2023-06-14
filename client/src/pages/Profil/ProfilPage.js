import React from 'react';
import Profil from '../../components/Profile/Profile2';
import Header from '../../components/Header/Header';

const Home = () => {
  return (
    <div>
      <Header isConnected={true}/>
      <Profil/>
    </div>
  );
};

export default Home;
