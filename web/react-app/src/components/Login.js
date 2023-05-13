import React from 'react';
import '../style/style.css';
import {Navigate} from 'react-router-dom';
import TelegramLoginButton from 'react-telegram-login';

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            authorized: false,
            userData: null
        }
        this.handleTelegramResponse = this.handleTelegramResponse.bind(this);
    }

    handleTelegramResponse(response) {
        console.log(response);
        this.setState({
            authorized: true,
            userData: response
        });
    };

    render() {
        if (this.state.authorized) {
            const userData = this.state.userData;
            return <Navigate to="/baraholka/profile" state={userData} />
        }

        return (
            <div className="login-container">
                <div className="login">
                    <p>Тут какой-то текст про то какой классный у нас бот и что он вообще делает</p>
                    <TelegramLoginButton dataOnauth={this.handleTelegramResponse} botName="BaraholkaAdBot" />
                </div>
            </div>
        )
    }
}

export default Login;