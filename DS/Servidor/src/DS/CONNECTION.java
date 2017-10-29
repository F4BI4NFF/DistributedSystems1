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
                JSONObject fromclient = processJSON(comando);
                Console console = System.console();
                if (fromclient.get("tipo").equals("Cliente")){
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
                            System.out.println("[Servidor Central]Comando invalido\n");
                        }
                        comando = is.readUTF();

                    }
                    System.out.println("[Servidor Central] En espera de peticiones...");
                    os.close();
                    is.close();
                    this.cs.close();
                }else if (fromclient.get("tipo").equals("Distrito")){
                    if (fromclient.get("comando").equals("1")){
                        // Logica recepcion de lista (Capturados/Asesinados Globales)
                        this.ListarCapturados();
                    }else if (fromclient.get("comando").equals("2")){
                        // Logica envio de peticion de lista (Capturados/Asesinados Globales)
                        this.ListarAsesinados();
                    }else {
                        System.out.println("Comando invalido recivido de Distrito");
                    }



                    // OJO que tambien sirve el paso de comandos por json

                }

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
    public static JSONObject processJSON(String mensaje){
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        try {
            obj = (JSONObject) parser.parse(mensaje);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return obj;
    }

    public void ListarCapturados() throws Exception{
        os.writeUTF("Hola soy Listar Capturados");
        System.out.println("Hola soy L Capturados");
    }
    public void ListarAsesinados() throws Exception{
        os.writeUTF("Hola soy Listar Asesinados");
        System.out.println("Hola soy L Asesinados");
    }
}
