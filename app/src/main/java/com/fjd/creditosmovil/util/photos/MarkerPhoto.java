package com.fjd.creditosmovil.util.photos;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import com.fjd.creditosmovil.R;
import com.watermark.androidwm.Watermark;
import com.watermark.androidwm.WatermarkBuilder;
import com.watermark.androidwm.bean.WatermarkText;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MarkerPhoto {

    Context context;
    Intent data;
    String tecnico, coordenadas, zona1, zona2, infraestructura;
    Bitmap bitmap = null, imageBitmap2, imageBitmap;
    Uri photoUri;


    public MarkerPhoto(Context context, Uri photoUri, String coordenadas, String infraestructura, String barrio) {
        this.context = context;
        this.coordenadas = coordenadas;
        this.infraestructura = infraestructura;
        this.photoUri = photoUri;

    }

    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @SuppressLint("SimpleDateFormat")
    public byte[] WaterMaker() {
        String text_fecha = "Fecha: " + new SimpleDateFormat("MM/dd/yy").format(new Date()) + " - " + new SimpleDateFormat("HH:mm:ss").format(new Date());
        Bitmap bitmap, imageBitmap2, imageBitmap;
        int orientation;
        List<WatermarkText> watermarkTexts = new ArrayList<>();
        Bitmap watermarkedBitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photoUri);
            orientation = getBitmapOrientation(photoUri);
            imageBitmap2 = RotatePhoto(bitmap, orientation);
            imageBitmap = imageBitmap2;
            watermarkTexts.add(new WatermarkText(text_fecha).setPositionX(0.01).setPositionY(0.92).setTextAlpha(200).setTextSize(9)
                    .setTextFont(R.font.bucking_regular).setTextColor(context.getColor(R.color.waterMaker))
                    .setTextShadow(5, 1, 1, Color.BLACK).setBackgroundColor(Color.TRANSPARENT));

            // Agrega más marcas de agua según sea necesario
            WatermarkBuilder watermarkBuilder = WatermarkBuilder.create(context, imageBitmap).loadWatermarkTexts(watermarkTexts);
            Watermark watermark = watermarkBuilder.getWatermark();
            watermarkedBitmap = watermark.getOutputImage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return getBytes(watermarkedBitmap);

    }

    private Bitmap RotatePhoto(Bitmap originalBitmap, int orientation) {

        // Obtener la rotación correspondiente
        int rotation = 0;
        switch (orientation) {
            case 90:
                rotation = 90;
                break;
            case 180:
                rotation = 180;
                break;
            case 270:
                rotation = 270;
                break;
            default:
                rotation = 0;
                break;
        }

        // Aplicar la rotación a la imagen
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);

        Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);

        return rotatedBitmap;
    }

    private int getBitmapOrientation(Uri photoUri) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            cursor = context.getContentResolver().query(photoUri, projection, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int orientationColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
                return cursor.getInt(orientationColumnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return 0; // Valor predeterminado si no se puede obtener la orientación
    }
}
