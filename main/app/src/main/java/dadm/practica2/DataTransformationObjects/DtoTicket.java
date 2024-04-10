package dadm.practica2.DataTransformationObjects;

import java.util.Date;

public class DtoTicket {

    //PROPIEDADES
    private int id;
    private String foto;
    private String titulo;
    private int categoria;
    private double precio;
    private long fecha_alta;
    private String short_desc;
    private String ocr;

    //CONSTRUCTOR
    public DtoTicket() {
        setFecha_alta(new Date().getTime());
    }

    //GETTERS Y SETTERS
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }
    public int getCategoria() {
        return categoria;
    }
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public long getFecha_alta() {
        return fecha_alta;
    }
    public void setFecha_alta(long fecha_alta) {
        this.fecha_alta = fecha_alta;
    }
    public String getShort_desc() {
        return short_desc;
    }
    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getOcr() {
        return ocr;
    }
    public void setOcr(String ocr) {
        this.ocr = ocr;
    }
}
