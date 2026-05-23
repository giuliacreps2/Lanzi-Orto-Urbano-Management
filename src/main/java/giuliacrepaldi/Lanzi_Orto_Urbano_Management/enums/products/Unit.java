package giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products;

import lombok.Getter;

@Getter

public enum Unit {
    //UNITà PESO
    MILLIGRAM("mg", true),
    GRAM("g", true),
    KILOGRAM("kg", true),

    //UNITà LIQUIDI
    MILLILITER("ml", true),
    LITER("l", true),

    //UNITà CONFEZIONAMENTO
    PIECE("pz", false),
    PACK("conf", false),
    PORTTION("raz", false),
    TRAY("vasc", false);

    private final String symbol;
    private final boolean isMeasurable;

    Unit(String symbol, boolean isMeasurable) {
        this.symbol = symbol;
        this.isMeasurable = isMeasurable;
    }

}
