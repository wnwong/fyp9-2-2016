package product;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class earphone extends RealmObject {

    private int pid;
    private String brand;
    private String model;
    private String warranty;
    private String price;
    private String location;
    private String image;



    public earphone(){}

    public earphone(int pid, String brand, String model, String warranty, String price, String location,String image) {
        this.pid = pid;
        this.brand = brand;
        this.model = model;
        this.warranty = warranty;
        this.price = price;
        this.location = location;
        this.image = image;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
