package DS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Console;
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

    public JSONArray getDistritosActivos() {
        return DistritosActivos;
    }

    public void DistritosActivos(JSONArray distritosActivos) {
        DistritosActivos = distritosActivos;
    }

    private JSONArray DistritosActivos = new JSONArray();
    private JSONArray ClientesActivos = new JSONArray();

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
        Console console = System.console();
        Server servidor = new Server();
        servidor.setListenPort(Integer.parseInt(
                console.readLine("[Servidor Central] Ingresar Puerto de Trabajo Servidor Central \n")
                )
            );

        for (String cmd = console.readLine("[Servidor Central] Ingrese accion a realizar\n[Servidor Central] 1.- Agregar Distrito\n[Servidor Central] 2.- Para Correr Server\n[Servidor Central] x.- Para Salir\n");
             !cmd.equals("x");
             cmd = console.readLine("****************************************************\n[Servidor Central] Ingrese accion a realizar\n[Servidor Central] 1.- Agregar Distrito\n[Servidor Central] 2.- Para Correr Server\n[Servidor Central] x.- Para Salir\n")) {
            if (cmd.equals("1")) {
                String DistritName = console.readLine("[Servidor Central] Nombre Distrito: ");
                servidor.setMultiCastIP(console.readLine("[Servidor Central] IP Multicast: "));
                servidor.setMultiCastPort(Integer.parseInt(console.readLine("[Servidor Central] Puerto Multicast: ")));
                String IPPeticiones = console.readLine("[Servidor Central] IP Peticiones: ");
                int port = Integer.parseInt(console.readLine("[Servidor Central] Puerto Peticiones :"));

                JSONArray ActiveD = servidor.getDistritosActivos();
                JSONObject objD = initJSONDistrit(DistritName,servidor.getMultiCastIP(),IPPeticiones,servidor.getMultiCastPort(),port);
                ActiveD.add(objD);
                servidor.DistritosActivos(ActiveD);

            }
            else if (cmd.equals("2")){
                try{
                    ServerSocket listenSocket = new ServerSocket(servidor.getListenPort());
                    System.out.println("Server Central en espera...");
                    while (true) {
                        Socket cs = listenSocket.accept();
                        console.readLine("Dar autorizacion a/"+listenSocket.getInetAddress() +" por Distrito"+)
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
    }

    private static JSONObject initJSONDistrit(String name,String ipmulti,String ippeti,int portmulti,int portpeti){
        JSONObject obj = new JSONObject();
        obj.put("Nombre",name);
        obj.put("IPMulticast",ipmulti);
        obj.put("PuertoMulticast",portmulti);
        obj.put("IPPeticiones",ippeti);
        obj.put("PuertoPeticiones",portpeti);
        return obj;
    }
}
