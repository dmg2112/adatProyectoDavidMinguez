
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JSONModelo {

	ApiRequests encargadoPeticiones;
	private String SERVER_PATH, GET_DISC, SET_Disco,UPDATE_Disco,DeleteDisco,DeleteAll,addVarios; // Datos de la conexion
	private Vista miVista;
	private HashMap<Integer, Disco> listaServer;
	private int id = 0;

	public JSONModelo() {

		encargadoPeticiones = new ApiRequests();

		SERVER_PATH = "http://localhost/PHPDOCS/InventarioDiscosServer/";
		GET_DISC = "leeDiscos.php";
		SET_Disco = "escribirDisco.php";
		UPDATE_Disco = "actualizarDisco.php";
		DeleteDisco = "deleteDisco.php";
		DeleteAll = "deleteAll.php";
		addVarios = "escribirVarios.php";

	}

	public void cargarJSON() {
		DefaultTableModel miModelo = new DefaultTableModel();
		listaServer = new HashMap<Integer, Disco>();
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

		try {

			String url = SERVER_PATH + GET_DISC; // Sacadas de configuracion

			// System.out.println("La url a la que lanzamos la petición es " +
			// url); // Traza para pruebas

			String response = encargadoPeticiones.getRequest(url);

			// System.out.println(response); // Traza para pruebas

			// Parseamos la respuesta y la convertimos en un JSONObject
			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) { // Si hay algún error de parseo (json
										// incorrecto porque hay algún caracter
										// raro, etc.) la respuesta será null
				System.out.println("El json recibido no es correcto. Finaliza la ejecución");
				System.exit(-1);
			} else { // El JSON recibido es correcto
				// Sera "ok" si todo ha ido bien o "error" si hay algún problema
				String estado = (String) respuesta.get("estado");
				// Si ok, obtenemos array de jugadores para recorrer y generar hashmap
				if (estado.equals("ok")) {
					JSONArray array = (JSONArray) respuesta.get("discos");

					if (array.size() > 0) {

						// Declaramos variables
						Disco disc;
						String nombre;
						String artista;
						String fecha;
						String genero;

						int id;

						for (int i = 0; i < array.size(); i++) {
							JSONObject row = (JSONObject) array.get(i);

							nombre = row.get("nombre").toString();
							artista = row.get("artista").toString();
							id = Integer.parseInt(row.get("id").toString());
							fecha = row.get("fecha").toString();
							genero = row.get("genero").toString();
							disc = new Disco();
							disc.setArtista(artista);
							disc.setGenero(genero);
							disc.setId(id);
							disc.setNombre(nombre);
							disc.setYear(fecha);

							contenido[0] = id;
							contenido[1] = nombre;
							contenido[2] = artista;
							contenido[3] = fecha;
							contenido[4] = genero;
							miModelo.addRow(contenido);
							if (id > this.id) {
								this.id = id;
							}

							listaServer.put(id, disc);
						}

					}

				} else { // Hemos recibido el json pero en el estado se nos
							// indica que ha habido algún error

					System.out.println("Ha ocurrido un error en la busqueda de datos");
					System.out.println("Error: " + (String) respuesta.get("error"));
					System.out.println("Consulta: " + (String) respuesta.get("query"));

					System.exit(-1);

				}
			}

		} catch (Exception e) {
			System.out.println("Ha ocurrido un error en la busqueda de datos");

			e.printStackTrace();

			System.exit(-1);
		}

		miVista.setTabla(miModelo);

	}

	public void anadirDisco(Disco auxDisc) {

		try {
			JSONObject objDisco = new JSONObject();
			JSONObject objPeticion = new JSONObject();

			objDisco.put("nombre", auxDisc.getNombre());
			objDisco.put("artista", auxDisc.getArtista());
			objDisco.put("fecha", auxDisc.getYear());
			objDisco.put("genero", auxDisc.getGenero());

			// Tenemos el jugador como objeto JSON. Lo añadimos a una peticion
			// Lo transformamos a string y llamamos al
			// encargado de peticiones para que lo envie al PHP

			objPeticion.put("peticion", "add");
			objPeticion.put("disco", objDisco);

			String json = objPeticion.toJSONString();

			String url = SERVER_PATH + SET_Disco;

			String response = encargadoPeticiones.postRequest(url, json);

			System.out.println(response.toString());

			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) { // Si hay algún error de parseo (json
										// incorrecto porque hay algún caracter
										// raro, etc.) la respuesta será null
				System.out.println("El json recibido no es correcto. Finaliza la ejecución");

				System.exit(-1);
			} else { // El JSON recibido es correcto

				// Sera "ok" si todo ha ido bien o "error" si hay algún problema
				String estado = (String) respuesta.get("estado");
				System.out.println(respuesta.toString());
				if (estado.equals("ok")) {

					cargarJSON();

				} else {

					miVista.alertErrorEscritura();

				}
			}
		} catch (Exception e) {
			System.out.println(
					"Excepcion desconocida. Traza de error comentada en el método 'annadirJugador' de la clase JSON REMOTO");
			// e.printStackTrace();
			System.out.println("Fin ejecución");
			System.exit(-1);
		}

	}

	public void setMiVista(Vista miVista) {
		this.miVista = miVista;
	}

	public void delete(Disco auxDisc) {

		try {
			JSONObject objDisco = new JSONObject();
			JSONObject objPeticion = new JSONObject();
			
		
			

			objPeticion.put("peticion", "deleteOne");
			objPeticion.put("id", auxDisc.getId());

			String json = objPeticion.toJSONString();

			String url = SERVER_PATH + DeleteDisco;

			String response = encargadoPeticiones.postRequest(url, json);

			System.out.println(response.toString());

			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) { // Si hay algún error de parseo (json
										// incorrecto porque hay algún caracter
										// raro, etc.) la respuesta será null
				System.out.println("El json recibido no es correcto. Finaliza la ejecución");

				System.exit(-1);
			} else { // El JSON recibido es correcto

				// Sera "ok" si todo ha ido bien o "error" si hay algún problema
				String estado = (String) respuesta.get("estado");
				System.out.println(respuesta.toString());
				if (estado.equals("ok")) {

					cargarJSON();

				} else {

					miVista.alertErrorEscritura();

				}
			}
		} catch (Exception e) {
			System.out.println(
					"Excepcion desconocida. Traza de error comentada en el método 'annadirJugador' de la clase JSON REMOTO");
			// e.printStackTrace();
			System.out.println("Fin ejecución");
			System.exit(-1);
		}
	}
	
	
	
	
	public void delete() {

		try {
			JSONObject objDisco = new JSONObject();
			JSONObject objPeticion = new JSONObject();
			
		
			

			objPeticion.put("peticion", "deleteAll");
			

			String json = objPeticion.toJSONString();

			String url = SERVER_PATH + DeleteAll;

			String response = encargadoPeticiones.postRequest(url, json);

			System.out.println(response.toString());

			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) { // Si hay algún error de parseo (json
										// incorrecto porque hay algún caracter
										// raro, etc.) la respuesta será null
				System.out.println("El json recibido no es correcto. Finaliza la ejecución");

				System.exit(-1);
			} else { // El JSON recibido es correcto

				// Sera "ok" si todo ha ido bien o "error" si hay algún problema
				String estado = (String) respuesta.get("estado");
				System.out.println(respuesta.toString());
				if (estado.equals("ok")) {

					cargarJSON();

				} else {

					miVista.alertErrorEscritura();

				}
			}
		} catch (Exception e) {
			System.out.println(
					"Excepcion desconocida. Traza de error comentada en el método 'annadirJugador' de la clase JSON REMOTO");
			// e.printStackTrace();
			System.out.println("Fin ejecución");
			System.exit(-1);
		}
	}


	public void updateDisco(Disco auxDisc) {

		try {
			JSONObject objDisco = new JSONObject();
			JSONObject objPeticion = new JSONObject();
			
			objDisco.put("id", auxDisc.getId());
			objDisco.put("nombre", auxDisc.getNombre());
			objDisco.put("artista", auxDisc.getArtista());
			objDisco.put("fecha", auxDisc.getYear());
			objDisco.put("genero", auxDisc.getGenero());

			// Tenemos el jugador como objeto JSON. Lo añadimos a una peticion
			// Lo transformamos a string y llamamos al
			// encargado de peticiones para que lo envie al PHP

			objPeticion.put("peticion", "update");
			objPeticion.put("disco", objDisco);

			String json = objPeticion.toJSONString();

			String url = SERVER_PATH + UPDATE_Disco;

			String response = encargadoPeticiones.postRequest(url, json);

			System.out.println(response.toString());

			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) { // Si hay algún error de parseo (json
										// incorrecto porque hay algún caracter
										// raro, etc.) la respuesta será null
				System.out.println("El json recibido no es correcto. Finaliza la ejecución");

				System.exit(-1);
			} else { // El JSON recibido es correcto

				// Sera "ok" si todo ha ido bien o "error" si hay algún problema
				String estado = (String) respuesta.get("estado");
				System.out.println(respuesta.toString());
				if (estado.equals("ok")) {

					cargarJSON();

				} else {

					miVista.alertErrorEscritura();

				}
			}
		} catch (Exception e) {
			System.out.println(
					"Excepcion desconocida. Traza de error comentada en el método 'annadirJugador' de la clase JSON REMOTO");
			// e.printStackTrace();
			System.out.println("Fin ejecución");
			System.exit(-1);
		}
	}
	

	public void aServer(HashMap<Integer, Disco> lista) {

		try {
			
			JSONObject objPeticion = new JSONObject();
			JSONArray listaDiscos = new JSONArray();
			
		
			

			objPeticion.put("peticion", "addVarios");
			
			
			
			Iterator<?> it = lista.entrySet().iterator();
			
			while (it.hasNext()) {
				Map.Entry<Integer, Disco> pair = (Entry<Integer, Disco>) it.next();
				int id = pair.getKey();
				Disco tmp = pair.getValue();
				
				if (!serverContiene(tmp)) {
					JSONObject objDisco = new JSONObject();
					
					objDisco.put("nombre", tmp.getNombre());
					objDisco.put("artista", tmp.getArtista());
					objDisco.put("fecha", tmp.getYear());
					objDisco.put("genero", tmp.getGenero());
					
					listaDiscos.add(objDisco);
					
					
					
					
				}

			}
			if(listaDiscos.size()>0) {
				
				objPeticion.put("discos",listaDiscos);
				
				String json = objPeticion.toJSONString();

				String url = SERVER_PATH + addVarios;
				
				

				String response = encargadoPeticiones.postRequest(url, json);

				System.out.println(response.toString());

				JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

				if (respuesta == null) { // Si hay algún error de parseo (json
											// incorrecto porque hay algún caracter
											// raro, etc.) la respuesta será null
					System.out.println("El json recibido no es correcto. Finaliza la ejecución");

					System.exit(-1);
				} else { // El JSON recibido es correcto

					// Sera "ok" si todo ha ido bien o "error" si hay algún problema
					String estado = (String) respuesta.get("estado");
					
					if (estado.equals("ok")) {

						cargarJSON();
						miVista.esServidor();

					} else {

						miVista.alertErrorEscritura();

					}
				}
				
			}
			

			
		} catch (Exception e) {
			System.out.println(
					"Excepcion desconocida. Traza de error comentada en el método 'annadirJugador' de la clase JSON REMOTO");
			 e.printStackTrace();
			System.out.println("Fin ejecución");
			System.exit(-1);
		}
	}

	private boolean serverContiene(Disco tmp) {
		
		Boolean presente = false;
		if(listaServer!=null ) {
			Iterator it = listaServer.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				Disco temp = (Disco) pair.getValue();
				if (tmp.hashCode() == tmp.hashCode()) {
					presente = true;
				}

			}
		}
		

		return presente;
		
	}

	public HashMap<Integer, Disco> getServer() {
		
		return this.listaServer;
	}

}
