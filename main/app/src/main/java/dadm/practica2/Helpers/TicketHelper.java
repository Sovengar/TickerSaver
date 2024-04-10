package dadm.practica2.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import dadm.practica2.DataTransformationObjects.DtoTicket;

public class TicketHelper extends SQLiteOpenHelper {

    //OTROS
    private Cursor cursor;
    private SQLiteDatabase db;
    private DtoTicket ticket;
    private ArrayList<DtoTicket> arrayTickets;

    private static final String BD_NAME = "DBTickets";
    private static final String TABLE_TICKETS  = "Tickets";
    private static int BD_VERSION = 10;

    //CAMPOS PARA LA TABLA
    private static final String ID = "_id";
    private static final String FOTO = "foto";
    private static final String TITULO = "titulo";
    private static final String CATEGORIA = "categoria";
    private static final String PRECIO = "precio";
    private static final String FECHA = "fecha";
    private static final String SHORT_DESC = "desccorta";
    private static final String OCR = "ocr";
    private static final String[] CAMPOS = { ID, FOTO, TITULO, CATEGORIA, PRECIO, FECHA, SHORT_DESC, OCR};

    //QUERY
    private static final String CREATE_DB = "CREATE TABLE " + TABLE_TICKETS +"("+
            ID+" INTEGER PRIMARY KEY, "+
            FOTO+" TEXT NOT NULL, "+
            TITULO+" TEXT NOT NULL, "+
            CATEGORIA+" INTEGER NOT NULL, "+
            PRECIO+" DOUBLE NOT NULL, "+
            FECHA+" LONG NOT NULL, "+
            SHORT_DESC+" TEXT NOT NULL, "+
            OCR+" TEXT"+
            ")";

    //CONSTRUCTOR
    public TicketHelper(Context contexto, SQLiteDatabase.CursorFactory factory) {
        super(contexto, BD_NAME, factory, BD_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) { db.execSQL(CREATE_DB); }

    @Override public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKETS);
        db.execSQL(CREATE_DB);
    }

    public ArrayList<DtoTicket> obtenerTickets() {
        db = getWritableDatabase();
        cursor = db.query(TABLE_TICKETS, CAMPOS, "", null, null, null, null);
        arrayTickets = new ArrayList<DtoTicket>();

        if (cursor.moveToFirst()) {
            do {
                ticket = obtenerValoresTicket();
                arrayTickets.add(ticket);
            } while(cursor.moveToNext());
        }
        return arrayTickets;
    }

    private DtoTicket obtenerValoresTicket(){
        ticket = new DtoTicket();
        ticket.setId(cursor.getInt(0));
        ticket.setFoto(cursor.getString(1));
        ticket.setTitulo(cursor.getString(2));
        ticket.setCategoria(cursor.getInt(3));
        ticket.setPrecio(cursor.getDouble(4));
        ticket.setFecha_alta(cursor.getLong(5));
        ticket.setShort_desc(cursor.getString(6));
        ticket.setOcr(cursor.getString(7));
        return ticket;
    }

    private ContentValues asignarValoresTicket(DtoTicket ticket) {
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put(ID,ticket.getId());
        nuevoRegistro.put(FOTO, ticket.getFoto());
        nuevoRegistro.put(TITULO, ticket.getTitulo());
        nuevoRegistro.put(CATEGORIA, ticket.getCategoria());
        nuevoRegistro.put(PRECIO, ticket.getPrecio());
        nuevoRegistro.put(FECHA, ticket.getFecha_alta());
        nuevoRegistro.put(SHORT_DESC, ticket.getShort_desc());
        nuevoRegistro.put(OCR, ticket.getOcr());
        return nuevoRegistro;
    }

    public void agregarTicket(DtoTicket ticket) {
        ContentValues nuevoRegistro = asignarValoresTicket(ticket);
        db = getWritableDatabase();
        db.insert(TABLE_TICKETS, null, nuevoRegistro);
    }

    public void borrarTicket(int ticket) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TICKETS, ID+" = "+ticket, null);
        db.close();
    }

    public  void editarTicket(DtoTicket ticket, int id){
        ContentValues editarRegistro = asignarValoresTicket(ticket);
        db = getWritableDatabase();
        try {
            db.update(TABLE_TICKETS, editarRegistro, ID+" = "+id, null);
        }catch (Exception e){
            e.getMessage();
        }
    }

}

