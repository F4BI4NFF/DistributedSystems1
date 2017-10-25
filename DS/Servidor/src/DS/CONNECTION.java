package DS;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;

/**
 * Created by breathtaKing on 07-06-2016.
 */
public class CONNECTION implements Runnable {
    private Socket cs;
    private DataInputStream is = null;
    private DataOutputStream os = null;

    public CONNECTION(Socket socket){
        this.cs = socket;
    }

    @Override
    public void run(){
        try {
            try{
                is = new DataInputStream(cs.getInputStream());
                os = new DataOutputStream(cs.getOutputStream());
                try{
                    String comando = is.readUTF();
                    System.out.println("Aqui en el servidor se ejecuta el comando :"+comando);
                    processMsg(comando);
                }catch (CommandUnavailableException e) {
                    System.out.println(e);
                    assert (false);
                }

            }catch (IOException e){
                System.out.println("cacaa aqui");
                System.out.println(e);
            }
            String readString = is.readUTF();
        } catch (IOException e) {
            System.out.println("Cliente desconectado...");
        }


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
        else{
            System.out.println("Comando no reconocido, prueba nuevamente.");
            try{cs.close();}
            catch (IOException e){System.out.println("Error al formar desconexion.");}
        }
    }


    public void ListarTitanes() throws Exception{
        os.writeUTF("Hola soy Listar");
        System.out.println("Hola soy Listar");

    }
    public void ListarCapturados() throws Exception{
        System.out.println("Hola soy L Capturados");
    }
    public void ListarAsesinados() throws Exception{
        System.out.println("Hola soy L Asesiandos");
    }
    public void Capturar(int id) throws Exception{
        System.out.println("Hola soy Capturar"+ id);
    }
    public void Asesinar(int id) throws Exception {
        System.out.println("Hola soy Asesinar"+ id);
    }
    public void ChangeDistrit(String name) throws  Exception{
        System.out.println("Hola soy cambio de distrito a "+name);
    }
}
