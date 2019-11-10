import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class HibernateModelo {
	private HibernateUtil util;
	private Session session;
	private DefaultTableModel miModelo;
	private Vista miVista;
	private HashMap<Integer, Disco> listaHib = new HashMap<Integer, Disco>();

	public HibernateModelo() {
		util = new HibernateUtil();
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
		session = util.getSessionFactory().openSession();

	}

	public void cargarHib() {
		@SuppressWarnings("unchecked")
		Query<Disco> q = session.createQuery("select d from Disco d");
		List<Disco> results = q.list();
		miModelo = new DefaultTableModel();
		listaHib = new HashMap<Integer, Disco>();
		Object[] contenido = new Object[5];
		contenido[0] = "ID";
		contenido[1] = "Nombre";
		contenido[2] = "Artista";
		contenido[3] = "Fecha";
		contenido[4] = "Genero";
		for (Object obj : contenido) {
			miModelo.addColumn(obj);
			
		}
		Iterator<Disco> discIt = results.iterator();
		while (discIt.hasNext()) {
			Disco tmp = (Disco) discIt.next();
			System.out.println(tmp.toString());
			listaHib.put(tmp.getId(), tmp);
			contenido[0] = tmp.getId();
			contenido[1] = tmp.getNombre();
			contenido[2] = tmp.getArtista();
			contenido[3] = tmp.getYear();
			contenido[4] = tmp.getGenero();
			miModelo.addRow(contenido);

		}
		miVista.setTabla(miModelo);

	}

	public void guardaHib(Disco disco) {
		session.beginTransaction();
		int id = hibGetMax();
		disco.setId(id);
		session.save(disco);
		session.getTransaction().commit();

		cargarHib();

	}

	private int hibGetMax() {

		int max = (Integer) session.createQuery("SELECT COALESCE(MAX(id), 0) FROM Disco").uniqueResult();

		return max + 1;
	}

	public void editaHib(Disco disco) {
		session.beginTransaction();
		Query q = session.createQuery(
				"UPDATE Disco set nombre = :name, artista = :artista, year = :year, genero = :genero WHERE id = :id");
		q.setParameter("name", disco.getNombre());
		q.setParameter("artista", disco.getArtista());
		q.setParameter("year", disco.getYear());
		q.setParameter("genero", disco.getGenero());
		q.setParameter("id", disco.getId());

		q.executeUpdate();
		session.flush();
		session.clear();
		session.getTransaction().commit();
		cargarHib();

	}

	public void AHib(HashMap<Integer, Disco> lista) {
		Iterator<?> it = lista.entrySet().iterator();
		session.beginTransaction();
		while (it.hasNext()) {
			Map.Entry<Integer, Disco> pair = (Entry<Integer, Disco>) it.next();
			int id = pair.getKey();
			Disco tmp = pair.getValue();
			if (!hibContiene(tmp)) {
				tmp.setId(hibGetMax());
				session.save(tmp);
			}

		}
		session.getTransaction().commit();
		miVista.esHib();
		cargarHib();
		

	}

	

	public void borraRegistroHib(Disco disco) {
		
		session.beginTransaction();
		
		session.delete(disco);
		session.flush();
		session.clear();
		session.getTransaction().commit();
		cargarHib();

	}
	public void setVista(Vista v) {
		this.miVista=v;
		
	}

	public boolean hibContiene(Disco disco) {
		
		Query q = session.createQuery("select d from Disco d where nombre = :name AND artista = :artista ");
		q.setParameter("name", disco.getNombre());
		q.setParameter("artista", disco.getArtista());

		List results = q.list();

		return results.size() > 0;

	}
	public HashMap<Integer,Disco> getHib(){
		return listaHib;
	}

	public void borraHib() {
		session.createQuery("delete from Disco").executeUpdate();
		cargarHib();
		
	}
	

}
