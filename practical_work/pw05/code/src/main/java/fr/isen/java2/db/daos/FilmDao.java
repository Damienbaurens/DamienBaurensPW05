package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import fr.isen.java2.db.entities.Film;
import fr.isen.java2.db.entities.Genre;


public class FilmDao {
	
	DataSource dataSource = DataSourceFactory.getDataSource();
	
	public List<Film> listFilms() {
		// Création de connection
		List<Film> filmList = new ArrayList<>() ;
		Integer iD;
		String title;
		LocalDate releaseDate;
		Genre genre = new Genre();
		Integer duration;
		String director;
		String summary;
		
		try(Connection connection = dataSource.getConnection()) 
		{
			//Création du Statement
			try(Statement statement = connection.createStatement())
			{
				//Query
				String sqlQuery = "SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre";
				try(ResultSet resultSet = statement.executeQuery(sqlQuery))
				{
					while(resultSet.next()) 
					{
						
						iD = resultSet.getInt("idfilm");
						title =  resultSet.getString("title");
						releaseDate = resultSet.getDate("release_date").toLocalDate();
						genre.setId(resultSet.getInt("idgenre"));
						genre.setName(resultSet.getString("name"));
						duration = resultSet.getInt("duration");
						director = resultSet.getString("director");
						summary = resultSet.getString("summary");
						
						Film Film = new Film(iD,title,releaseDate,genre,duration,director,summary);
						
						filmList.add(Film);
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
		
		return filmList;
	}

	
	public List<Film> listFilmsByGenre(String filmGenre)
	{
		
		List<Film> filmList = new ArrayList<>();
		Integer iD;
		String title;
		LocalDate releaseDate;
		Genre genre = new Genre();
		Integer duration;
		String director;
		String summary;
		
		try(Connection connection = dataSource.getConnection()) 
		{
			//Query
			String sqlQuery = "SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre WHERE genre.name = ? ";
			
			//Création du Statement
			try(PreparedStatement statement = connection.prepareStatement(sqlQuery))
			{
				//attribution du paramètre au statement
				statement.setString(1, filmGenre);
				
				//stockage du résultat
				try(ResultSet resultSet = statement.executeQuery())
				{
					while(resultSet.next()) 
					{
						iD = resultSet.getInt("idfilm");
						title =  resultSet.getString("title");
						releaseDate = resultSet.getDate("release_date").toLocalDate();
						genre.setId(resultSet.getInt("idgenre"));
						genre.setName(resultSet.getString("name"));
						duration = resultSet.getInt("duration");
						director = resultSet.getString("director");
						summary = resultSet.getString("summary");
						
						Film Film = new Film(iD,title,releaseDate,genre,duration,director,summary);
						
						filmList.add(Film);
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
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return filmList;
		
	}

	public Film addFilm(Film film) {
		
		Film addedFilm = new Film();
		Genre genre = new Genre();		
		try(Connection connection = dataSource.getConnection()) 
		{
			//Query
			String sqlQuery = "INSERT INTO film(title,release_date,genre_id,duration,director,summary) VALUES(?,?,?,?,?,?)";
			
			//Création du Statement
			try(PreparedStatement statement = connection.prepareStatement(sqlQuery))
			{
				//attribution du paramètre au statement
				statement.setString(1, film.getTitle());
				statement.setDate(2, Date.valueOf(film.getReleaseDate()));
				statement.setInt(3, film.getGenre().getId());
				statement.setInt(4, film.getDuration());
				statement.setString(5, film.getDirector());
				statement.setString(6, film.getSummary());
				
				
				//exécution de la requète
				int nbRows = statement.executeUpdate();
				System.out.println(String.format("%d row(s) have been modified",nbRows));
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			
			//préparation de la variable retournée 
			
			sqlQuery = " SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre WHERE film.title = ?";
			try(PreparedStatement statement = connection.prepareStatement(sqlQuery))
			{
				statement.setString(1, film.getTitle());
				try(ResultSet resultSet = statement.executeQuery())
				{
				
						addedFilm.setId(resultSet.getInt("idfilm"));
						addedFilm.setTitle( resultSet.getString("title"));
						addedFilm.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
						genre.setId(resultSet.getInt("idgenre"));
						genre.setName(resultSet.getString("name"));
						addedFilm.setGenre(genre);
						addedFilm.setDuration(resultSet.getInt("duration"));
						addedFilm.setDirector(resultSet.getString("director"));
						addedFilm.setSummary(resultSet.getString("summary"));						
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
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return addedFilm;
	}
	
}
