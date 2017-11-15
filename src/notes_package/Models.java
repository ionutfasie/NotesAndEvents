package notes_package;

import java.sql.*;

public class Models {
//conexiunea la baza de date
	public Connection connect(){
		Connection conn = null;
		
		try{
			String url = "jdbc:sqlite:/D:/Eclipse_Projects/First_Project/src/NotesAndEventsDB.db";
			conn = DriverManager.getConnection(url);
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		
		return conn;	
	}
	//craere tabel + exceptii
	/*public void createTableNotesLong(){
		
		String sqlCreate ="CREATE TABLE Notes(id integer PRIMARY KEY, \n"
						+ "body text NOT NULL);";
		
		Connection conn=null;
		Statement stmt=null;
		
		try{
			conn = this.connect();
			stmt = conn.createStatement();
			stmt.executeQuery(sqlCreate);			// scrie intr-un script sqlCreate.
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}finally{
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}	
		
	}
	public static void createNewDatabase(){
		String url ="jdbc:sqlite:/D:/Eclipse_Projects/First_Project/src/NotesAndEventsDB.db";
		
		try(Connection conn= this.connect()){
			if(conn!=null)
				DatabaseMetaData meta = conn.getMetaData();
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}
	}
	*/
	
	public void createTableNotes(){
		String sqlCreate ="CREATE TABLE IF NOT EXISTS Notes(id integer PRIMARY KEY, \n"
				+ "body text NOT NULL);";

		try(Connection conn= this.connect();
				Statement stmt = conn.createStatement()){
					stmt.execute(sqlCreate);
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
	}
	
	public void insertRowNotes(String body){
		String sqlInsert = "INSERT INTO Notes(body) VALUES (?)";
		try(Connection conn= this.connect(); 
				PreparedStatement pstmt = conn.prepareStatement(sqlInsert)){
			pstmt.setString(1, body);
			pstmt.executeUpdate();
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}		
	}
	
	public void updateRowNotes(String body, int id){
		String sqlUpdate = "UPDATE Notes SET body = ? WHERE id = ?";
		try(Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)){
			pstmt.setString(1, body);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
	}
	
	public void deleteRowNotes(int id){
		String sqlDelete = "DELETE FROM Notes WHERE id=?";
		try(Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sqlDelete)){
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}
	}
	
	
	
}
