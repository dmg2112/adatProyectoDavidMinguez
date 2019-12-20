public class Main {

	public static void main(String[] args) {
		Vista miVista = new Vista();

		Controlador miControl = new Controlador();
		
		MySQLModelo mysql = new MySQLModelo();
		
		HibernateModelo  hibernate = new HibernateModelo();
		
		FicheroModelo fichero = new FicheroModelo();
		MongoModelo mongo = new MongoModelo();
		
		JSONModelo api = new JSONModelo();
		

		
		mysql.setVista(miVista);
		mongo.setVista(miVista);
		fichero.setVista(miVista);
		hibernate.setVista(miVista);
		api.setMiVista(miVista);
		
		

		miControl.setVista(miVista);
		miControl.setFichero(fichero);
		miControl.setHibernate(hibernate);
		miControl.setMysql(mysql);
		miControl.setMongo(mongo);
		miControl.setApi(api);
		miVista.setControlador(miControl);
		

		

	}
	
	

}
