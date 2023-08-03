import React from 'react';
import '../style/style.css';
import RequestService from "../services/RequestService";
import Rodal from 'rodal';
import '../style/rodal.css';
import ReactLoading from "react-loading";
import Objav from "../services/Objav";


class MyObjav extends React.Component {
    constructor(props) {
        super(props);
        this.handleDeleteClick = this.handleDeleteClick.bind(this);
        this.state = {
            obyavs: [],
            msg: "Загрузка данных"
        };
    }

    componentDidMount() {
        let userData = JSON.parse(localStorage.getItem('userData'));
        RequestService.getMyObyavs(userData).then((response) => {
            if (response.data) {
                this.setState({
                    obyavs: response.data,
                    msg: response.data.length === 0 ? "У вас нет новых объявлений" : "Ваши актуальные объявления"
                });
                console.log("My obyavs", response.data);
            }
        }).catch(err => {
            console.log(err);
            this.setState({
                msg: "Ошибка при получении данных. " + err
            });
        });
    }

    handleDeleteClick(val) {
        let userData = JSON.parse(localStorage.getItem('userData'));
        RequestService.postDeleteObyav(userData, val).then((response) => {
            if (response.status === 200) {
                let deleteobyavs = this.state.obyavs.find(ad => ad.message_id === val);
                let obyavsSize = this.state.obyavs.length - 1;
                this.setState({
                    obyavs: this.state.obyavs.filter(ad => ad !== deleteobyavs),
                    msg: obyavsSize === 0 ? "У вас нет новых объявлений" : "Ваши актуальные объявления"
                });
            }
        }).catch(err => {
            console.log(err);
        });
    }

    render() {
        const listOfobyavs = this.state.obyavs?.map((ad) => <Foundobyavs key={ad.message_id} id={ad.message_id}
                                                                photos={ad.photos}
                                                                tags={ad.tags.toString().replaceAll(",", " ")}
                                                                price={ad.price}
                                                                description={ad.description} phone={ad.phone}
                                                                contacts={ad.contacts}
                                                                delete={(id) => this.handleDeleteClick(id)}/>);
        const showLoad = this.state.msg === "Загрузка данных";
        return (<form>
            <div className="main__form-title">{this.state.msg}</div>
            {showLoad ? <Example/> : null}
            <div className="grid">
                {listOfobyavs}
            </div>
        </form>)
    }
}

class Foundobyavs extends React.Component {
    render() {
        return (<div className="obyav__form">
            <div className="main__form-row">
                <div className="text__padding"></div>
                <Dialog id={this.props.id} delete={(id) => this.props.delete(id)}></Dialog>
            </div>

            <Objav key={this.props.key} id={this.props.id}
                   photos={this.props.photos}
                   tags={this.props.tags}
                   price={this.props.price}
                   description={this.props.description} phone={this.props.phone}
                   contacts={this.props.contacts}
            ></Objav>
        </div>);
    }
}

const Example = () => (
    <ReactLoading className="obyav__form" type="spin" color="#419FD9" height={40} width={40}/>
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
                    <img src={'/img/del_obyavleniye.png'} height="20"/>
                </button>
                <Rodal visible={this.state.visible} animation="fade" height="100" onClose={this.hide.bind(this)}>
                    <div className="main__form-title">Удалить</div>
                    <div>Вы уверены?</div>
                    <div className="main__form-row obyav__form">
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

export default MyObjav;