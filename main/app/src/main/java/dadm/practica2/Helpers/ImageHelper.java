package dadm.practica2.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import dadm.practica2.Interfaces.Image_Properties;

public class ImageHelper extends Activity implements Image_Properties {

    private File storageDir;
    private String path;
    private Uri pathUri;
    private Bitmap bitmap;

    //CONSTRUCTOR
    public ImageHelper(Context context){
        storageDir = new File(context.getExternalFilesDir(null),Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) storageDir.mkdirs();
        //storageDir = /storage/emulated/0/Android/data/dadm.practica2/files/Pictures
    }
    /*
    public void obtenerImagenCompleta(Context context, AddTicket_Activity a){
        path = getPhotoPath(context);
        photoDir = new File (path);
        pathUri = FileProvider.getUriForFile(a.this, BuildConfig.APPLICATION_ID + ".provider", photoDir);
        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pathUri);
        a.startActivityForResult(takePictureIntent , REQUEST_TAKE_PHOTO);
    }
    public void obtenerGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"),SELECT_PICTURE);
    }
    */

    public String getPhotoPath(Context context){
        Long timestamp =  System.currentTimeMillis() / 1000;
        String imageName = timestamp.toString() + ".jpg";
        return context.getExternalFilesDir(null) + File.separator + Environment.DIRECTORY_PICTURES + File.separator + imageName;
    }

    /*
    Obtiene un bitmap a partir de la uri
    Crea y almacena la ruta fisica de la foto
    Creamos un File con la ruta fisica de la foto
    Guardamos la foto en la ruta con el formato JPEG
    Devolvemos la ruta fisica
    */
    public String saveBitmapOnStorage(Context context){
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), pathUri);
            path = getPhotoPath(context);
            File photoDir = new File (path);
            FileOutputStream out = new FileOutputStream(photoDir, false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }  catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
    public void setImageFromPathString(String path, ImageView img) {
        bitmap = BitmapFactory.decodeFile(path);
        img.setImageBitmap(bitmap);
    }
    public void setImageFromPathUri (Intent data, ImageView img) {
        pathUri = data.getData();
        img.setImageURI(pathUri);
    }




}
