import React from 'react';
import '../style/style.css';
//import avatar from '../img/avatar.svg';

class Profile extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let userData = JSON.parse(localStorage.getItem('userData'));
        return (
            <div className="profile">
                <div className="profile-photo-container">
                    <img className="profile-photo" alt="profile photo"
                         src={userData.photo_url ? userData.photo_url : "/img/avatar"}></img>
                </div>
                <div className="profile-info">
                    <div className="user-name">{userData.first_name} {userData.last_name}</div>
                    <div className="user-login">{userData.username}</div>
                </div>
            </div>
        )
    }
}

export default Profile;