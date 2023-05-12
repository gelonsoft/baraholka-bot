import React, {useState} from 'react';
import '../style/style.css';


class SearchAds extends React.Component {
    constructor(props) {
        super(props);
        this.handleStartClick = this.handleStartClick.bind(this);
        this.handleNewClick = this.handleNewClick.bind(this);
        this.state = {isStarted: false};
    }

    handleStartClick() {
        this.setState({isStarted: true});
    }

    handleNewClick() {
        this.setState({isStarted: false});
    }

    render() {
        const isStarted = this.state.isStarted;
        return <Search isStarted={isStarted} handleStart={this.handleStartClick} handleNew={this.handleNew}/>
    }
}

class FoundAds extends React.Component {
    listOfRef = this.props.contacts.map((contact, index) =>
        <Contacts key={index} contact={contact}/>
    )

    render() {
        return (
            <div className="grid__item">
                <div className="ad__form">
                    <div>{this.props.username}</div>
                    <div className="add__content">
                        {/*TODO:Реализовать отображение фото в объявлении*/}
                        <div className="ad-tags">{this.props.tags}</div>
                        <div className="ad-bottom-padding">Цена: {this.props.price}</div>
                        <div className="ad-bottom-padding">Описание: {this.props.description}</div>
                        <div className="ad-bottom-padding">--------------------------------------------------------
                        </div>
                        <div>Номер телефона: {this.props.phone}</div>
                        <div>Социальные сети:</div>
                        {this.listOfRef}
                        <div className="ad-bottom-padding"></div>
                    </div>
                </div>
            </div>
        );
    }
}

class Contacts extends React.Component {
    render() {
        return <a className="ref-color" href={this.props.contact}>{this.props.contact}</a>
    }
}

function ChooseSearchTags(props) {
    return (
        <form>
            <div className="main__form-title">Город</div>
            <div>Выберите город, по которому хотите выполнить поиск.</div>
            <select>
                <option selected disabled>Не выбран</option>
            </select>
            <div className="main__form-title">Тип объявления</div>
            <div>Выберите типы объявления, по которым хотите выполнить поиск.</div>
            <div className="main__form-row">
                <div className="custom-checkbox">
                    <label>
                        <input type="checkbox"/>
                        Продажа
                    </label>
                </div>
                <div className="custom-checkbox">
                    <label>
                        <input type="checkbox"/>
                        Обмен
                    </label>
                </div>
                <div className="custom-checkbox">
                    <label>
                        <input type="checkbox"/>
                        Дар
                    </label>
                </div>
            </div>
            <div className="main__form-title">Категории</div>
            <div>Выберите категории, по которым хотите выполнить поиск.</div>
            <div className="main__form-row">
                <div className="custom-checkbox">
                    <label>
                        <input type="checkbox"/>
                        Одежда
                    </label>
                </div>
                <div className="custom-checkbox">
                    <label>
                        <input type="checkbox"/>
                        Обувь
                    </label>
                </div>
                <div className="custom-checkbox">
                    <label>
                        <input type="checkbox"/>
                        Книги
                    </label>
                </div>
                <div className="custom-checkbox">
                    <label>
                        <input type="checkbox"/>
                        Хобби
                    </label>
                </div>
                <div className="custom-checkbox">
                    <label>
                        <input type="checkbox"/>
                        Электроника
                    </label>
                </div>
                <div className="custom-checkbox">
                    <label>
                        <input type="checkbox"/>
                        Спорт
                    </label>
                </div>
                <div className="custom-checkbox">
                    <label>
                        <input type="checkbox"/>
                        Красота и здоровье
                    </label>
                </div>
                <div className="custom-checkbox">
                    <label>
                        <input type="checkbox"/>
                        Детские товары
                    </label>
                </div>
                <div className="custom-checkbox">
                    <label>
                        <input type="checkbox"/>
                        Бытовая техника
                    </label>
                </div>
                <div className="custom-checkbox">
                    <label>
                        <input type="checkbox"/>
                        Женское
                    </label>
                </div>
                <div className="custom-checkbox">
                    <label>
                        <input type="checkbox"/>
                        Мужское
                    </label>
                </div>
                <div className="custom-checkbox">
                    <label>
                        <input type="checkbox"/>
                        Другое
                    </label>
                </div>
            </div>
            <input type="submit" className="btn btn-dark" value="Искать" onClick={props.start}/>
        </form>
    )
}

const ads = [
    {
        id: 1,
        username: 'user1',
        tags: '#продажа',
        price: '100',
        description: 'Спецификации Oukitel U7 Pro включают 5.5-дюймовый HD-дисплей (1280 х 720 точек) со стеклом 2.5D. В основе его лежит 4-ядерный процессор Mediatek MT6580 с частотой 1300 МГц, 1 Гб оперативной и 8 Гб встроенной памяти для хранения данных. Смартфон доступен в трёх цветах, он оснащён батареей ёмкостью 2500 мАч, и работает на операционной системе Android 5.1 Lollipop. Смартфон частично поддерживает жестовое управление для запуска некоторых приложений без разблокировки устройства.',
        phone: '89006008090',
        contacts: ['https://vk.com/useruser']
    },
    {
        id: 2, username: 'user2', tags: [
            '#Омск',
            '#продажа',
            '#обмен',
            '#книги',
            '#хобби',
            '#электроника'
        ], price: '100', description: 'desc', phone: '89006008090',
        contacts: []
    }
]

function StartNewSearch(props) {
    // const ads = props.ads;
    const listOfAds = ads.map((ad) =>
        <FoundAds key={ad.id} username={ad.username} tags={ad.tags.toString().replaceAll(",", " ")} price={ad.price}
                  description={ad.description} phone={ad.phone} contacts={ad.contacts}/>
    );
    return (
        <form>
            <div className="main__form-title">Хэштеги</div>
            <div>Вы выбрали следующие хэштеги для выполнения поиска.</div>
            <div className="custom-text">#хэштеги</div>
            <input type="submit" className="btn btn-dark" value="Выполнить новый поиск"/>
            <div className="grid--4">
                {listOfAds}
            </div>
        </form>
    )
}

function Search(props) {
    const isStarted = props.isStarted;
    if (isStarted) {
        return <StartNewSearch new={props.handleNew}/>;
    }
    return <ChooseSearchTags start={props.handleStart}/>;
}

export default SearchAds;