import React from 'react';
import '../style/style.css';

class NewAd extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <form>
                <div className="main__form-title">Добавить фотографии</div>
                <div>Добавьте от 1 до 10 фотографий к вашему объявлению. Рекомендуемое число - 5.</div>
                <label className="btn btn-light file-btn">
                    <input type="file"/>
                    Выбрать файл
                </label>
                <div className="main__form-title">Добавить описание</div>
                <div>Добавьте краткое описание товара.</div>
                <input type="text" placeholder="Описание"/>
                <div className="main__form-title">Добавить город</div>
                <div>Выберите город для публикации объявления.</div>
                <select>
                    <option selected disabled>Не выбран</option>
                </select>
                <div className="main__form-title">Добавить тип объявления</div>
                <div>Выберите тип объявления.</div>
                <div className="main__form-row">
                    <div className="custom-checkbox">
                        <label>
                            <input type="checkbox" />
                                Продажа
                        </label>
                    </div>
                    <div className="custom-checkbox">
                        <label>
                            <input type="checkbox" />
                                Обмен
                        </label>
                    </div>
                    <div className="custom-checkbox">
                        <label>
                            <input type="checkbox" />
                                Дар
                        </label>
                    </div>
                </div>
                <div className="main__form-title">Добавить категории</div>
                <div>Выберите категории, наиболее подходящие для описания вашего товара.</div>
                <div className="main__form-row">
                    <div className="custom-checkbox">
                        <label>
                            <input type="checkbox" />
                                Одежда
                        </label>
                    </div>
                    <div className="custom-checkbox">
                        <label>
                            <input type="checkbox" />
                                Обувь
                        </label>
                    </div>
                    <div className="custom-checkbox">
                        <label>
                            <input type="checkbox" />
                                Книги
                        </label>
                    </div>
                    <div className="custom-checkbox">
                        <label>
                            <input type="checkbox" />
                                Хобби
                        </label>
                    </div>
                    <div className="custom-checkbox">
                        <label>
                            <input type="checkbox" />
                                Электроника
                        </label>
                    </div>
                    <div className="custom-checkbox">
                        <label>
                            <input type="checkbox" />
                                Спорт
                        </label>
                    </div>
                    <div className="custom-checkbox">
                        <label>
                            <input type="checkbox" />
                                Красота и здоровье
                        </label>
                    </div>
                    <div className="custom-checkbox">
                        <label>
                            <input type="checkbox" />
                                Детские товары
                        </label>
                    </div>
                    <div className="custom-checkbox">
                        <label>
                            <input type="checkbox" />
                                Бытовая техника
                        </label>
                    </div>
                    <div className="custom-checkbox">
                        <label>
                            <input type="checkbox" />
                                Женское
                        </label>
                    </div>
                    <div className="custom-checkbox">
                        <label>
                            <input type="checkbox" />
                                Мужское
                        </label>
                    </div>
                    <div className="custom-checkbox">
                        <label>
                            <input type="checkbox" />
                                Другое
                        </label>
                    </div>
                </div>
                <div className="main__form-title">Добавить стоимость</div>
                <div>Укажите стоимость товара в рублях, если она имеется (необязательно).</div>
                <input type="text" placeholder="1000"/>
                <div className="main__form-title">Добавить номер телефона</div>
                <div>Добавьте номер телефона (необязательно).</div>
                <input type="tel" pattern="+7-[0-9]{3}-[0-9]{3}-[0-9]{2}-[0-9]{2}" placeholder="+7-900-000-00-00" />
                <div className="main__form-title">Добавить социальные сети</div>
                <div>Добавьте ссылки на ваши социальные сети (необязательно).</div>
                <input type="tel" pattern="https://.+/.+" placeholder="https://vk.com/useruser"/>
                <button className="btn btn-light">Добавить социальную сеть</button><br />
                <input type="submit" className="btn btn-dark" value="Опубликовать"/>
            </form>
        )
    }
}

export default NewAd;