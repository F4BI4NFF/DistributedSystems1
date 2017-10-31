package DS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by breathtaKing on 07-06-2016.
 */
public class CONNECTION implements Runnable {
    private MulticastSocket multisocket;
    private Distrit distrito;

    private DatagramSocket ServerSocket;
    private DatagramPacket sendPacket;
    private DatagramPacket receivePacket;
    private byte[] sendData;
    private byte[] receiveData;

    private InetAddress clienthost;
    private int clientport;

    public String getMessage()
    {
        receiveData = new byte[1024];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try
        {
            this.ServerSocket.receive(this.receivePacket);
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
            System.err.println("No llego el datagrama");
            return "";
        }
        return new String(this.receivePacket.getData()).trim();
    }
    public void sendMessage(String line, InetAddress ServerIP, int port) {
        System.out.println(line);
        sendData = line.getBytes();
        sendPacket = new DatagramPacket(sendData, sendData.length, ServerIP, port);
        try {
            ServerSocket.send(sendPacket);
        } catch (IOException e) {
            System.err.println("No se envio el datagrama");
        }
    }
    public void sendMultiCast(String line, InetAddress ServerIP, int port) {
        System.out.println(line);
        sendData = line.getBytes();
        sendPacket = new DatagramPacket(sendData, sendData.length, ServerIP, port);
        try {
            multisocket.send(sendPacket);
        } catch (IOException e) {
            System.err.println("No se envio el datagrama");
        }
    }

    public CONNECTION(DatagramSocket socket, MulticastSocket msocket, Distrit distrito, DatagramPacket packet){
        this.ServerSocket = socket;
        this.multisocket = msocket;
        this.distrito = distrito;
        this.receivePacket = packet;
        this.clienthost = packet.getAddress();
        this.clientport = packet.getPort();
    }

    @Override
    public void run(){
        //is = new DataInputStream(cs.getInputStream());
        //os = new DataOutputStream(cs.getOutputStream());
        //Console console = System.console();
        try{
            //String comando = is.readUTF();
            String comando = new String(this.receivePacket.getData()).trim();
            System.out.println(comando);
            System.out.println("Aqui en el servidor se ejecuta el comando : "+comando);
            processMsg(comando);
        }catch (CommandUnavailableException e) {
            System.out.println(e);
            assert (false);
        }
    }
    private static String initJSONfirst(String mode){
        JSONObject obj = new JSONObject();
        obj.put("nombre",mode);
        obj.put("tipo","Distrito");
        return obj.toJSONString();
    }
    private static String initJSON(String mode,String name){
        //Para codificar el comando que se envia al servidor
        JSONObject obj = new JSONObject();
        obj.put("comando",mode);
        // name comodin por si se necesita modificar a gusto
        obj.put("tipo",name);
        return obj.toJSONString();
    }
    private static String unitJSON(String comando,String nombre,int tipo,int id){
        //Para codificar el comando que se envia al servidor
        JSONObject obj = new JSONObject();
        obj.put("comando",comando);
        // name comodin por si se necesita modificar a gusto
        obj.put("tipo","Distrito");
        JSONArray ls = new JSONArray();
        ls.add(nombre);
        ls.add(tipo);
        ls.add(id);
        obj.put("datos",ls);
        return obj.toJSONString();
    }
    public void processMsg(String jsonString) throws CommandUnavailableException{
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        try {
            obj = (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        if(obj.get("comando").equals("1")){
            //Listar titanes
            try{
                this.ListarTitanes();

            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else if (obj.get("comando").equals("3")){
            //Capturar titan
            Integer id = Integer.parseInt(obj.get("id").toString());
            try{
                this.Capturar(id);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else if (obj.get("comando").equals("4")){
            //Asesinar titan
            Integer id = Integer.parseInt(obj.get("id").toString());
            try{
                this.Asesinar(id);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else if (obj.get("comando").equals("5")){
            //Listar titanes capturados
            try{
                this.ListarCapturados();
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else if (obj.get("comando").equals("6")){
            //Listar titanes asesinados
            try{
                this.ListarAsesinados();
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else if (obj.get("comando").equals("7")){
            //Redirigir
            //Recibir del server central
            JSONParser parser1 = new JSONParser();
            try {
                obj = (JSONObject) parser1.parse((String)obj.get("mensaje"));
            } catch (ParseException e) {
                e.printStackTrace();
                System.exit(-1);
            }
            JSONObject fromserver = obj;
            //Enviar al cliente
            sendMessage(fromserver.toJSONString(),clienthost,clientport);
        }
        else {
            System.out.println("Comando invalido ha llegado al distrito");
        }
    }


    public void ListarTitanes() throws Exception{
        //os.writeUTF("Titanes en "+distrito.getDistritName()+":\n" + distrito.getLista_titanes());
        System.out.println("Hola soy Listar");
        JSONObject titanes = new JSONObject();
        //List<Titan> list = new ArrayList<>();
        for (Titan t: distrito.getLista_titanes()) {
            JSONArray list = new JSONArray();
            list.add(t.getNombre_titan());
            list.add(t.getTipo_titan());
            titanes.put(t.getID_titan(),list);
        }
        String response = titanes.toJSONString();
        System.out.println("respuesta de titanes : "+response);
        System.out.println(response);
        sendMessage(response,clienthost,clientport);

    }
    public void ListarCapturados() throws Exception{
        //os.writeUTF("Hola soy Listar Capturados");
        System.out.println("Hola soy L Capturados");
        String mensaje = initJSON("1","Distrito");
        sendMessage(mensaje,InetAddress.getByName(distrito.getCentralIP()),distrito.getCentralPort());


    }
    public void ListarAsesinados() throws Exception{
        //os.writeUTF("Hola soy Listar Asesinados");
        System.out.println("Hola soy L Asesinados");
        String mensaje = initJSON("2","Distrito");
        sendMessage(mensaje,InetAddress.getByName(distrito.getCentralIP()),distrito.getCentralPort());
    }
    public void Capturar(int id) throws Exception{
        //os.writeUTF("Hola soy Capturar");
        System.out.println("Hola soy Capturar"+ id);
        //List<Titan> list = new ArrayList<>();
        String tipo = "";
        for (Titan t : distrito.getLista_titanes()){
            if (t.getID_titan() == id && (t.getTipo_titan() == 1 || t.getTipo_titan() == 3)) {
                //Sacar y Capturar + multicast
                distrito.getLista_titanes().remove(t);
                distrito.getLista_titanes_capturados().add(t);
                tipo = t.getNombreTip(t.getTipo_titan());
                String mensaje = "[Cliente] Se ha capturado un titán! " + t.getNombre_titan() + ", tipo " + tipo + ", id " + t.getID_titan();
                // Multicast y Server
                sendMessage(unitJSON("4",t.getNombre_titan(),t.getTipo_titan(),t.getID_titan()),
                        InetAddress.getByName(distrito.getCentralIP()),distrito.getCentralPort());
                sendMultiCast(mensaje, InetAddress.getByName(distrito.getMultiCastIp()), distrito.getMultiCastPort());
            }
        }

    }
    public void Asesinar(int id) throws Exception {
        //os.writeUTF("Hola soy Asesinar");
        System.out.println("Hola soy Asesinar"+ id);
        String tipo = "";
        for (Titan t : distrito.getLista_titanes()){
            if (t.getID_titan() == id && (t.getTipo_titan() == 1 || t.getTipo_titan() == 2)){
                distrito.getLista_titanes().remove(t);
                distrito.getLista_titanes_asesinados().add(t);
                tipo = t.getNombreTip(t.getTipo_titan());
                String mensaje = "[Cliente] Se ha asesinado un titán! "+t.getNombre_titan()+", tipo "+tipo+", id "+t.getID_titan();
                // Multicast
                sendMessage(unitJSON("5",t.getNombre_titan(),t.getTipo_titan(),t.getID_titan()),
                        InetAddress.getByName(distrito.getCentralIP()),distrito.getCentralPort());

                sendMultiCast(mensaje,InetAddress.getByName(distrito.getMultiCastIp()),distrito.getMultiCastPort());
            }
        }
    }

}
