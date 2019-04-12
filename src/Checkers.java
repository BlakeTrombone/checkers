import processing.core.PApplet;
import java.net.*;

public class Checkers extends PApplet {
    public static void main(String[] args) {
        PApplet.main("Checkers", args);
    }

    //Global Variables
    private static piece[] chips = new piece[16];
    private int clicker=0;
    private boolean npcTurn=false;
    private boolean ready=false;
    private boolean server=false;
    private boolean client=false;
    private String serverIp;
    private static npc comp;
    private boolean pass=true;
    private static boolean jumped=false;
    private static piece jumpedPiece;
    //End Global Variables

    public void drawBoard()
    {
        stroke(108, 57, 22);
        fill(108, 57, 22);
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                int x = i*75;
                int y = j*75;
                if (i%2==1 && j%2==0)
                    rect(x-1,y-1,75,75);
                if (i%2==0 && j%2==1)
                    rect(x-1,y-1,75,75);
            }
        }
        stroke(219, 211, 200);
        fill(219, 211, 200);
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                int x = i*75;
                int y = j*75;
                if (i%2==0 && j%2==0)
                    rect(x-1,y-1,75,75);
                if (i%2==1 && j%2==1)
                    rect(x-1,y-1,75,75);
            }
        }
    }

    public void piecesSetup()
    {
        for (int i = 0; i < 16; i++)
        {
            int tempx;
            int tempy;
            String tempclr="black";
            if (i < 8)
                tempclr="red";
            tempy=(i/4)+1;
            if (tempy >=3)
                tempy=8-(4-tempy);
            tempx=(2*(i%4))+(tempy%2)+1;
            Checkers.chips[i]=new piece(tempx,tempy,tempclr);
        }
    }

    public void piecesDraw()
    {
        for (piece p : Checkers.chips)
        {
            if (p.getColor().equals("red"))
            {
                stroke(255,0,0);
                fill(255,0,0);
            } else {
                stroke(0,0,0);
                fill(0,0,0);
            }
            if (Math.abs(mouseY-p.getYPixel()) <= 75/3 && Math.abs(mouseX-p.getXPixel()) <= 75/3 && p.getColor().equals("black") && p.getIn())
                stroke(0,255,0);


            if(p.isPressed())
            {
                stroke(255,255,0);
                fill(255, 255, 0,100);
                if (isAvailable(p.getX()-1, p.getY()-1) && !jumped)//front left
                    rect((p.getX()-2)*75, (p.getY()-2)*75, 75,75);
                if(isAvailable(p.getX()+1, p.getY()-1) && !jumped)//front right
                    rect((p.getX())*75, (p.getY()-2)*75, 75,75);
                if (isAvailable(p.getX()-1, p.getY()+1) && p.isKing() && !jumped)//back left
                    rect((p.getX()-2)*75, (p.getY())*75, 75,75);
                if(isAvailable(p.getX()+1, p.getY()+1) && p.isKing() && !jumped)//back right
                    rect((p.getX())*75, (p.getY())*75, 75,75);


                //jump drawing code
                if (isAvailable(p.getX()-2, p.getY()-2) && pieceColorAt(p.getX()-1, p.getY()-1).equals("red"))//front left
                    rect((p.getX()-3)*75, (p.getY()-3)*75, 75,75);
                if(isAvailable(p.getX()+2, p.getY()-2) && pieceColorAt(p.getX()+1, p.getY()-1).equals("red"))//front right
                    rect((p.getX()+1)*75, (p.getY()-3)*75, 75,75);
                if (isAvailable(p.getX()-2, p.getY()+2) && p.isKing() && pieceColorAt(p.getX()-1, p.getY()+1).equals("red"))//back left
                    rect((p.getX()-3)*75, (p.getY()+1)*75, 75,75);
                if(isAvailable(p.getX()+2, p.getY()+2) && p.isKing() && pieceColorAt(p.getX()+1, p.getY()+1).equals("red"))//back right
                    rect((p.getX()+1)*75, (p.getY()+1)*75, 75,75);
                //end jump drawing code
                fill(0,0,0);
            }

            ellipse (p.getXPixel(), p.getYPixel(), (float)(75/1.5),(float)(75/1.5));
            if (p.isKing())
            {
                fill (255/2,0,255);
                stroke(255/2,0,255);
                ellipse (p.getXPixel(), p.getYPixel(), 75/3,75/3);
            }
        }
    }

    public void movePiece(int newX, int newY)
    {
        for (int i=0; i < 16; i++)
            if (Checkers.chips[i].isPressed() && isAvailable(newX, newY))
            {
                Checkers.chips[i].setCoords(newX, newY);
                Checkers.chips[i].setPress(false);
            }
        if (!this.jumped)
            npcTurn=true;
    }

    public boolean canJump(piece p)
    {
        if (isAvailable(p.getX()-2, p.getY()-2) && pieceColorAt(p.getX()-1, p.getY()-1).equals("red"))//front left
            return true;
        if(isAvailable(p.getX()+2, p.getY()-2) && pieceColorAt(p.getX()+1, p.getY()-1).equals("red"))//front right
            return true;
        if (isAvailable(p.getX()-2, p.getY()+2) && p.isKing() && pieceColorAt(p.getX()-1, p.getY()+1).equals("red"))//back left
            return true;
        if(isAvailable(p.getX()+2, p.getY()+2) && p.isKing() && pieceColorAt(p.getX()+1, p.getY()+1).equals("red"))//back right
            return true;
        return false;
    }

    public void outPiece(piece p)
    {
        p.setCoords(9, 9);
        p.setPress(false);
        p.setIn(false);
        System.out.println("piece out!");
        this.jumped=false;
        for (int i=0; i < 16; i++)
            if (canJump(Checkers.chips[i]) && Checkers.chips[i].isPressed()) {
                System.out.println("can jump");
                this.jumped=true;
                this.jumpedPiece=Checkers.chips[i];
            }
    }

    public piece getPieceAt(int x, int y)
    {
        for (int i=0; i < 16; i++)
            if (y==Checkers.chips[i].getY() && x==Checkers.chips[i].getX())
            {
                return Checkers.chips[i];
            }
        System.out.println("green");
        return new piece(9,9,"green");
    }

    public boolean isAvailable(int x, int y)
    {
        for (piece pt : Checkers.chips)
        {
            if(pt.getX()==x && pt.getY()==y)
                return false;
        }
        if (x > 8 || y > 8 || x<1 || y<1)
            return false;
        return true;
    }

    public String pieceColorAt(int x, int y)
    {
        if(!isAvailable(x,y))
            return getPieceAt(x,y).getColor();
        else return "green";
    }

    public void crownKings()
    {
        for (piece pt : Checkers.chips)
        {
            if(pt.getY()==1 && pt.getColor().equals("black"))
                pt.setKing(true);
        }

    }

    public void forceMultiple()
    {
        if (this.jumped)
        {
            for (piece pt : Checkers.chips)
                pt.setPress(false);
            this.jumpedPiece.setPress(true);
        }
    }

    public void settings()
    {
        size(600,600);
    }
    public void setup()
    {
        piecesSetup();
    }

    public void drawPrompt()
    {
        if(!client && !server)
        {
            textAlign(CENTER, CENTER);
            textSize(32);
            if((mouseX>=100 && mouseX<=250) && (mouseY>=250 && mouseY<=350))
            {
                strokeWeight(3);
                fill(256,256,256);
            } else {
                strokeWeight(1);
                fill(204,204,204);
            }
            stroke(37,152,206);
            rect(100, 250, 150, 100);
            fill(0,0,0);
            text("Player 1", 100, 250, 150, 100);

            if((mouseX>=350 && mouseX<=500) && (mouseY>=250 && mouseY<=350))
            {
                strokeWeight(3);
                fill(256,256,256);
            } else {
                strokeWeight(1);
                fill(204,204,204);
            }
            stroke(95,196,49);
            rect(350, 250, 150, 100);
            fill(0,0,0);
            text("Player 2", 350, 250, 150, 100);
        }

        if(client)//client menu drawing
        {
            if (this.comp==null) {
                strokeWeight(1);
                fill(204, 204, 204);
                stroke(204, 204, 204);
                rect(0, 0, 600, 600);
                fill(0, 0, 0);
                stroke(0, 0, 0);
                textSize(20);
                text("Please enter partner's IP: This is the number showing up on your partner's computer", 150, 80, 300, 200);
                fill(255, 255, 255);
                stroke(255, 255, 255);
                rect(150, 300, 300, 50);
                fill(0, 0, 0);
                stroke(0, 0, 0);
                text(serverIp, 150, 300, 300, 50);
                if (mouseX >= 150 && mouseX <= 450 && mouseY >= 375 && mouseY <= 425) {
                    fill(255, 255, 255);
                    stroke(95, 196, 49);
                    strokeWeight(3);
                } else {
                    strokeWeight(1);
                    fill(204, 204, 204);
                    stroke(95, 196, 49);
                }
                rect(150, 375, 300, 50);
                fill(0, 0, 0);
                text("Next", 150, 375, 300, 50);
            }
            if (this.comp!=null)
            {
                stroke(204,204,204);
                fill(204,204,204);
                rect(0,0,600,600);
                textSize(32);
                fill(0,0,0);
                text("Loading...", 200,275,200,50);
                if (this.comp.isHandshook() && !pass)
                {
                    Checkers.chips=this.comp.firstRun();
                    npcTurn=false;
                    ready=true;
                    pass=true;
                }
                else
                    pass=false;
            }
        }

        if(server)
        {
            fill(204,204,204);
            stroke(204,204,204);
            rect(0,0,600,600);
            textAlign(CENTER, CENTER);
            textSize(20);
            fill(0,0,0);
            stroke(0,0,0);
            text("Enter this IP address into partner's computer: "+getMyIp(), 200,200,200,200);
            if (this.comp.isHandshook())
                ready=true;
        }
    }


    public String getMyIp()
    {
        InetAddress ip;
        String ips="";
        try {

            ip = InetAddress.getLocalHost();
            ips= ""+ip.getHostAddress();

        } catch (UnknownHostException e) {
            System.out.println("failed to get ip address");
        }
        return ips;
    }


    public void draw()
    {
        if (!ready)
        {
            drawPrompt();
        }
        if (ready)
        {
            background(255/2);
            drawBoard();
            crownKings();
            forceMultiple();
            piecesDraw();
            if (npcTurn && !pass)
            {
                Checkers.chips=this.comp.turn(Checkers.chips);
                npcTurn=false;
                pass=true;
            }
            if (npcTurn && pass)
                pass=false;
        }
    }


    public void keyPressed()
    {
        if(!ready && client && !server)
        {
            if("0123456789.".contains(""+key))
            {
                serverIp+=key;
            }
            if ((char) key == 8 && serverIp.length()>0)
            {
                serverIp=serverIp.substring(0,serverIp.length()-1);
            }
            System.out.println(serverIp);
        }
    }


    public void mouseClicked()
    {
        int clickX=mouseX;
        int clickY=mouseY;
        System.out.println("Click! "+ ++clicker);
        if (!ready)
        {
            if (!client && !server)
            {
                if((clickX>=100 && clickX<=250) && (clickY>=250 && clickY<=350))
                {
                    server=true;
                    try{this.comp = new npc(true);} catch (Exception e){System.out.println("OOPS!");}
                }

                if((clickX>=350 && clickX<=500) && (clickY>=250 && clickY<=350))
                {
                    client=true;
                    serverIp="";
                }
            }

            if (client)
            {
                if (clickX >=150 && clickX <=450 && clickY>=375 && clickY<=425)
                {
                    System.out.println("next");
                    if (this.comp==null) {
                        try {
                            this.comp = new npc(serverIp);
                            npcTurn = true;
                        } catch (Exception e) {
                            System.out.println("OOPS!");
                        }
                    }
                }
            }

        }

        if (ready)
        {
            for (piece p : Checkers.chips)
            {
                if(Math.abs(clickY-p.getYPixel()) <= 75/3 && Math.abs(clickX-p.getXPixel()) <= 75/3 && p.getColor().equals("black") && p.getIn())
                {
                    boolean adf = p.isPressed();
                    for (piece pt : Checkers.chips)
                        pt.setPress(false);
                    p.setPress(!adf);
                }

                if (clickX >= (p.getX()-2)*75 && clickX <=((p.getX()-2)*75)+74 && clickY >= (p.getY()-2)*75 && clickY <=((p.getY()-2)*75)+74 && p.isPressed())
                {
                    movePiece((clickX/75)+1, (clickY/75)+1);
                }

                if (clickX >= (p.getX())*75 && clickX <=((p.getX())*75)+74 && clickY >= (p.getY()-2)*75 && clickY <=((p.getY()-2)*75)+74 && p.isPressed())
                {
                    movePiece((clickX/75)+1, (clickY/75)+1);
                }

                if (clickX >= (p.getX()-2)*75 && clickX <=((p.getX()-2)*75)+74 && clickY >= (p.getY())*75 && clickY <=((p.getY())*75)+74 && p.isPressed() &&p.isKing())
                {
                    movePiece((clickX/75)+1, (clickY/75)+1);
                }

                if (clickX >= (p.getX())*75 && clickX <=((p.getX())*75)+74 && clickY >= (p.getY())*75 && clickY <=((p.getY())*75)+74 && p.isPressed() && p.isKing())
                {
                    movePiece((clickX/75)+1, (clickY/75)+1);
                }

                //jump code
                if (clickX >= (p.getX()-3)*75 && clickX <=((p.getX()-3)*75)+74 && clickY >= (p.getY()-3)*75 && clickY <=((p.getY()-3)*75)+74 && p.isPressed() && isAvailable(p.getX()-2, p.getY()-2) && pieceColorAt(p.getX()-1, p.getY()-1).equals("red"))
                {
                    outPiece(getPieceAt(p.getX()-1, p.getY()-1));
                    movePiece((clickX/75)+1, (clickY/75)+1);
                }

                if (clickX >= (p.getX()+1)*75 && clickX <=((p.getX()+1)*75)+74 && clickY >= (p.getY()-3)*75 && clickY <=((p.getY()-3)*75)+74 && p.isPressed()&& isAvailable(p.getX()+2, p.getY()-2) && pieceColorAt(p.getX()+1, p.getY()-1).equals("red"))
                {
                    outPiece(getPieceAt(p.getX()+1, p.getY()-1));
                    movePiece((clickX/75)+1, (clickY/75)+1);
                }

                if (clickX >= (p.getX()-3)*75 && clickX <=((p.getX()-3)*75)+74 && clickY >= (p.getY()+1)*75 && clickY <=((p.getY()+1)*75)+74 && p.isPressed() &&p.isKing()&& isAvailable(p.getX()-2, p.getY()+2) && pieceColorAt(p.getX()-1, p.getY()+1).equals("red"))
                {
                    outPiece(getPieceAt(p.getX()-1, p.getY()+1));
                    movePiece((clickX/75)+1, (clickY/75)+1);
                }

                if (clickX >= (p.getX()+1)*75 && clickX <=((p.getX()+1)*75)+74 && clickY >= (p.getY()+1)*75 && clickY <=((p.getY()+1)*75)+74 && p.isPressed() && p.isKing()&& isAvailable(p.getX()+2, p.getY()+2) && pieceColorAt(p.getX()+1, p.getY()+1).equals("red"))
                {
                    outPiece(getPieceAt(p.getX()+1, p.getY()+1));
                    movePiece((clickX/75)+1, (clickY/75)+1);
                }
                //end jump code

            }
        }

    }

}
