public class Main {

	public static void main(String[] args) {
		Vista miVista = new Vista();

		Controlador miControl = new Controlador();
		
		MySQLModelo mysql = new MySQLModelo();
		
		
		
		FicheroModelo fichero = new FicheroModelo();
		MongoModelo mongo = new MongoModelo();
		
		JSONModelo api = new JSONModelo();
		NodeModelo node= new NodeModelo();
		try {
			HibernateModelo  hibernate = new HibernateModelo();
			hibernate.setVista(miVista);
			miControl.setHibernate(hibernate);
			miControl.setNode(node);
			
		} catch (Exception e) {
			miVista.ocultaHibernate();
		}
		mysql.setVista(miVista);
		mongo.setVista(miVista);
		fichero.setVista(miVista);
		
		api.setMiVista(miVista);
		
		

		miControl.setVista(miVista);
		miControl.setFichero(fichero);
	
		miControl.setMysql(mysql);
		miControl.setMongo(mongo);
		miControl.setApi(api);
		miVista.setControlador(miControl);
		
		
		

		

	}
	
	

}
