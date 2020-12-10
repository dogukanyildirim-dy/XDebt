import java.io.*;
import java.util.*;

public class QuestionThreeSolution {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Lutfen bir dosya ve borc miktari girişi yapiniz!");
            System.exit(1);
        } else if (args.length == 1) {
            System.err.println("Lutfen borc miktari girişi yapiniz!");
            System.exit(1);
        }

        String marketDosyasi = args[0];
        String borcMiktari = args[1];

        System.out.println("Market dosyasi: " + marketDosyasi);
        System.out.println("Borc miktari: " + borcMiktari + "TL");

        Map<String, List<String>> fileContentMap = new HashMap<>();

        File csvFile = new File(marketDosyasi);
        if (csvFile.isFile()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
                bufferedReader.readLine(); //For skip first line
                String row;
                while (Objects.nonNull(row = bufferedReader.readLine())) {
                    String[] columns = row.split(",");
                    fileContentMap.put(columns[0], Arrays.asList(Arrays.copyOfRange(columns, 1, 3)));
                }
                fileContentMap.entrySet().forEach(System.out::println);

                int debtCapacity = fileContentMap.values().stream().map(strings -> strings.get(1)).mapToInt(Integer::valueOf).sum();
                System.out.println(debtCapacity);

                if (debtCapacity < Integer.parseInt(borcMiktari)) {
                    System.err.println("Girilen miktara uygun bir borc bulunamadi!");
                }


            } catch (FileNotFoundException e) {
                System.err.println("Dosya bulunamadi!");
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Dosya okunurken bir hata olustu!");
                System.exit(1);
            }
        }

    }

}
