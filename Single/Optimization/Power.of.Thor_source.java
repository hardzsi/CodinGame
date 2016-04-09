// Power of Thor - Code size 372
import java.util.*;
class Player{
	public static void main(String[] r){
		Scanner s=new Scanner(System.in);
		int x=s.nextInt(),y=s.nextInt(),X=s.nextInt(),Y=s.nextInt();
		while(true){
			s.next();int a=Y,b=X;
			Y+=Math.signum(y-Y);X+=Math.signum(x-X);
			System.out.println((""+"S N".charAt(a-Y+1)+"E W".charAt(b-X+1)).trim());
		}
	}
}