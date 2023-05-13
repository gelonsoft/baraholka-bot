import React from 'react';
import '../style/style.css';
import {Outlet, useLocation} from 'react-router-dom';
import NavigationMenu from "./NavigationMenu";
import Header from "./Header";

const Baraholka = props => {
    const location = useLocation();
    const userData = location.state;
    return <BaraholkaContent userData={userData} {...props} />
}


class BaraholkaContent extends React.Component {
    constructor(props) {
        super(props);
        console.log(props.userData);
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