import React from 'react';
import '../style/style.css';
import ReactSpoiler from "react-spoiler";
import ReadMoreReact from 'read-more-react';
import AliceCarousel from "react-alice-carousel";
export default class Ad extends React.Component {
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
                            const ref = contact.at(0) === "@" ? "https://t.me/"+contact.substring(1) : contact
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