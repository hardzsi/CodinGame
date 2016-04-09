// Power of Thor - Code size 373
import java.util.*;
class Player{
	public static void main(String[] r){
		Scanner s=new Scanner(System.in);
		int x=s.nextInt(),y=s.nextInt(),X=s.nextInt(),Y=s.nextInt();
		while(true){
			s.next();int a=Y,b=X;
			Y+=Math.signum(y-Y);X+=Math.signum(x-X);
			String[] c={"S","","N","E","","W"};System.out.println(c[a-Y+1]+c[b-X+4]);
		}
	}
}