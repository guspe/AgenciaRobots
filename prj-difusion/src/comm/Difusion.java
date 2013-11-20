package comm;

import java.io.*;
import java.net.*;
import corba.camara.IPYPortD;

public class Difusion{

  MulticastSocket socket;
  corba.camara.IPYPortD ipyport;
  public InetAddress group;

//------------------------------------------------------------------------------
  public  Difusion(IPYPortD ipyport){
    this.ipyport = ipyport;
    
    try
    {
	    socket = new MulticastSocket(ipyport.port);
	    group = InetAddress.getByName(ipyport.ip);
	    socket.joinGroup(group);
    }
    catch(UnknownHostException e)
    {
    	e.printStackTrace();
    }
    catch(IOException e)
    {
    	e.printStackTrace();
    }
  //EJERCICIO: 
  //Crear el socket multicast 
  //EJERCICIO: 
  //Obtener la direccion del grupo 
  //EJERCICIO: 
  //Unirse al grupo 

  }

//------------------------------------------------------------------------------
  public Object receiveObject(){

    Object object = null;
    ObjectInputStream ois = null;
    byte[] buffer;
    DatagramPacket packet;
    ByteArrayInputStream bis;

    buffer = new byte[4096];
    packet = new DatagramPacket(buffer, buffer.length,group,ipyport.port);
    try
    {
	    socket.receive(packet);
	    buffer = packet.getData();
	  //EJERCICIO: recibir el paquete y deserializarlo 
	    bis = new ByteArrayInputStream(buffer);
	    ois = new ObjectInputStream(bis);
	    object = ois.readObject();
    }
    catch(ClassNotFoundException e)
    {
    	e.printStackTrace();
    }
    catch ( IOException e) {
		e.printStackTrace();
	}
    return object;
  }

//------------------------------------------------------------------------------
  public void sendObject(Object object){

    ByteArrayOutputStream bos;
    ObjectOutputStream oos = null;
    byte[] buffer;
    DatagramPacket packet;

    try
    {
	    buffer = new byte[4096];
	    bos = new ByteArrayOutputStream();
	    oos = new ObjectOutputStream(bos);
	    oos.writeObject(object);
	    buffer = bos.toByteArray();
	    packet = new DatagramPacket(buffer, buffer.length,group,ipyport.port);
	    socket.send(packet);
    }
    catch(IOException e)
    {
    	e.printStackTrace();
    }
  //EJERCICIO: serializar el paquete y difundirlo 
  }
}