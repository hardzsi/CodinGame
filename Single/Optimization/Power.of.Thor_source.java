// Power of Thor - Code size 374
import java.util.*;
class Player{
	public static void main(String[] a){
		Scanner s=new Scanner(System.in);
		int x=s.nextInt(),y=s.nextInt(),X=s.nextInt(),Y=s.nextInt();
		while(true){
			s.next();String m="";
			if(y<Y){m="N";Y--;}else if(y>Y){m="S";Y++;}
			if(x>X){m+="E";X++;}else if(x<X){m+="W";X--;}
			System.out.println(m);
		}
	}
}