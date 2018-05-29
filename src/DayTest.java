import java.util.Calendar;
import java.util.GregorianCalendar;

public class DayTest {
    public static void main(String[] args) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        int d = cal.get(Calendar.DAY_OF_WEEK);
        System.out.println(d);
        System.out.println(Mensaplan.getDayName(d));
    }
}
