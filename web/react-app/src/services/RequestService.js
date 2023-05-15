import axios from "axios";

const GET_TAGS_URL = "http://localhost:8080/api/all_tags";

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
                headers: { "Content-Type": "multipart/form-data" },
            });
        } catch (e) {
            console.error(e);
        }
    }
}

export default new RequestService();