import React from 'react';
import '../style/style.css';
import {Outlet} from 'react-router-dom';
import NavigationMenu from "./NavigationMenu";
import Header from "./Header";

class Baraholka extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let userData = localStorage.getItem('userData');
        if (userData) {
            return (
                <div>
                    <Header/>
                    <div className="baraholka">
                        <NavigationMenu/>
                        <div className="main">
                            <Outlet></Outlet>
                        </div>
                    </div>
                </div>
            )
        } else return (
            <div className="login-container">
                <div className="login">
                    Чтобы иметь доступ к контенту, необходимо авторизоваться
                </div>
            </div>
        )
    }
}

export default Baraholka;