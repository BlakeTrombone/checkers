import java.util.ArrayList;

public class move
{
  public final int jumps;
  public final int holes;
  public final int total;
  public final piece[] output;
  public final boolean moved;
  private piece[] pieces;
  private int pieceIndex;

  public move(piece[] board, int i, int direction)
  {
    pieceIndex=i;
    pieces = board.clone();
    piece p = pieces[i];
    boolean movedt=false;

    //moves piece to available direction
    if (direction==1)//front left code
    {
      if (isAvailable(p.getX()-1, p.getY()-1))
      {
        p.setCoords(p.getX()-1, p.getY()-1);
        movedt=true;
      }
      else if (canJump(p, 1) && p.isKing())
      {
        jump(p,1);
        movedt=true;
      }
    }
    else if(direction==2 && p.isKing())//front right code
    {
      if (isAvailable(p.getX()+1, p.getY()-1))
      {
        p.setCoords(p.getX()+1, p.getY()-1);
        movedt=true;
      }
      else if (canJump(p, 2))
      {
        jump(p,2);
        movedt=true;
      }
    }
    else if(direction==3)//back right code
    {
      if (isAvailable(p.getX()+1, p.getY()+1))
      {
        p.setCoords(p.getX()+1, p.getY()+1);
        movedt=true;
      }
      else if (canJump(p, 3))
      {
        jump(p,3);
        movedt=true;
      }
    }
    else if(direction==4)//back left code
    {
      if (isAvailable(p.getX()-1, p.getY()+1))
      {
        p.setCoords(p.getX()-1, p.getY()+1);
        movedt=true;
      }
      else if (canJump(p, 4))
      {
        jump(p,4);
        movedt=true;
      }
    }
    moved=movedt;
    output=pieces;
    //end piece move

    //begin assessing move
    int temphol=0;
    for (int x=0; x < 8; x++)
    {
      if(canJump(pieces[8+x],1))
        temphol++;
      if(canJump(pieces[8+x],2))
        temphol++;
      if(canJump(pieces[8+x],3))
        temphol++;
      if(canJump(pieces[8+x],4))
        temphol++;
    }
    holes=temphol;

    int tempjum=0;
    for (int x=0; x < 8; x++)
    {
      if(canJump(pieces[x],1))
        tempjum++;
      if(canJump(pieces[x],2))
        tempjum++;
      if(canJump(pieces[x],3))
        tempjum++;
      if(canJump(pieces[x],4))
        tempjum++;
    }
    jumps=tempjum;
    total=jumps-holes;

  }
  private boolean canJump(piece j, int direc)
  {
    if (direc==1)
    {
      return (!isAvailable(j.getX()-1, j.getY()-1) &&  pieceColorAt(j.getX()-1, j.getY()-1).equals((j.getColor().equals("black")) ? "red" : "black") && isAvailable(j.getX()-2, j.getY()-2) && j.isKing());
    }
    if (direc==2)
    {
      return (!isAvailable(j.getX()+1, j.getY()-1) &&  pieceColorAt(j.getX()+1, j.getY()-1).equals((j.getColor().equals("black")) ? "red" : "black") && isAvailable(j.getX()+2, j.getY()-2) && j.isKing());
    }
    if (direc==3)
    {
      return (!isAvailable(j.getX()+1, j.getY()+1) &&  pieceColorAt(j.getX()+1, j.getY()+1).equals((j.getColor().equals("black")) ? "red" : "black") && isAvailable(j.getX()+2, j.getY()+2));
    }
    if (direc==4)
    {
      return (!isAvailable(j.getX()-1, j.getY()+1) &&  pieceColorAt(j.getX()-1, j.getY()+1).equals((j.getColor().equals("black")) ? "red" : "black") && isAvailable(j.getX()-2, j.getY()+2));
    }
    return false;
  }

  private boolean canJump(piece j)
  {

      return (!isAvailable(j.getX()-1, j.getY()-1) &&  pieceColorAt(j.getX()-1, j.getY()-1).equals((j.getColor().equals("black")) ? "red" : "black") && isAvailable(j.getX()-2, j.getY()-2) && j.isKing())
    ||
      (!isAvailable(j.getX()+1, j.getY()-1) &&  pieceColorAt(j.getX()+1, j.getY()-1).equals((j.getColor().equals("black")) ? "red" : "black") && isAvailable(j.getX()+2, j.getY()-2) && j.isKing())
    ||
      (!isAvailable(j.getX()+1, j.getY()+1) &&  pieceColorAt(j.getX()+1, j.getY()+1).equals((j.getColor().equals("black")) ? "red" : "black") && isAvailable(j.getX()+2, j.getY()+2))
    ||
      (!isAvailable(j.getX()-1, j.getY()+1) &&  pieceColorAt(j.getX()-1, j.getY()+1).equals((j.getColor().equals("black")) ? "red" : "black") && isAvailable(j.getX()-2, j.getY()+2));
  }

  private boolean isAvailable(int x, int y)
  {
    for (piece pt : pieces)
    {
      if(pt.getX()==x && pt.getY()==y)
        return false;
    }
    if (x > 8 || y > 8 || x<1 || y<1)
      return false;
    return true;
  }

  piece getPieceAt(int x, int y)
  {
    for (int i=0; i < 16; i++)
      if (y==pieces[i].getY() && x==pieces[i].getX())
      {
        return pieces[i];
      }
    System.out.println("green");
    return new piece(9,9,"green");
  }
  String pieceColorAt(int x, int y)
  {
    return getPieceAt(x,y).getColor();
  }

  void outPiece(piece p)
  {
    p.setCoords(9, 9);
    p.setPress(false);
    p.setIn(false);
  }

  void jump(piece f, int direc)
  {
    if(canJump(f, direc))
    {
      if (direc==1)//front left code
      {
        if (isAvailable(f.getX()-2, f.getY()-2))
        {
          outPiece(getPieceAt(f.getX()-1, f.getY()-1));
          f.setCoords(f.getX()-2, f.getY()-2);
        }
      }
      if (direc==2)//front right code
      {
        if (isAvailable(f.getX()+2, f.getY()-2))
        {
          outPiece(getPieceAt(f.getX()+1, f.getY()-1));
          f.setCoords(f.getX()+2, f.getY()-2);
        }
      }
      if (direc==3)//back right code
      {
        if (isAvailable(f.getX()+2, f.getY()+2))
        {
          outPiece(getPieceAt(f.getX()+1, f.getY()+1));
          f.setCoords(f.getX()+2, f.getY()+2);
        }
      }
      if (direc==4)//back left code
      {
        if (isAvailable(f.getX()-2, f.getY()+2))
        {
          outPiece(getPieceAt(f.getX()-1, f.getY()+1));
          f.setCoords(f.getX()-2, f.getY()+2);
        }
      }
    }
    
  }
  
  void jump(piece f)
  {
    
    
  }
}
