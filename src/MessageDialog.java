import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;

public class MessageDialog extends JDialog {
    private JPanel contentPane;
    private JButton btnOK;
    private JButton btnCancel;
    private JTextArea txtMessage;
    private JButton btnCopy;

    public MessageDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnOK);

        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        btnCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(txtMessage.getText()), null);
                btnCopy.setText("Copied!");
            }
        });


// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        setSize(new Dimension(300, 200));
    }

    public MessageDialog(String message) {
        this();
        txtMessage.setText(message);
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        MessageDialog dialog = new MessageDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
