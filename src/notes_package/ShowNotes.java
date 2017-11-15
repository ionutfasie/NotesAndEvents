package notes_package;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

public class ShowNotes {

	private JFrame frame;	
	private JPanel pnlControl;
	private JPanel pnlNotes;
	private JScrollPane pnlScrollNotes;
	public ShowNotes(){
		buildGui();			//apelare functie de creare frame
	}
	
	public static void main(String[] args){
		new ShowNotes();	//contructor
		
		
	}
	
	//creare framework
	private void buildGui(){
		frame=new JFrame();
		
		frame.setTitle("NoteSiEvenimente");
		frame.setSize(800,800);
		frame.setLayout(new BorderLayout());
		createComponents();
		//adaugare panel
		frame.add(pnlControl,BorderLayout.NORTH);
		frame.add(pnlScrollNotes,BorderLayout.CENTER);
		
		frame.setResizable(false);			
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	//creare componenta
	private void createComponents(){
		pnlControl = new JPanel();
		pnlControl.setLayout(new FlowLayout());
		
		pnlNotes=new JPanel();
		pnlNotes.setLayout(new FlowLayout());
		
		pnlScrollNotes=new JScrollPane(pnlNotes);
		pnlScrollNotes.setPreferredSize(new Dimension(750,750));
		pnlScrollNotes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		//creare butoane
		JButton btnEvents=new JButton("Events");
		JButton btnAdd=new JButton("ADD");
		
		//actiuni butoane
		btnEvents.setActionCommand("GoToEvents");
		btnAdd.setActionCommand("ADD");
		
		//verificare comanda pentru buton
		btnEvents.addActionListener(new MyButtonListener());
		btnAdd.addActionListener(new MyButtonListener());
		
		//creare obiect de tip model pentru a putea apela metodele din Models		
		Models model = new Models();
		
		//adaugare cale
		String sqlSelectNotes = "SELECT id, body FROM Notes;";
		try(Connection conn= model.connect();
				PreparedStatement pstmt = conn.prepareStatement(sqlSelectNotes);
				ResultSet rs= pstmt.executeQuery()){
			//populare panel
			if(rs!=null)
				if(!rs.isBeforeFirst()){
					JLabel noNotes = new JLabel("No notes.. Press ADD");
					pnlNotes.add(noNotes);
					}else{
					while(rs.next()){
				JPanel pnlWrapNotes= new JPanel();   //panel in care tinem notitele
				
				JTextArea textArea= new JTextArea(3,50);
				textArea.setFont(new Font("Helvetica",0, 18));
				textArea.setEnabled(false);
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				textArea.setBackground(Color.GRAY);
				textArea.setForeground(Color.LIGHT_GRAY);
				
				String body= rs.getString("body");
				final int MAX_CHAR= 100;
				if(body.length()<MAX_CHAR){
					textArea.setText(body);			//adaugare text
				}else {
					textArea.setText(body.substring(0, MAX_CHAR)+ "...");			//add maxim 100 de caractere
				}
				final int ID_NOTE = rs.getInt("id");
				textArea.addMouseListener(new MouseAdapter(){
					@Override
					public void mousePressed(MouseEvent e){
						new AddNotes(ID_NOTE);
						
						frame.setVisible(false);
						frame.dispose();
					}
				});
				
				pnlNotes.setPreferredSize(new Dimension(0, 100*pnlNotes.getComponents().length));
				pnlWrapNotes.add(textArea);		//adaugare panel de text in panelul de notite
				pnlNotes.add(pnlWrapNotes);
								
			}
				}
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}
		pnlScrollNotes.repaint();
		pnlScrollNotes.revalidate();
		pnlControl.add(btnEvents);
		pnlControl.add(btnAdd);
		
		
	
	}
	//functie de verificare a comenzii
	private class MyButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			
			if(command.equals("GoToEvents")){
					System.out.println("Events");
			}
			else if(command.equals("ADD")){
				new AddNotes();	
				
				frame.setVisible(false);
				frame.dispose();
				}
			}
	}
	
	
	
}
