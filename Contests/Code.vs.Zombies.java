// Code vs Zombies 11.29 17:33
import java.util.*;

class Player {
    public static final int XMAX = 15999;                   // Global (within Player class) constants 
    public static final int YMAX = 8999;
    
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int defendHumanID = -1;      
        boolean firstLoop = true;
        Human coder;
        Humans humans = new Humans();
        Zombies zombies = new Zombies();
        // game loop
        while (true) {
            String out = "";
            int[] xEdge = {XMAX, 0};                        // xMin, xMax
            int[] yEdge = {YMAX, 0};                        // yMin, yMax
            int x = in.nextInt();
            int y = in.nextInt();
            coder = new Human(-1, x, y);
            System.err.println("coder:\n" + coder + "\n" );            
            int humanCount = in.nextInt();
            System.err.println(humanCount + " human(s):");
            for (int i = 0; i < humanCount; i++) {
                int humanID = in.nextInt();
                int humanX = in.nextInt();
                int humanY = in.nextInt();
                Human human = new Human(humanID, humanX, humanY);
                humans.add(human);
                System.err.println(human.toString());
                if (human.getX() < xEdge[0]) { xEdge[0] = human.getX(); }
                if (human.getX() > xEdge[1]) { xEdge[1] = human.getX(); }
                if (human.getY() < yEdge[0]) { yEdge[0] = human.getY(); }
                if (human.getY() > yEdge[1]) { yEdge[1] = human.getY(); }
            }
            System.err.println("\nx (min-max): " + xEdge[0] + "-" + xEdge[1]);
            System.err.println("y (min-max): " + yEdge[0] + "-" + yEdge[1]);          
            int zombieCount = in.nextInt();
            System.err.println("\n" + zombieCount + " zombie(s):");                     
            for (int i = 0; i < zombieCount; i++) {
                int zombieID = in.nextInt();
                int zombieX = in.nextInt();
                int zombieY = in.nextInt();
                int zombieXNext = in.nextInt();
                int zombieYNext = in.nextInt();
                Zombie zombie = new Zombie(zombieID, zombieX, zombieY, zombieXNext, zombieYNext);
                zombies.add(zombie);
                System.err.println(zombie.toString());               
            }

            int xAvg = xEdge[0] + (int)((xEdge[1] - xEdge[0]) / 2);
            int yAvg = yEdge[0] + (int)((yEdge[1] - yEdge[0]) / 2);                      
            if (humanCount == 2) {
                if (defendHumanID < 0) { defendHumanID = (int)(Math.random() * 2); }    // random choice: 0 or 1
                System.err.println("\ndefendHumanID:" + defendHumanID);
                ArrayList<Creature> hums = humans.getAll();
                Human human0 = (Human)hums.get(0);
                Human human1 = (Human)hums.get(1);
                int xDefend = defendHumanID == 0 ? human0.getX() : human1.getX();
                int yDefend = defendHumanID == 0 ? human0.getY() : human1.getY();
                int xOffset = (int)(Math.abs(xAvg - xDefend) / 2);
                int yOffset = (int)(Math.abs(yAvg - yDefend) / 2);
                xAvg += (xDefend > xAvg ? xOffset : -xOffset);
                yAvg += (yDefend > yAvg ? yOffset : -yOffset);
                out = xAvg + " " + yAvg;
            } else {
                out = xAvg + " " + yAvg;
            }
            System.out.println(out); // Your destination coordinates
        }
    } // main()
} // class Player

class Humans extends Creatures {
    public Humans() { super(); }
    
    public void add(Human human) { super.add(human); } 
} // class Humans

class Zombies extends Creatures {
    public Zombies() { super(); }
    
    public void add(Zombie zombie) { super.add(zombie); } 
} // class Zombies

class Creatures {
    public Creatures() {;}
    
    public void add(Creature creature)  { creatures.add(creature); }
    public ArrayList<Creature> getAll() { return creatures; }

    private ArrayList<Creature> creatures = new ArrayList<>();
} // class Creatures

class Human extends Creature {
    public Human() { super(); }
    
    public Human(int ID, int x, int y) {
        super(ID, x, y);
    }
    
    public int getClosestZombieID() { return closestZombieID; }
    
    @Override
    public String toString() { return "human " + super.toString(); }
    
    private int closestZombieID = -1;
} // class Human

class Zombie extends Creature {
    public Zombie() { super(); }
    
    public Zombie(int ID, int x, int y, int xNext, int yNext) {
        super(ID, x, y);
        this.xNext = xNext;
        this.yNext = yNext;
    }
    
        @Override
    public String toString() { return "zombie " + super.toString(); }
    
    private int xNext;
    private int yNext;
} // class Zombie

class Creature {
    public Creature() { ID = x = y = 0; }
    
    public Creature(int ID, int x, int y) {
        this.ID = ID;
        this.x = x;
        this.y = y;
    }
    
    public int getID()       { return ID; }
    public int getX()        { return x; }
    public int getY()        { return y; }
    public boolean isAlive() { return alive == true; }
    public boolean isDead()  { return alive == false; }
    public int[] getCoords() { int[] coords = {x, y}; return coords; }
    
    public void setX(int x)  { this.x = (x >= 0 && x <= Player.XMAX) ? x : x < 0 ? 0 : Player.XMAX; }
    public void setY(int y)  { this.y = (y >= 0 && y <= Player.YMAX)  ? y : y < 0 ? 0 : Player.YMAX; }
    public void setCoords(int[] coords) {
        x = (coords[0] >= 0 && coords[0] <= Player.XMAX) ? coords[0] : (coords[0] < 0) ? 0 : Player.XMAX;
        y = (coords[1] >= 0 && coords[1] <= Player.YMAX)  ? coords[1] : (coords[1] < 0) ? 0 : Player.YMAX;
    }
    public void kill()       { alive = false; }
    public void revive()     { alive = true; }
    
    @Override
    public String toString() { return "ID:" + ID + (alive ? " x:" + x + " y:" + y : " dead"); }
    
    private int ID;
    private int x;
    private int y;
    private boolean alive = true;
} // class Creature