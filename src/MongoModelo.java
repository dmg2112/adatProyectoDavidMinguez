import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.DefaultTableModel;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;

public class MongoModelo {
	private MongoCollection<Document> con;
	private Vista miVista;
	private DefaultTableModel miModelo;
	private HashMap<Integer,Disco> listaMongo = new HashMap<Integer,Disco>();

	public MongoModelo() {
		MongoClient mongoClient = MongoClients.create();
		MongoDatabase database = mongoClient.getDatabase("tiendaDiscos");
		con = database.getCollection("inventario");

	}

	public void guardaMongo(Disco disco) {
		Document doc = new Document();
		int key = getNewIdMongo();
		doc.append("_id", key).append("nombre", disco.getNombre()).append("artista", disco.getArtista())
				.append("fecha", disco.getYear()).append("genero", disco.getGenero());
		con.insertOne(doc);
		cargaMongo();

	}

	public void cargaMongo() {
		listaMongo = new HashMap<Integer,Disco>();
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
		FindIterable<Document> list = con.find();

		if (list.first() != null) {
			for (Document document : list) {
				
				int id = document.getInteger("_id");
				String nombre = document.getString("nombre");
				String artista = document.getString("artista");
				String year = document.getString("fecha");
				String genero = document.getString("genero");
				
				

				contenido[0] = id;
				contenido[1] = nombre;
				contenido[2] = artista;
				contenido[3] = year;
				contenido[4] = genero;
				
				miModelo.addRow(contenido);
				Disco tmp = new Disco(id,nombre,artista,year,genero);
				listaMongo.put(id, tmp);

			}
		}
		miVista.esMongo();
		miVista.setTabla(miModelo);

	}

	private int getNewIdMongo() {
		FindIterable<Document> maxList = con.find().sort(new Document("_id", -1)).limit(1);
		if(maxList.first() != null) {
			return maxList.first().getInteger("_id", 1) + 1;
		}else {
			return 1;
		}

		

	}

	public void setVista(Vista v) {
		this.miVista = v;

	}

	public void editaMongo(Disco tmp) {
		if (!mongoContiene(tmp)) {
			Document query = new Document("_id", tmp.getId());
			Document data = new Document();
			data.append("nombre", tmp.getNombre());
			data.append("artista", tmp.getArtista());
			data.append("fecha", tmp.getYear());
			data.append("genero", tmp.getGenero());
			Document update = new Document("$set", data);
			con.updateOne(query, update);
		} else {
			miVista.alertDuplicado();
		}
		cargaMongo();

	}
	public void aMongo(HashMap<Integer,Disco> lista) {
		Iterator<?> it = lista.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry<Integer, Disco> pair = (Entry<Integer, Disco>) it.next();
			int id = pair.getKey();
			Disco tmp = pair.getValue();
			if (!mongoContiene(tmp)) {
				guardaMongo(tmp);
				
				
			}

		}
		cargaMongo();
	}

	private boolean mongoContiene(Disco tmp) {
		return con.find(new Document("nombre", tmp.getNombre()).append("artista", tmp.getArtista())).first() != null;
	}
	public HashMap<Integer,Disco> getMongo(){
		return listaMongo;
	}

	public void borraMongo(Disco disco) {
		
		con.deleteOne(new Document("_id",disco.getId()));
		cargaMongo();
		
	}
	public void borraMongo() {
		
		con.deleteMany(new Document());
		cargaMongo();
		
	}

}
