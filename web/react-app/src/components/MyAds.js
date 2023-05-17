import React, {useState} from 'react';
import Flickity from "react-flickity-component";
import '../style/style.css';
import '../style/flickity.css';
import ReactSpoiler from "react-spoiler";
import RequestService from "../services/RequestService";


class MyAds extends React.Component {
    constructor(props) {
        super(props);
        this.handleDeleteClick = this.handleDeleteClick.bind(this);
        this.state = {
            ads: [],
            error : false
        };
    }

    componentDidMount() {
        let userData = localStorage.getItem('userData');
       // let userData = {"auth_date":1684270687,"first_name":"Valeria","hash":"1155ef8296632b5a507bdc602c3f920896f5a26d4602fbab62d5258cc1399266","id":654017127,"last_name":"Lyashenko","photo_url":"https://t.me/i/userpic/320/nq9IaEKOIkhrECFMvSDkNrde4NL2J9QZozdni9dxttw.jpg","username":"leovaldez00"}

        RequestService.getMyAds(userData).then((response) => {
            if (response.data) {
                this.setState({
                    ads: response.data,
                    error : false
                });
                console.log("My ads", response.data);
            }
        }).catch(err => {
            console.log(err);
            this.setState({
                error : true
            });
        });
    }

    handleDeleteClick(val) {
        let userData = localStorage.getItem('userData');
        RequestService.postDeleteAd(userData, val).then((response) => {
            if (response.data) {
                let deleteAds = this.state.ads.find(ad => ad.id === val);
                this.setState({
                    ads: this.state.ads.filter(ad => ad !== deleteAds)
                });
                console.log("Del ads", response.data);
            }
        }).catch(err => {
            console.log(err);
        });
    }


    render() {
        const listOfAds = this.state.ads?.map((ad) => <FoundAds key={ad.id} username={ad.username} photos={ad.photos}
                                                                tags={ad.tags.toString().replaceAll(",", " ")}
                                                                price={ad.price}
                                                                description={ad.description} phone={ad.phone}
                                                                contacts={ad.contacts}
                                                                delete={this.handleDeleteClick}/>);
        const msg = this.state.error ? "Ошибка при получении данных" : listOfAds.length === 0 ? "У вас нет новых объявлений" : "Ваши объявления"

        return (<form>
            <div className="main__form-title">{msg}</div>
            <div className="grid">
                {listOfAds}
            </div>
        </form>)
    }
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
    checkLine = this.checkPhone === "none" && this.checkContacts === "none"? "none" : "block";

    render() {
        return (<div className="ad__form">
            <div className="main__form-row">
                <div className="text__padding">{this.props.username}</div>
                <button type="button" className="del-btn" onClick={() => this.props.delete(this.props.id)}>
                    <img src={require('../img/del_ad.png')} height="20"/>
                </button>
            </div>

            <div className="add__content">
                <div className="ad__photo">
                    <Carousel photos={this.listOfPhotos}/>
                </div>
                <div className="text__padding">
                    <div className="ad-tags">{this.props.tags}</div>
                    <span style={{display: this.checkPrice}}
                          className="ad-bottom-padding">Цена: {this.props.price} руб.</span>
                    <div className="ad-bottom-padding">Описание: {this.props.description}</div>
                    <span className="ad-bottom-padding" style={{display: this.checkLine}}>--------------------------------------------------------</span>
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

export default MyAds;