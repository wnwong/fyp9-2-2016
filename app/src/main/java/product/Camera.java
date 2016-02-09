package product;

import java.util.ArrayList;
import java.util.List;



public class Camera   {
    public String name;
    public String price;
    public String warranty;
    public String place;
    public int photoid;
    public String seller;
    public String newPrice;
    public String phonNo;
    public String email;


    public static List<Camera> cameras = new ArrayList<>();

    //Constructor
    public Camera(String name, String price, String warranty, String place, int photoid, String seller, String newPrice, String phonNo, String email){
        this.name = name;
        this.price = price;
        this.warranty = warranty;
        this.place = place;
        this.photoid = photoid;
        this.seller = seller;
        this.newPrice = newPrice;
        this.phonNo = phonNo;
        this.email = email;
    }


    public void add(Camera camera){

        cameras.add(camera);

    }

    public static List<Camera> get (){
        return cameras;
    }




    private void initData(){
        cameras = new ArrayList<>();

    }






//    private String name;
//    private String price;
//    private String warranty;
//    private String place;
//    private int photoid;
//    private String seller;
//    private String newPrice;
//    private String phonNo;
//    private String email;
}
