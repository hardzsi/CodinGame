// Mars Lander - Fuel Optimization (1934)
import java.util.*;
import java.awt.Point;

class Player {
    // Global variables for easy data exchange between methods
    private static Point[] site;                    // Side positions (left:0, right:1) of landing site as points
    private static int leftX;                       // X coord of left side of landing site
    private static int rightX;                      // X coord of right side of landing site
    private static int siteY;                       // Y coord (height) of landing site
    private static Point lander;                    // Current location of lander
    private static int myR = 0;                     // Desired rotation angle
    private static int myP = 0;                     // Desired thrust power

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();                       // the number of points used to draw the surface of Mars.
        Point[] surface = new Point[N];             // Store coordinates describing landing surface
        for (int i = 0; i < N; i++) {
            int LAND_X = in.nextInt();              // X coordinate of a surface point. (0 to 6999)
            int LAND_Y = in.nextInt();              // Y coordinate of a surface point. By linking all points together..
            surface[i] = new Point(LAND_X, LAND_Y); // ..in a sequential fashion, you form the surface of Mars
        }
        site = getSite(surface);
        leftX = (int)site[0].getX();                // Left side of landing site
        rightX = (int)site[1].getX();               // Right side of landing site
        siteY = (int)site[0].getY();                // Height of landing site

        int myP = 0;                                // Desired thrust power
        // game loop
        while (true) {
            int X = in.nextInt();                   // X coordinate of Mars Lander (in meters)
            int Y = in.nextInt();                   // Y coordinate of Mars Lander (in meters)
            int HS = in.nextInt();                  // the horizontal speed (in m/s), can be negative if going left
            int VS = in.nextInt();                  // the vertical speed (in m/s), can be negative if going down
            int F = in.nextInt();                   // the quantity of remaining fuel in liters.
            int R = in.nextInt();                   // the rotation angle in degrees (-90 to 90).
            int P = in.nextInt();                   // the thrust power (0 to 4).
            lander = new Point(X, Y);
            
            myR = getRotation(R, VS, HS);
            myP = getPower(R, VS, HS);
            System.out.println(msg(myR, myP));      // R P. R is desired rotation angle. P is desired thrust power.
        }
    } // main()

    // Returns desired rotation angle
    private static int getRotation (int R, int VS, int HS) {
        int X = (int)lander.getX();
        int Y = (int)lander.getY();
        int angleXcomp = 0;                         // Angle to compensate for distance between lander and landing site
        int angleHScomp = 0;                        // Angle to compensate for horizontal speed
        int angle = 0;                              // Result angle between [-90 .. +90]

        if (shouldKeepAltitude(X, Y) && HS != 0) {  // If keeping altitude has priority, lander should stand
            angle = 0;
        } else {
            angleXcomp = (int)Math.round(getDistance(X) * (3 / 185.0)); 
            angleXcomp += (int)(angleXcomp / 0.7);
            if (Math.abs(HS) > 6 && Y > siteY + 100) {  // Compensating for h.speed only if landing site isn't nearby 
                angleHScomp = (int)Math.round(HS * (9 / 24.7));
                angleHScomp += (int)(angleHScomp / 0.7);
            } else {
                angleHScomp = 0;
            }
            angle = angleXcomp + angleHScomp;
            if (angle > 90 || angle < -90) {
                angle = angle > 90 ? 90 : -90;
            }
        }
        debug("angleXcomp:" + angleXcomp + ", angleHScomp:" + angleHScomp + ", angle:" + angle);        
        debug("site height:" + siteY + ", leftX:" + leftX + "m, rightX:" + rightX + "m, dist:" + getDistance(X) + "m");
        return angle;
    } // getRotation()

    // Returns desired thrust power between 0 (off) to 4;
    private static int getPower(int R, int VS, int HS) {
        int X = (int)lander.getX();
        int Y = (int)lander.getY();
        int pow = 0;
        int HScomp = 0;
        int VScomp = 0;
        if (shouldKeepAltitude(X, Y) && VS < -1) {
            pow = 4;
        } else {
            if ((R < 0 && HS < 0) || (R > 0 && HS > 0)) {
                HScomp = Math.abs((int)Math.round(HS / 15.0));  
            } else {
                HScomp = 0;
            }
            VScomp = -(int)Math.round(VS / 6.0);
            pow = Math.min(HScomp + VScomp, 4);
        }
        if (getDistance(X) == 0 && R == 0 && (Y - siteY) < 123) {    // Switch off engines near landing to save fuel
            debug("Power OFF!");
            pow = 0;
        }
        
        debug("HScomp:" + HScomp + ", VScomp:" + VScomp + ", pow:" + pow);
        return pow;
    } // getPower()

    // Returns true, if altitude keeping (not losing much height) has a priority over landing
    private static boolean shouldKeepAltitude(int X, int Y) {
        return ((Y - siteY) < 600 && getDistance(X) > 1200) ? true : false;
    } // shouldKeepAltitude()

    // Returns distance in meters of lander (X) to closest point of landing site. Negative if lander is left from site
    private static int getDistance(int X) {
        int dist = 0;
        if (X < leftX) {
            dist = X - leftX;
        } else if (X >= leftX && X <= rightX) {
            dist = 0;            
        } else {
            dist = X - rightX;
        }
        return dist;
    } // getDistance()

    // Returns left and right points of flat surface, a.k.a. the landing site
    private static Point[] getSite(Point[] surface) {
        Point[] ret = new Point[2];
        for (int i = 0; i < surface.length - 1; ++i) {
            if (surface[i].getY() == surface[i + 1].getY()) {
                ret[0] = new Point(surface[i]);
                ret[1] = new Point(surface[i + 1]);
                break;
            }
        }
        return ret;
    } // getSite()

    private static String msg(int R, int P) { return R + " " + P; }

    private static void debug(String s) { System.err.println(s); }
} // class Player