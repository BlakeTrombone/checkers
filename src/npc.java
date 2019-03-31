import java.io.*;
import java.net.*;

public class npc extends Thread
{
    private DatagramSocket ds;
    private InetAddress ip;
    private boolean server;
    private String serverIP;
    private boolean handshook=false;

    public npc(boolean server) throws Exception//server startup
    {
        server=true;
        this.start();
    }

    public npc(String serveip) throws Exception//client startup
    {
        server=false;
        serverIP=serveip;
        this.start();
    }

    private piece[] flip(piece[] pieces)//flips reds and blacks (have to be constructed)q
    {
        piece[] output = new piece[16];
        for (int x = 0; x < 16; x++)
            output[x]=pieces[x+6].flip();
        return output;
    }

    private String deconstruct(piece[] pieces)//converts array of pieces to a string for network transmission
    {
        String output="";
        for (piece p : pieces)
            output+=p.toString()+ " ";
        return output;
    }

    private piece[] reconstruct(String s)//converts a string back into an array of pieces
    {
        String[] ps = s.split(" ");
        piece[] pieces = new piece[16];
        for (int i = 0; i < 16; i++)
        {
            pieces[i]= new piece (Integer.parseInt(ps[i].charAt(0)+""),
                    Integer.parseInt(ps[i].charAt(1)+""),
                    ((ps[i].charAt(2)=='r') ? "Red" : "Black"),
                    ps[i].charAt(3)=='t',
                    ps[i].charAt(4)=='t',
                    ps[i].charAt(5)=='t');
        }
        return pieces;
    }

    public static piece[] tturn (piece[] pieces)
    {
        return pieces;
    }
    public boolean isHandshook()
    {
        return handshook;
    }

    public piece[] firstRun()
    {
        try{
            byte[] recieveData = new byte[1024];
            ds = new DatagramSocket();
            InetAddress ip = InetAddress.getByName(serverIP);
            DatagramPacket dpr = new DatagramPacket(recieveData, recieveData.length);
            ds.receive(dpr);
            String recieveString = new String(recieveData);
            return reconstruct(recieveString);
        } catch (Exception e){return null;}
    }


    public piece[] turn(piece[] pieces)
    {
        try{
            ds = new DatagramSocket();
            InetAddress ip = InetAddress.getByName(serverIP);
            String sendData=deconstruct(flip(pieces));
            DatagramPacket dp = new DatagramPacket(sendData.getBytes(), sendData.getBytes().length,ip,7059);
            ds.send(dp);
            byte[] recieveData = new byte[1024];
            DatagramPacket dpr = new DatagramPacket(recieveData, recieveData.length);
            ds.receive(dpr);
            String recieveString = new String(recieveData);
            return reconstruct(recieveString);
        } catch (Exception e){return null;}
    }


    public void run()
    {

        if(server)
        {
            try{
                ds = new DatagramSocket(7059);
                String ready="ready";
                byte[] test = new byte[1024];
                DatagramPacket dp = new DatagramPacket(test,test.length);
                ds.receive(dp);
                String info = new String(dp.getData());
                ip=dp.getAddress();
                String responce="ydaer";
                DatagramPacket dps = new DatagramPacket(responce.getBytes(), responce.getBytes().length, ip, dp.getPort());
                ds.send(dps);
                handshook=true;
            } catch (Exception e){}
        }

        if (!server)
        {
            try{
                ds = new DatagramSocket();
                ip = InetAddress.getByName(serverIP);
                String check="ready";
                DatagramPacket dp = new DatagramPacket(check.getBytes(), check.getBytes().length,ip,7059);
                ds.send(dp);
                byte[] test = new byte[1024];
                DatagramPacket dpr = new DatagramPacket(test, test.length);
                ds.receive(dpr);
                handshook=true;
            } catch (Exception e){}
        }
    }
}


