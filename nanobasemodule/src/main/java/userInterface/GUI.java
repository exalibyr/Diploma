package userInterface;

import logic.DataManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

    private JTable resultTable = null;
    private JScrollPane resultPanel = null;

    public GUI(){
        setTitle("NaNobase module");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

        final Container container = getContentPane();
//        final GroupLayout mainLayout = new GroupLayout(container);
//        container.setLayout(mainLayout);

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new VerticalLayout());

        final JList<String> queryList = GUIBuilder.buildQueryList();
        JButton getResultButton = new JButton("get result");
        actionPanel.add(queryList);
        actionPanel.add(getResultButton);
        container.add(BorderLayout.WEST, actionPanel);
//        mainLayout.setVerticalGroup(mainLayout
//                .createParallelGroup()
//                .addComponent(actionPanel)
//        );

        final DataManager dataManager = new DataManager();

        getResultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(resultTable != null) {
                    container.remove(resultPanel);
                }
                resultTable =  dataManager.getResultTable(queryList.getSelectedValue());
                resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                resultPanel = new JScrollPane(resultTable,
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
//                mainLayout.setVerticalGroup(mainLayout
//                        .createParallelGroup(GroupLayout.Alignment.LEADING)
//                        .addComponent(resultPanel)
//                );
                container.add(BorderLayout.CENTER, resultPanel);
                revalidate();
            }
        });

        pack();
        setVisible(true);

    }

}
