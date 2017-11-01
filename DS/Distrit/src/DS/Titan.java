package DS;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase que describe un titan
 */
public class Titan {

    private String Nombre_titan;
    private int tipo_titan;
    private int ID_titan;

    public String getNombre_titan() {
        return Nombre_titan;
    }

    public void setNombre_titan(String nombre_titan) {
        Nombre_titan = nombre_titan;
    }

    public int getTipo_titan() {
        return tipo_titan;
    }

    public void setTipo_titan(int tipo_titan) {
        this.tipo_titan = tipo_titan;
    }

    public int getID_titan() {
        return ID_titan;
    }

    public void setID_titan(int ID_titan) {
        this.ID_titan = ID_titan;
    }

    public String getNombreTip(int tipo){
        String result = "";
        if (tipo == 1) {
            result = "Normal";
        }else if(tipo == 2){
            result = "Excentrico";
        }else if(tipo == 3){
            result = "Cambiante";
        }
        return result;
    }

    //Creacion con ID unica.

    Titan(String Nombre_titan, int tipo_titan, int ID_titan){
        this.Nombre_titan = Nombre_titan;
        this.tipo_titan = tipo_titan;
        this.ID_titan = ID_titan;
    }

}

