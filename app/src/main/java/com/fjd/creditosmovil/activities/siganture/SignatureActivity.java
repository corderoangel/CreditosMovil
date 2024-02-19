package com.fjd.creditosmovil.activities.siganture;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fjd.creditosmovil.databinding.ActivitySignatureBinding;
import com.fjd.creditosmovil.util.singletons.CaptureBitmapView;

public class SignatureActivity extends AppCompatActivity {

    ActivitySignatureBinding binding;
    CaptureBitmapView captureBitmapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignatureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        captureBitmapView = new CaptureBitmapView(this, null);
        binding.contentSignatureView.addView(captureBitmapView);
        binding.clearSignatureButton.setOnClickListener(v-> captureBitmapView.ClearCanvas());
    }
}