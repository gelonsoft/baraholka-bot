import React from 'react';
import '../style/style.css';
import RequestService from '../services/RequestService';
import deleteBtn from '../img/delete-btn.svg'

class NewAd extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            chosenPhotos: [],
            chosenPhotosStrings: [],
            cityTags: ['#Москва', '#СПб', '#Екатеринбург', '#Челябинск', '#Ульяновск', '#Омск', '#Белгород', '#Петропавловск', '#Пермь', '#Волгоград', '#Киров', '#Хабаровск'],
            advertisementTypeTags: ['#продажа', '#обмен', '#дар', '#торг_уместен', '#срочно'],
            categoriesTags: ['#одежда', '#обувь', '#детские_товары', '#красота_и_здоровье', '#книги', '#хобби', '#домашняя_техника', '#электроника', '#спорт', '#другое', '#мужское', '#женское'],
            update: false,
            showPrice: false
        };
        this.createNewAd = this.createNewAd.bind(this);
        this.getBase64 = this.getBase64.bind(this);
        this.onPhotosChange = this.onPhotosChange.bind(this);
        this.deletePhoto = this.deletePhoto.bind(this);
        this.updatePriceForm = this.updatePriceForm.bind(this);
    }

    componentDidMount() {
        let userData = localStorage.getItem('userData');
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
        const textarea = document.querySelector("textarea");
        textarea.addEventListener("keyup", e =>{
            textarea.style.height = "60px";
            let scHeight = e.target.scrollHeight;
            textarea.style.height = `${scHeight}px`;
        });
    }

    getBase64(file) {
        const reader = new FileReader();
        reader.onloadend = () => {
            return reader.result
                .replace('data:', '')
                .replace(/^.+,/, '');
        };
        reader.readAsDataURL(file);
    }

    createNewAd(e) {
        e.preventDefault();
        if (!e.target.checkValidity()) {
            alert("Кажется, вы заполнили не все поля");
            return;
        }
        if (this.state.chosenPhotos.length === 0) {
            alert("Кажется, вы заполнили не все поля");
            return;
        }
        const convertToBase64 = (file) => {
            return new Promise((resolve, reject) => {
                const fileReader = new FileReader();
                fileReader.readAsDataURL(file);
                fileReader.onload = () => {
                    resolve(fileReader.result);
                };
                fileReader.onerror = (error) => {
                    reject(error);
                };
            });
        };

        const photoFiles = this.state.chosenPhotos.map(async photoFile => {
            return convertToBase64(photoFile);
        });

        const sendNewAd = () => {
            let userData = JSON.parse(localStorage.getItem('userData'));
            if (e.target[1] === "") {
                alert("Кажется, вы заполнили не все поля");
                return;
            }
            let description = e.target[1].value;
            let tags = [];
            if (e.target[2].value === "Не выбран") {
                alert("Кажется, вы заполнили не все поля");
                return;
            }
            tags.push('#' + e.target[2].value.toLowerCase().replaceAll(" ", "_"));
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
            if (price === 0 || price === "") {
                price = null;
            }
            let phone = e.target[i++].value;
            const phoneRegex = /\+7-\d{3}-\d{3}-\d{2}-\d{2}/;
            if (!phoneRegex.test(phone)) {
                alert("Пожалуйста, проверьте корректность введенных данных");
                return;
            }
            if (phone === "") {
                phone = null;
            }
            let contacts = [];
            let contact = e.target[i++].value;
            if (contact !== "") {
                contacts.push(contact);
            }
            let body = {
                "id": userData.id,
                "first_name": userData.first_name,
                "last_name": userData.last_name,
                "username": userData.username,
                "photo_url": userData.photo_url,
                "auth_date": userData.auth_date,
                "hash": userData.hash,
                "photos": this.state.chosenPhotosStrings,
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

        Promise.all(photoFiles)
            .then(base64Strings => {
                let photosStrings = [];
                base64Strings.forEach(base64String => {
                    photosStrings.push(base64String.replace('data:', '').replace(/^.+,/, ''));
                });
                this.setState({
                    chosenPhotosStrings : photosStrings
                }, () => {
                    sendNewAd();
                });
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

    deletePhoto(e, index) {
        let input = document.getElementById('photosInput');
        input.value = "";
        this.state.chosenPhotos.splice(index, 1);
        this.state.chosenPhotosStrings.splice(index, 1);
        this.setState({update: true});
    }

    updatePriceForm(e, tag) {
        console.log(e);
        console.log(tag);
        if (tag === "#продажа") {
            this.setState({showPrice: e.target.checked});
        }
    }

    render() {
        return (
            <form id="new-ad-form" onSubmit={this.createNewAd}>
                <div className="main__form-title">Добавить фотографии</div>
                <div>Добавьте от 1 до 10 фотографий к вашему объявлению. Рекомендуемое число - 5.</div>
                <label className="btn btn-light file-btn">
                    <input type="file" id="photosInput" onChange={this.onPhotosChange} multiple />
                    Выбрать файл
                </label>
                <div className="chosen-photos">
                    {this.state.chosenPhotos.map((photo, index) => {
                    return (
                        <div className="chosen-photo-container">
                            <img className="delete-pic-btn" alt="delete photo"
                                 onClick={(event) => this.deletePhoto(event, index)} src={deleteBtn} />
                            <img className="chosen-photo" alt="preview image" src={URL.createObjectURL(photo).toString()}/>
                        </div>
                    )
                })}
                </div>
                <div className="main__form-title">Добавить описание</div>
                <div>Добавьте краткое описание товара (до 800 символов).</div>
                <textarea id="descriptionTextarea" placeholder="Описание" maxLength='800'></textarea>
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
                    {this.state.advertisementTypeTags.map((tag) => {
                        return (
                            <label className="custom-checkbox">
                                <input onChange={(event) => this.updatePriceForm(event, tag)} className="type" type="checkbox" />
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
                {
                    this.state.showPrice ?
                        <div>
                            <div className="main__form-title">Добавить стоимость</div>
                            <div>Укажите стоимость товара в рублях, если она имеется (необязательно).</div>
                            <input type="text" placeholder="1000"/>
                        </div>
                    : null
                }
                <div className="main__form-title">Добавить номер телефона</div>
                <div>Добавьте номер телефона (необязательно).</div>
                <input type="tel" pattern="+7-[0-9]{3}-[0-9]{3}-[0-9]{2}-[0-9]{2}" placeholder="+7-900-000-00-00" />
                <div className="main__form-title">Добавить социальные сети</div>
                <div>Добавьте ссылки на ваши социальные сети (необязательно).</div>
                <input type="tel" pattern="https://.+/.+" placeholder="https://vk.com/useruser"/>
                {/*<button className="btn btn-light">Добавить социальную сеть</button><br />*/}
                <input type="submit" className="btn btn-dark" value="Опубликовать" />
            </form>
        )
    }
}

export default NewAd;