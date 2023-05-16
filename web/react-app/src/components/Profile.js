import React from 'react';
import '../style/style.css';

class Profile extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        //let userData = JSON.parse(localStorage.getItem('userData'));
        let userData = {"auth_date":1684051188,"first_name":"Алиса","hash":"afc6a8181ae6eb8f494551c94c39a63fae2835470210428556f8db7f54b66603","id":538160964,"last_name":"Селецкая","photo_url":"https://t.me/i/userpic/320/Uim0VYUr3WRDc7ofnIj40wRzPe1L7t63Nv0FXKqydjM.jpg","username":"sealisaa"};
        return (
            <div className="profile">
                <img className="profile-photo" alt="profile photo" src={userData.photo_url}></img>
                <div className="profile-info">
                    <div className="user-name">{userData.first_name} {userData.last_name}</div>
                    <div className="user-login">{userData.username}</div>
                </div>
            </div>
        )
    }
}

export default Profile;