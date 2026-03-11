package Modelo;

import java.sql.Timestamp;

    //Clase que representa un "Ticket" de error en el sistema
    //Encapsulación las variables
public class ErrorTicket {

    private int id;
    private String titulo;
    private String descripcion;
    private Severidad severidad;
    private Fase fase;
    private String solucion;

    //Constructor de ErrorTicket
   public ErrorTicket(String titulo, String descripcion, Severidad severidad, Fase fase) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.severidad = severidad;
    this.fase = fase;
}


    
    public int getId() {
        return id;  
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Severidad getSeveridad() {
        return severidad;
    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
    }

    private Timestamp fecha;
    
    public Timestamp getFecha(){
        return fecha;
    }
    
    public void setFecha(Timestamp fecha){
        this.fecha =fecha;
    }
    
    public String getSolucion() {
        return solucion;
    }

    public void setSolucion(String solucion) {
        this.solucion = solucion;
    }
    
    
}
