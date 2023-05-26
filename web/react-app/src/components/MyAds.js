import React from 'react';
import '../style/style.css';
import ReactSpoiler from "react-spoiler";
import RequestService from "../services/RequestService";
import ReadMoreReact from 'read-more-react';
import Rodal from 'rodal';
import '../style/rodal.css';
import AliceCarousel from "react-alice-carousel";
import ReactLoading from "react-loading";
import Contact from "../services/Contact";
import Photo from "../services/Photo";


class MyAds extends React.Component {
    constructor(props) {
        super(props);
        this.handleDeleteClick = this.handleDeleteClick.bind(this);
        this.state = {
            ads: [],
            msg: "Загрузка данных"
        };
    }

    componentDidMount() {
        let userData = localStorage.getItem('userData');
        RequestService.getMyAds(userData).then((response) => {
            if (response.data) {
                this.setState({
                    ads: response.data,
                    msg: response.data.length === 0 ? "У вас нет новых объявлений" : "Ваши актуальные объявления"
                });
                console.log("My ads", response.data);
            }
        }).catch(err => {
            console.log(err);
            this.setState({
                msg: "Ошибка при получении данных. " + err
            });
        });
    }

    handleDeleteClick(val) {
        let userData = localStorage.getItem('userData');
        RequestService.postDeleteAd(userData, val).then((response) => {
            if (response.status === 200) {
                let deleteAds = this.state.ads.find(ad => ad.message_id === val);
                let adsSize = this.state.ads.length - 1;
                this.setState({
                    ads: this.state.ads.filter(ad => ad !== deleteAds),
                    msg: adsSize === 0 ? "У вас нет новых объявлений" : "Ваши актуальные объявления"
                });
            }
        }).catch(err => {
            console.log(err);
        });
    }


    render() {
        const listOfAds = this.state.ads?.map((ad) => <FoundAds key={ad.message_id} id={ad.message_id} photos={ad.photos}
                                                                tags={ad.tags.toString().replaceAll(",", " ")}
                                                                price={ad.price}
                                                                description={ad.description} phone={ad.phone}
                                                                contacts={ad.contacts}
                                                                delete={(id) => this.handleDeleteClick(id)}/>);
        const showLoad = this.state.msg === "Загрузка данных";
        return (<form>
            <div className="main__form-title">{this.state.msg}</div>
            { showLoad ? <Example /> : null }
            <div className="grid">
                {listOfAds}
            </div>
        </form>)
    }
}

class FoundAds extends React.Component {
    listOfRef = this.props.contacts.map((contact, index) => <Contact key={index} contact={contact}/>)
    listOfPhotos = this.props.photos.map((photo, index) => <Photo key={index} photo={photo}/>)
    checkPrice = this.props.price == null ? "none" : "block";
    checkPhone = this.props.phone == null ? "none" : "block";

    render() {
        return (<div className="ad__form">
            <div className="main__form-row">
                <div className="text__padding"></div>
                <Dialog id={this.props.id} delete={(id) => this.props.delete(id)}></Dialog>
            </div>

            <div className="add__content">
                <div className="ad__photo">
                    <Carousel photos={this.listOfPhotos}/>
                </div>
                <div className="text__padding">
                    <div className="ad-tags">{this.props.tags}</div>
                    <span style={{display: this.checkPrice}}
                          className="ad-bottom-padding">Цена: {this.props.price} руб.</span>
                    <ReadMoreReact text={`Описание: ${this.props.description}`}
                                   min={150}
                                   ideal={150}
                                   max={200}
                                   readMoreText="Подробнее"/>
                    <div>--------------------------------------------------------</div>
                    <ReactSpoiler blur={10} hoverBlur={8}>
                        <span style={{display: this.checkPhone}}>Номер телефона: {this.props.phone}</span>
                        <div>Контакты:</div>
                        {this.listOfRef}
                    </ReactSpoiler>
                </div>
            </div>
        </div>);
    }
}

function Carousel(props) {
    const listOfPhotos = props.photos
    return (
        <AliceCarousel autoWidth={false} mouseTracking items={listOfPhotos}/>
    );
}

const Example = () => (
    <ReactLoading className="ad__form" type="spin" color="#419FD9" height={40} width={40}/>
);

class Dialog extends React.Component {
    constructor(props) {
        super(props);
        this.state = {visible: false};
    }

    show() {
        this.setState({visible: true});
    }

    hide() {
        this.setState({visible: false});
    }

    render() {
        return (
            <div>
                <button type="button" className="del-btn" onClick={this.show.bind(this)}>
                    <img src={require('../img/del_ad.png')} height="20"/>
                </button>
                <Rodal visible={this.state.visible} animation="fade" height="100" onClose={this.hide.bind(this)}>
                    <div className="main__form-title">Удалить объявление</div>
                    <div>Вы желаете удалить выбранное объявление?</div>
                    <div className="main__form-row ad__form">
                        <input type="button" className="btn btn-dialog btn-light-color" value="Да" onClick={() => {
                            this.props.delete(this.props.id);
                            this.setState({visible: false});
                        }}/>
                        <input type="button" className="btn btn-dialog btn-dark-color" value="Нет"
                               onClick={this.hide.bind(this)}/>
                    </div>
                </Rodal>
            </div>
        );
    }
}

export default MyAds;