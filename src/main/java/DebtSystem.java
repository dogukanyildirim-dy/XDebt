import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dogukan.yildirim
 * @implNote Uygulamaya yönelik hesaplamalar yapan yardımcı metotların bulunduğu sinif
 */
public class DebtSystem implements DebtSystemInterface {

    /**
     * Bu metod market.csv (ya da benzer formatta herhangi bir csv dosyasının) dosya içeriğinin Map'e çevrilmiş
     * nesnesini oluşturup dönen metottur.
     *
     * @param marketFile İşlenecek dosya adi
     * @param debtValue  Borç alınacak miktar
     * @return market.csv dosya içeriğinin Map'e çevrilmiş nesnesi
     */
    @Override
    public Map<String, List<String>> getFileToMap(String marketFile, Integer debtValue) {
        Map<String, List<String>> fileContentMap = new HashMap<>();
        File csvFile = new File(marketFile);
        if (csvFile.isFile()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
                bufferedReader.readLine(); //For skip first line
                String row;
                while (Objects.nonNull(row = bufferedReader.readLine())) {
                    String[] columns = row.split(",");
                    fileContentMap.put(columns[0], Arrays.asList(Arrays.copyOfRange(columns, 1, 3)));
                }
                int debtCapacity = fileContentMap.values().stream().map(strings -> strings.get(1)).mapToInt(Integer::valueOf).sum();
                if (debtCapacity < debtValue) {
                    System.err.println("Girilen miktara uygun bir borc bulunamadi!");
                    System.exit(1);
                }
            } catch (FileNotFoundException e) {
                System.err.println("Dosya bulunamadi!");
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Dosya okunurken bir hata olustu!");
                System.exit(1);
            }
        } else {
            System.err.println("Dosya bulunamadi!");
            System.exit(1);
        }
        return fileContentMap;
    }

    /***
     * Bu metot Borç miktarının sisteme uygun olup olmadığını denetler.
     *
     * @param debtValue Borç alınacak miktar
     */
    @Override
    public void debtValueValidation(Integer debtValue) {
        if (debtValue < 1000 || debtValue > 15000) {
            System.err.println("Alınacak borç 1000-15000TL arasında olmalıdır!");
            System.exit(1);
        }
        if (debtValue % 100 != 0) {
            System.err.println("Alınacak borç 100'ün katı olmalıdır!");
            System.exit(1);
        }
    }

    /**
     * Bu metot ortalama faiz miktarını, toplam geri ödemeyi ve aylık ödemeyi hesaplayıp yazdırır.
     *
     * @param fileContentMap market.csv dosya içeriğinin Map'e çevrilmiş nesnesi
     * @param debtValue      Borç alınacak miktar
     */
    @Override
    public void calculateRateAndTotalDebt(Map<String, List<String>> fileContentMap, Integer debtValue) {
        Map<Float, List<Integer>> ratesAndValuesMap = fileContentMap.values().stream()
                .collect(Collectors.groupingBy(a -> Float.parseFloat(a.get(0)),
                        Collectors.mapping(a -> Integer.parseInt(a.get(1)), Collectors.toList())));

        float totalRates = 0.0F;
        int divideBy = 0;
        int totalDebt = 0;
        float totalInterest = 0.0F;

        List<Float> sortedRateList = ratesAndValuesMap.keySet().stream().sorted().collect(Collectors.toList());
        for (Float rate : sortedRateList) {
            List<Integer> sortedValueList = ratesAndValuesMap.get(rate).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            for (Integer value : sortedValueList) {
                if (totalDebt < debtValue) {
                    totalRates += rate;
                    divideBy++;
                    if (value + totalDebt > debtValue) {
                        value = debtValue - totalDebt;
                    }
                    totalDebt += value;
                    totalInterest += (value * rate);
                }
            }
        }
        System.out.println("Istenilen Miktar: " + totalDebt + "TL");

        DecimalFormat rateDF = new DecimalFormat("0.0");
        Float avgRate = (totalRates / divideBy) * 100;
        System.out.println("Faiz Orani: " + "%" + rateDF.format(avgRate));

        DecimalFormat paymentDF = new DecimalFormat("0.00");
        Float totalRepayment = totalDebt + totalInterest;
        Float monthlyRepayment = totalRepayment / 36;
        System.out.println("Aylik Odeme: " + paymentDF.format(monthlyRepayment));
        System.out.println("Toplam Geri Odeme: " + paymentDF.format(totalRepayment));
    }
}
