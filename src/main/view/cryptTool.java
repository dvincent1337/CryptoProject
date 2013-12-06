package main.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

import main.model.CryptoNet;

import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.TextArea;

public class cryptTool extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					cryptTool frame = new cryptTool();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public cryptTool() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 909, 354);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(49, 64, 139, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Encrypt");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				encryptBtnPress();
			}
		});
		btnNewButton.setBounds(49, 99, 139, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Decrypt");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				decryptBtnPress();
			}
		});
		btnNewButton_1.setBounds(49, 133, 139, 23);
		contentPane.add(btnNewButton_1);
		
		JLabel lblKey = new JLabel("Key");
		lblKey.setBounds(49, 43, 46, 14);
		contentPane.add(lblKey);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setBounds(241, 57, 603, 234);
		contentPane.add(textArea);
	}
	
	public void encryptBtnPress()
	{
		textArea.setText(CryptoNet.polyEncrypt(textArea.getText(),textField.getText()));
	}
	public void decryptBtnPress()
	{
		textArea.setText(CryptoNet.polyDecrypt(textArea.getText(),textField.getText()));
	}
}
