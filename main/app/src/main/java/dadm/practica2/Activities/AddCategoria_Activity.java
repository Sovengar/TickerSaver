package dadm.practica2.Activities;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.io.File;
import java.util.ArrayList;

import dadm.practica2.BuildConfig;
import dadm.practica2.DataTransformationObjects.DtoCategoria;
import dadm.practica2.Helpers.CategoriaHelper;
import dadm.practica2.Helpers.ImageHelper;
import dadm.practica2.Interfaces.Image_Properties;
import dadm.practica2.Interfaces.Intent_Properties;
import dadm.practica2.R;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AddCategoria_Activity extends AppCompatActivity implements Intent_Properties, Image_Properties {

    //VISTAS
    private RelativeLayout rl;
    private ImageView img;
    private Button btnAceptar;
    private EditText etNombre;
    private EditText etDescripcion;
    private FloatingActionButton fab_camara;

    //OTROS
    private CategoriaHelper categoriaHelper;
    private DtoCategoria categoria;
    private ArrayList<DtoCategoria> arrayCategorias;

    private boolean isEdit;
    private int id;
    private Intent intent;

    private File photoDir;
    private String path;
    private Uri pathUri;
    private ImageHelper imageHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcategoria_activity);

        //Instanciamos las vistas
        rl = (RelativeLayout) findViewById(R.id.relativelayout);
        img = (ImageView) findViewById(R.id.img);
        btnAceptar = (Button) findViewById(R.id.btnAdd);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etDescripcion = (EditText) findViewById(R.id.etDescripcion);
        fab_camara = (FloatingActionButton) findViewById(R.id.fab_camera);

        //Instanciamos el Helper para trabajar las fotos
        imageHelper = new ImageHelper(getApplicationContext());

        //OBTENEMOS LAS CATEGORIAS EN MEMORIA Y CREAMOS UN OBJETO CATEGORIA
        arrayCategorias = new ArrayList<DtoCategoria>();
        categoriaHelper  = new CategoriaHelper();
        categoriaHelper.cargarCategorias(getApplicationContext());
        arrayCategorias = categoriaHelper.getCategorias();
        categoria = new DtoCategoria();

        //COMPROBAR CON UN INTENT SI ES UNA CATEGORIA PARA EDITAR
        isEditableCategoria();

        //COMPROBAMOS SI TENEMOS PERMISOS PARA MOSTRAR EL BOTON
        if(mayRequestStoragePermission()) fab_camara.setEnabled(true);
        else fab_camara.setEnabled(false);

        //LOGICA VISTAS
        fab_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(AddCategoria_Activity.this, v);
                popup.getMenuInflater().inflate(R.menu.menu_open_take_foto, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.camara:
                                obtenerImagenCompleta();
                                return true;
                            case R.id.galeria:
                                obtenerGaleria();
                                return true;
                        }
                        return false;
                    }
                });
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categoria.setTitulo(etNombre.getText().toString());
                categoria.setShort_desc(etDescripcion.getText().toString());

                if (isEdit == true){
                    categoria.setId(id);
                    categoria.setFoto(arrayCategorias.get(id).getFoto());
                    categoriaHelper.editCategoria(id,categoria,getApplicationContext());
                }else{
                    categoria.setId(arrayCategorias.size());
                    categoriaHelper.addCategoria(categoria,getApplicationContext());
                }
                startActivity(new Intent(AddCategoria_Activity.this, Categoria_Activity.class));
            }
        });
    }

    private void isEditableCategoria() {
        intent = getIntent();
        isEdit = intent.getBooleanExtra(PARAM1, false);
        id = intent.getIntExtra(PARAM2, id);

        if(isEdit == true) {
            etNombre.setText(arrayCategorias.get(id).getTitulo());
            etDescripcion.setText(arrayCategorias.get(id).getShort_desc());
            imageHelper.setImageFromPathString(arrayCategorias.get(id).getFoto(),img);
        }
    }

    private void obtenerThumbnail() {
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }
    private void obtenerImagenCompleta(){
        path = imageHelper.getPhotoPath(getApplicationContext());
        photoDir = new File(path);
        pathUri = FileProvider.getUriForFile(AddCategoria_Activity.this, BuildConfig.APPLICATION_ID + ".provider", photoDir);
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pathUri);
        startActivityForResult(intent , REQUEST_TAKE_PHOTO);
    }
    private void obtenerGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, ""),SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            switch (requestCode){

                case REQUEST_TAKE_PHOTO:
                    MediaScannerConnection.scanFile(
                            this,
                            new String []{path},
                            null,
                            new MediaScannerConnection.OnScanCompletedListener(){
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path);
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });
                    imageHelper.setImageFromPathString(path,img);
                    categoria.setFoto(path);
                    break;

                case SELECT_PICTURE:
                    imageHelper.setImageFromPathUri(data,img);
                    categoria.setFoto(imageHelper.saveBitmapOnStorage(getApplicationContext()));
                    break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("file_path", path);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        path = savedInstanceState.getString("file_path");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS){
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                fab_camara.setEnabled(true);
            } else {
                showExplanation();
            }
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCategoria_Activity.this);
        builder.setTitle(getString(R.string.permisos3));
        builder.setMessage(getString(R.string.permisos2));

        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }

    private boolean mayRequestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        if ((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(rl, getString(R.string.permisos),Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            }).show();
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }
        return false;
    }



}