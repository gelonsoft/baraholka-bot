import React from 'react';
import {Link} from "react-router-dom";
import '../style/style.css';

class Header extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <header>
                <div className="header__logo">
                    <div className="header__logo-icon">
                    </div>
                    <div className="header__logo-text">
                        baraholka-bot
                    </div>
                </div>
                <Link to="/">
                    <button className="header__exit-btn">
                        Выйти
                    </button>
                </Link>
            </header>
        )
    }
}

export default Header;