import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by Morton on 1/28/16.
 */
public class Server implements Runnable {
    public final static int DEFAULT_PORT = 10000;

    ServerSocket ss;
    Socket socket;
    int code;
    boolean onSystemExit;
    MainWindow mw;

    public Server(MainWindow mw) {
        try {
            ss = new ServerSocket(Server.DEFAULT_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        onSystemExit = false;
        code = renewCode();
        this.mw = mw;
        mw.updateLblCode(code);
    }

    public int renewCode() {
        Random random = new Random();
        return random.nextInt(9999) + 1;
    }

    public void run() {
        try {
            while (!onSystemExit) {
                socket = ss.accept();
                System.out.println("Incoming transmission from: " +
                        socket.getInetAddress().toString().substring(1));
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF_16"));
                String line = br.readLine();
                String[] params = line.split(" ");
                String code = params[0]; String transmissionType = params[1];
                if (Integer.parseInt(code) == this.code) {
                    if (transmissionType.equals("TEXT")) {
                        line = br.readLine();
                        MessageDialog md = new MessageDialog(line);
                        System.out.println("Message received: " + line);
                        md.setVisible(true);
                    } else if (transmissionType.equals("FILE")) {
                        line = br.readLine();
                        String[] fileParams = line.split(" ");
                        int option = JOptionPane.showConfirmDialog(mw.getPanel(),
                                "Incoming file. Do you want to save it?\nFilename: " + fileParams[0] +
                                        "\nSize: " + (Math.round(Double.parseDouble(fileParams[1]) / 1024.0 * 100) / 100.0) + " KB", "Incoming", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            JFileChooser fc = new JFileChooser();
                            fc.setSelectedFile(new File(fileParams[0]));
                            int fileOption = fc.showSaveDialog(mw.getPanel());
                            boolean confirmDiscard = false;

                            while (fileOption != JFileChooser.APPROVE_OPTION || confirmDiscard) {
                                int confirmOption = JOptionPane.showConfirmDialog(mw.getPanel(), "You haven't selected a file yet, do you want to discard it?",
                                        "Confirm File Discard", JOptionPane.WARNING_MESSAGE);
                                if (confirmOption == JOptionPane.YES_OPTION) confirmDiscard = true;
                            }
                            if (!confirmDiscard) {
                                File file = fc.getSelectedFile();
                                FileOutputStream fos = new FileOutputStream(file);
                                IOUtils.copy(socket.getInputStream(), fos);
                                JOptionPane.showMessageDialog(mw.getPanel(), "File written to file system.");
                                System.out.println("File written to file system");
                                fos.close();
                            }
                        }
                    }
                } else {
                    System.out.println("Code mismatch. File/text discarded.");
                }
                this.code = renewCode();
                this.mw.updateLblCode(this.code);
            }
        } catch (InterruptedIOException e1) {
            onSystemExit = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
