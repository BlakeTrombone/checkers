import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class serverTest {
    private static DatagramSocket ds;
    private static InetAddress ip;
    private static boolean handshook=false;
    private static String serverIP;
    public static void main(String[] args)
    {
        try{
            ds = new DatagramSocket(7059);
            byte[] test = new byte[1024];
            DatagramPacket dp = new DatagramPacket(test,test.length);
            ds.receive(dp);
            String info = new String(dp.getData());
            ip=dp.getAddress();
            String responce=new StringBuilder(info).reverse().toString();;
            DatagramPacket dps = new DatagramPacket(responce.getBytes(), responce.getBytes().length, ip, dp.getPort());
            ds.send(dps);
            handshook=true;
            System.out.println("server handshook");
        } catch (Exception e){System.out.println("ERROR");}
    }
}
