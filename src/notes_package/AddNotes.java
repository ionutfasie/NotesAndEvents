package notes_package;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class AddNotes {
	
	private JFrame frame;	
	private JPanel pnlControl;
	private JPanel pnlNoteText;
	private JTextArea textArea=new JTextArea(5,20);
	
	private int id= -1;
	public AddNotes(){
		buildGui();			//apelare functie de creare frame
	}
	
	public AddNotes(int note_id){
		id = note_id;
		buildGui();
	}
	
	private void buildGui(){
		frame=new JFrame();
		
		frame.setTitle("NoteSiEvenimente");
		frame.setSize(800,800);
		frame.setLayout(new BorderLayout());
		createComponents();
		frame.add(pnlControl, BorderLayout.NORTH);
		frame.add(pnlNoteText, BorderLayout.CENTER);
		frame.setResizable(false);			
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	//creare componenta
	private void createComponents(){
		pnlControl = new JPanel();
		pnlControl.setLayout(null);
		//setare dimensiune panel contro(butoane)
		pnlNoteText = new JPanel();
		pnlNoteText.setLayout(new FlowLayout());
		pnlControl.setBounds(0, 0, 800, 50);
		pnlControl.setPreferredSize(new Dimension(800,40));
		//creare butoane
		JButton btnCancel=new JButton("Cancel");
		JButton btnSave=new JButton("Save");
		JButton btnDelete=new JButton("Delete");
		
		//actiuni butoane
		btnCancel.setActionCommand("Cancel");
		btnSave.setActionCommand("Save");
		btnDelete.setActionCommand("Delete");
		
		//verificare comanda pentru buton
		btnCancel.addActionListener(new MyButtonListener());
		btnSave.addActionListener(new MyButtonListener());
		btnDelete.addActionListener(new MyButtonListener());
		
		//dimensiune butoane
		btnCancel.setBounds(15, 5, 100, 25);
		btnSave.setBounds(355, 5, 100, 25);
		btnDelete.setBounds(685, 5, 100, 25);
		textArea=new JTextArea(5,20);
		textArea.setFont(new Font("Helvetica", Font.BOLD, 20));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(true);
		textArea.setFocusable(true);
		
		if (id != -1){
			String sqlSelect = "SELECT body FROM Notes WHERE id = ?";
			try(Connection conn = new Models().connect();
					PreparedStatement stmt = conn.prepareStatement(sqlSelect);
					){
				stmt.setInt(1, id);			
				try(ResultSet rs = stmt.executeQuery()){
					if(rs!=null){
						while(rs.next()){
							textArea.setText(rs.getString("body"));
						}
					}	
				}catch (SQLException ex){
					System.out.println(ex.getMessage());
				}
				
			} catch (SQLException e){
				System.out.print(e.getMessage());
			}
		}
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(700, 730));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		pnlNoteText.add(scrollPane);
		//adaugare butoane
		pnlControl.add(btnCancel);
		pnlControl.add(btnSave);
		if(id != -1){			// daca sunt in add nu arat butonul de delete, nu are sens, il arat doar daca sunt in edit
			pnlControl.add(btnDelete);
		}
	
	}
	//functie de verificare a comenzii
	private class MyButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			
			if(command.equals("Cancel")){
					new ShowNotes();
					
					frame.setVisible(false);
					frame.dispose();
			}
			else if(command.equals("Save")){
				String body = textArea.getText();
				Models model = new Models();
				
				if (id == -1 && !body.equals("")){
					model.createTableNotes();
					model.insertRowNotes(body);
				} else if (!body.equals("")){
					model.updateRowNotes(body, id);
				}
				
				new ShowNotes();
				frame.setVisible(false);
				frame.dispose();
				
				}
			else if (command.equals("Delete")){
				Models model = new Models();
				model.deleteRowNotes(id);
				
				new ShowNotes();
				frame.setVisible(false);
				frame.dispose();
			}
		}
	}
	
	
	
}
