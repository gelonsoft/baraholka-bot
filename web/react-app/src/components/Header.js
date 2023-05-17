import React from 'react';
import {Navigate} from "react-router-dom";
import '../style/style.css';

class Header extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            logout: false
        };
        this.logout = this.logout.bind(this);
    }

    logout() {
        localStorage.removeItem('userData');
        this.setState({logout: true});
    }

    render() {
        if (this.state.logout) {
            return <Navigate to="/" />
        }
        return (
            <header>
                <div className="header__logo">
                    <div className="header__logo-icon">
                    </div>
                    <div className="header__logo-text">
                        baraholka-bot
                    </div>
                </div>
                <button onClick={this.logout} className="header__exit-btn">
                    Выйти
                </button>
            </header>
        )
    }
}

export default Header;