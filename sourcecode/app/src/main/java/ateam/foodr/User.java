package ateam.foodr;

public class User {

    //The variables for the user
    public String id;
    public String email;
    public String password_hash;
    public String[] Restaurant;
    public String user_type;
    public String user_name;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String[] getRestaurant() {
        return Restaurant;
    }

    public void setRestaurant(String[] restaurant) {
        Restaurant = restaurant;
    }
}
