package com.demo.UI;

import com.demo.utils.FileUploadUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileUploadFrame extends JFrame {

    public FileUploadFrame() {
        setTitle("File Upload Example");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton uploadButton = new JButton("Upload File");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (!selectedFile.getAbsolutePath().endsWith(".jpg")){
                        JOptionPane.showMessageDialog(null, "Please select a .jpg file.");
                        return;
                    }
                    // 处理上传逻辑，可以在这里将selectedFile上传到服务器或者进行其他操作
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    String s = FileUploadUtil.uploadFileToLocal(selectedFile, "upload/");
                }
            }
        });

        JPanel panel = new JPanel();
        panel.add(uploadButton);
        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileUploadFrame();
            }
        });
    }
}
