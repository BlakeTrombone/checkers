import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class clientTest {
    private static DatagramSocket ds;
    private static InetAddress ip;
    private static boolean handshook=false;
    private static String serverIP;
    public static void main(String[] args)
    {

        if(false)
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
                handshook=true;
            } catch (Exception e){System.out.println("ERROR");}
        }

        if (!false)
        {
            try{
                ds = new DatagramSocket();
                ip = InetAddress.getByName("127.0.0.1");
                String check="ready";
                DatagramPacket dp = new DatagramPacket(check.getBytes(), check.getBytes().length,ip,7059);
                ds.send(dp);
                byte[] test = new byte[1024];
                DatagramPacket dpr = new DatagramPacket(test, test.length);
                ds.receive(dpr);
                handshook=true;
                System.out.println("client handshook");
            } catch (Exception e){}
        }
    }
}