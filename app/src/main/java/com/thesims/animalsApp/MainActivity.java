package com.thesims.animalsApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
    }

    public void buttonRecognizeOnClick(View v) {
        InputImage image = InputImage.fromBitmap(bmp, 0);
        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        System.out.println("1");
        labeler.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> labels) {
                        float maxConfidence = -1.0f;
                        String recognizedLabel = "";
                        for (ImageLabel label : labels) {
                            if(label.getConfidence() > maxConfidence) {
                                maxConfidence = label.getConfidence();
                                recognizedLabel = label.getText();
                            }
                        }
                        System.out.println(recognizedLabel);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("RECOGNIZER", e.toString());
                    }
                });
    }
}