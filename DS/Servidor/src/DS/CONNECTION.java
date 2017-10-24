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
        if(obj.get("comando").equals("PUT")){
            //PUT
            String name = obj.get("filename").toString();
            this.Put(name);
        }
        else if (obj.get("comando").equals("GET")){
            //GET
            String name = obj.get("filename").toString();
            this.Get(name);
        }
        else if (obj.get("comando").equals("DELETE")){
            //DELETE
            String name = obj.get("filename").toString();
            this.Delete(name);
        }
        else if (obj.get("comando").equals("LIST")){
            //LIST
            this.List();
        }
        else{
            System.out.println("Comando no reconocido, prueba nuevamente.");
            try{cs.close();}
            catch (IOException e){System.out.println("Error al forar desconexion.");}
        }
    }

    public void sendFile(String fileName) {
        try {
            //handle file read
            File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) myFile.length()];

            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //bis.read(mybytearray, 0, mybytearray.length);

            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);

            //handle file send over socket
            OutputStream os = cs.getOutputStream();

            //Sending file name and file size to the server
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(myFile.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();
            System.out.println("File "+fileName+" sent to client.");
        } catch (Exception e) {
            System.err.println("File does not exist!");
        }
    }
    public void receiveFile() {
        try {
            int bytesRead;

            DataInputStream clientData = new DataInputStream(cs.getInputStream());

            String fileName = clientData.readUTF();
            OutputStream output = new FileOutputStream(("received_from_client_" + fileName));
            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }

            output.close();
            clientData.close();

            System.out.println("File "+fileName+" received from client.");
        } catch (IOException ex) {
            System.err.println("Client error. Connection closed.");
        }
    }
    public void Get(String filename) throws CommandUnavailableException{
        System.out.println("Hola soy get");

    }
    public void Put(String filename) throws CommandUnavailableException{
        System.out.println("Hola soy put");
    }
    public void Delete(String filename) throws CommandUnavailableException{
        System.out.println("Hola soy delete");
    }
    public void List() throws CommandUnavailableException{
        System.out.println("Hola soy list");
    }
    public void Sincro() throws Exception {
        System.out.println("Hola soy sincro");
    }
}
