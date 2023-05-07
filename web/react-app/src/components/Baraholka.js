import React from 'react';
import '../style/style.css';
import {Route, Routes} from 'react-router-dom';
import NavigationMenu from "./NavigationMenu";
import Profile from "./Profile";
import MyAds from "./MyAds";
import NewAd from "./NewAd";
import SearchAds from "./SearchAds";
import DeletedAds from "./DeletedAds";
import Help from "./Help";
import Header from "./Header";

class Baraholka extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <Header/>
                <div className="app">
                    <NavigationMenu/>
                    <div className="main">
                        <Routes>
                            <Route path="/" element={<Profile />} />
                            <Route path="/my_ads" element={<MyAds />} />
                            <Route path="/new_ad" element={<NewAd />} />
                            <Route path="/search_ads" element={<SearchAds />} />
                            <Route path="/deleted_ads" element={<DeletedAds />} />
                            <Route path="/help" element={<Help />} />
                        </Routes>
                    </div>
                </div>
            </div>
        )
    }
}

export default Baraholka;