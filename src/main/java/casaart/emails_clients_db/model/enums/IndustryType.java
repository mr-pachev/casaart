package casaart.emails_clients_db.model.enums;

public enum IndustryType {
    KITCHEN("КУХНЯ"),
    BAR("БАР"),
    ARTISTS("АРТИСТИ"),
    COSMETICS("КОЗМЕТИКА"),
    PHOTOGRAPHY("ФОТОГРАФИ"),
    TRANSPORT("ТРАНСПОРТ"),
    INTERNET("ИНТЕРНЕТ"),
    MOVERS("ХАМАЛИ"),
    IT("IT"),
    TECHNOLOGY("ТЕХНИКА"),
    DENTAL_CARE("ДЕНТАЛНА ГРИЖА");

    private final String displayName;

    IndustryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    // Метод за намиране на IndustryType по кирилица
    public static IndustryType fromCyrillicName(String cyrillicName) {
        for (IndustryType industryType : values()) {
            if (industryType.getDisplayName().equals(cyrillicName)) {
                return industryType;
            }
        }
        throw new IllegalArgumentException("No enum constant with display name " + cyrillicName);
    }
}

