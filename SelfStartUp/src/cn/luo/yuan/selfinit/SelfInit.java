package cn.luo.yuan.selfinit;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SelfInit extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextPane textPane;
    private JTextPane editorPane;

    public SelfInit() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(200, 400);

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
    }

    private void onOK() {
        addToSelfStart();
    }

    private void onCancel() {
        dispose();
    }

    private void removeSelfStart() {
        File startUp = new File(editorPane.getText()+ "\\startup.vbs");
        startUp.deleteOnExit();
    }

    /****重点关注这一段，其他可以忽然****/
    private void addToSelfStart() {//这一段代码是自启动代码范例，你修改一下就可以应用到自己的程序中了
        String selfPath = editorPane.getText();
        File dir = new File(selfPath);
        if (dir.exists()) {
            File bat = new File("startup.bat");

            try {
                if (!bat.exists()) {
                    bat.createNewFile();
                }//创建bat文件用来启动jar程序
                FileWriter writer = new FileWriter(bat);
                writer.write("java -jar " + "SelfStartUp.jar");//这是一个范例，你参考这个方法就可以实现自己的自启动代码了
                writer.flush();
                writer.close();
                writer = new FileWriter(selfPath + "\\startup.vbs");//注册一个脚本到系统的自启动目录下，这个脚本指向之前创建的那个bat文件
                writer.write("createobject(\"wscript.shell\").run \"" + bat.getAbsolutePath() + "\",0 ");
                writer.flush();
                writer.close();
                textPane.setText("注册成功！");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
            textPane.setText("注册失败！请使用管理员模式在此运行程序！");
        } else {
            textPane.setText("注册失败！请修改start的文件夹目录后重试！");
        }
    }

    public static void main(String[] args) {
        SelfInit dialog = new SelfInit();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
