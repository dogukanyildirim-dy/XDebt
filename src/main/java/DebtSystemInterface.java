import java.util.List;
import java.util.Map;

public interface DebtSystemInterface {

    Map<String, List<String>> getFileToMap(String marketFile, Integer debtValue);

    void debtValueValidation(Integer debtValue);

    void calculateRateAndTotalDebt(Map<String, List<String>> fileContentMap, Integer debtValue);
}
