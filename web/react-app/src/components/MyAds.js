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
            ads: []
        };
    }

    componentDidMount() {
        let userData = localStorage.getItem('userData');
        RequestService.getMyAds(userData).then((response) => {
            if (response.data) {
                this.setState({
                    ads: response.data
                });
                console.log("My ads", response.data);
            }
        }).catch(err => {
            console.log(err);
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
        return (<form>
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
        return <img src={`data:image/png;base64,${this.props.photo}`}/>
    }
}

export default MyAds;