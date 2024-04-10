package dadm.practica2.DataTransformationObjects;

public class DtoCategoria {

    //PROPIEDADES
    private int id;
    private String titulo;
    private String short_desc;
    private String foto;

    //CONSTRUCTOR
    public DtoCategoria() {};

    //GETTERS Y SETTERS
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getShort_desc() {
        return short_desc;
    }
    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }
    public String getFoto() {
        return foto;
    }
    public void setFoto(String img) {
        this.foto = img;
    }




}
