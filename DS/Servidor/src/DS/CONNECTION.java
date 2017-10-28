package DS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;

/**
 * Created by breathtaKing on 07-06-2016.
 */
public class CONNECTION implements Runnable {
    private Socket cs;
    private DataInputStream is = null;
    private DataOutputStream os = null;

    public JSONObject getDA() {
        return DA;
    }

    public void setDA(JSONObject DA) {
        this.DA = DA;
    }

    private JSONObject DA = new JSONObject();

    public CONNECTION(Socket socket,JSONObject distritos){
        this.cs = socket;
        this.DA = distritos;
    }

    @Override
    public void run(){
        try{
            is = new DataInputStream(cs.getInputStream());
            os = new DataOutputStream(cs.getOutputStream());
            try{
                String comando = is.readUTF();
                Console console = System.console();
                for(String cmd =console.readLine("[Servidor Central]Dar autorizacion a "+cs.getInetAddress() +" por Distrito "+comando+"\n1.-SI\n2.-NO\nx.- Salir\n"); !cmd.equals("x");cmd=console.readLine("**************************************************************\n[Servidor Central]Dar autorizacion a/"+cs.getInetAddress() +" por Distrito "+comando+"\n1.-SI\n2.-NO\nx.- Salir\n")){
                    if(cmd.equals("1")){
                        JSONObject mensaje = getDA();
                        JSONArray conectiondata = (JSONArray) mensaje.get(comando);
                        JSONObject respuesta = new JSONObject();
                        respuesta.put("response","aceptado");
                        respuesta.put("datos",conectiondata);
                        os.writeUTF(respuesta.toJSONString());
                        //Enviar info

                    }
                    else if(cmd.equals(("2"))){
                        os.writeUTF("rechazado");
                    }
                    else{
                        System.out.println("[Servidor Central]Comando invalido");
                    }

                }
                System.out.println("[Servidor Central] En espera de peticiones...");
                os.close();
                is.close();
                this.cs.close();
                //System.out.println("Aqui en el servidor se ejecuta el comando : "+comando);
                //processMsg(comando);
            }catch (Exception e) {
                System.out.println(e);
                assert (false);
            }
        }catch (IOException e){
            System.out.println("cacaa aqui");
            System.out.println(e);
        }
        //String readString = is.readUTF();


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
