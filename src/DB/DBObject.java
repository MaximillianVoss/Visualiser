package DB;

import java.io.File;
import java.util.ArrayList;

public class DBObject {
    /**
     * поля объекта
     */
    public ArrayList<Field> fields;
    /**
     * тип обЪекта, можно не заполнять
     */
    public String typeName;

    public DBObject(ArrayList<Field> fields, String typeName) {
        this.fields = fields;
        this.typeName = typeName;
    }

    public DBObject(ArrayList<String> fieldNames, ArrayList<String> fieldsValues) throws Exception {
        if (fieldNames.size() != fieldsValues.size())
            throw new Exception("Невозможно заполнить объект - несовпадает количество полей и значений!");
        ArrayList<Field> fields = new ArrayList<>();
        for (int i = 0; i < fieldNames.size(); i++)
            fields.add(new Field(fieldNames.get(i), fieldNames.get(i), fieldsValues.get(i)));
        this.fields = fields;
        this.typeName = "N/A";
    }

    public DBObject() {
        this(new ArrayList<>(), "N/A");
    }

    public boolean HasField(String fieldName) {
        for (int i = 0; i < this.fields.size(); i++)
            if (this.fields.get(i).name == fieldName)
                return true;
        return false;
    }

    public Field GetField(int index) throws Exception {
        if (index > -1 && index < this.fields.size())
            return this.fields.get(index);
        throw new Exception("Индекс за пределами диапазона!");
    }

    public Field GetField(String fieldName) throws Exception {
        for (int i = 0; i < this.fields.size(); i++)
            if (this.fields.get(i).name == fieldName)
                return this.fields.get(i);
        throw new Exception("Поле не надено!");
    }

    public void AddField(Field field) {
        this.fields.add(field);
    }

}
