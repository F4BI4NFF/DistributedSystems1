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
    private Distrit distrito;
    private DataInputStream is = null;
    private DataOutputStream os = null;

    public CONNECTION(Socket socket, Distrit distrito){
        this.cs = socket;
        this.distrito = distrito;
    }

    @Override
    public void run(){
        try {
            try{
                is = new DataInputStream(cs.getInputStream());
                os = new DataOutputStream(cs.getOutputStream());
                try{
                    String comando = is.readUTF();
                    System.out.println("Aqui en el servidor se ejecuta el comando : "+comando);
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
        else{
            System.out.println("Comando no reconocido, prueba nuevamente.");
            try{cs.close();}
            catch (IOException e){System.out.println("Error al formar desconexion.");}
        }
    }


    public void ListarTitanes() throws Exception{
        os.writeUTF("Titanes en "+distrito.getDistritName()+":\n" + distrito.getLista_titanes());
        System.out.println("Hola soy Listar");
        System.out.println(distrito.getLista_titanes());

    }
    public void ListarCapturados() throws Exception{
        os.writeUTF("Hola soy Listar Capturados");
        System.out.println("Hola soy L Capturados");
    }
    public void ListarAsesinados() throws Exception{
        os.writeUTF("Hola soy Listar Asesinados");
        System.out.println("Hola soy L Asesinados");
    }
    public void Capturar(int id) throws Exception{
        os.writeUTF("Hola soy Capturar");
        System.out.println("Hola soy Capturar"+ id);
    }
    public void Asesinar(int id) throws Exception {
        os.writeUTF("Hola soy Asesinar");
        System.out.println("Hola soy Asesinar"+ id);
    }
    public void ChangeDistrit(String name) throws  Exception{
        os.writeUTF("Hola soy cambio de distrito a "+name);
        System.out.println("Hola soy cambio de distrito a "+name);
    }
}
