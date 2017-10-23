import java.io.*;
import java.net.*;
import java.util.*;

public class enviador {

 public static void main(String[] args) throws IOException {

MulticastSocket enviador = new MulticastSocket();

// El dato que queramos enviar en el mensaje, como array de bytes.
byte [] dato = new byte[] {1,2,3,4};

// Usamos la direccion Multicast 230.0.0.1, por poner alguna dentro del rango
// y el puerto 55557, uno cualquiera que esté libre.
DatagramPacket dgp = new DatagramPacket(dato, dato.length, InetAddress.getByName("230.0.0.1"), 55557);

// Envío
enviador.send(dgp);
	}
}