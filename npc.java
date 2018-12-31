import java.util.ArrayList;

public class npc
{
  private static move[] moves = new move[32];
  public static piece[] turn(piece[] pieces)
  {
    piece[] output;
    for(int i =0; i < 32; i++)
      moves[i]=new move(pieces, i/4 ,(i%4)+1);
    
    int curbest=0;
      for (int o=0; o < 5; o++)
        if(moves[o].total > moves[curbest].total)
          curbest=o;
      ArrayList<move> asdf = new ArrayList<move>();
      for(move m : moves)
        if(m.total>=moves[curbest].total)
          asdf.add(m);
      output=asdf.get((int)(Math.random()*asdf.size())).output;
    
    
    
    return pieces;
  }
}
