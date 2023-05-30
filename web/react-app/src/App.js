import './App.css';
import './style/style.css';
import { Routes, Route } from 'react-router-dom';
import Login from "./components/Login";
import Baraholka from "./components/Baraholka";
import React from 'react';
import Profile from "./components/Profile";
import MyAds from "./components/MyAds";
import NewAd from "./components/NewAd";
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
                  <Route path="/baraholka/my_ads" element={<MyAds />} />
                  <Route path="/baraholka/new_ad" element={<NewAd />} />
                  <Route path="/baraholka/search_ads" element={<SearchAds />} />
                  <Route path="/baraholka/deleted_ads" element={<DeletedAds />} />
                  <Route path="/baraholka/help" element={<Help />} />
              </Route>
          </Routes>
      </div>
  );
}

export default App;
