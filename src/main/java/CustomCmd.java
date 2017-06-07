import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class CustomCmd {

    public void stepOne() {
        System.out.println("step one");
    }

    public void stepTwo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        while (true) {
            System.out.print("天王盖地虎: ");
            Scanner scanner = new Scanner(System.in);
            String secretSignal = scanner.nextLine();
            if (sdf.format(new Date()).equals(secretSignal)) {
                System.out.println("√√√√√");
                break;
            } else {
                System.out.println("×××××");
            }
        }
    }

    public void stepThree() {
        System.out.println("step three");
    }
}
