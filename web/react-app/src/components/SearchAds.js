import React from 'react';
import '../style/style.css';
import ReactSpoiler from "react-spoiler";
import RequestService from '../services/RequestService';
import ReadMoreReact from 'read-more-react';
import AliceCarousel from 'react-alice-carousel';
import '../style/alice-carousel.css';
import ReactLoading from 'react-loading';
import Photo from '../services/Photo';

class SearchAds extends React.Component {
    constructor(props) {
        super(props);
        this.handleStartClick = this.handleStartClick.bind(this);
        this.handleNewClick = this.handleNewClick.bind(this);
        this.state = {
            isStarted: false,
            choosenTags: [],
            ads: [],
            currentCity: 'Не выбран',
            msg: "Поиск объявлений",
            cityTags: ['#Москва', '#СПб', '#Екатеринбург', '#Челябинск', '#Ульяновск', '#Омск', '#Белгород', '#Петропавловск', '#Пермь', '#Волгоград', '#Киров', '#Хабаровск'],
            advertisementTypeTags: ['#продажа', '#обмен', '#дар', '#торг_уместен', '#срочно'],
            categoriesTags: ['#одежда', '#обувь', '#детские_товары', '#красота_и_здоровье', '#книги', '#хобби', '#домашняя_техника', '#электроника', '#спорт', '#другое', '#мужское', '#женское'],
        };
    }

    componentDidMount() {
        let userData = JSON.parse(localStorage.getItem('userData'));
        RequestService.getTags(userData).then((response) => {
            if (response.data) {
                this.setState({
                    cityTags: response.data.city,
                    advertisementTypeTags: response.data.advertisement_type,
                    categoriesTags: response.data.product_categories
                });
            }
        }).catch(err => {
            console.log(err);
        });
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
        if (event.target.value !== "Не выбран") {
            currentTags = [...this.state.choosenTags, event.target.value];
        }
        if (this.state.choosenTags.includes(this.state.currentCity)) {
            currentTags = currentTags.filter(tag => tag !== this.state.currentCity);
        }
        this.setState({choosenTags: currentTags, currentCity: event.target.value});
    };

    handleStartClick() {
        let userData = JSON.parse(localStorage.getItem('userData'));
        RequestService.getSearchAds(userData, this.state.choosenTags).then((response) => {
            if (response.data) {
                this.setState({
                    ads: response.data,
                    msg: response.data.length === 0 ? "По вашему запросу ничего не нашлось" : "Найденные объявления"
                });
            }
        }).catch(err => {
            console.log(err);
            this.setState({
                msg: "Ошибка при получении данных. " + err
            });
        });
        this.setState({isStarted: true});
    }

    handleNewClick() {
        this.setState({isStarted: false, choosenTags: [], ads: [], msg: "Поиск объявлений"});
    }

    render() {
        const isStarted = this.state.isStarted;
        if (isStarted) {
            return <StartNewSearch ads={this.state.ads} msg={this.state.msg} choosenTags={this.state.choosenTags}
                                   new={this.handleNewClick}/>;
        }
        return <ChooseSearchTags cities={this.state.cityTags} types={this.state.advertisementTypeTags}
                                 categories={this.state.categoriesTags}
                                 change={this.handleCheckboxChange} start={this.handleStartClick}
                                 select={this.handleSelectChange}/>;
    }
}

function ChooseSearchTags(props) {

    return (<form>
        <div className="main__form-title">Город</div>
        <div>Выберите город, по которому хотите выполнить поиск.</div>
        <select defaultValue="Не выбран" onChange={props.select}>
            <option value="Не выбран">Не выбран</option>
            {props.cities.map(function (tag) {
                return (
                    <option value={tag.toString()}>{tag.substring(1).replaceAll("_", " ")}</option>
                )
            })}
        </select>
        <div className="main__form-title">Тип объявления</div>
        <div>Выберите типы объявления, по которым хотите выполнить поиск.</div>
        <div className="main__form-row">
            {props.types.map(function (tag) {
                return (
                    <label className="custom-checkbox">
                        <input className="type" type="checkbox" value={tag.toString()} onChange={props.change}/>
                        {tag.substring(1).replaceAll("_", " ")}
                    </label>
                )
            })}
        </div>
        <div className="main__form-title">Категории</div>
        <div>Выберите категории, по которым хотите выполнить поиск.</div>
        <div className="main__form-row">
            {props.categories.map(function (tag) {
                return (
                    <label className="custom-checkbox">
                        <input className="type" type="checkbox" value={tag.toString()} onChange={props.change}/>
                        {tag.substring(1).replaceAll("_", " ")}
                    </label>
                )
            })}
        </div>
        <input type="button" className="btn btn-dark ad__form" value="Искать" onClick={props.start}/>
    </form>)
}

function StartNewSearch(props) {
    const listOfAds = props.ads?.map((ad) => <FoundAds key={ad.id} username={ad.username} photos={ad.photos}
                                                       tags={ad.tags.toString().replaceAll(",", " ")}
                                                       price={ad.price}
                                                       description={ad.description} phone={ad.phone}
                                                       contacts={ad.contacts}/>);
    const showLoad = props.msg === "Поиск объявлений";
    return (<form>
        <div className="main__form-title">Хэштеги</div>
        <div>Вы выбрали следующие хэштеги для выполнения поиска.</div>
        <div className="custom-text">{props.choosenTags?.toString().replaceAll(",", " ")}</div>
        <input type="button" className="btn btn-dark ad__form" value="Выполнить новый поиск" onClick={props.new}/>
        <div className="main__form-title mgs-margin">{props.msg}</div>
        {showLoad ? <Example/> : null}
        <div className="grid">
            {listOfAds}
        </div>
    </form>)
}


class FoundAds extends React.Component {
    constructor(props) {
        super(props);
        this.handleBlurClick = this.handleBlurClick.bind(this);
        this.state = {clickable: false};
    }

    listOfPhotos = this.props.photos.map((photo, index) => <Photo key={index} photo={photo}/>)
    checkPrice = this.props.price == null ? "none" : "block";
    checkPhone = this.props.phone == null ? "none" : "block";

    handleBlurClick() {
        const currentClickable = this.state.clickable;
        this.setState({clickable: !currentClickable});
    }

    render() {
        return (<div className="ad__form">
            <div className="text__padding"></div>
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
                    <ReactSpoiler blur={10} hoverBlur={8} onClick={this.handleBlurClick}>
                        <span style={{display: this.checkPhone}}>Номер телефона: {this.props.phone}</span>
                        <div>Контакты:</div>
                        {this.props.contacts.map((contact) => {
                            const ref = contact.at(0) === "@" ? "https://t.me/" + contact.substring(1) : contact
                            return <a className={this.state.clickable ? "enabled ref-color" : "disabled"}
                                      href={ref}>{contact}</a>
                        })}
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

export default SearchAds;