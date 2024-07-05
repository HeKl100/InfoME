package file;

import java.util.ArrayList;
import java.util.List;

public class LineValidator {

    // Check if the line is valid based on the first column and column count
    public static boolean isValid(String line) {
        if (line == null || line.trim().isEmpty()) {
            return false;
        }
        String[] data = StringSplitter.splitString(line);
        return data.length == 71 && isValidFirstColumn(data[0]);
    }

    // Check if the first column value is one of the valid cases
    private static boolean isValidFirstColumn(String firstColumn) {
        switch (firstColumn) {
            case "KV Genehmigt":
            case "Vorgang":
            case "Kostenvoranschlag":
            case "Auftrag":
            case "Lieferschein":
                return true;
            default:
                return false;
        }
    }

    // Method to filter valid lines and return a new list of valid lines
    public static List<String> validateLines(List<String> lines) {
        List<String> validLines = new ArrayList<>();

        for (String line : lines) {
            if (isValid(line)) {
                validLines.add(line);
            }
        }

        return validLines;
    }

    public static void main(String[] args) {
        // Example usage
        LineValidator processor = new LineValidator();
        List<String> lines = List.of(
                "Auftrag,03.07.2024,10018094,06.07.2023,Ohne Zuordnung,20.06.2023,2023,2023-Juni,290279,\"Jandl, Alois (290279)\",Kasse,1001,Österr.Gesundheitskasse (1001),10,Fil. Gradnerstraße (10),Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,183,\"Danyi, Eva (183)\",183,\"Danyi, Eva (183)\",20,GRADNERSTRAßE (20),160506,Malcher (160506),Ohne Zuordnung,Ohne Zuordnung,Inko 20.6.23,Inko ..,Ohne Zuordnung,4,Inko saugend (4),Ohne Zuordnung,41716308,20.06.2023,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,ohne KV,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,\"0,00\",\"0,00\",\"105,60\",\"0,00\",\"0,00\",\"192,00\",nicht geliefert,nicht abgerechnet,Ohne Zuordnung,Ohne Zuordnung,35,21,1,20.06.2023,378,älter als 5 Wochen,ja,ohne WaWi",
                "Vorgang,03.07.2024,10013439,21.06.2023,Ohne Zuordnung,21.06.2023,2023,2023-Juni,530304,\"Hiden, Annemarie (530304)\",Ohne Zuordnung,1001,Österr.Gesundheitskasse Landesstelle Stmk. (1001),10,Fil. Gradnerstraße (10),Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,205,\"Röggla, Tanja (205)\",205,\"Röggla, Tanja (205)\",20,GRADNERSTRAßE (20),Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Stoma Neoprengürtel,Stoma Neoprengürtel,Ohne Zuordnung,7,Freiverkauf (7),Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,ohne KV,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,\"0,00\",\"0,00\",\"0,00\",\"0,00\",\"0,00\",Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,35,21,1,21.06.2023,377,älter als 5 Wochen,ja,Ohne Zuordnung",
                "Vorgang,03.07.2024,10013661,21.06.2023,Ohne Zuordnung,21.06.2023,2023,2023-Juni,696466,\"Köck, Peter (696466)\",Ohne Zuordnung,1001,Österr.Gesundheitskasse Landesstelle Stmk. (1001),10,Fil. Gradnerstraße (10),Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,205,\"Röggla, Tanja (205)\",205,\"Röggla, Tanja (205)\",20,GRADNERSTRAßE (20),Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Pari Boy,Pari Boy,Ohne Zuordnung,1,Tarifliche Leistung (1),Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,ohne KV,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,\"0,00\",\"0,00\",\"0,00\",\"0,00\",\"0,00\",Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,35,21,1,21.06.2023,377,älter als 5 Wochen,ja,Ohne Zuordnung",
                "Kostenvoranschlag,03.07.2024,10013410,21.06.2023,Ohne Zuordnung,21.06.2023,2023,2023-Juni,555262,\"Fuchs, Eduard (555262)\",Kasse,1001,Österr.Gesundheitskasse (1001),187,Fil. Frohnleiten (187),Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,203,\"Reisinger, Kornelia (203)\",203,\"Reisinger, Kornelia (203)\",33,FROHNLEITEN (33),220364,Hitziger (220364),142,Christa Stadlober (142),Tena Pants,Tena Pants,Ohne Zuordnung,2,Pants (2),Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,20002164,21.06.2023,zur Klärung,Ohne Zuordnung,nicht vorab,Ohne Zuordnung,\"Retour an Frohnleiten am 23.06.2023",
                "Tena Pants abgelehnt.\",nein,Ohne Zuordnung,\"21,48\",\"0,00\",\"214,76\",\"0,00\",\"0,00\",\"182,00\",Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,35,21,1,21.06.2023,377,älter als 5 Wochen,nein,Ohne Zuordnung",
                "KV Genehmigt,03.07.2024,10013449,21.06.2023,Ohne Zuordnung,21.06.2023,2023,2023-Juni,688572,\"Viterna, Alfons (688572)\",Kasse,2001,BVAEB (2001),10,Fil. Gradnerstraße (10),Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,167,\"Friedrich, Eva Maria (167)\",167,\"Friedrich, Eva Maria (167)\",20,GRADNERSTRAßE (20),6550,KH Weiz (6550),217,Selina Koch (217),Kompr. Strümpfe,Kompr. Strümpfe,Ohne Zuordnung,1,Tarifliche Leistung (1),Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,20002170,21.06.2023,genehmigt,21.06.2023,nicht vorab,Ohne Zuordnung,Ohne Zuordnung,nein,Ohne Zuordnung,\"130,00\",\"0,00\",\"592,04\",\"0,00\",\"0,00\",\"4,00\",Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,Ohne Zuordnung,35,21,1,21.06.2023,377,älter als 5 Wochen,nein,Ohne Zuordnung"
        );

        List<String> validLines = validateLines(lines);

        // Print the valid lines
        for (String line : validLines) {
            System.out.println(line);
        }
    }
}