import java.util.Objects;

public class Disco {
	private String nombre;
	private String artista;
	private String year;
	private String genero;
	private int id;
	
	
	public Disco(int id, String nombre, String artista, String year, String genero ) {
		setNombre(nombre);
		setArtista(artista);
		setGenero(genero);
		setYear(year);
		setId(id);
		
		
	}
	public Disco(String nombre, String artista, String year, String genero ) {
		setNombre(nombre);
		setArtista(artista);
		setGenero(genero);
		setYear(year);
		
		
		
	}
	public Disco() {
		
	}
	@Override
	public String toString() {
		return this.getNombre() + ";" + this.getArtista() + ";"
		+ this.getYear() + ";" + this.getGenero() ;
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return Objects.hash(nombre,artista,year,genero);
	}

	public String getArtista() {
		return artista;
	}

	public void setArtista(String artista) {
		this.artista = artista;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	

}
