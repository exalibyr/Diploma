package userInterface;

import javax.swing.*;
import java.util.Vector;

public class GUIBuilder {

    public static JList<String> buildQueryList(){
        Vector<String> queryListContent = new Vector<>();
        queryListContent.add("info_about_ceramic_composites");
        queryListContent.add("questions_and_answers_for_ceramic_composites");
        JList<String> queryList = new JList<>(queryListContent);
        queryList.setAutoscrolls(true);
        return queryList;
    }
}
