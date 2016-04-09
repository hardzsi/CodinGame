// Power of Thor - Code size 368
import java.util.*;
class Player{
	public static void main(String[] r){
		Scanner s=new Scanner(System.in);
		int x=s.nextInt(),y=s.nextInt(),X=s.nextInt(),Y=s.nextInt(),a,b;
		while(String[] c={"S","","N","E","","W"}){
			s.next();a=Y;b=X;
			Y+=Math.signum(y-Y);X+=Math.signum(x-X);
			System.out.println(c[a-Y+1]+c[b-X+4]);
		}
	}
}