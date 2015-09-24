package com.alibaba.lvdebug.ui;

import java.awt.BorderLayout;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JTabbedPane;
import com.alibaba.lvdebug.Center;
import com.alibaba.lvdebug.ClientCmd;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;

public class DebuggerFrame extends JFrame {

	private static final long serialVersionUID = -4760175577170083855L;

	private JPanel contentPane;
	private JTextField textFieldCmdInput;
	private final Center center;

	/**
	 * Create the frame.
	 */
	public DebuggerFrame(final Center c) {
		this.center = c;
		setTitle("Lua调试器 V1.0.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 750);
		contentPane = new JPanel();
		// contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panelHead = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelHead.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.add(panelHead, BorderLayout.NORTH);

		textFieldCmdInput = new JTextField();
		textFieldCmdInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				center.cmdBuffer.pushCmd(new ClientCmd(textFieldCmdInput.getText().trim()));
			}
		});
		panelHead.add(textFieldCmdInput);
		textFieldCmdInput.setColumns(15);

		buttonNextLine = new JButton("下一行");
		buttonNextLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				center.srcCodeCenter.clearGotoLine();
				setIsDebugging(false);
				center.cmdBuffer.pushCmd(new ClientCmd("n"));
			}
		});
		try {
			Image img = ImageIO.read(getClass().getResource("play.png"));
			Image img2 = ImageIO.read(getClass().getResource("play2.png"));
			buttonNextBreakPoint = new ImageButton(new ImageIcon(img), new ImageIcon(img2));
			buttonNextBreakPoint.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					center.srcCodeCenter.clearGotoLine();
					setIsDebugging(false);
					center.cmdBuffer.pushCmd(new ClientCmd("c"));
				}
			});
		} catch (Exception ex) {
		}

		label = new JLabel("  ");
		panelHead.add(label);
		panelHead.add(buttonNextBreakPoint);
		panelHead.add(buttonNextLine);

		buttonNextCodeLine = new JButton("下一步");
		buttonNextCodeLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				center.srcCodeCenter.clearGotoLine();
				setIsDebugging(false);
				center.cmdBuffer.pushCmd(new ClientCmd("s"));
			}
		});
		panelHead.add(buttonNextCodeLine);

		buttonCallStack = new JButton("显示调用栈");
		buttonCallStack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				center.cmdBuffer.pushCmd(new ClientCmd("bt"));
			}
		});
		panelHead.add(buttonCallStack);

		JPanel panelBody = new JPanel();
		panel.add(panelBody, BorderLayout.CENTER);
		panelBody.setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();

		// splitPane.setBorderPainted(false);// 不打印边框
		splitPane.setBorder(null);// 除去边框
		splitPane.setResizeWeight(0.8);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelBody.add(splitPane, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		splitPane.setRightComponent(scrollPane);

		outputArea = new JTextArea();
		outputArea.setLineWrap(true);
		scrollPane.setViewportView(outputArea);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(null);
		splitPane.setLeftComponent(tabbedPane);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				center.worker.close();
			}
		});
	}

	private JTabbedPane tabbedPane;
	private JTextArea outputArea;
	private JButton buttonNextLine;
	private JButton buttonNextCodeLine;
	private JButton buttonNextBreakPoint;
	private JButton buttonCallStack;
	private JLabel label;

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public JTextArea getOutputArea() {
		return outputArea;
	}

	public JTextField getTextFieldCmdInput() {
		return textFieldCmdInput;
	}

	public JButton getButtonNextLine() {
		return buttonNextLine;
	}

	public JButton getButtonNextCodeLine() {
		return buttonNextCodeLine;
	}

	public JButton getButtonNextBreakPoint() {
		return buttonNextBreakPoint;
	}

	public void setIsDebugging(boolean yes) {
		this.getButtonNextLine().setEnabled(yes);
		this.getButtonNextCodeLine().setEnabled(yes);
		this.getButtonNextBreakPoint().setEnabled(yes);
		this.getTextFieldCmdInput().setEnabled(yes);
		this.buttonCallStack.setEnabled(yes);
	}
}
