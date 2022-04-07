package com.thesims.animalsApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateNewAnimal();
    }

    public void buttonRecognizeOnClick(View v) {
        InputImage image = InputImage.fromBitmap(bmp, 0);
        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
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
                        setRecognizedText(recognizedLabel);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("RECOGNIZER", e.toString());
                        System.out.println("error : " + e);
                    }
                });
    }

    public void buttonNextOnClick(View v){
        generateNewAnimal();
    }

    private void generateNewAnimal(){
        setRecognizedText("");
        Map<Integer, String> animalsMap = generateAnimalsMap();
        List<Integer> animalKeys = new ArrayList<>(animalsMap.keySet());
        Integer randomKey = animalKeys.get(new Random().nextInt(animalKeys.size()));
        bmp = BitmapFactory.decodeResource(getResources(), randomKey);
        ImageView animalImageView = findViewById(R.id.imageView);
        animalImageView.setBackgroundResource(randomKey);
    }

    private Map<Integer, String> generateAnimalsMap(){
        Map<Integer, String> animalsMap = new HashMap<>();
        animalsMap.put(R.drawable.cat, "@/drawable-v24/cat.bmp");
        animalsMap.put(R.drawable.bird, "@/drawable-v24/bird.bmp");
        animalsMap.put(R.drawable.dog, "@/drawable-v24/dog.bmp");
        animalsMap.put(R.drawable.horse, "@/drawable-v24/horse.bmp");
        return animalsMap;
    }

    private void setRecognizedText(String newText){
        TextView recognizedAnimalText = findViewById(R.id.textViewLabel);
        recognizedAnimalText.setText(newText);
    }
}