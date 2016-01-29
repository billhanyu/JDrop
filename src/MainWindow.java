import org.apache.commons.io.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by Morton on 1/28/16.
 */
public class MainWindow {
    private JTextField txtDestIP;
    private JTextField txtMessage;
    private JButton btnBrowse;
    private JTextField txtFile;
    private JButton btnSend;
    private JButton btnExit;
    private JLabel lblIPAddress;
    private JLabel lblCurrentIP;
    private JLabel lblTextMessage;
    private JPanel panelMain;
    private JLabel lblCodeIndicator;
    private JLabel lblCode;

    File file;
    ServerSocket ss;
    Socket socket;

    public MainWindow() {
        btnExit.addActionListener(e -> System.exit(0));
        String ip = "";
        try {
            ip = Inet4Address.getLocalHost().getHostAddress();
            lblCurrentIP.setText(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        btnBrowse.addActionListener(new ActionListener() {
            boolean fileSelected = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fileSelected) {  //  If no file is selected, open file chooser
                    final JFileChooser fc = new JFileChooser();
                    int optionChoosed = fc.showOpenDialog(panelMain);

                    if (optionChoosed == JFileChooser.APPROVE_OPTION) {
                        file = fc.getSelectedFile();
                        txtFile.setText(file.getAbsolutePath());
                        txtMessage.setEnabled(false);
                        fileSelected = true;
                        btnBrowse.setText("Clear");
                    }
                } else {  // Else clear file path textfield and reset state
                    txtMessage.setEnabled(true);
                    file = null;
                    fileSelected = false;
                    btnBrowse.setText("Browse");
                    txtFile.setText("");
                }
            }
        });

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateEntries()) {
                    try {
                        socket = new Socket(txtDestIP.getText(), Server.DEFAULT_PORT);
                        if (file != null) {  //  Sending file
                            FileInputStream fis = new FileInputStream(file);
                            IOUtils.copy(fis, socket.getOutputStream());
                        } else {  //  Sending text
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            bw.write(txtMessage.getText());
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    public boolean validateEntries() {  // Verifies if all entries have been satisfied
        if (txtDestIP.getText().equals("")) {
            JOptionPane.showMessageDialog(panelMain, "Please enter destination IP address.");
            txtDestIP.requestFocus();
            return false;
        } else {
            final Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
            if (!PATTERN.matcher(txtDestIP.getText()).matches()) {
                JOptionPane.showMessageDialog(panelMain, "Could not validate destination IP address.");
                return false;
            }
        }

        if (txtMessage.getText().equals("") && file == null) {
            JOptionPane.showMessageDialog(panelMain, "Please select a file or enter text to be transmitted");
            txtMessage.requestFocus();
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        Thread serverThread = new Thread(new Server());
        serverThread.start();
    }
}
