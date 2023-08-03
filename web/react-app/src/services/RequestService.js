import axios from "axios";

const API_URL='https://baraholka.sighold.com/api'
const GET_TAGS_URL = API_URL+"/all_tags";
const GET_SEARCH_OBYAV_URL = API_URL+"/search_obyavleniyes";
const GET_MY_OBYAV_URL = API_URL+"/my_obyavleniyes";
const POST_DEL_OBYAV_URL = API_URL+"/save/";
const NEW_OBYAV_URL = API_URL+"/add_obyavleniye";

class RequestService {
    getTags(userData) {
        try {
            return axios({
                method: "post",
                url: GET_TAGS_URL,
                data: userData,
                headers: { "Content-Type": "application/json" },
            });
        } catch (e) {
            console.error(e);
        }
    }

    newAd(body) {
        try {
            return axios({
                method: "post",
                url: NEW_OBYAV_URL,
                data: body,
                headers: { "Content-Type": "application/json" },
            });
        } catch (e) {
            console.error(e);
        }
    }

    getSearchObyavs(userData, tags) {
        const bodyFormData = new FormData();
        bodyFormData.append('id', userData.id);
        bodyFormData.append('first_name', userData.first_name);
        bodyFormData.append('last_name', userData.last_name);
        bodyFormData.append('username', userData.username);
        bodyFormData.append('photo_url', userData.photo_url);
        bodyFormData.append('auth_date', userData.auth_date);
        bodyFormData.append('hash', userData.hash);
        if (tags.length !== 0) tags.forEach(tag => bodyFormData.append('tags[]', tag))
        else bodyFormData.append('tags[]', '')
        try {
            return axios({
                method: "post",
                url: GET_SEARCH_OBYAV_URL,
                data: bodyFormData,
                headers: {"Content-Type": "application/json"},
            });
        } catch (e) {
            console.error(e);
        }
    }

    getMyObyavs(userData) {
        const bodyFormData = new FormData();
        bodyFormData.append('id', userData.id);
        bodyFormData.append('first_name', userData.first_name);
        bodyFormData.append('last_name', userData.last_name);
        bodyFormData.append('username', userData.username);
        bodyFormData.append('photo_url', userData.photo_url);
        bodyFormData.append('auth_date', userData.auth_date);
        bodyFormData.append('hash', userData.hash);
        try {
            return axios({
                method: "post",
                url: GET_MY_OBYAV_URL,
                data: bodyFormData,
                headers: {"Content-Type": "application/json"},
            });
        } catch (e) {
            console.error(e);
        }
    }

    postDeleteObyav(userData, mess_id) {
        const bodyFormData = new FormData();
        bodyFormData.append('id', userData.id);
        bodyFormData.append('first_name', userData.first_name);
        bodyFormData.append('last_name', userData.last_name);
        bodyFormData.append('username', userData.username);
        bodyFormData.append('photo_url', userData.photo_url);
        bodyFormData.append('auth_date', userData.auth_date);
        bodyFormData.append('hash', userData.hash);
        try {
            return axios({
                method: "post",
                url: POST_DEL_OBYAV_URL + mess_id,
                data: bodyFormData,
                headers: {"Content-Type": "application/json"},
            });
        } catch (e) {
            console.error(e);
        }
    }
}

export default new RequestService();