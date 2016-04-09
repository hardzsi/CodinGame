// Power of Thor - Code size 383
import java.util.*;
class Player{
	public static void main(String[] r){
		Scanner s=new Scanner(System.in);
		int x=s.nextInt(),y=s.nextInt(),X=s.nextInt(),Y=s.nextInt();
		while(true){
			int[] c={83,13,78,69,13,87,Y,X};
			s.next();
			Y+=Math.signum(y-Y);X+=Math.signum(x-X);
			System.out.println(""+(char)c[c[6]-Y+1]+(char)c[c[7]-X+4]);
		}
	}
}