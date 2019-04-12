public class piece
{
    private int x;
    private int y;
    private boolean king;
    private String colr;
    private boolean isIn;
    private boolean pressed;

    public piece(int nx, int ny, String clr)
    {
        setCoords(nx, ny);
        colr=clr;
        king=false;
        isIn=true;
    }

    public piece(int nx, int ny, String clr, boolean kng, boolean isn, boolean psd)
    {
        setCoords(nx, ny);
        king=kng;
        isIn=isn;
        pressed=psd;
        colr=clr;
    }

    public void setCoords(int nx, int ny)
    {
        x=nx;
        y=ny;
    }

    public boolean isKing()
    {
        return king;
    }

    public void setKing(boolean k)
    {
        king=k;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public float getXPixel()
    {
        return ((x-1)*75)+(75/2);
    }

    public float getYPixel()
    {
        return ((y-1)*75)+(75/2);
    }

    public String getColor()
    {
        return colr;
    }

    public void switchColor()
    {
        if (colr.equals("black"))
            colr="red";
        else if (colr.equals("red"))
            colr="black";
        else
            System.out.println("I DON'T KNOW WHAT THE COLOR IS");
    }

    public boolean getIn()
    {
        return isIn;
    }

    public void setIn(boolean i)
    {
        isIn=i;
    }

    public void setPress(boolean p)
    {
        pressed=p;
    }

    public boolean isPressed()
    {
        return pressed;
    }

    public piece clone()
    {
        return new piece(x, y, colr, king, isIn, pressed);
    }

    public piece flip()
    {
        return new piece (9-x, 9-y, ((colr.equals("red")) ? "black" : "red"), king, isIn, false);
    }

    public String toString()
    {
        String output="";
        output+=""+x;
        output+=""+y;
        output+=""+((colr.equals("red")) ? "r" : "b");
        output+=""+((king) ? "t" : "f");
        output+=""+((isIn) ? "t" : "f");
        output+=""+((pressed) ? "t" : "f");
        return output;
    }
}
