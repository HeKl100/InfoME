package GUI;

import Tasks.ReadWriteKV;
import database.DatabaseManager;
import database.DatabaseQuery;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainWindow extends JFrame
{
    private JPanel pnMain;
    private JButton btSettings;
    private JButton btStart;
    private String dbUrl;

    public MainWindow()
    {
        setContentPane(pnMain);
        setTitle("InfoME");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        // button "Einstellungen"

        btSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // open the "Einstellungen" settings-UI

                SwingUtilities.invokeLater(() -> new Settings());
            }
        });


        btStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    ReadWriteKV readWriteKV = new ReadWriteKV();
                try {
                    readWriteKV.readAndWrite();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });
    }}
