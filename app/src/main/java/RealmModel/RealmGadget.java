package RealmModel;


import io.realm.RealmObject;

public class RealmGadget extends RealmObject {
    private int product_id;
    private String brand;
    private String model;
    private String warranty;
    private String price;
    private String seller_location;
    private String seller;
    private String scratch;
    private String color;
    private String image;
    private String type;
    private String image1;
    private String seller_date;
    private String seller_time;

    public String getSeller_date() {
        return seller_date;
    }

    public void setSeller_date(String seller_date) {
        this.seller_date = seller_date;
    }

    public String getSeller_time() {
        return seller_time;
    }

    public void setSeller_time(String seller_time) {
        this.seller_time = seller_time;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSeller_location() {
        return seller_location;
    }

    public void setSeller_location(String seller_location) {
        this.seller_location = seller_location;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getScratch() {
        return scratch;
    }

    public void setScratch(String scratch) {
        this.scratch = scratch;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
