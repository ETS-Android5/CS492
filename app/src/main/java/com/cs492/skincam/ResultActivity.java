package com.cs492.skincam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ResultActivity extends AppCompatActivity {

    Button btn_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        btn_return = (Button) findViewById(R.id.btn_return);

        ImageView imageview = (ImageView)findViewById(R.id.imageview);
        TextView textView = (TextView)findViewById(R.id.textview);

        Bundle extras = getIntent().getExtras();

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        // 형이 model sampling

        // load pretrained model
        Module module = null;
        try {
            Log.i("MODEL", "MODEL_LOAD_SUCCESS");
            module = Module.load(assetFilePath(getApplicationContext(), "mobile_resnet18.ptl"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        float[] zeros = new float[]{0f, 0f, 0f};
        float[] ones = new float[]{1f, 1f, 1f};  // Since the return values is already 0~1

        Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(bitmap,0,0,224,224,
                zeros, ones);
        float[] inp=inputTensor.getDataAsFloatArray();

        Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
        float[] scores = outputTensor.getDataAsFloatArray();

        float maxScore = -Float.MAX_VALUE;
        int maxScoreIdx = -1;
        for (int i = 0; i < scores.length; i++) {
            Log.i("MODEL_SCORE", String.valueOf(scores[i]));
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                maxScoreIdx = i;
            }
        }
        Log.i("MODEL_RESULT", String.valueOf(maxScoreIdx));


        if(maxScoreIdx == 0) {
            textView.setText("acne");
        }
        else if(maxScoreIdx == 1){
            textView.setText("Actinic Keratosis Basal Cell Carcinoma/Malignant Lesion");
        }
        else if(maxScoreIdx == 2){
            textView.setText("Atopic Dermatitis");
        }
        else if(maxScoreIdx == 3){
            textView.setText("Bullous Disease");
        }
        else if(maxScoreIdx == 4){
            textView.setText("Cellulitis Impetigo and other Bacterial Infection");
        }
        else if(maxScoreIdx == 5){
            textView.setText("Eczema");
        }
        else if(maxScoreIdx == 6){
            textView.setText("Exanthem/Drug Eruption");
        }
        else if(maxScoreIdx == 7){
            textView.setText("Hair Loss or Hair Disease");
        }
        else if(maxScoreIdx == 8){
            textView.setText("Herpes HPV or STD");
        }
        else if(maxScoreIdx == 9){
            textView.setText("Light Disease/Disorder of Pigmentation");
        }
        else if(maxScoreIdx == 10){
            textView.setText("Lupus/Connective Tissue disease");
        }
        else if(maxScoreIdx == 11){
            textView.setText("Melanoma Skin Cancer");
        }
        else if(maxScoreIdx == 12){
            textView.setText("Nail Fungus or Nail Disease");
        }
        else if(maxScoreIdx == 13){
            textView.setText("Contact Dermatitis");
        }
        else if(maxScoreIdx == 14){
            textView.setText("Psoriasis/Lichen Planus and related disease");
        }
        else if(maxScoreIdx == 15){
            textView.setText("Scabies Lyme Disease/Infestation/Bite");
        }
        else if(maxScoreIdx == 16){
            textView.setText("Seborrheic Keratoses and other Benign Tumor");
        }
        else if(maxScoreIdx == 17){
            textView.setText("Systemic Disease");
        }
        else if(maxScoreIdx == 18){
            textView.setText("Fungal Infection");
        }
        else if(maxScoreIdx == 19){
            textView.setText("Urticaria Hives");
        }
        else if(maxScoreIdx == 20){
            textView.setText("Vascular Tumor");
        }
        else if(maxScoreIdx == 21){
            textView.setText("Vasculitis");
        }
        else{
            textView.setText("Warts/Viral Infection");
        }

        imageview.setImageBitmap(bitmap);

        btn_return.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                switch (view.getId()){
                    case R.id.btn_return:
                        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainIntent);
                        break;
                }
            }
        });


    }

    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }
}