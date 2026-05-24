package giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products;

import lombok.Getter;

@Getter

public enum Unit {
    //UNITà PESO
    MILLIGRAMS("mg", "Milligrammi", true),
    GRAMS("g", "Grammi", true),
    KILOGRAMS("kg", "Chilogrammi", true),

    //UNITà LIQUIDI
    MILLILITERS("ml", "Millilitri", true),
    LITERS("l", "Litri", true),

    //UNITà GRADI
    CELSIUS("°C", "Gradi Celsius", true),
    PERCENT("%", "Percentuale", false),
    DAYS("giorni", "Giorni", true),
    MINUTES("minuti", "Minuti", true),
    CURRENCY("€", "Valuta", false),

    //UNITà CONFEZIONAMENTO
    PIECE("pz", "Pezzo singolo", false),
    PACK("conf", "Confezione / Pacchetto", false),
    PORTTION("porz", "Porzione", false),
    TRAY("vasc", "Vaschetta", false),
    JAR("vas", "Vasetto", false);

    private final String symbol;
    private final String description;
    private final boolean isMeasurable;

    Unit(String symbol, String description, boolean isMeasurable) {
        this.symbol = symbol;
        this.description = description;
        this.isMeasurable = isMeasurable;
    }

}
