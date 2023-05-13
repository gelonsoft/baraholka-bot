import React from 'react';
import '../style/style.css';
import {Link} from "react-router-dom";

class NavigationMenu extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            currentTab: "profile"
        }
    }

    render() {
        return (
            <div className="main__menu">
                <Link to="/baraholka/profile">
                    <div className={
                        this.state.currentTab === "profile" ? "main__menu-tab main__menu-tab-active" : "main__menu-tab"
                    }
                    onClick={
                        () => {
                            this.setState({currentTab: "profile"});
                        }
                    }>
                        <img src={require('../img/user.png')} alt="Профиль" className="main__menu-tab-pic"/>
                        <span>Профиль</span>
                    </div>
                </Link>
                <Link to="/baraholka/my_ads">
                    <div className={
                        this.state.currentTab === "my_ads" ? "main__menu-tab main__menu-tab-active" : "main__menu-tab"
                    }
                     onClick={
                         () => {
                             this.setState({currentTab: "my_ads"});
                         }
                     }>
                        <img src={require('../img/ad.png')} alt="Мои объявления" className="main__menu-tab-pic"/>
                        <span>Мои объявления</span>
                    </div>
                </Link>
                <Link to="/baraholka/new_ad">
                    <div className={
                        this.state.currentTab === "new_ad" ? "main__menu-tab main__menu-tab-active" : "main__menu-tab"
                    }
                    onClick={
                        () => {
                            this.setState({currentTab: "new_ad"});
                        }
                    }>
                        <img src={require('../img/new-ad.png')} alt="Создать объявление" className="main__menu-tab-pic"/>
                        <span>Создать объявление</span>
                    </div>
                </Link>
                <Link to="/baraholka/search_ads">
                    <div className={
                        this.state.currentTab === "search_ad" ? "main__menu-tab main__menu-tab-active" : "main__menu-tab"
                    }
                    onClick={
                        () => {
                            this.setState({currentTab: "search_ad"});
                        }
                    }>
                        <img src={require('../img/search.png')} alt="Поиск объявлений" className="main__menu-tab-pic"/>
                        <span>Поиск объявлений</span>
                    </div>
                </Link>
                <Link to="/baraholka/help">
                    <div className={
                        this.state.currentTab === "help" ? "main__menu-tab main__menu-tab-active" : "main__menu-tab"
                    }
                    onClick={
                        () => {
                            this.setState({currentTab: "help"});
                        }
                    }>
                        <img src={require('../img/help.png')} alt="Помощь" className="main__menu-tab-pic"/>
                        <span>Помощь</span>
                    </div>
                </Link>
            </div>
        )
    }
}

export default NavigationMenu;