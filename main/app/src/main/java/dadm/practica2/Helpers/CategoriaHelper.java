package dadm.practica2.Helpers;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import dadm.practica2.DataTransformationObjects.DtoCategoria;

public class CategoriaHelper {

    //CONSTANTES
    private static final String FICHERO="categorias.json";

    //OTROS
    private Gson mGson;
    private ArrayList<DtoCategoria> arrayCategorias;

    //CONSTRUCTOR
    public CategoriaHelper() {
        arrayCategorias = new ArrayList<DtoCategoria>();
        mGson = new GsonBuilder().create();
    }

    public void addCategoria(DtoCategoria categoria, Context context){
        cargarCategorias(context);
        arrayCategorias = getCategorias();
        arrayCategorias.add(categoria);
        guardarCategorias(context);
    }

    public void deleteCategoria(int index, Context context){
        cargarCategorias(context);
        arrayCategorias = getCategorias();
        arrayCategorias.remove(index);
        guardarCategorias(context);
    }

    public void editCategoria(int index, DtoCategoria categoria, Context context){
        cargarCategorias(context);
        arrayCategorias = getCategorias();
        arrayCategorias.set(index,categoria);
        guardarCategorias(context);
    }

    //Recupera las categorias del almacenamiento interno
    public void cargarCategorias(Context context){

        File directorio = context.getFilesDir();
        File file = new File(directorio, FICHERO);
        isFileCreated(file);

        try{
            FileInputStream fis = context.openFileInput(FICHERO);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null)
                sb.append(line);
            if(sb.toString().equals(""))
                return;
            else
                arrayCategorias = mGson.fromJson(sb.toString(), new TypeToken<ArrayList<DtoCategoria>>(){}.getType());
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //Guarda las categorias al almacenamiento interno /data/data/com.example.myapplication/files/FICHERO
    public void guardarCategorias(Context context){

        String categorias = mGson.toJson(arrayCategorias);
        FileOutputStream outputStream;

        File directorio = context.getFilesDir();
        File file = new File(directorio, FICHERO);
        isFileCreated(file);

        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(categorias.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DtoCategoria> getCategorias(){
        return arrayCategorias;
    }

    //Si no existe el fichero lo crea
    private void isFileCreated(File file){
        if(!file.exists()){
            try {
                file.createNewFile();
            }catch(IOException ioe){
            }
        }
    }

    // ALMACENAMIENTO EXTERNO, NO LO USO //

    //Escribir en el fichero mediante almacenamiento externo
    private void GuardarDatosFichero_Externo() {

        //Creamos dos variables booleanas que nos permitirán registrar los estados de la memoria
        boolean mExternaHabilitada= false;
        boolean mExternaEscribible= false;

        /*
        Revisar que tenemos los permisos correspondientes en el Manifest
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
        <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
        */

        //Crearemos una string para guardar el estado de dicha memoria
        String estadoMemoria = Environment.getExternalStorageState();
        //Comprobaremos si podemos leer y escribir en la memoria externa
        if(estadoMemoria.equals(Environment.MEDIA_MOUNTED)){
            mExternaHabilitada = true;
            mExternaEscribible = true;
        } //Podremos leer pero no escribir
        else if (estadoMemoria.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            mExternaHabilitada = true;
            mExternaEscribible = false;
        }
        else { //No se puede ni leer ni escribir
            mExternaHabilitada = false;
            mExternaEscribible = false;
        }
    }

    public File getMediaDir(Context context){
        //return contexto.getExternalFilesDir(Environment.DIRECTORY_DCIM + "/datos");
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }
}
