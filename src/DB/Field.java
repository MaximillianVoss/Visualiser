package DB;

public class Field {
    public String titie;
    public String name;
    public String value;

    public Field(String title, String name, String value) {
        this.titie = title;
        this.name = name;
        this.value = value;
    }

    public Field() {
        this("N/A", "N/A", "N/A");
    }
}
