import java.text.SimpleDateFormat;
import java.util.Date;


public class test {
	public static void main(String[] args){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String test = formatter.format(new Date());
		for(int i=0; i<10; i++){
			System.out.println(test+" "+(int)(Math.random()*100));
		}
	}
}
