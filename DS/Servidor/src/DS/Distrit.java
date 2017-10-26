package DS;

import java.awt.*;
import java.io.IOException;
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

    public static void main(String[] args){
        Server servidor = new Server();
        Scanner scanner = new Scanner(System.in);
        System.out.println("[Distrito] Ingresar Puerto de Trabajo Distrito");
        servidor.setListenPort(scanner.nextInt());
        scanner.close();

        try{

            ServerSocket listenSocket = new ServerSocket(servidor.getListenPort());
            System.out.println("Distrito en espera...");
            while (true) {
                Socket cs = listenSocket.accept();
                System.out.println("Nueva conexion entrante: ");
                System.out.println("Puerto: " + servidor.getMultiCastPort());
                Scanner scanner2 = new Scanner(System.in);
                System.out.println("aaaaaaaaaaaaaa");
                System.out.println(scanner2.nextLine());
                Titan prueba = publicar_titan("prueba");
                System.out.println("Nuevo titan publicado!");
                System.out.println(prueba.getNombre_titan());

                Thread t = new Thread(new CONNECTION(cs));
                t.start();

                //Publicar un titan --
            }
        } catch (IOException e){
            System.out.println(e.getMessage());

            //Logica servidor de distrito

        }
    }

    private static Titan publicar_titan(String nombre_distrito) {
        Titan titan = new Titan();
        String consola_distrito = "[Distrito "+ nombre_distrito +"]";
        System.out.println(consola_distrito+" Publicar Titán");
        System.out.println(consola_distrito+" Introducir nombre");
        Scanner scanner2 = new Scanner(System.in);
        String nombre_titan = scanner2.next();
        titan.setNombre_titan(nombre_titan);
        System.out.println(consola_distrito+" Introducir tipo");
        System.out.println("1.- Normal");
        System.out.println("2.- Excentrico");
        System.out.println("3.- Cambiante");
        titan.setTipo_titan(scanner2.nextInt());
        System.out.println(consola_distrito+ " Se ha publicado el titan:"+ titan.getNombre_titan());
        System.out.println("***************");
        System.out.println("ID: "+titan.getID_titan());
        System.out.println("Nombre: "+titan.getNombre_titan());
        System.out.println("Tipo: " + titan.getTipo_titan());
        System.out.print("***************");
        return titan;
        //Falta alertar el cliente!
    }
}
