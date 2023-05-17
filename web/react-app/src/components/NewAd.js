import React from 'react';
import '../style/style.css';
import axios from 'axios';
import RequestService from '../services/RequestService';

class NewAd extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            chosenPhotos: [],
            cityTags: ['#Москва', '#СПб', '#Екатеринбург', '#Челябинск', '#Ульяновск', '#Омск', '#Белгород', '#Петропавловск', '#Пермь', '#Волгоград', '#Киров', '#Хабаровск'],
            advertisementTypeTags: ['#продажа', '#обмен', '#дар', '#торг_уместен', '#срочно'],
            categoriesTags: ['#одежда', '#обувь', '#детские_товары', '#красота_и_здоровье', '#книги', '#хобби', '#домашняя_техника', '#электроника', '#спорт', '#другое', '#мужское', '#женское']
        };
        this.createNewAd = this.createNewAd.bind(this);
        this.getBase64 = this.getBase64.bind(this);
        this.onPhotosChange = this.onPhotosChange.bind(this);
    }

    componentDidMount() {
        // TODO: раскомментить первую строку и убрать вторую
        let userData = localStorage.getItem('userData');
        //let userData = {"auth_date":1684051188,"first_name":"Алиса","hash":"afc6a8181ae6eb8f494551c94c39a63fae2835470210428556f8db7f54b66603","id":538160964,"last_name":"Селецкая","photo_url":"https://t.me/i/userpic/320/Uim0VYUr3WRDc7ofnIj40wRzPe1L7t63Nv0FXKqydjM.jpg","username":"sealisaa"};
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

    getBase64(file) {
        const reader = new FileReader();
        reader.onloadend = () => {
            const base64String = reader.result
                .replace('data:', '')
                .replace(/^.+,/, '');
            return base64String;
        };
        reader.readAsDataURL(file);
    }

    createNewAd(e) {
        console.log(e);
        e.preventDefault();
        const NEW_AD_URL = "http://localhost:8080/api/add_advertisement";
        // TODO: раскомментить первую строку и убрать вторую
        let userData = JSON.parse(localStorage.getItem('userData'));
        //let userData = {"auth_date":1684051188,"first_name":"Алиса","hash":"afc6a8181ae6eb8f494551c94c39a63fae2835470210428556f8db7f54b66603","id":538160964,"last_name":"Селецкая","photo_url":"https://t.me/i/userpic/320/Uim0VYUr3WRDc7ofnIj40wRzPe1L7t63Nv0FXKqydjM.jpg","username":"sealisaa"};
        let photos = [];
        for (let i = 0; i < this.state.chosenPhotos.length; i++) {
            const reader = new FileReader();
            reader.onloadend = () => {
                const base64String = reader.result
                    .replace('data:', '')
                    .replace(/^.+,/, '');
                photos.push(base64String)
            };
            reader.readAsDataURL(this.state.chosenPhotos[i]);
        }
        let description = e.target[1].value;
        let tags = [];
        tags.push('#' + e.target[2].value);
        let i = 3;
        while (e.target[i].className === "type") {
            if (e.target[i].checked) {
                tags.push('#' + e.target[i].labels[0].innerText.toLowerCase().replaceAll(" ", "_"));
            }
            i++;
        }
        while (e.target[i].className === "category") {
            if (e.target[i].checked) {
                tags.push('#' + e.target[i].labels[0].innerText.toLowerCase().replaceAll(" ", "_"));
            }
            i++;
        }
        let price = e.target[i++].value;
        let phone = e.target[i++].value;
        let contacts = [];
        contacts.push(e.target[i++].value);
        let body = {
            "id": userData.id,
            "first_name": userData.first_name,
            "last_name": userData.last_name,
            "username": userData.username,
            "photo_url": userData.photo_url,
            "auth_date": userData.auth_date,
            "hash": userData.hash,
            "photos": photos,
            "description": description,
            "tags": tags,
            "price": Number(price),
            "phone": phone,
            "contacts": contacts
        };
        RequestService.newAd(body).then((response) => {
            alert("Объявление успешно добавлено");
            document.getElementById("new-ad-form").reset();
        }).catch(err => {
            console.log(err);
        });
    }

    onPhotosChange(e) {
        let photos = [];
        if (e.target.files.length > 10) {
            alert('Вы прикрепили слишком много фотографий. Чтобы добавить объявление, пожалуйста, оставьте не более 10 фото.');
        }
        if (e.target.files && e.target.files[0]) {
            for (let i = 0; i < e.target.files.length; i++) {
                if (i > 10) {
                    break;
                }
                photos.push(e.target.files[i]);
            }
        }
        this.setState({chosenPhotos: photos});
    }

    render() {
        return (
            <form id="new-ad-form" onSubmit={this.createNewAd}>
                <div className="main__form-title">Добавить фотографии</div>
                <div>Добавьте от 1 до 10 фотографий к вашему объявлению. Рекомендуемое число - 5.</div>
                <label className="btn btn-light file-btn">
                    <input type="file" onChange={this.onPhotosChange} multiple />
                    Выбрать файл
                </label>
                <div className="chosen-photos">
                    {this.state.chosenPhotos.map(function(photo) {
                    return <img className="chosen-photo" alt="preview image" src={URL.createObjectURL(photo).toString()}/>
                })}
                </div>
                <div className="main__form-title">Добавить описание</div>
                <div>Добавьте краткое описание товара.</div>
                <input type="text" placeholder="Описание"/>
                <div className="main__form-title">Добавить город</div>
                <div>Выберите город для публикации объявления.</div>
                <select defaultValue="Не выбран">
                    <option>Не выбран</option>
                    {this.state.cityTags.map(function(tag) {
                        return (
                            <option>{tag.substring(1).replaceAll("_", " ")}</option>
                        )
                    })}
                </select>
                <div className="main__form-title">Добавить тип объявления</div>
                <div>Выберите тип объявления.</div>
                <div className="main__form-row">
                    {this.state.advertisementTypeTags.map(function(tag) {
                        return (
                            <label className="custom-checkbox">
                                <input className="type" type="checkbox" />
                                {tag.substring(1).replaceAll("_", " ")}
                            </label>
                        )
                    })}
                </div>
                <div className="main__form-title">Добавить категории</div>
                <div>Выберите категории, наиболее подходящие для описания вашего товара.</div>
                <div className="main__form-row">
                    {this.state.categoriesTags.map(function(tag) {
                        return (
                            <label className="custom-checkbox">
                                <input className="category" type="checkbox" />
                                {tag.substring(1).replaceAll("_", " ")}
                            </label>
                        )
                    })}
                </div>
                <div className="main__form-title">Добавить стоимость</div>
                <div>Укажите стоимость товара в рублях, если она имеется (необязательно).</div>
                <input type="text" placeholder="1000"/>
                <div className="main__form-title">Добавить номер телефона</div>
                <div>Добавьте номер телефона (необязательно).</div>
                <input type="tel" pattern="+7-[0-9]{3}-[0-9]{3}-[0-9]{2}-[0-9]{2}" placeholder="+7-900-000-00-00" />
                <div className="main__form-title">Добавить социальные сети</div>
                <div>Добавьте ссылки на ваши социальные сети (необязательно).</div>
                <input type="tel" pattern="https://.+/.+" placeholder="https://vk.com/useruser"/>
                <button className="btn btn-light">Добавить социальную сеть</button><br />
                <input type="submit" className="btn btn-dark" value="Опубликовать" />
            </form>
        )
    }
}

export default NewAd;