package RealmModel;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmGadget extends RealmObject {
    @PrimaryKey
    private int product_id;
    private String brand;
    private String model;
    private String warranty;
    private String price;
    private String seller_location;
    private String seller_location_2;
    private String seller;
    private String scratch;
    private String color;
    private String image;
    private String type;
    private String image1;
    private String seller_date;
    private String seller_date_2;
    private String seller_time_start;
    private String seller_time_start_2;
    private String seller_time_end;
    private String seller_time_end_2;
    private String availability;
    private String buyer;
    private String buyer_location;
    private String trade_date;
    private String trade_time;
    private String seller_phone;
    private String buyer_phone;
    private float rating;

    public RealmGadget() {
        //default constructor
    }

    public RealmGadget(int product_id, String buyer, String buyer_location, String trade_date, String trade_time, int rating, String availability, String seller_phone, String buyer_phone) {
        this.product_id = product_id;
        this.buyer = buyer;
        this.buyer_location = buyer_location;
        this.trade_date = trade_date;
        this.trade_time = trade_time;
        this.rating = rating;
        this.availability = availability;
        this.seller_phone = seller_phone;
        this.buyer_phone = buyer_phone;
    }

    public float getRating() {
        return rating;
    }

    public String getBuyer_location() {
        return buyer_location;
    }

    public void setBuyer_location(String buyer_location) {
        this.buyer_location = buyer_location;
    }

    public String getTrade_time() {
        return trade_time;
    }

    public void setTrade_time(String trade_time) {
        this.trade_time = trade_time;
    }

    public void setRating(float rating) {

        this.rating = rating;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getTrade_date() {
        return trade_date;
    }

    public void setTrade_date(String trade_date) {
        this.trade_date = trade_date;
    }

    public String getSeller_date() {
        return seller_date;
    }

    public void setSeller_date(String seller_date) {
        this.seller_date = seller_date;
    }

    public String getSeller_time_start() {
        return seller_time_start;
    }

    public void setSeller_time_start(String seller_time_start) {
        this.seller_time_start = seller_time_start;
    }

    public String getSeller_time_end() {
        return seller_time_end;
    }

    public void setSeller_time_end(String seller_time_end) {
        this.seller_time_end = seller_time_end;
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

    public String getSeller_phone() {
        return seller_phone;
    }

    public void setSeller_phone(String seller_phone) {
        this.seller_phone = seller_phone;
    }

    public String getBuyer_phone() {
        return buyer_phone;
    }

    public void setBuyer_phone(String buyer_phone) {
        this.buyer_phone = buyer_phone;
    }

    public String getSeller_location_2() {
        return seller_location_2;
    }

    public void setSeller_location_2(String seller_location_2) {
        this.seller_location_2 = seller_location_2;
    }

    public String getSeller_date_2() {
        return seller_date_2;
    }

    public void setSeller_date_2(String seller_date_2) {
        this.seller_date_2 = seller_date_2;
    }

    public String getSeller_time_start_2() {
        return seller_time_start_2;
    }

    public void setSeller_time_start_2(String seller_time_start_2) {
        this.seller_time_start_2 = seller_time_start_2;
    }

    public String getSeller_time_end_2() {
        return seller_time_end_2;
    }

    public void setSeller_time_end_2(String seller_time_end_2) {
        this.seller_time_end_2 = seller_time_end_2;
    }
}
