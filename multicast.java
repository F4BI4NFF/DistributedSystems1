import java.io.*;
import java.net.*;
import java.util.*;

public class multicast {
    public static void main(String[] args) throws IOException {

        System.out.println("Servidor multicast");
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        MulticastSocket socket = new MulticastSocket(4445);
// las direcciones multicast rango: 224.0.0.1 a 239.255.255.255
        socket.joinGroup(InetAddress.getByName("localhost"));

        socket.receive(packet);
        String recibido = new String(packet.getData());

        System.out.println("llego paquete: " + recibido);

        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);


        socket.close();
    }
}