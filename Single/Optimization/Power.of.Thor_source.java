// Power of Thor - Code size 350
import java.util.*;
class Player{
	public static void main(String[] a){
		Scanner s=new Scanner(System.in);
		int x=s.nextInt(),y=s.nextInt(),X=s.nextInt(),Y=s.nextInt();
		while(true){
			s.next();
			System.out.println((y<Y?"N":y>Y?"S":"")+(x<X?"W":x>X?"E":""));
			Y+=Math.signum(y-Y);X+=Math.signum(x-X);
		}
	}
}