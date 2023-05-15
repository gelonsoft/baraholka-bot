import React from 'react';
import '../style/style.css';

class Profile extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let userData = JSON.parse(localStorage.getItem('userData'));
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