import React from "react";
import '../style/style.css';

export default class Photo extends React.Component {
    render() {
        return (<div className="box">
                <img src={`data:image/png;base64,${this.props.photo}`}/>
            </div>
        )
    }
}