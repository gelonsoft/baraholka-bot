import axios from "axios";

const GET_TAGS_URL = "http://localhost:8080/api/all_tags";
const GET_SEARCH_ADS_URL = "http://localhost:8080/api/search_advertisements";
const GET_MY_ADS_URL = "http://localhost:8080/api/my_advertisements";
const POST_DEL_AD_URL = "http://localhost:8080/api/delete_advertisement/";

class RequestService {
    getTags(userData) {
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
                url: GET_TAGS_URL,
                data: bodyFormData,
                headers: {"Content-Type": "application/json"},
            });
        } catch (e) {
            console.error(e);
        }
    }

    getSearchAds(userData, tags) {
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
                url: GET_SEARCH_ADS_URL,
                data: bodyFormData,
                headers: {"Content-Type": "application/json"},
            });
        } catch (e) {
            console.error(e);
        }
    }

    getMyAds(userData) {
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
                url: GET_MY_ADS_URL,
                data: bodyFormData,
                headers: {"Content-Type": "application/json"},
            });
        } catch (e) {
            console.error(e);
        }
    }

    postDeleteAd(userData, mess_id) {
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
                url: POST_DEL_AD_URL + mess_id,
                data: bodyFormData,
                headers: {"Content-Type": "application/json"},
            });
        } catch (e) {
            console.error(e);
        }
    }

}

export default new RequestService();