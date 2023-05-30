import React from 'react';
import '../style/style.css';
import {Navigate} from 'react-router-dom';
import TelegramLoginButton from 'react-telegram-login';

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            authorized: false
        }
        this.handleTelegramResponse = this.handleTelegramResponse.bind(this);
    }

    handleTelegramResponse(response) {
        console.log(response);
        let userData = {
            auth_date: response.auth_date,
            first_name: response.first_name,
            hash: response.hash,
            id: response.id,
            last_name: response.last_name,
            photo_url: response.photo_url,
            username: response.username
        }
        localStorage.setItem('userData', JSON.stringify(userData));
        this.setState({
            authorized: true,
        });
    };

    render() {
        if (this.state.authorized) {
            return <Navigate to="/baraholka/profile" />
        }
        return (
            <div className="login-container">
                <div className="login">
                    <p>Функции веб-сервиса:<br/>1. Добавление объявлений<br/>2. Удаление объявлений<br/>3. Поиск по акутальным объявлениям<br/>4. Все актуальные объявления пользователя</p>
                    <TelegramLoginButton dataOnauth={this.handleTelegramResponse} botName="BaraholkaAdBot" />
                </div>
            </div>
        )
    }
}

export default Login;