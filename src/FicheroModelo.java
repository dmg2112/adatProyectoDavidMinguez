import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.DefaultTableModel;

public class FicheroModelo {
	private HashMap<Integer, Disco> listaFichero = new HashMap<Integer,Disco>();
	private int idFichero;
	private Vista miVista;
	private DefaultTableModel miModelo;

	public void aFichero(List<Disco> list) {
		try {

			Iterator it = list.iterator();
			while (it.hasNext()) {

				Disco tmp = (Disco) it.next();
				Boolean existe = !ficheroContiene(tmp);
				if (existe) {
					int id = getNewIdFichero();
					tmp.setId(id);

					listaFichero.put(id, tmp);

					Writer output;
					output = new BufferedWriter(new FileWriter("Datos.txt", true));
					output.append(Integer.toString(id) + ";" + tmp.getNombre() + ";" + tmp.getArtista() + ";"
							+ tmp.getYear() + ";" + tmp.getGenero().trim() + "\n");
					output.close();

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		cargarFichero();
		actualizaTablaFichero();
	}

	public void cargarFichero() {
		if (listaFichero.isEmpty()) {
			try {
				listaFichero = new HashMap<Integer,Disco>();
				listaFichero.clear();
				BufferedReader br = new BufferedReader(new FileReader("datos.txt"));
				String line = br.readLine();

				while (line != null) {
					line = line.trim();
					String[] contenido = line.split(";");

					int id = Integer.parseInt(contenido[0]);
					Disco tmp = new Disco(id, contenido[1], contenido[2], contenido[3], contenido[4]);
					listaFichero.put(id, tmp);

					line = br.readLine();

				}
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		actualizaTablaFichero();
		
	}

	private Integer getNewIdFichero() {
		getMaxIdFichero();
		idFichero += 1;
		return idFichero;
	}

	private void getMaxIdFichero() {
		if (new File("datos.txt").length() != 0) {
			try {

				BufferedReader br = new BufferedReader(new FileReader("datos.txt"));
				String line = br.readLine();

				while (line != null) {
					line = line.trim();
					String[] contenido = line.split(";");

					int id = Integer.parseInt(contenido[0]);
					if (id > idFichero) {
						idFichero = id;
					}

					line = br.readLine();

				}
				br.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			idFichero = 0;
		}

	}
	public void actualizaTablaFichero() {
		miModelo = new DefaultTableModel();

		int numColumnas = 5;

		

		Object[] contenido = new Object[numColumnas];
		
		contenido[0] = "ID";
		contenido[1] = "Nombre";
		contenido[2] = "Artista";
		contenido[3] = "Fecha";
		contenido[4] = "Genero";
		for (Object obj : contenido) {
			miModelo.addColumn(obj);
			
		}

		Iterator it = listaFichero.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer,Disco> pair =  (Entry<Integer, Disco>) it.next();
			contenido[0] = pair.getKey();
			Disco tmp =  pair.getValue();
			contenido[1] = tmp.getNombre();
			contenido[2] = tmp.getArtista();
			contenido[3] = tmp.getYear();
			contenido[4] = tmp.getGenero();
			miModelo.addRow(contenido);
		}
		miVista.setTabla(miModelo);
	}

	public void GuardarFichero(Disco disco) {
		int id = getNewIdFichero();
		disco.setId(id);
		

		listaFichero.put(id, disco);
		escribeFichero();

	}
	public void editaFichero(Disco disco) {
		if (!ficheroContiene(disco)) {

			listaFichero.put(disco.getId(), disco);
			escribeFichero();
			actualizaTablaFichero();
			System.out.println(listaFichero.toString());

		}
	}
	
	void borraFichero(Disco disco) {
		listaFichero.remove(disco.getId());
		escribeFichero();

	}
	public void aFichero(HashMap<Integer,Disco> lista) {
		try {
			
			Iterator it = lista.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				int llave = (int) pair.getKey();
				Disco tmp = (Disco) pair.getValue();
				Boolean existe = !ficheroContiene(tmp);
				System.out.println("El archivo existe es: " + existe);
				if (existe) {
					int id = getNewIdFichero();

					listaFichero.put(id, tmp);

					Writer output;
					output = new BufferedWriter(new FileWriter("Datos.txt", true));
					output.append(Integer.toString(id) + ";" + tmp.getNombre() + ";" + tmp.getArtista() + ";"
							+ tmp.getYear() + ";" + tmp.getGenero().trim() + "\n");
					output.close();

				}

			}
			miVista.esFichero();

		} catch (Exception e) {
			e.printStackTrace();
		}
		cargarFichero();
		actualizaTablaFichero();

	}
	private Boolean ficheroContiene(Disco disco) {
		Boolean presente = false;

		Iterator it = listaFichero.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			Disco tmp = (Disco) pair.getValue();
			if (tmp.hashCode() == disco.hashCode()) {
				presente = true;
			}

		}

		return presente;
	}
	public void setVista(Vista v) {
		this.miVista=v;
		
	}

	private void escribeFichero() {
		try {
			Iterator it = listaFichero.entrySet().iterator();
			Writer output;
			output = new BufferedWriter(new FileWriter("Datos.txt", false));
			while (it.hasNext()) {

				System.out.println("entro");
				Map.Entry pair = (Map.Entry) it.next();
				Disco tmp = (Disco) pair.getValue();

				output.append(pair.getKey().toString() + ";" + tmp.getNombre() + ";" + tmp.getArtista() + ";"
						+ tmp.getYear() + ";" + tmp.getGenero() + "\n");

			}
			actualizaTablaFichero();
			output.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public HashMap<Integer,Disco> geFichero(){
		return listaFichero;
	}

	public void borraFichero() {

		try {
			FileWriter output = new FileWriter(new File("Datos.txt"));
			output.write("");
			output.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listaFichero.clear();
		actualizaTablaFichero();

	}
}
