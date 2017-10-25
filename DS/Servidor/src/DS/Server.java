package DS;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    private String MultiCastIP;
    private int MultiCastPort;

    private int ListenPort;


    public String getMultiCastIP() {
        return MultiCastIP;
    }

    public void setMultiCastIP(String multiCastIP) {
        MultiCastIP = multiCastIP;
    }

    public int getMultiCastPort() {
        return MultiCastPort;
    }

    public void setMultiCastPort(int multiCastPort) {
        MultiCastPort = multiCastPort;
    }

    public int getListenPort() {
        return ListenPort;
    }

    public void setListenPort(int listenPort) {
        ListenPort = listenPort;
    }

    //----------------//
    //Insertar customs//
    //----------------//



    public static void main(String[] args){
        Server servidor = new Server();
        Scanner scanner = new Scanner(System.in);
        System.out.println("[Servidor Central] Ingresar Puerto de Trabajo Servidor Central");
        servidor.setListenPort(scanner.nextInt());
        scanner.close();

        try{
            ServerSocket listenSocket = new ServerSocket(servidor.getListenPort());
            System.out.println("Server Central en espera...");
            while (true) {
                Socket cs = listenSocket.accept();
                System.out.println("Nueva conexion entrante: "+ listenSocket.getLocalSocketAddress());
                System.out.println("Puerto: " + listenSocket.getLocalPort() );

                Thread t = new Thread(new CONNECTION(cs));
                t.start();
            }
        } catch (IOException e) {
            // Por omisión de texto se detecta el modo distrito
            System.out.println(e.getMessage());
        }
    }
}
