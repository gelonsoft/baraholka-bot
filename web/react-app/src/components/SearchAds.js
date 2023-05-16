import React, {useState} from 'react';
import Flickity from "react-flickity-component";
import '../style/style.css';
import '../style/flickity.css';
import ReactSpoiler from "react-spoiler";
import RequestService from '../services/RequestService';

const types = ['#продажа', '#обмен', '#дар', '#торг_уместен', '#срочно']
const categories = ['#одежда', '#обувь', '#детские_товары', '#красота_и_здоровье', '#книги', '#хобби', '#домашняя_техника', '#электроника', '#спорт', '#другое', '#мужское', '#женское']
const cities = ['Москва', 'СПб', 'Екатеринбург', 'Челябинск', 'Ульяновск', 'Омск', 'Белгород', 'Петропавловск', 'Пермь', 'Волгоград', 'Киров', 'Хабаровск']

class SearchAds extends React.Component {
    constructor(props) {
        super(props);
        this.handleStartClick = this.handleStartClick.bind(this);
        this.handleNewClick = this.handleNewClick.bind(this);
        this.state = {
            isStarted: false, choosenTags: [], ads: [], currentCity: 'Не выбран'
        };
    }

    handleCheckboxChange = (event) => {
        let currentTags = [...this.state.choosenTags, event.target.value];
        if (this.state.choosenTags.includes(event.target.value)) {
            currentTags = currentTags.filter(tag => tag !== event.target.value);
        }
        this.setState({choosenTags: currentTags});
    };

    handleSelectChange = (event) => {
        let currentTags = this.state.choosenTags;
        if (event.target.value !== "Не выбран"){
            currentTags = [...this.state.choosenTags, event.target.value];
        }
            if (this.state.choosenTags.includes(this.state.currentCity)) {
                currentTags = currentTags.filter(tag => tag !== this.state.currentCity);
            }
            this.setState({choosenTags: currentTags, currentCity: event.target.value});
    };

    handleStartClick() {
        //TODO: Заменить на let userData = localStorage.getItem('userData');
        let userData = {"auth_date":1684051188,"first_name":"Алиса","hash":"afc6a8181ae6eb8f494551c94c39a63fae2835470210428556f8db7f54b66603","id":538160964,"last_name":"Селецкая","photo_url":"https://t.me/i/userpic/320/Uim0VYUr3WRDc7ofnIj40wRzPe1L7t63Nv0FXKqydjM.jpg","username":"sealisaa"};
        RequestService.getSearchAds(userData, this.state.choosenTags).then((response) => {
            if (response.data) {
                this.setState({
                    ads: response.data
                });
            }
        }).catch(err => {
            console.log(err);
        });
        this.setState({isStarted: true});
    }

    handleNewClick() {
        this.setState({isStarted: false, choosenTags: [], ads: []});
    }

    render() {
        const isStarted = this.state.isStarted;
        if (isStarted) {
            return <StartNewSearch ads={this.state.ads} choosenTags={this.state.choosenTags} new={this.handleNewClick}/>;
        }
        return <ChooseSearchTags change={this.handleCheckboxChange} start={this.handleStartClick} select={this.handleSelectChange}/>;
    }
}

function ChooseSearchTags(props) {
    const listOfTypes = types.map((type, index) => <CheckBox key={index} lable={type} change={props.change}/>)
    const listOfCategories = categories.map((category, index) => <CheckBox key={index} lable={category} change={props.change}/>)
    const listOfCities = cities.map((city, index) => <SelectItem key={index} lable={city}/>)
    return (<form>
        <div className="main__form-title">Город</div>
        <div>Выберите город, по которому хотите выполнить поиск.</div>
        <select onChange={props.select}>
            <option selected value="Не выбран">Не выбран</option>
            {listOfCities}
        </select>
        <div className="main__form-title">Тип объявления</div>
        <div>Выберите типы объявления, по которым хотите выполнить поиск.</div>
        <div className="main__form-row">
            {listOfTypes}
        </div>
        <div className="main__form-title">Категории</div>
        <div>Выберите категории, по которым хотите выполнить поиск.</div>
        <div className="main__form-row">
            {listOfCategories}
        </div>
        <input type="button" className="btn btn-dark ad__form" value="Искать" onClick={props.start}/>
    </form>)
}

class CheckBox extends React.Component {
    render() {
        return (
            <div className="custom-checkbox">
                <label>
                    <input type="checkbox" value={this.props.lable} onChange={this.props.change}/>
                    {this.props.lable}
                </label>
            </div>
        )
    }
}

function SelectItem(props){
    return <option value={"#"+props.lable.toString()}>{props.lable}</option>
}


function StartNewSearch(props) {
    const listOfAds = props.ads?.map((ad) => <FoundAds key={ad.id} username={ad.username} photos={ad.photos}
    // const listOfAds = ads1?.map((ad) => <FoundAds key={ad.id} username={ad.username} photos={ad.photos}
                                                  tags={ad.tags.toString().replaceAll(",", " ")}
                                                  price={ad.price}
                                                  description={ad.description} phone={ad.phone}
                                                  contacts={ad.contacts}/>);
    return (<form>
        <div className="main__form-title">Хэштеги</div>
        <div>Вы выбрали следующие хэштеги для выполнения поиска.</div>
        <div className="custom-text">{props.choosenTags?.toString().replaceAll(",", " ")}</div>
        <input type="button" className="btn btn-dark ad__form" value="Выполнить новый поиск" onClick={props.new}/>
        <div className="grid">
            {listOfAds}
        </div>
    </form>)
}

const flickityOptions = {
    setGallerySize: false
}

class FoundAds extends React.Component {
    listOfRef = this.props.contacts.map((contact, index) => <Contact key={index} contact={contact}/>)
    listOfPhotos = this.props.photos.map((photo, index) => <Photo key={index} photo={photo}/>)
    checkPrice = this.props.price == null ? "none" : "block";
    checkPhone = this.props.phone == null ? "none" : "block";
    checkContacts = this.listOfRef.length === 0 ? "none" : "block";

    render() {
        return (<div className="ad__form">
            <div className="text__padding">{this.props.username}</div>
            <div className="add__content">
                <div className="ad__photo">
                    <Carousel photos={this.listOfPhotos}/>
                </div>
                <div className="text__padding">
                    <div className="ad-tags">{this.props.tags}</div>
                    <span style={{display: this.checkPrice}}
                          className="ad-bottom-padding">Цена: {this.props.price} руб.</span>
                    <div className="ad-bottom-padding">Описание: {this.props.description}</div>
                    <div className="ad-bottom-padding">--------------------------------------------------------
                    </div>
                    <ReactSpoiler blur={10} hoverBlur={8}>
                        <span style={{display: this.checkPhone}}>Номер телефона: {this.props.phone}</span>
                        <span style={{display: this.checkContacts}}>Контакты:</span>
                        {this.listOfRef}
                    </ReactSpoiler>
                </div>
            </div>
        </div>);
    }
}

class Contact extends React.Component {
    render() {
        return <a className="ref-color" href={this.props.contact}>{this.props.contact}</a>
    }
}


function Carousel(props) {
    const listOfPhotos = props.photos
    return (<Flickity
        className={'carousel'}
        options={flickityOptions}
    >
        {listOfPhotos}
    </Flickity>);
}


class Photo extends React.Component {
    render() {
        return <img src={`data:image/png;base64,${this.props.photo}`} height="400"/>
    }
}

export default SearchAds;