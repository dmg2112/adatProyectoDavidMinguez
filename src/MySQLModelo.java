import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.table.DefaultTableModel;

public class MySQLModelo {
	private HashMap<Integer, Disco> listabbdd = new HashMap<Integer, Disco>();
	private String url;
	private String usr;
	private String pwd;
	private String driver;
	private Connection conexion;
	private Vista miVista;
	private int idBBDD;
	private String sqlTabla = "select * from inventario";
	private DefaultTableModel miModelo;

	public MySQLModelo() {
		Properties propiedades = new Properties();
		InputStream entrada = null;
		try {
			File miFichero = new File("src/configuracionDavid.ini");
			if (miFichero.exists()) {
				entrada = new FileInputStream(miFichero);
				// cargamos el fichero de configuraci�n
				propiedades.load(entrada);
				/*
				 * obtenemos las propiedades y las almecenamos en variables para su posterior
				 * utilizacion en la conexion con la bd
				 */
				this.url = propiedades.getProperty("url");
				this.usr = propiedades.getProperty("usuario");
				this.pwd = propiedades.getProperty("clave");
				this.driver = propiedades.getProperty("driver");
				// Query necesaria para hacer login

				String sqlTabla = "SELECT * FROM inventario";
				try {

					Class.forName(driver);

					conexion = DriverManager.getConnection(url, usr, pwd);
				} catch (Exception e) {
					e.printStackTrace();
					miVista.alertBBDD();

					
				}

			} else
				System.err.println("Fichero no encontrado");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (entrada != null) {
				try {
					entrada.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void cargarBBDD() {
		if (listabbdd.isEmpty()) {
			listabbdd = new HashMap<Integer, Disco>();
			int numColumnas = getNumColumnas(sqlTabla);
			Object[] contenido = new Object[numColumnas];
			PreparedStatement pstmt;
			try {
				pstmt = conexion.prepareStatement(sqlTabla);
				ResultSet rset = pstmt.executeQuery();
				while (rset.next()) {
					listabbdd.put(rset.getInt(1), new Disco(rset.getInt(1), rset.getString(2), rset.getString(3),
							rset.getString(4), rset.getString(5)));

				}

				pstmt.close();
				rset.close();

			} catch (

			SQLException e) {
				e.printStackTrace();
			}

		}

		actualizaTablaBBDD();
		miVista.esBBDD();

	}

	private int getNewIdBBDD() {
		getMaxIdBBDD();
		idBBDD += 1;
		return idBBDD;

	}

	private void getMaxIdBBDD() {
		try {
			PreparedStatement pstmt = conexion.prepareStatement("Select Max(id) from inventario");

			ResultSet rset = pstmt.executeQuery();

			while (rset.next()) {
				idBBDD = rset.getInt(1);

			}
			rset.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private int getNumColumnas(String sql) {
		int num = 0;
		try {
			PreparedStatement pstmt = conexion.prepareStatement(sql);

			ResultSet rset = pstmt.executeQuery();
			ResultSetMetaData rsmd = rset.getMetaData();
			num = rsmd.getColumnCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}

	public void actualizaTablaBBDD() {
		miModelo = new DefaultTableModel();
		int numColumnas = getNumColumnas(sqlTabla);
		Object[] contenido = new Object[numColumnas];
		PreparedStatement pstmt;
		try {
			pstmt = conexion.prepareStatement(sqlTabla);
			ResultSet rset = pstmt.executeQuery();
			ResultSetMetaData rsmd = rset.getMetaData();
			for (int i = 0; i < numColumnas; i++) {
				miModelo.addColumn(rsmd.getColumnName(i + 1));
			}
			Iterator it = listabbdd.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				contenido[0] = pair.getKey();
				Disco tmp = (Disco) pair.getValue();
				contenido[1] = tmp.getNombre();
				contenido[2] = tmp.getArtista();
				contenido[3] = tmp.getYear();
				contenido[4] = tmp.getGenero();
				miModelo.addRow(contenido);

			}

			pstmt.close();
			rset.close();

		} catch (

		SQLException e) {
			e.printStackTrace();
		}
		this.miVista.setTabla(miModelo);
	}

	private boolean bbddContiene(Disco tmp) {
		try {
			PreparedStatement pstmt = conexion
					.prepareStatement("select * from inventario where nombre = ? and artista = ?");
			pstmt.setString(1, tmp.getNombre());
			pstmt.setString(2, tmp.getArtista());
			System.out.println(pstmt.executeQuery().first());

			return pstmt.executeQuery().first();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public void guardarBase(Disco disco) {
		try {
			PreparedStatement pstmt = conexion.prepareStatement(
					"INSERT INTO `inventario` (`Nombre`, `Artista`, `fecha`, `Genero`) VALUES (?,?,?,?);");

			pstmt.setString(1, disco.getNombre());
			pstmt.setString(2, disco.getArtista());
			pstmt.setString(3, disco.getYear());
			pstmt.setString(4, disco.getYear());
			pstmt.executeUpdate();
			int id = getNewIdBBDD();
			disco.setId(id);
			listabbdd.put(id, disco );

		} catch (SQLException e) {
			e.printStackTrace();
		}
		actualizaTablaBBDD();

	}

	public void editaBBDD(Disco disco) {
		if (!bbddContiene(disco)) {
			System.out.println("entro en edicion de bbdd");
			try {
				PreparedStatement pstmt = conexion.prepareStatement(
						"UPDATE `inventario` SET nombre = ?,artista = ?,fecha=?,genero= ? WHERE id = ?;");
				pstmt.setString(1, disco.getNombre());
				pstmt.setString(2, disco.getArtista());
				pstmt.setString(3, disco.getYear());
				pstmt.setString(4, disco.getGenero());
				pstmt.setInt(5, disco.getId());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			listabbdd.put(disco.getId(), disco);
			actualizaTablaBBDD();
		}

	}

	public void borraBBDD() {

		try {
			PreparedStatement pstmt = conexion.prepareStatement("delete from inventario;");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listabbdd.clear();
		actualizaTablaBBDD();

	}

	public void borraBBDD(int id) {

		try {
			PreparedStatement pstmt = conexion.prepareStatement("delete from inventario where id = ?;");

			pstmt.setInt(1, id);

			pstmt.executeUpdate();
			listabbdd.remove(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		actualizaTablaBBDD();

	}

	public void aBBDD(HashMap<Integer,Disco> lista) {

		Iterator it = lista.entrySet().iterator();

		while (it.hasNext()) {

			Map.Entry pair = (Map.Entry) it.next();
			Disco tmp = (Disco) pair.getValue();

			try {

				if (!bbddContiene(tmp)) {
					PreparedStatement pstmt2 = conexion.prepareStatement(
							"INSERT INTO `inventario` (Nombre, Artista, fecha, Genero) VALUES (?,?,?,?);");

					pstmt2.setString(1, tmp.getNombre());
					pstmt2.setString(2, tmp.getArtista());
					pstmt2.setString(3, tmp.getYear());
					pstmt2.setString(4, tmp.getGenero());
					int rset2 = pstmt2.executeUpdate();

					pstmt2.close();

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		cargarBBDD();
		actualizaTablaBBDD();

	}

	public void setVista(Vista v) {
		this.miVista=v;
		
	}
	public HashMap<Integer,Disco> getBBDD(){
		return listabbdd;
	}

	

}
