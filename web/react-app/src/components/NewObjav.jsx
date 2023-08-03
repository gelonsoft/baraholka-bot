import React, {useEffect, useState} from 'react';
import '../style/style.css';
import RequestService from '../services/RequestService';
import {useNavigate} from "react-router-dom";


export default function NewObjav() {

    const [chosenPhotos, set_chosenPhotos] = useState([])
    const [chosenPhotosStrings, set_chosenPhotosStrings] = useState([])
    const [cityTags, set_cityTags] = useState(['#Москва', '#СПб', '#Екатеринбург', '#Челябинск', '#Ульяновск', '#Омск', '#Белгород', '#Петропавловск', '#Пермь', '#Волгоград', '#Киров', '#Хабаровск'])
    const [obyavleniyeTypeTags, set_obyavleniyeTypeTags] = useState(['#продажа', '#обмен', '#дар', '#торг_уместен', '#срочно'])
    const [categoriesTags, set_categoriesTags] = useState(['#одежда', '#обувь', '#детские_товары', '#красота_и_здоровье', '#книги', '#хобби', '#домашняя_техника', '#электроника', '#спорт', '#другое', '#мужское', '#женское'])
    const [update, set_update] = useState(false)
    const [showPrice, set_showPrice] = useState(false)

    const navigate=useNavigate()

    useEffect(() => {
        let userData = localStorage.getItem('userData');
        RequestService.getTags(userData).then((response) => {
            if (response.data) {
                set_cityTags(response.data.city)
                set_obyavleniyeTypeTags(response.data.obyavleniye_type)
                set_categoriesTags(response.data.product_categories)
            }
        }).catch(err => {
            console.log(err);
        });
        const textarea = document.querySelector("textarea");
        textarea.addEventListener("keyup", e => {
            textarea.style.height = "60px";
            let scHeight = e.target.scrollHeight;
            textarea.style.height = `${scHeight}px`;
        });
    }, [])

    const getBase64 = (file) => {
        const reader = new FileReader();
        reader.onloadend = () => {
            return reader.result
                .replace('data:', '')
                .replace(/^.+,/, '');
        };
        reader.readAsDataURL(file);
    }

    const createNewAd = (e) => {
        e.preventDefault();
        if (!e.target.checkValidity()) {
            alert("Кажется, вы заполнили не все поля");
            return;
        }
        if (chosenPhotos.length === 0) {
            alert("Кажется, вы заполнили не все поля");
            return;
        }
        if (chosenPhotos.length > 10) {
            alert("Кажется, вы добавили слишком много фотографий");
            return;
        }
        if (e.target[1].value.trim() === "") {
            alert("Кажется, вы заполнили не все поля");
            return;
        }
        if (e.target[2].value === "Не выбран") {
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

        const photoFiles = chosenPhotos.map(async photoFile => {
            return convertToBase64(photoFile);
        });

        const sendNewAd = () => {
            let userData = JSON.parse(localStorage.getItem('userData'));
            let description = e.target[1].value;
            let tags = [];
            tags.push('#' + e.target[2].value.replaceAll(" ", "_"));
            let i = 3;
            let typeCount = 0;
            while (e.target[i].className === "type") {
                if (e.target[i].checked) {
                    tags.push('#' + e.target[i].labels[0].innerText.toLowerCase().replaceAll(" ", "_"));
                    typeCount++;
                }
                i++;
            }
            if (typeCount === 0) {
                alert("Кажется, вы заполнили не все поля");
                return;
            }
            let categoryCount = 0;
            while (e.target[i].className === "category") {
                if (e.target[i].checked) {
                    tags.push('#' + e.target[i].labels[0].innerText.toLowerCase().replaceAll(" ", "_"));
                    categoryCount++;
                }
                i++;
            }
            if (categoryCount === 0) {
                alert("Кажется, вы заполнили не все поля");
                return;
            }
            let price = null;
            if (showPrice) {
                price = e.target[i++].value.trim();
                if (price === "") {
                    alert("Кажется, вы заполнили не все поля");
                    return;
                }
                if (isNaN(price) || price < 0) {
                    alert("Пожалуйста, проверьте корректность введенных данных");
                    return;
                } else {
                    price = Number(price);
                }
                if (price === 0) {
                    price = null;
                }
            }
            let phone = e.target[i++].value.trim();
            const phoneRegex = /\+7-\d{3}-\d{3}-\d{2}-\d{2}/;
            if (phone !== "" && !phoneRegex.test(phone)) {
                alert("Пожалуйста, проверьте корректность введенного номера телефона - он должен соответствовать формату +7-XXX-XXX-XX-XX");
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
                "photos": chosenPhotosStrings,
                "description": description,
                "tags": tags,
                "price": price,
                "phone": phone,
                "contacts": contacts
            };
            RequestService.newAd(body).then((response) => {
                alert("Объявление успешно добавлено");
                document.getElementById("new-ad-form").reset();
                navigate('/baraholka/my_obyavleniye')
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
                set_chosenPhotosStrings(photosStrings)
                sendNewAd();
            });
    }

    const onPhotosChange = (e) => {
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
        set_chosenPhotos(photos)
    }

    const deletePhoto = (e, index) => {
        let input = document.getElementById('photosInput');
        input.value = "";
        const newChosenPhotos=[...chosenPhotos]
        newChosenPhotos.splice(index, 1)
        set_chosenPhotos(newChosenPhotos)

        const newChosenPhotosStrings=[...chosenPhotosStrings]
        newChosenPhotos.splice(index, 1)
        set_chosenPhotosStrings(newChosenPhotosStrings)
        set_update(true)
    }

    const updatePriceForm = (e, tag) => {
        if (tag === "#продажа") {
            set_showPrice(e.target.checked)
        }
    }


    return (
        <form id="new-ad-form" onSubmit={createNewAd}>
            <div className="main__form-title">Добавить фотографии</div>
            <div>Добавьте от 1 до 10 фотографий к вашему объявлению. Рекомендуемое число - 5.</div>
            <label className="btn btn-light file-btn">
                <input type="file" accept="image/*" id="photosInput" onChange={onPhotosChange} multiple/>
                Выбрать файл
            </label>
            <div className="chosen-photos">
                {chosenPhotos.map((photo, index) => {
                    return (
                        <div className="chosen-photo-container">
                            <img className="delete-pic-btn" alt="delete photo"
                                 onClick={(event) => deletePhoto(event, index)} src={"/img/delete-btn.svg"}/>
                            <img className="chosen-photo" alt="preview image"
                                 src={URL.createObjectURL(photo).toString()}/>
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
                {cityTags.map(function (tag) {
                    return (
                        <option>{tag.substring(1).replaceAll("_", " ")}</option>
                    )
                })}
            </select>
            <div className="main__form-title">Добавить тип объявления</div>
            <div>Выберите тип объявления.</div>
            <div className="main__form-row">
                {obyavleniyeTypeTags.map((tag) => {
                    return (
                        <label className="custom-checkbox">
                            <input onChange={(event) => updatePriceForm(event, tag)} className="type"
                                   type="checkbox"/>
                            {tag.substring(1).replaceAll("_", " ")}
                        </label>
                    )
                })}
            </div>
            <div className="main__form-title">Добавить категории</div>
            <div>Выберите категории, наиболее подходящие для описания вашего товара.</div>
            <div className="main__form-row">
                {categoriesTags.map(function (tag) {
                    return (
                        <label className="custom-checkbox">
                            <input className="category" type="checkbox"/>
                            {tag.substring(1).replaceAll("_", " ")}
                        </label>
                    )
                })}
            </div>
            {
                showPrice ?
                    <div>
                        <div className="main__form-title">Добавить стоимость</div>
                        <div>Укажите стоимость товара в рублях, если она имеется (необязательно).</div>
                        <input type="text" placeholder="1000"/>
                    </div>
                    : null
            }
            <div className="main__form-title">Добавить номер телефона</div>
            <div>Добавьте номер телефона (необязательно).</div>
            <input type="tel" pattern="+7-[0-9]{3}-[0-9]{3}-[0-9]{2}-[0-9]{2}" placeholder="+7-900-000-00-00"/>
            <div className="main__form-title">Добавить социальные сети</div>
            <div>Добавьте ссылки на ваши социальные сети (необязательно).</div>
            <input type="tel" pattern="https://.+/.+" placeholder="https://vk.com/useruser"/>
            {/*<button className="btn btn-light">Добавить социальную сеть</button><br />*/}
            <input type="submit" className="btn btn-dark" value="Опубликовать"/>
        </form>
    )
}
