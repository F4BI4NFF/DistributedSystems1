package DS;

import java.net.InetAddress;

/**
 * Clase que decribe que es un cliente
 */
public class Cliente {
    InetAddress IpCliente;
    int PuertoCliente;
    String distrito;

    public Cliente(InetAddress i,int p,String d){
        this.IpCliente = i;
        this.PuertoCliente = p;
        this.distrito = d;
    }
}
