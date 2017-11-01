package DS;

import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.net.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {


    private static final AtomicInteger Unico = new AtomicInteger(0);

    public static synchronized AtomicInteger getUnico() {
        return Unico;
    }

    private int ListenPort;
    private JSONObject DistritosActivos = new JSONObject();
    private JSONObject ClientesActivos = new JSONObject();



    public JSONObject getDistritosActivos() {
        return DistritosActivos;
    }

    public void DistritosActivos(JSONObject distritosActivos) {
        DistritosActivos = distritosActivos;
    }

    public int getListenPort() {
        return ListenPort;
    }

    public void setListenPort(int listenPort) {
        ListenPort = listenPort;
    }

    private static Server servidor= null;

    private Server(){}
    private synchronized static void createInstance(){
        if (servidor == null){
            servidor = new Server();
        }
    }
    public static Server getInstance(){
        if(servidor == null){
            createInstance();
        }
        return servidor;

    }

    public List<Cliente> getClienteConectados() {
        return ClienteConectados;
    }

    public void setClienteConectados(List<Cliente> clienteConectados) {
        ClienteConectados = clienteConectados;
    }

    List<Cliente> ClienteConectados = new ArrayList<Cliente>();

    List<Titan> lista_titanes_capturados = Collections.synchronizedList(new ArrayList<Titan>());
    List<Titan> lista_titanes_asesinados = Collections.synchronizedList(new ArrayList<Titan>());


    public List<Titan> getLista_titanes_capturados() {
        return lista_titanes_capturados;
    }

    public void setLista_titanes_capturados(List<Titan> lista_titanes_capturados) {
        this.lista_titanes_capturados = lista_titanes_capturados;
    }

    public List<Titan> getLista_titanes_asesinados() {
        return lista_titanes_asesinados;
    }

    public void setLista_titanes_asesinados(List<Titan> lista_titanes_asesinados) {
        this.lista_titanes_asesinados = lista_titanes_asesinados;
    }


    //----------------//
    //Insertar customs//
    //----------------//



    public static void main(String[] args){
        Console console = System.console();
        Server servidor = Server.getInstance();
        servidor.setListenPort(Integer.parseInt(
                console.readLine("[Servidor Central] Ingresar Puerto de Trabajo Servidor Central \n")
                )
            );

        for (String cmd = console.readLine("[Servidor Central] Ingrese accion a realizar\n[Servidor Central] 1.- Agregar Distrito\n[Servidor Central] 2.- Para Correr Server\n[Servidor Central] x.- Para Salir\n");
             !cmd.equals("x");
             cmd = console.readLine("****************************************************\n[Servidor Central] Ingrese accion a realizar\n[Servidor Central] 1.- Agregar Distrito\n[Servidor Central] 2.- Para Correr Server\n[Servidor Central] x.- Para Salir\n")) {
            if (cmd.equals("1")) {
                String DistritName = console.readLine("[Servidor Central] Nombre Distrito: ");
                String MultiCastIP = console.readLine("[Servidor Central] IP Multicast: ");
                int MultiCastPort = Integer.parseInt(console.readLine("[Servidor Central] Puerto Multicast: "));
                String IPPeticiones = console.readLine("[Servidor Central] IP Peticiones: ");
                int port = Integer.parseInt(console.readLine("[Servidor Central] Puerto Peticiones :"));

                JSONObject ActiveD = servidor.getDistritosActivos();
                JSONArray list = new JSONArray();
                list.add(MultiCastIP);
                list.add(MultiCastPort);
                list.add(IPPeticiones);
                list.add(port);
                ActiveD.put(DistritName,list);
                servidor.DistritosActivos(ActiveD);

            }
            else if (cmd.equals("2")){
                try{
                    DatagramSocket listenSocket = new DatagramSocket(servidor.getListenPort());
                    while (true) {
                        System.out.println("[Servidor Central] En espera de peticiones...");

                        byte[] receiveData = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
                        try {
                            listenSocket.receive(receivePacket);
                        } catch (IOException e) {
                            System.err.println("Error: no se pudo recibir el packete UDP");
                            System.exit(-1);
                        }
                        //Socket cs = listenSocket.accept();
                        //System.out.println("packet:"+ new String(receivePacket.getData()).trim());
                        System.out.println("Nueva conexion entrante: "+ receivePacket.getAddress());
                        System.out.println("Puerto: " + receivePacket.getPort() );

                        //Inicia thread para manejar la respuesta al cliente

                        Thread t = new Thread(new CONNECTION(receivePacket,servidor));
                        t.start();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private static JSONObject initJSONDistrit(String name,String ipmulti,String ippeti,int portmulti,int portpeti){
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        list.add(ipmulti);
        list.add(portmulti);
        list.add(ippeti);
        list.add(portpeti);
        obj.put(name,list);
        return obj;
    }
}
