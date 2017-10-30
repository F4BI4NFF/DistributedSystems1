package DS;

import sun.rmi.server.InactiveGroupException;

import javax.xml.crypto.Data;
import java.io.Console;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Distrit {
    //**********Server Information***********//
    private String DistritName;
    private String MultiCastIp;
    private int MultiCastPort;
    private String RequestIP;
    private int RequestPort;

    private DatagramSocket serverSocket;
    private DatagramPacket Packet;
    private byte[] Data;

    private int packetSize = 1024;

    public int getPacketSize() {
        return packetSize;
    }

    //***********Informacion relevante**************//
    List<String> lista_titanes = Collections.synchronizedList(new ArrayList<String>());
    List<String> lista_titanes_capturados = Collections.synchronizedList(new ArrayList<String>());
    List<String> lista_titanes_asesinados = Collections.synchronizedList(new ArrayList<String>());
    //*************************//

    public List<String> getLista_titanes() {
        return lista_titanes;
    }

    public void setLista_titanes(List<String> lista_titanes) {
        this.lista_titanes = lista_titanes;
    }

    public int getRequestPort() {
        return RequestPort;
    }

    public void setRequestPort(int requestPort) {
        RequestPort = requestPort;
    }

    public String getDistritName() {
        return DistritName;
    }

    public void setDistritName(String distritName) {
        DistritName = distritName;
    }

    public String getMultiCastIp() {
        return MultiCastIp;
    }

    public void setMultiCastIp(String multiCastIp) {
        MultiCastIp = multiCastIp;
    }

    public int getMultiCastPort() {
        return MultiCastPort;
    }

    public void setMultiCastPort(int multiCastPort) {
        MultiCastPort = multiCastPort;
    }

    public String getRequestIP() {
        return RequestIP;
    }

    public void setRequestIP(String requestIP) {
        RequestIP = requestIP;
    }

    //********************************************//

    public List<String> getLista_titanes_capturados() {
        return lista_titanes_capturados;
    }

    public void setLista_titanes_capturados(List<String> lista_titanes_capturados) {
        this.lista_titanes_capturados = lista_titanes_capturados;
    }

    public List<String> getLista_titanes_asesinados() {
        return lista_titanes_asesinados;
    }

    public void setLista_titanes_asesinados(List<String> lista_titanes_asesinados) {
        this.lista_titanes_asesinados = lista_titanes_asesinados;
    }

    //********************************************//

    public static void main(String[] args) {
        Console console = System.console();
        Distrit distrito = new Distrit();
        distrito.setDistritName(console.readLine("[Distrito] Nombre Servidor : "));
        distrito.setMultiCastIp(console.readLine("[Distrito " + distrito.getDistritName() + "] IP Multicast : "));
        distrito.setMultiCastPort(Integer.parseInt(console.readLine("[Distrito " + distrito.getDistritName() + " Puerto Multicast : ")));
        distrito.setRequestIP(console.readLine("[Distrito " + distrito.getDistritName() + "] IP Peticiones : "));
        distrito.setRequestPort(Integer.parseInt(console.readLine("[Distrito " + distrito.getDistritName() + "] Puerto Peticiones : ")));

        try {
            DatagramSocket serverSocket = new DatagramSocket(distrito.getRequestPort());
            MulticastSocket castSocket = new MulticastSocket(distrito.getMultiCastPort());
            castSocket.joinGroup(InetAddress.getByName(distrito.getMultiCastIp()));

            System.out.println("[Distrito " + distrito.getDistritName() + "] En espera de peticiones...");
            byte[] Data = new byte[distrito.getPacketSize()];
            DatagramPacket Packet = new DatagramPacket(Data, Data.length);

            while (true) {
                try {
                    serverSocket.receive(Packet);
                } catch (IOException e) {
                    System.err.println("Error: no se pudo recibir el packete UDP");
                    System.exit(-1);
                }
                System.out.println("Nueva conexion entrante: ");
                System.out.println("Host: " + serverSocket.getInetAddress() + " , Port: "+ serverSocket.getPort());
                Thread t = new Thread(new CONNECTION(serverSocket, castSocket, distrito, Data, Packet));
                //System.out.println("Opciones... \n Publicar un titan:");
                t.start();
            }
        }catch (IOException e) {
            //no se pudo abrir socket
            System.out.println(e.getMessage());
        }
    }

    private static Titan publicar_titan(String nombre_distrito) throws IOException {
        System.out.println("Publicar Titán ");
        Titan titan = new Titan();
        Scanner scanner = new Scanner(System.in);
        String consola_distrito = "[Distrito " + nombre_distrito + "]";
        System.out.println(consola_distrito + " Introducir nombre:");
        String nombre = scanner.nextLine();
        titan.setNombre_titan(nombre);
        System.out.println(consola_distrito + " Introducir tipo");
        System.out.println("1.- Normal");
        System.out.println("2.- Excentrico");
        System.out.println("3.- Cambiante");
        Integer tipo = scanner.nextInt();
        titan.setTipo_titan(tipo);
        System.out.println(consola_distrito + " Se ha publicado el titan:" + titan.getNombre_titan());
        System.out.println("***************");
        System.out.println("ID: " + titan.getID_titan());
        System.out.println("Nombre: " + titan.getNombre_titan());
        System.out.println("Tipo: " + titan.getTipo_titan());
        System.out.println("***************");
        System.out.println("Nuevo titan publicado!");
        //Falta alertar el cliente! con Multicast
        return titan;

    }

    protected static int getID() {
        int ID = -1;


        return ID;
    }

    public DatagramPacket getPacket() {
        Data = new byte[1024];
        Packet = new DatagramPacket(Data, Data.length);
        try {
            serverSocket.receive(Packet);
        } catch (IOException e) {
            System.err.println("Error: error while recieving packet");
            return null;
        }
        return Packet;
    }

    public void sendPacket(DatagramPacket packet) {
        try {
            serverSocket.send(packet);
        } catch (IOException e) {
            System.err.println("Error: error while sending packet");
        }
    }
}
