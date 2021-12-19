
package com.cs492.skincam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class GalleryActivity extends AppCompatActivity {
    ImageView imageView;
    String imgName = "osz.png";    // 이미지 이름

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        imageView = findViewById(R.id.imageView);

        try {
            String imgpath = getCacheDir() + "/" + imgName;   // Image path stored in internal storage
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            imageView.setImageBitmap(bm);   // Set the image stored in the internal storage to the imageview
            Toast.makeText(getApplicationContext(), "파일 로드 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
        }
    }

    public void bt1(View view) {    // Click to select an image and it will be executed Open the gallery to select an image
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // gallery
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                ContentResolver resolver = getContentResolver();
                try {
                    InputStream instream = resolver.openInputStream(fileUri);
                    Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                    imageView.setImageBitmap(imgBitmap);    // Selected image set to imageview
                    instream.close();   // close the stream
                    saveBitmapToJpeg(imgBitmap);    // save to internal storage
                    Toast.makeText(getApplicationContext(), "파일 불러오기 성공", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void saveBitmapToJpeg(Bitmap bitmap) {   // Save selected image to internal storage
        File tempFile = new File(getCacheDir(), imgName);    // Enter the file path and name
        try {
            tempFile.createNewFile();   // Initialize create empty files
            FileOutputStream out = new FileOutputStream(tempFile);  // Preparing a file writable stream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // Saving a bitmap to a stream using the compress function
            out.close();    // close the stream
            Toast.makeText(getApplicationContext(), "파일 저장 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }

    public void bt2(View view) {    // delete image
        try {
            File file = getCacheDir();  // Get the internal storage cache path
            File[] flist = file.listFiles();
            for (int i = 0; i < flist.length; i++) {    // iterate over the size of the array
                if (flist[i].getName().equals(imgName)) {   // If there is a file name that is the same as the name you want to delete, run it
                    flist[i].delete();  // delete file
                    Toast.makeText(getApplicationContext(), "파일 삭제 성공", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 삭제 실패", Toast.LENGTH_SHORT).show();
        }
    }

    public void bt3(View view) {    // Click on the image selection button to send the bitmap file to the result activity.
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        float scale = (float) (1024/(float)bitmap.getWidth());
        int image_w = (int) (bitmap.getWidth() * scale);
        int image_h = (int) (bitmap.getHeight() * scale);
        Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
        resize.compress(Bitmap.CompressFormat.JPEG, 100, stream); // The process of converting photos to the right size
        byte[] byteArray = stream.toByteArray(); // save picture to byte array

        Intent intent = new Intent(GalleryActivity.this,ResultActivity.class); // send a bitmap to results activity
        intent.putExtra("image", byteArray);

        startActivity(intent);
    }




}



