package baraholkateam.util;

import java.io.Serializable;

public class TelegramUserInfo implements Serializable {

    private final Long id;

    private final String first_name;

    private final String last_name;

    private final String username;

    private final String photo_url;

    private final Integer auth_date;

    private final String hash;

    public TelegramUserInfo(Long id, String first_name, String last_name, String username, String photo_url,
                            Integer auth_date, String hash) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.photo_url = photo_url;
        this.auth_date = auth_date;
        this.hash = hash;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getUsername() {
        return username;
    }

    public String getPhotoUrl() {
        return photo_url;
    }

    public Integer getAuthDate() {
        return auth_date;
    }

    public String getHash() {
        return hash;
    }

    public String getCheckString() {
        return "auth_date=" + auth_date + "\n"
                + "first_name=" + first_name + "\n"
                + "id=" + id + "\n"
                + "last_name=" + last_name + "\n"
                + "photo_url=" + photo_url + "\n"
                + "username=" + username;
    }
}
