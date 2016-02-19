package user;

/**
 * Created by User on 19/2/2016.
 */
public class userGadget {
    private String seller, time, date, location, product;

    public userGadget(String product, String seller, String time, String date, String location) {
        this.product = product;
        this.seller = seller;
        this.time = time;
        this.date = date;
        this.location = location;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
