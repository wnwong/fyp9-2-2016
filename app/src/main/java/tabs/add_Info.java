package tabs;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.secondhandtradingplatform.R;
import com.example.user.secondhandtradingplatform.addGadget;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import product.Camera;
import server.ServerRequests;
import user.UserLocalStore;

public class add_Info extends Fragment implements View.OnClickListener {
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_CAMERA = 10;
    private static final int RESULT_LOAD_IMAGE2 = 2;
    private static final int REQUEST_CAMERA2 = 20;
    public static final String SERVER_ADDRESS = "http://php-etrading.rhcloud.com/";
    ImageView imageToUpload, imageToUpload2;
    ImageButton addGalleryBtn, addCameraBtn, addGalleryBtn2, addCameraBtn2;
    Button bUploadImage;
    EditText uploadImageName, gprice;
    Spinner productType, productBrand, productModel;
    RadioGroup rgroup;
    RadioButton yesBtn, noBtn;
    String warranty;
    UserLocalStore userLocalStore;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        imageToUpload = (ImageView) v.findViewById(R.id.imageToUpload);
        imageToUpload2 = (ImageView) v.findViewById(R.id.imageToUpload2);

        bUploadImage = (Button) v.findViewById(R.id.bUploadImage);
        addGalleryBtn = (ImageButton) v.findViewById(R.id.addGalleryBtn);
        addCameraBtn = (ImageButton) v.findViewById(R.id.addCameraBtn);
        addGalleryBtn2 = (ImageButton) v.findViewById(R.id.addGalleryBtn2);
        addCameraBtn2 = (ImageButton) v.findViewById(R.id.addCameraBtn2);

        uploadImageName = (EditText) v.findViewById(R.id.etUploadName);
        gprice = (EditText) v.findViewById(R.id.gprice);

        productType = (Spinner) v.findViewById(R.id.productType);
        String[] type = getResources().getStringArray(R.array.productType);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.myspinner, type);
        adapter.setDropDownViewResource(R.layout.myspinner);
        productType.setAdapter(adapter);

        productBrand = (Spinner) v.findViewById(R.id.productBrand);
        String[] brand = getResources().getStringArray(R.array.epBrand);
        ArrayAdapter brandAdapter = new ArrayAdapter(getActivity(), R.layout.myspinner, brand);
        brandAdapter.setDropDownViewResource(R.layout.myspinner);
        productBrand.setAdapter(brandAdapter);

        productModel = (Spinner) v.findViewById(R.id.productModel);
        String[] model = getResources().getStringArray(R.array.shureModel);
        ArrayAdapter modelAdapter = new ArrayAdapter(getActivity(), R.layout.myspinner, model);
        modelAdapter.setDropDownViewResource(R.layout.myspinner);
        productModel.setAdapter(modelAdapter);

        rgroup = (RadioGroup) v.findViewById(R.id.rgroup);
        yesBtn = (RadioButton) v.findViewById(R.id.yesButton);
        noBtn = (RadioButton) v.findViewById(R.id.noButton);

        addGalleryBtn.setOnClickListener(this);
        addCameraBtn.setOnClickListener(this);
        addGalleryBtn2.setOnClickListener(this);
        addCameraBtn2.setOnClickListener(this);
        bUploadImage.setOnClickListener(this);
        imageToUpload.setOnClickListener(this);
        imageToUpload2.setOnClickListener(this);
        rgroup.setOnCheckedChangeListener(listener);
        yesBtn.setOnClickListener(this);
        noBtn.setOnClickListener(this);

        userLocalStore = new UserLocalStore(getContext());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_addinfo, container, false);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            CursorLoader cursorLoader = new CursorLoader(getContext(), selectedImageUri, projection, null, null,
                    null);
            Cursor cursor = cursorLoader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            String selectedImagePath = cursor.getString(column_index);
            Bitmap bm;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            final int REQUIRED_SIZE = 200;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(selectedImagePath, options);
            imageToUpload.setImageBitmap(bm);
        } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            //           Uri selectedImage = data.getData();  // Get the address of the selected image
            //          imageToUpload.setImageURI(selectedImage);
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageToUpload.setImageBitmap(bitmap);
        } else if (requestCode == RESULT_LOAD_IMAGE2 && resultCode == Activity.RESULT_OK && data != null) {
//            Uri selectedImage = data.getData();  // Get the address of the selected image
//            imageToUpload2.setImageURI(selectedImage);
            Uri selectedImageUri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            CursorLoader cursorLoader = new CursorLoader(getContext(), selectedImageUri, projection, null, null,
                    null);
            Cursor cursor = cursorLoader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            String selectedImagePath = cursor.getString(column_index);
            Bitmap bm;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            final int REQUIRED_SIZE = 200;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(selectedImagePath, options);
            imageToUpload2.setImageBitmap(bm);
        } else if (requestCode == REQUEST_CAMERA2 && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageToUpload2.setImageBitmap(bitmap);
        }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bUploadImage:
                String type = productType.getSelectedItem().toString();
                String brand = productBrand.getSelectedItem().toString();
                String model = productModel.getSelectedItem().toString();
                String price = gprice.getText().toString();
                String location = ((addGadget) getActivity()).provideLocation();
                Log.i("custom_check", "location value in activity");
 //               Log.i("custom_check", location);
                Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
                new UploadImage(type, brand, model, warranty, price, image, uploadImageName.getText().toString()).execute();
                break;
            case R.id.addGalleryBtn:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case R.id.addCameraBtn:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
                break;
            case R.id.addGalleryBtn2:
                Intent galleryIntent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent2, RESULT_LOAD_IMAGE2);
                break;
            case R.id.addCameraBtn2:
                Intent cameraIntent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent2, REQUEST_CAMERA2);
                break;
            case R.id.imageToUpload:
                Bitmap bitmap = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
                if(bitmap != null)
                {
                    imageToUpload.setImageBitmap(RotateBitmap(bitmap, 90f));
                }
                break;
            case R.id.imageToUpload2:
                Bitmap bitmap2 = ((BitmapDrawable) imageToUpload2.getDrawable()).getBitmap();
                if(bitmap2 != null)
                {
                    imageToUpload2.setImageBitmap(RotateBitmap(bitmap2, 90f));
                }
                break;
        }
    }
    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int p = group.indexOfChild((RadioButton) getView().findViewById(checkedId));
            int count = group.getChildCount();
            switch (checkedId){
                case R.id.yesButton:
                    warranty = getString(R.string.yes);
                    Log.i("custom_check", warranty);
                    break;
                case R.id.noButton:
                    warranty = getString(R.string.no);
                    Log.i("custom_check", warranty);
                    break;
            }
        }
    };

    private class UploadImage extends AsyncTask<Void, Void, Void> {
        Bitmap image;
        String name, price, warranty, type, brand, model;
        OutputStreamWriter writer = null;
        BufferedReader reader = null;

        public UploadImage(String type, String brand, String model, String warranty, String price, Bitmap image, String name) {
            this.type = type;
            this.brand = brand;
            this.model = model;
            this.price = price;
            this.image = image;
            this.name = name;
            this.warranty = warranty;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] array = byteArrayOutputStream.toByteArray();
            String encodeImage = Base64.encodeToString(array, Base64.DEFAULT);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("type", type )
                    .appendQueryParameter("brand", brand)
                    .appendQueryParameter("model", model )
                    .appendQueryParameter("warranty", warranty )
                    .appendQueryParameter("price", price )
                    .appendQueryParameter("image", encodeImage)
                    .appendQueryParameter("name", name)
                    .appendQueryParameter("user_id", String.valueOf(userLocalStore.getLoggedInUser().getUser_id()));
            String query = builder.build().getEncodedQuery();

            Log.i("custom_check","The encoded query:");
            Log.i("custom_check", query);
            try {
                URL url = new URL(SERVER_ADDRESS + "SavePicture.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                Log.i("custom_check", "HTTP connection opened!!");
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                Log.i("custom_check", "Start Writing from Server");
                writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(query);
                writer.flush();
                Log.i("custom_check", "Start Reading from Server");
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.i("custom_check", "Server Response when uploading items");
                    Log.i("custom_check", line);
                }
                writer.close();
                reader.close();
                con.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT).show();
        }
    }
}
