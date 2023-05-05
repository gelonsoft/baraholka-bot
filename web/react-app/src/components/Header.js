import React from 'react';
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
                <button className="header__exit-btn">
                    Выйти
                </button>
            </header>
        )
    }
}

export default Header;