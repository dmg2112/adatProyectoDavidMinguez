import java.awt.Dimension;
import java.util.List;
import javax.swing.JFrame;

public class Controlador {
	
	private Vista miVista;
	private MySQLModelo mysql;
	private FicheroModelo fichero;
	private HibernateModelo hibernate;
	private MongoModelo mongo;
	private JSONModelo api;

	// NAVEGACION
	public Controlador() {

	}

	

	public void setVista(Vista vista) {
		this.miVista = vista;
		inicio();
	}

	private void inicio() {
		miVista.setVisible(true);

	}

	public void setMysql(MySQLModelo mysql) {
		this.mysql = mysql;
	}



	public void setFichero(FicheroModelo fichero) {
		this.fichero = fichero;
	}



	public void setHibernate(HibernateModelo hibernate) {
		this.hibernate = hibernate;
	}
	
	public void cargaServer() {
		api.cargarJSON();
	}


	public void cargaFichero() {
		fichero.cargarFichero();
		
	}
	public void cargaSQL() {
		this.mysql.cargarBBDD();
		
	}
	public void cargaHib() {
		this.hibernate.cargarHib();
		
	}
	public void cargaMongo() {
		this.mongo.cargaMongo();
	}



	public void setMongo(MongoModelo mongo) {
		this.mongo = mongo;
	}



	public void guarda(String source, Disco tmp) {
		switch(source) {
		case "mysql": 
			mysql.guardarBase(tmp);
			break;
		case "hib":
			hibernate.guardaHib(tmp);
			break;
		case "fichero":
			fichero.GuardarFichero(tmp);
			break;
		case "mongo":
			mongo.guardaMongo(tmp);
			break;
		case "server":
			api.anadirDisco(tmp);
			break;
		
		}
		
	}



	public void edita(String source, Disco tmp) {
		switch(source) {
		case "mysql": 
			mysql.editaBBDD(tmp);
			break;
		case "hib":
			hibernate.editaHib(tmp);
			break;
		case "fichero":
			fichero.editaFichero(tmp);
			break;
		case "mongo":
			mongo.editaMongo(tmp);
			break;
		case "server":
			api.updateDisco(tmp);
			break;
		
		
			
		}
		
		
		
	}



	public void aBBDD(String source) {
		switch(source) {
		case "hib":
			mysql.aBBDD(hibernate.getHib());
			break;
		case "fichero":
			mysql.aBBDD(fichero.geFichero());
			break;
		case "mongo":
			mysql.aBBDD(mongo.getMongo());
			break;
			
		}
		
		
	}
	
	public void aMongo(String source) {
		switch(source) {
		case "hib":
			mongo.aMongo(hibernate.getHib());
			break;
		case "fichero":
			mongo.aMongo(fichero.geFichero());
			break;
		case "mysql":
			mongo.aMongo(mysql.getBBDD());
			break;
			
		}
		
		
	}
	
	public void aFichero(String source) {
		switch(source) {
		case "hib":
			fichero.aFichero(hibernate.getHib());
			break;
		case "mongo":
			fichero.aFichero(mongo.getMongo());
			break;
		case "mysql":
			fichero.aFichero(mysql.getBBDD());
			break;
			
		}
		
	}
	
	public void aHib(String source) {
		switch(source) {
		case "fichero":
			hibernate.AHib(fichero.geFichero());
			break;
		case "mongo":
			hibernate.AHib(mongo.getMongo());
			break;
		case "mysql":
			hibernate.AHib(mysql.getBBDD());
			
		}
		
	}
	public void borra(String source) {
		switch(source) {
		case "mysql": 
			mysql.borraBBDD();
			break;
		case "hib":
			hibernate.borraHib();
			break;
		case "fichero":
			fichero.borraFichero();
			break;
		case "mongo":
			mongo.borraMongo();
			break;
			
		}
		
	}



	public void borra(String source, Disco disco) {
		switch(source) {
		case "mysql": 
			mysql.borraBBDD(disco.getId());
			break;
		case "hib":
			hibernate.borraRegistroHib(disco);
			break;
		case "fichero":
			fichero.borraFichero(disco);
			break;
		case "mongo":
			mongo.borraMongo(disco);
			break;
			
		}
		
	}



	public void setApi(JSONModelo api) {
		this.api = api;
	}

}
