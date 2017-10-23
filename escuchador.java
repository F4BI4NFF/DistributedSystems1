import java.io.*;
import java.net.*;
import java.util.*;

public class escuchador {

 public static void main(String[] args) throws IOException {
	// El mismo puerto que se uso en la parte de enviar.
	MulticastSocket escucha = new MulticastSocket(55557);

	System.out.println(" Servidor multicast a la escucha");
	// Nos ponemos a la escucha de la misma IP de Multicast que se uso en la parte de enviar.
	escucha.joinGroup(InetAddress.getByName("230.0.0.1"));
						
	// Un array de bytes con tamaño suficiente para recoger el mensaje enviado, 
	// bastaría con 4 bytes.
	byte [] dato = new byte [1024];

	// Se espera la recepción. La llamada a receive() se queda
	// bloqueada hasta que llegue un mesnaje.
	DatagramPacket dgp = new DatagramPacket(dato, dato.length);
	escucha.receive(dgp);

	// Obtención del dato ya relleno.
	byte [] dato_recibir = dgp.getData();
	System.out.println(dato_recibir);
	}
}