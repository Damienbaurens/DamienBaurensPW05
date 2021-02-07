package fr.isen.java2.db.daos;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.isen.java2.db.entities.Genre;

public class GenreDao {
	
	DataSource dataSource = DataSourceFactory.getDataSource();
	
	public List<Genre> listGenres(){
		// Création de connection
		
		List<Genre> genreList = new ArrayList<>() ;
		String resultName;
		int resultID;
		try(Connection connection = dataSource.getConnection()) 
		{
			//Création du Statement
			try(Statement statement = connection.createStatement())
			{
				//Query
				String sqlQuery = "SELECT * FROM genre";
				try (ResultSet resultSet = statement.executeQuery(sqlQuery)){
					while(resultSet.next()) 
					{
						resultName =  resultSet.getString("name");
						resultID = resultSet.getInt("idgenre");
						Genre genre = new Genre(resultID,resultName);
						
						genreList.add(genre);
					}	
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
				

			}		
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return genreList;
	}

	
	public Genre getGenre(String name){
		Genre genre = new Genre();
		
		try(Connection connection = dataSource.getConnection()) 
		{
			//Query
			String sqlQuery = "SELECT * FROM genre WHERE name = ?";
			
			//Création du Statement
			try(PreparedStatement statement = connection.prepareStatement(sqlQuery))
			{
				//attribution du paramètre au statement
				statement.setString(1, name);
				
				//stockage du résultat
				try(ResultSet resultSet = statement.executeQuery())
				{
					if(! resultSet.isClosed()) 
					{
						genre.setName(resultSet.getString("name"));
						genre.setId(resultSet.getInt("idgenre"));
					}
					  
				}
				
				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Si la variable retournée a des valeurs, on la retourne, sinon on renvoie un null
		if(genre.getId() != null)
		{
			return genre;
		}
		else
		{
			return null;
		}
	}
	
	

	public void addGenre(String name) 
	{
		int nbRows;
		
		try(Connection connection = dataSource.getConnection()) 
		{
			//Query
			String sqlQuery = "INSERT INTO genre(name) VALUES(?)";
			
			//Création du Statement
			try(PreparedStatement statement = connection.prepareStatement(sqlQuery))
			{
				//attribution du paramètre au statement
				statement.setString(1, name);
				//exécution de la requète
				nbRows = statement.executeUpdate();
				System.out.println(String.format("%d row(s) have been modified",nbRows));
			}
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
