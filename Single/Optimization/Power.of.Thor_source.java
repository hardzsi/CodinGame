// Power of Thor - Code size 374
import java.util.*;
class Player{
	public static void main(String[] r){
		Scanner s=new Scanner(System.in);
		int x=s.nextInt(),y=s.nextInt(),X=s.nextInt(),Y=s.nextInt(),a,b;
		while(true){
			char[] c={83,13,78,69,13,87};
			s.next();a=Y;b=X;
			Y+=Math.signum(y-Y);X+=Math.signum(x-X);
			System.out.println(""+c[a-Y+1]+c[b-X+4]);
		}
	}
}