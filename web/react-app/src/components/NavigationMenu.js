import React from 'react';
import '../style/style.css';
import {Link, useLocation} from "react-router-dom";

const NavigationMenu = props => {
    const location = useLocation();
    React.useEffect(() => {
        console.log(location);
    }, [location]);
    let currentTab = location.pathname;
    return (
        <div className="main__menu">
            <Link to="/baraholka/profile">
                <div className={
                    location.pathname === "/baraholka/profile" ? "main__menu-tab main__menu-tab-active" : "main__menu-tab"
                }>
                    <img src={require('../img/user.png')} alt="Профиль" className="main__menu-tab-pic"/>
                    <span>Профиль</span>
                </div>
            </Link>
            <Link to="/baraholka/my_ads">
                <div className={
                    location.pathname === "/baraholka/my_ads" ? "main__menu-tab main__menu-tab-active" : "main__menu-tab"
                }>
                    <img src={require('../img/ad.png')} alt="Мои объявления" className="main__menu-tab-pic"/>
                    <span>Мои объявления</span>
                </div>
            </Link>
            <Link to="/baraholka/new_ad">
                <div className={
                    location.pathname === "/baraholka/new_ad" ? "main__menu-tab main__menu-tab-active" : "main__menu-tab"
                }>
                    <img src={require('../img/new-ad.png')} alt="Создать объявление" className="main__menu-tab-pic"/>
                    <span>Создать объявление</span>
                </div>
            </Link>
            <Link to="/baraholka/search_ads">
                <div className={
                    location.pathname === "/baraholka/search_ads" ? "main__menu-tab main__menu-tab-active" : "main__menu-tab"
                }>
                    <img src={require('../img/search.png')} alt="Поиск объявлений" className="main__menu-tab-pic"/>
                    <span>Поиск объявлений</span>
                </div>
            </Link>
            <Link to="/baraholka/help">
                <div className={
                    location.pathname === "/baraholka/help" ? "main__menu-tab main__menu-tab-active" : "main__menu-tab"
                }>
                    <img src={require('../img/help.png')} alt="Помощь" className="main__menu-tab-pic"/>
                    <span>Помощь</span>
                </div>
            </Link>
        </div>
    )
}

export default NavigationMenu;