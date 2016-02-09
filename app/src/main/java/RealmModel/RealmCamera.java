package RealmModel;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class RealmCamera extends RealmObject {
    @PrimaryKey
    private String name;
    private String price;
    private String warranty;
    private String place;
    private int photoid;
    private String seller;
    private String newPrice;
    private String phonNo;
    private String email;

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setPhotoid(int photoid) {
        this.photoid = photoid;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public void setPhonNo(String phonNo) {
        this.phonNo = phonNo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getWarranty() {
        return warranty;
    }

    public String getPlace() {
        return place;
    }

    public int getPhotoid() {
        return photoid;
    }

    public String getSeller() {
        return seller;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public String getPhonNo() {
        return phonNo;
    }

    public String getEmail() {
        return email;
    }
}