
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
  
  private piece(int nx, int ny, String clr, boolean kng, boolean isn, boolean psd)
  {
    setCoords(nx, ny);
    king=kng;
    isIn=isn;
    pressed=psd;
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
}
