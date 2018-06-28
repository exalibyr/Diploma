package logic;

import java.util.Vector;

//класс для хранения отобранных свойств
//англоязычный список необходим для преобразования
// и избежания проблем с запросами, содержащими кириллицу
public class Properties {
    //вектор свойств с названием на русском языке
    private Vector<String> propertiesRus = new Vector<>();
    //вектор свойств с названием на английском языке
    private Vector<String> propertiesEng = new Vector<>();

    //метод для добавления данных в объект
    public void addPair(String propertyRus, String propertyEng){
        this.propertiesRus.add(propertyRus);
        this.propertiesEng.add(propertyEng);
    }

    public Vector<String> getPropertiesRus() {
        return propertiesRus;
    }

    public Vector<String> getPropertiesEng() {
        return propertiesEng;
    }

    public void setPropertiesRus(Vector<String> propertiesRus) {
        this.propertiesRus = propertiesRus;
    }

    public void setPropertiesEng(Vector<String> propertiesEng) {
        this.propertiesEng = propertiesEng;
    }
}
