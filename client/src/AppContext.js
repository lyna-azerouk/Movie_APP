import { createContext, useState } from "react";

const AppContext = createContext();

function AppContextProvider({ children }) {
  const [isConnected, setIsConnected] = useState(false);
  const [identifiant, setIdentifiant] = useState('');
  const [commentaires, setCommentaires] = useState(0);
  const [evaluations, setEvaluations] = useState(0);

  return (
    <AppContext.Provider value={{ isConnected, setIsConnected, identifiant, setIdentifiant,commentaires,setCommentaires, evaluations, setEvaluations }}>
      {children}
    </AppContext.Provider>
  );
}

export { AppContext, AppContextProvider };
