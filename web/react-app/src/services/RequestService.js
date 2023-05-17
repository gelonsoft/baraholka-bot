import axios from "axios";

const GET_TAGS_URL = "http://localhost:8080/api/all_tags";
const GET_SEARCH_ADS_URL = "http://localhost:8080/api/search_advertisements";
const GET_MY_ADS_URL = "http://localhost:8080/api/my_advertisements";
const POST_DEL_AD_URL = "http://localhost:8080/api/delete_advertisement/";
const NEW_AD_URL = "http://localhost:8080/api/add_advertisement";

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
                url: NEW_AD_URL,
                data: body,
                headers: { "Content-Type": "application/json" },
            });
        } catch (e) {
            console.error(e);
        }
    }

    getSearchAds(userData, tags) {
        const bodyJSON = {
            "id": userData.id,
            "first_name": userData.first_name,
            "last_name": userData.last_name,
            "username": userData.username,
            "photo_url": userData.photo_url,
            "auth_date": userData.auth_date,
            "hash": userData.hash,
            "tags": tags
        }
        try {
            return axios({
                method: "post",
                url: GET_SEARCH_ADS_URL,
                data: bodyJSON,
                headers: { "Content-Type": "application/json" },
            });
        } catch (e) {
            console.error(e);
        }
    }

    getMyAds(userData) {
        try {
            return axios({
                method: "post",
                url: GET_MY_ADS_URL,
                data: userData,
                headers: { "Content-Type": "application/json" },
            });
        } catch (e) {
            console.error(e);
        }
    }

    postDeleteAd(userData, mess_id) {
        try {
            return axios({
                method: "post",
                url: POST_DEL_AD_URL+mess_id,
                data: userData,
                headers: { "Content-Type": "application/json" },
            });
        } catch (e) {
            console.error(e);
        }
    }
}

export default new RequestService();