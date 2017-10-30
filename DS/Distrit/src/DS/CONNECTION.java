package DS;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.Socket;

/**
 * Created by breathtaKing on 07-06-2016.
 */
public class CONNECTION implements Runnable {
    private DatagramSocket cs;
    private MulticastSocket multisocket;
    private Distrit distrito;
    private byte[] Data;
    private DatagramPacket Packet;
    //private DataInputStream is = null;
    //private DataOutputStream os = null;

    public CONNECTION(DatagramSocket socket, MulticastSocket msocket, Distrit distrito, byte[] data, DatagramPacket packet){
        this.cs = socket;
        this.multisocket = msocket;
        this.distrito = distrito;
        this.Data = data;
        this.Packet = packet;
    }

    @Override
    public void run(){
        //is = new DataInputStream(cs.getInputStream());
        //os = new DataOutputStream(cs.getOutputStream());
        Console console = System.console();
        try{
            //String comando = is.readUTF();
            String comando = new String(Packet.getData());
            for (String cmd = console.readLine("[Distrito"+distrito.getDistritName()+"] Ingrese accion a realizar\n[Distrito"+distrito.getDistritName()+"] 1.- Publicar Titan\n[Distrito"+distrito.getDistritName()+"] x.- Para Salir\n");
                 !cmd.equals("x");
                 cmd = console.readLine("********************************\n[Distrito"+distrito.getDistritName()+"] Ingrese accion a realizar\n[Distrito"+distrito.getDistritName()+"] 1.- Publicar Titan\n[Distrito"+distrito.getDistritName()+"] x.- Para Salir\n")
                    ){
                if (cmd.equals("1")){
                    //Se agrega un Titan
                    String NombreTitan = console.readLine("[Distrito"+distrito.getDistritName()+"]Introducir nombre \n>");
                    String TipoTitan = console.readLine("[Distrito"+distrito.getDistritName()+"]Introducir tipo :\n 1.- Normal\n 2.- Excentrico\n 3.- Cambiante\n > ");
                    Titan titan = new Titan(NombreTitan,Integer.parseInt(TipoTitan),distrito.getID());
                    System.out.println("[Distrito"+distrito.getDistritName()+"] Se ha publicado el titán"+ NombreTitan );
                    System.out.println("*****************");
                    System.out.println("ID: "+titan.getID_titan());
                    System.out.println("Nombre: "+titan.getNombre_titan());
                    System.out.println("Tipo: " + titan.getTipo_titan());
                    System.out.println("*****************");
                }
                else{
                    System.out.println("[Distrito\"+distrito.getDistritName()+\"] Comando invalido");
                }
            }
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
        obj.put("name",name);
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
        else if (obj.get("comando").equals("2")){
            //Cambiar distrito
            String name = obj.get("distrit").toString();
            try{
                this.ChangeDistrit(name);
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
        else {
            System.out.println("Comando no reconocido, prueba nuevamente.");
        }
    }


    public void ListarTitanes() throws Exception{
        //os.writeUTF("Titanes en "+distrito.getDistritName()+":\n" + distrito.getLista_titanes());
        System.out.println("Hola soy Listar");
        System.out.println(distrito.getLista_titanes());

    }
    public void ListarCapturados() throws Exception{
        //os.writeUTF("Hola soy Listar Capturados");
        System.out.println("Hola soy L Capturados");
    }
    public void ListarAsesinados() throws Exception{
        //os.writeUTF("Hola soy Listar Asesinados");
        System.out.println("Hola soy L Asesinados");
    }
    public void Capturar(int id) throws Exception{
        //os.writeUTF("Hola soy Capturar");
        System.out.println("Hola soy Capturar"+ id);
    }
    public void Asesinar(int id) throws Exception {
        //os.writeUTF("Hola soy Asesinar");
        System.out.println("Hola soy Asesinar"+ id);
    }
    public void ChangeDistrit(String name) throws  Exception{
        //os.writeUTF("Hola soy cambio de distrito a "+name);
        System.out.println("Hola soy cambio de distrito a "+name);
    }

}
