import React from 'react';
import '../style/style.css';
import {Outlet} from 'react-router-dom';
import NavigationMenu from "./NavigationMenu";
import Header from "./Header";

class Baraholka extends React.Component {
    constructor(props) {
        super(props);
        console.log(JSON.parse(localStorage.getItem('userData')));
    }

    render() {
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
    }
}

export default Baraholka;