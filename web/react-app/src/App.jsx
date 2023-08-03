import './App.css';
import './style/style.css';
import { Routes, Route } from 'react-router-dom';
import Login from "./components/Login";
import Baraholka from "./components/Baraholka";
import React from 'react';
import Profile from "./components/Profile";
import MyObjav from "./components/MyObjav";
import NewObjav from "./components/NewObjav";
import SearchAds from "./components/SearchAds";
import DeletedAds from "./components/DeletedAds";
import Help from "./components/Help";

function App() {
  return (
      <div className="app">
          <Routes>
              <Route path="/" element={<Login />} />
              <Route path="/baraholka" element={<Baraholka />}>
                  <Route path="/baraholka/profile" element={<Profile />} />
                  <Route path="/baraholka/my_obyavleniye" element={<MyObjav />} />
                  <Route path="/baraholka/new_obyavleniye" element={<NewObjav />} />
                  <Route path="/baraholka/search_obyavleniye" element={<SearchAds />} />
                  <Route path="/baraholka/deleted_obyavleniye" element={<DeletedAds />} />
                  <Route path="/baraholka/help" element={<Help />} />
              </Route>
          </Routes>
      </div>
  );
}

export default App;
