import React from 'react';
import '../style/style.css';
import {Link} from "react-router-dom";

class Help extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="help">
                Раздел <Link to="/baraholka/profile"><span className="help-link">Профиль</span></Link> позволяет просмотреть ваши личные данные из аккаунта Telegram.
                <br></br><br></br>
                Раздел <Link to="/baraholka/my_obyavleniye"><span className="help-link">Мои объявления</span></Link> позволяет посмотреть все опубликованные вами объявления, открыть Telegram для просмотра комментариев под ними, а также удалить объявление. если оно потеряло актуальность.
                <br></br><br></br>
                Раздел <Link to="/baraholka/new_obyavleniye"><span className="help-link">Создать объявление</span></Link> позволяет перейти к процессу создания нового объявления. Вам необходимо ответить на вопросы и заполнить макет объявления. Чтобы прервать создание, нужно перейти на другой раздел.
                <br></br><br></br>
                Раздел <Link to="/baraholka/search_obyavleniye"><span className="help-link">Поиск объявлений</span></Link> позволяет выполнить поиск объявлений по хэштегам, а также открыть в Telegram для просмотра комментариев под ним.
            </div>
        )
    }
}

export default Help;