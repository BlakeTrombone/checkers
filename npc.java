import java.io.*;
import java.net.*;

public class npc
{
    public npc(boolean server)//server startup
    {
        
    }

    public npc(String serverIP)//client startup
    {
        
    }

    private piece[] flip(piece[] pieces)//flips reds and blacks
    {
        piece[] output = new piece[16];
        for (int x = 0; x < 16; x++)
            output[x]=pieces[x+6];
        return output;
    }

    private String convert(piece[] pieces)//converts array of pieces to a string for network transmission
    {
        String output="";
        for (piece p : pieces)
            output+=p.toString();
        return output;
    }

    public static piece[] turn (piece[] pieces)
    {
        return pieces;
    }
}
