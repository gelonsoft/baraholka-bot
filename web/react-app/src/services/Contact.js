import React from "react";
import '../style/style.css';

export default class Contact extends React.Component {
    ref = this.props.contact.at(0) === "@" ? "https://t.me/"+this.props.contact.substring(1) : this.props.contact
    render() {
        return <a className="ref-color" href={this.ref}>{this.props.contact}</a>
    }
}