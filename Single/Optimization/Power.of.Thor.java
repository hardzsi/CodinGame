import java.util.*;class Player{public static void main(String[] a){Scanner s=new Scanner(System.in);int x=s.nextInt(),y=s.nextInt(),X=s.nextInt(),Y=s.nextInt();while(y>0){s.next();System.out.println((y<Y?"N":y>Y?"S":"")+(x<X?"W":x>X?"E":""));Y+=y<Y?-1:y>Y?1:0;X+=x<X?-1:x>X?1:0;}}}