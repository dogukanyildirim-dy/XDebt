import java.util.List;
import java.util.Map;

/**
 * @author dogukan.yildirim
 * @apiNote X firması için 36 aylık borç verecek bir borç hesaplama sistemi uygulaması
 */
public class QuestionThreeSolution {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Lutfen bir dosya ve borc miktari girişi yapiniz!");
            System.exit(1);
        } else if (args.length == 1) {
            System.err.println("Lutfen borc miktari girişi yapiniz!");
            System.exit(1);
        }
        String marketFile = args[0];
        Integer debtValue = Integer.parseInt(args[1]);

        DebtSystem debtSystem = new DebtSystem();
        debtSystem.debtValueValidation(debtValue);
        Map<String, List<String>> fileContentMap = debtSystem.getFileToMap(marketFile, debtValue);
        debtSystem.calculateRateAndTotalDebt(fileContentMap, debtValue);
    }


}
