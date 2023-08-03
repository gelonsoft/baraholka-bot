import React, {useState} from "react";
import '../style/style.css';
import ReactSpoiler from "react-spoiler";
import AliceCarousel from "react-alice-carousel";

const ReadMore = ({children}) => {
    const text = children;
    const [isReadMore, setIsReadMore] = useState(true);
    const toggleReadMore = () => {
        setIsReadMore(!isReadMore);
    };

    const checkLength = children.length <= 150 ? "none" : "block";
    return (
        <p className="new-line">
            {isReadMore ? text.slice(0, 150) : text}
            <span style={{display: checkLength}} onClick={toggleReadMore} className="ref-color">
        {isReadMore ? "...Подробнее" : " Скрыть"}
      </span>
        </p>
    );
};
export default class Objav extends React.Component {
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
        return (
            <div className="add__content">
                <div className="objav__photo">
                    <Carousel photos={this.listOfPhotos}/>
                </div>
                <div className="text__padding">
                    <div className="objav-tags">{this.props.tags}</div>
                    <span style={{display: this.checkPrice}}
                          className="objav-bottom-padding">Цена: {this.props.price} руб.</span>
                    <ReadMore children={`Описание: ${this.props.description.trim()}`}/>
                    <div>--------------------------------------------------------</div>
                    <ReactSpoiler blur={10} hoverBlur={8} onClick={this.handleBlurClick}>
                        <span style={{display: this.checkPhone}}>Номер телефона: {this.props.phone}</span>
                        <div>Контакты:</div>
                        {this.props.contacts.map((contact) => {
                            const ref = contact.at(0) === "@" ? "https://t.me/" + contact.substring(1) : contact
                            return <a className={this.state.clickable ? "enabled ref-color" : "disabled"}
                                      href={ref}>{contact}<br/></a>
                        })}
                    </ReactSpoiler>
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

class Photo extends React.Component {
    render() {
        return (<div className="box">
                <img src={`data:image/png;base64,${this.props.photo}`}/>
            </div>
        )
    }
}