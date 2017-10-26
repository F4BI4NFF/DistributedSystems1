package DS;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Titan {

    private String Nombre_titan;
    private int tipo_titan;
    private int ID_titan;
    private static final AtomicInteger unico = new AtomicInteger(0);

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

    //Creacion con ID unica.

    Titan(){
        this.setID_titan(unico.incrementAndGet());
    }

    Titan(String Nombre_titan, int tipo_titan, int ID_titan){
        this.Nombre_titan = Nombre_titan;
        this.tipo_titan = tipo_titan;
        this.ID_titan = ID_titan;
    }

}

