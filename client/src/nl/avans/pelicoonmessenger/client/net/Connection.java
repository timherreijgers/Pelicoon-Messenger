package nl.avans.pelicoonmessenger.client.net;

import nl.avans.pelicoonmessenger.base.models.Message;
import nl.avans.pelicoonmessenger.base.models.Session;
import nl.avans.pelicoonmessenger.base.models.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection extends Thread {

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private MessageReceivedListener listener;

    public Connection(String ip, User user) {
        try {
            socket = new Socket(ip, 1337);

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            outputStream.writeObject(user);

            Session session = (Session) inputStream.readObject();
            System.out.println("Session: " + session.toString());

            start();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message){
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMessageReceivedListener(MessageReceivedListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        while(true){
            try {
                Object object = inputStream.readObject();
                System.out.println(object);
                if(object instanceof Message){
                    if(listener != null)
                        listener.onMessageReceived((Message) object);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public interface MessageReceivedListener{
        void onMessageReceived(Message message);
    }
}
