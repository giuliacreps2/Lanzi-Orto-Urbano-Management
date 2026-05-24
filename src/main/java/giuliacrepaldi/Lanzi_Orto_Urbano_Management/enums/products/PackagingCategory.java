package giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products;

import lombok.Getter;

@Getter

public enum PackagingCategory {
    BOX_CARTON("Scatola/Cassetta in Cartone"),
    BOX_WOOD("Cassetta di legno"),
    TRAY_PLASTIC("Vaschetta Plastica/PET"),
    TRAY_CELLULOSE("Vaschetta in Cellulosa"),
    GLASS_JAR("Barattolo di Vetro"),
    GLASS_VITAL("Fialetta in Vetro"),

    GROWING_PAD("Tappeto/Grow Pad"),

    BAG_PLASTIC("Sacchetto di Plastica"),
    BAG_PAPER("Sacchetto di Carta"),

    BOTTLE("Bottiglia"),
    BULK("Nessuno/Sfuso");

    private final String displayName;

    PackagingCategory(String displayName) {
        this.displayName = displayName;
    }
}
