package com; 

import java.awt.BorderLayout; 
import java.awt.Color; 
import java.awt.Dimension; 
import java.awt.Font; 
import java.awt.Graphics; 
import java.awt.Graphics2D; 
import java.awt.GridLayout; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.awt.event.KeyEvent; 
import java.awt.event.WindowAdapter; 
import java.awt.event.WindowEvent; 
import java.awt.print.PageFormat; 
import java.awt.print.PrinterException; 
import java.io.BufferedReader; 
import java.io.File; 
import java.io.FileReader; 
import java.io.FileWriter; 
import java.io.PrintWriter; 
import java.text.MessageFormat; 

import javax.swing.BorderFactory; 
import javax.swing.BoxLayout; 
import javax.swing.JButton; 
import javax.swing.JComboBox; 
import javax.swing.JFileChooser; 
import javax.swing.JFrame; 
import javax.swing.JLabel; 
import javax.swing.JList; 
import javax.swing.JMenu; 
import javax.swing.JMenuBar; 
import javax.swing.JMenuItem; 
import javax.swing.JOptionPane; 
import javax.swing.JPanel; 
import javax.swing.JScrollPane; 
import javax.swing.JSplitPane; 
import javax.swing.JTable; 
import javax.swing.JTextArea; 
import javax.swing.JTextField; 
import javax.swing.JTree; 
import javax.swing.ListSelectionModel; 
import javax.swing.ScrollPaneConstants; 
import javax.swing.event.ListSelectionEvent; 
import javax.swing.event.ListSelectionListener; 
import javax.swing.event.UndoableEditEvent; 
import javax.swing.event.UndoableEditListener; 
import javax.swing.tree.DefaultMutableTreeNode; 
import javax.swing.undo.CannotRedoException; 
import javax.swing.undo.UndoManager; 

import com.sun.java_cup.internal.runtime.*; 
import com.sun.*;

public class ListSelectionDemo extends JPanel 
{ 
JTextArea output; 
String temp; 
String newline = "\n"; 
private static final long serialVersionUID = 1L; 
static JFrame frame; 
JList list; 
ListSelectionModel listSelectionModel; 
JButton b; 
JLabel l; 
JTextField f; 
File file; 
File[] listData; 
JSplitPane splitPane; 
JSplitPane splitPanel; 
JTree tree; 
String ss; 
static UndoManager undoManager = new UndoManager(); 

static JTextField jl; 
static JButton savef; 
static JFrame fr; 
Font font; 
final static JMenuItem undo=new JMenuItem("Undo"); 
private static final int PAGE_EXISTS = 0; 
private static final int NO_SUCH_PAGE = 0; 
final JMenuItem open=new JMenuItem("Open"); 
final JMenuItem save=new JMenuItem("save"); 
final JMenuItem exit=new JMenuItem("Exit"); 
final JMenuItem New=new JMenuItem("New"); 
final JMenuItem saveas=new JMenuItem("SaveAs"); 

final JMenuItem copy=new JMenuItem("Copy"); 
final JMenuItem paste=new JMenuItem("Paste"); 
final JMenuItem cut=new JMenuItem("Cut"); 
final JMenuItem print=new JMenuItem("Print"); 
public ListSelectionDemo() 
{ 
super(new BorderLayout()); 

JMenu file1 = new JMenu("File"); 

open.addActionListener(new ItemListener()); 
saveas.addActionListener(new ItemListener()); 
New.addActionListener(new ItemListener()); 
save.addActionListener(new ItemListener()); 
exit.addActionListener(new ItemListener()); 
print.addActionListener(new ItemListener()); 
file1.setMnemonic(KeyEvent.VK_F); 
file1.add(New); 
file1.add(open); 
file1.addSeparator( ); 
file1.add(save); 
file1.add(saveas); 
JMenu sentTo=new JMenu("Sent To"); 
sentTo.add(new JMenuItem("Desktop")); 
file1.add(sentTo); 
file1.add(print); 
file1.add(exit); 


JMenu edit= new JMenu("Edit"); 
edit.setMnemonic(KeyEvent.VK_E); 
edit.add(undo); 
undo.addActionListener(new ItemListener()); 
edit.addSeparator(); 
edit.add(cut); 
cut.addActionListener(new ItemListener()); 
edit.add(copy); 
copy.addActionListener(new ItemListener()); 
edit.add(paste); 
paste.addActionListener(new ItemListener()); 

JMenuBar menuBar = new JMenuBar( ); 
menuBar.add(file1); 
menuBar.add(edit); 
frame.setJMenuBar(menuBar); 
final JPanel controlPane = new JPanel(); 
l=new JLabel("SEARCH"); 
f=new JTextField(7); 
b=new JButton("GO"); 
controlPane.add(l); 
controlPane.add(f); 
controlPane.add(b); 

output = new JTextArea(1, 10); 
output.setEditable(true); 
JScrollPane outputPane = new JScrollPane(output, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
output.getDocument().addUndoableEditListener( 
new UndoableEditListener() { 
public void undoableEditHappened(UndoableEditEvent e) { 
undoManager.addEdit(e.getEdit()); 
updateButtons(); 
} 
}); 
font = new Font("Verdana", Font.BOLD, 12); 
output.setFont(font); 
output.setBackground(Color.LIGHT_GRAY); 
output.setForeground(Color.BLACK); 

//Bottom Panel 
JScrollPane listPane = new JScrollPane(); 
JPanel listContainer = new JPanel(new GridLayout(1,1)); 
listContainer.setBorder(BorderFactory.createTitledBorder("List")); 

listContainer.add(listPane); 

//Left Panel 

JScrollPane leftPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
java.util.Properties p = System.getProperties(); 
tree = new JTree(p); 
tree.setRootVisible(true); 
leftPane = new JScrollPane(tree); 

//Top panel 
final JPanel bottomHalf = new JPanel(new BorderLayout()); 
bottomHalf.add(controlPane, BorderLayout.PAGE_START); 
bottomHalf.add(outputPane, BorderLayout.CENTER); 
bottomHalf.setPreferredSize(new Dimension(1020, 600)); 
splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT); 
splitPane.setTopComponent(bottomHalf); 
splitPane.setBottomComponent(listContainer); 
splitPane.setOneTouchExpandable(true); 
splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); 
splitPanel.setLeftComponent(leftPane); 
splitPanel.setRightComponent(splitPane); 
add(splitPanel, BorderLayout.WEST); 
splitPanel.setOneTouchExpandable(true); 


b.setActionCommand("search"); 

b.addActionListener(new ActionListener() 
{ 
public void actionPerformed(ActionEvent e) 
{ 
String s=b.getActionCommand(); 
if(s.equals("search")) 
{ 
String tf=f.getText(); 
System.out.println(tf); 
if(tf.equals("")) 
{ 
output.setText(""); 
output.setEditable(false); 

JPanel listContainer = new JPanel(new GridLayout(1,1)); 
listContainer.setBorder(BorderFactory.createTitledBorder("RESULT")); 
listContainer.add(new JLabel("PATH NOT FOUND")); 

splitPane.setBottomComponent(listContainer); 
} 
else 
{ 
file = new File(tf); 
listData = file.listFiles(); 
list = new JList(listData); 

listSelectionModel = list.getSelectionModel(); 
listSelectionModel.addListSelectionListener(new SharedListSelectionHandler()); 
JScrollPane listPane1 = new JScrollPane(list); 

JPanel listContainer1 = new JPanel(new GridLayout(1,1)); 
listContainer1.setBackground(Color.LIGHT_GRAY); 
listContainer1.setBorder(BorderFactory.createTitledBorder("List")); 
listContainer1.add(listPane1); 
output.setEditable(true); 
splitPane.setBottomComponent(listContainer1); 

} 

} 
} 
} 
); 

String[] modes = { "Option-1", 
"Option-2", 
"Option-3" }; 

final JComboBox comboBox = new JComboBox(modes); 
comboBox.setSelectedIndex(2); 
comboBox.addActionListener(new ActionListener() 
{ 
public void actionPerformed(ActionEvent e) 
{ 
String newMode = (String)comboBox.getSelectedItem(); 
if (newMode.equals("Option-1")) 
{ 

} 
else if (newMode.equals("Option-2")) 
{ 

} 
else 
{ 

} 
output.append("----------"+ "Mode: " + newMode + "----------" + newline); 
} 
} 
); 

controlPane.add(new JLabel("Selection mode:")); 
controlPane.add(comboBox); 
} 


private static void createAndShowGUI() 
{ 
frame =new JFrame("ListSelectionDemo"); 
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
ListSelectionDemo demo = new ListSelectionDemo(); 
demo.setOpaque(true); 
frame.setContentPane(demo); 
frame.pack(); 
frame.setVisible(true); 
} 


public static void updateButtons() { 
undo.setText(undoManager.getUndoPresentationName()); 
// redoButton.setText(undoManager.getRedoPresentationName()); 
undo.setEnabled(undoManager.canUndo()); 
// redoButton.setEnabled(undoManager.canRedo()); 
} 

public static void func() 
{ 
fr=new JFrame("SAVE FILE"); 
JPanel p=new JPanel(); 
JPanel save=new JPanel(); 

p.add(new JLabel("Enter Path:")); 
jl=new JTextField(9); 
p.add(jl); 
savef=new JButton("Save File"); 
save.add(savef); 
fr.add(p,BorderLayout.CENTER); 
fr.add(save,BorderLayout.SOUTH); 
fr.setBounds(500, 250,200,150); 
//fr.pack(); 
fr.setVisible(true); 
} 


public static void main(String[] args) 
{ 
javax.swing.SwingUtilities.invokeLater(new Runnable() 
{ 
public void run() 
{ 
createAndShowGUI(); 
} 
ListSelectionDemo d=new ListSelectionDemo();
} 
); 
} 


public class ItemListener implements ActionListener { 

public void actionPerformed(ActionEvent e) { 

JMenuItem s = (JMenuItem) e.getSource(); 

if(s.equals(open)) 
{ 
JFileChooser fc= new JFileChooser(); 
if(fc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) 
{ 
File file= fc.getSelectedFile(); 
ss=file.toString(); 
BufferedReader inputStream = null; 
try 
{ 

output.setEditable(true); 
output.setText(""); 
inputStream =new BufferedReader(new FileReader(file)); 

String l; 
while ((l = inputStream.readLine()) != null) 
{ 
output.append(l); 
output.append(newline); 
} 

int pos=output.getCaretPosition(); 
System.out.println(pos); 
} 
catch(Exception e1) 
{ 
System.out.println(e1); 
} 

} 
} 

else if(s.equals(save)) 
{ 

try 
{ 
FileWriter first = new FileWriter(new File(ss)); 
first.write(output.getText()); 
first.close(); 

} 
catch(Exception e1) 
{ 
System.out.println(e1); 
} 
} 

else if(s.equals(saveas)) 
{ 
func(); 

savef.setActionCommand("sfile"); 
savef.addActionListener( 
new ActionListener() 
{ 
public void actionPerformed(ActionEvent e) 
{ 

String s=savef.getActionCommand(); 

if(s.equals("sfile")) 
{ 
String field=jl.getText(); 

try 
{ 

System.out.println(field); 
File fil=new File(field); 

if(!fil.exists()) 
{ 

FileWriter first = new FileWriter(new File(field)); 
first.write(output.getText()); 
first.close(); 
fr.dispose(); 
} 
else 
{ 

jl.selectAll(); 
jl.requestFocus(); 
JOptionPane.showMessageDialog(fr, "File Exist", "ALERT", 0); 

} 

} 
catch(Exception e1) 
{ 
System.out.println(e1); 
} 

} 

} 
} 
); 

} 

else if(s.equals(New)) 
{ 

output.setText(""); 
} 

else if(s.equals(exit)) 
{ 

System.exit(0); 
} 

else if(s.equals(undo)) 
{ 
try { 
undoManager.undo(); 
} catch (CannotRedoException cre) { 
cre.printStackTrace(); 
} 
updateButtons(); 
} 

else if(s.equals(cut)) 
{ 
temp= output.getSelectedText(); 
output.replaceSelection(""); 
} 
else if(s.equals(copy)) 
{ 
temp= output.getSelectedText(); 
} 
else if(s.equals(paste)) 
{ 
output.append(temp); 
} 

else if(s.equals(print)) 
{ 
try{ 
output.print(); 
} catch(Exception excp){} 

} 
} 

} 

public int print(Graphics g, PageFormat pf, int page) throws PrinterException{ 
if (page > 0) 
{ 
return NO_SUCH_PAGE; 
} 

Graphics2D g2d = (Graphics2D)g; 
g2d.translate(pf.getImageableX(), pf.getImageableY()); 
frame.printAll(g); 
return PAGE_EXISTS; 
} 



class SharedListSelectionHandler implements ListSelectionListener 
{ 
public void valueChanged(ListSelectionEvent e) 
{ 
ss=list.getSelectedValue().toString(); 

BufferedReader inputStream = null; 
try 
{ 

output.setText(""); 
inputStream =new BufferedReader(new FileReader(ss)); 

String l; 
while ((l = inputStream.readLine()) != null) 
{ 
output.append(l); 
output.append(newline); 
} 

} 
catch(Exception e1) 
{ 
System.out.println(e1); 
} 
} 
} 
}

