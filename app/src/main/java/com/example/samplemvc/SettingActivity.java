package com.example.samplemvc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions;

import java.io.IOException;

public class SettingActivity extends AppCompatActivity {
     ImageView converter_clear,converter_getImage,converter_copy;
     EditText conv_result;
     Uri imageUri;
     TextRecognizer textRecognizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        converter_clear = findViewById(R.id.conv_clear);
        converter_getImage = findViewById(R.id.conv_camera);
        converter_copy = findViewById(R.id.conv_copy);
        conv_result = findViewById(R.id.conv_resultView);

        textRecognizer = TextRecognition.getClient(new JapaneseTextRecognizerOptions.Builder().build());

        converter_getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(SettingActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        converter_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = conv_result.getText().toString();
                if (text.isEmpty()) {
                    Toast.makeText(SettingActivity.this,"Empty",Toast.LENGTH_LONG).show();
                } else {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(SettingActivity.this.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Data", conv_result.getText().toString());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(SettingActivity.this,"Copied",Toast.LENGTH_LONG).show();

                }

            }
        });

        converter_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = conv_result.getText().toString();
                if (text == null) {
                    Toast.makeText(SettingActivity.this,"Empty",Toast.LENGTH_LONG).show();
                } else {
                    conv_result.setText("Result.......");
                }
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){

            if (data != null) {
                imageUri = data.getData();
                Toast.makeText(SettingActivity.this,"image selected",Toast.LENGTH_LONG).show();
                recognizeText();
            }
        } else {
            Toast.makeText(SettingActivity.this,"image not selected",Toast.LENGTH_LONG).show();
        }
    }
    private void recognizeText() {
        if (imageUri != null) {

            try {
                InputImage inputImage = InputImage.fromFilePath(SettingActivity.this, imageUri);

                Task<Text> result = textRecognizer.process(inputImage)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text text) {
                                String recognizedText = text.getText();
                                conv_result.setText(recognizedText);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (IOException e) {

                throw new RuntimeException(e);
            }
        }
    }
}