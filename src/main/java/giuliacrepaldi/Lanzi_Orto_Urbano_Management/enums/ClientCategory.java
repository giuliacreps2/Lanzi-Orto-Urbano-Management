package giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums;

public enum ClientCategory {
    B2C("Privato - Consumatore Finale"),
    B2B("Business - Partita IVA");

    private final String description;

    ClientCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}