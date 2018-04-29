package logic;

import java.util.Vector;

public class Properties {
    private Vector<String> propertiesRus = new Vector<>();
    private Vector<String> propertiesEng = new Vector<>();

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
