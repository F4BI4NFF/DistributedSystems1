package DS;

import java.awt.*;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Distrit {
    private String DistritName;
    private String MultiCastIp;
    private int MultiCastPort;
    private String RequestIP;
    private int ResquestPort;
    List<String> lista_titanes;
    private int ListenPort;

    public int getListenPort() {
        return ListenPort;
    }

    public void setListenPort(int listenPort) {
        ListenPort = listenPort;
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

    public int getResquestPort() {
        return ResquestPort;
    }

    public void setResquestPort(int resquestPort) {
        ResquestPort = resquestPort;
    }

    public static void main(String[] args)  {
        Distrit distrito = new Distrit();
        Scanner scanner = new Scanner(System.in);
        System.out.println("[Distrito] Ingresar Puerto de Trabajo Distrito");
        distrito.setListenPort(scanner.nextInt());
        scanner.nextLine();

        try{

            ServerSocket listenSocket = new ServerSocket(distrito.getListenPort());
            System.out.println("Distrito en espera...");
            while (true) {
                Socket cs = listenSocket.accept();
                System.out.println("Nueva conexion entrante: ");
                System.out.println("Puerto: " + distrito.getMultiCastPort());
                Titan nuevo_titan = publicar_titan("Trost");
                Thread t = new Thread(new CONNECTION(cs));
                t.start();

                //Publicar un titan --
            }
        } catch (IOException e){
            System.out.println(e.getMessage());

            //Logica servidor de distrito

        }
    }
    private static Titan publicar_titan(String nombre_distrito) throws IOException {
        System.out.println("Publicar Titán ");
        Titan titan = new Titan();
        Scanner scanner = new Scanner(System.in);
        String consola_distrito = "[Distrito "+ nombre_distrito +"]";
        System.out.println(consola_distrito+" Introducir nombre:");
        String nombre = scanner.nextLine();
        titan.setNombre_titan(nombre);
        System.out.println(consola_distrito+" Introducir tipo");
        System.out.println("1.- Normal");
        System.out.println("2.- Excentrico");
        System.out.println("3.- Cambiante");
        Integer tipo = scanner.nextInt();
        titan.setTipo_titan(tipo);
        System.out.println(consola_distrito+ " Se ha publicado el titan:"+ titan.getNombre_titan());
        System.out.println("***************");
        System.out.println("ID: "+titan.getID_titan());
        System.out.println("Nombre: "+titan.getNombre_titan());
        System.out.println("Tipo: " + titan.getTipo_titan());
        System.out.println("***************");
        System.out.println("Nuevo titan publicado!");
        //Falta alertar el cliente! con Multicast
        return titan;

    }
}
