import './App.css';
import { Routes, Route } from 'react-router-dom';
import Login from "./components/Login";
import Register from "./components/Register";
import ConfirmRegister from "./components/ConfirmRegister";
import Baraholka from "./components/Baraholka";

function App() {
  return (
      <div className="App">
          <Baraholka></Baraholka>
      </div>
  );
}

export default App;
