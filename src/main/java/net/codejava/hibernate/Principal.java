package net.codejava.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Principal {
    /*
    Variable boolean utilizada para el bucle del menú
     */
    private static boolean condicion = true;
    /*
    Variable que utilizo para comprobar que las cadenas
    introducidas corresponden con las pedidas
     */
    private static boolean comprobacion = true;
    /*
    Variable para la sessionFactory
     */
    private SessionFactory sessionFactory;
    /*
    Scanner que uitlizo solo para las variables tipo int
     */
    Scanner scInt = new Scanner(System.in);
    /*
    Scanner que uitlizo solo para las variables tipo String
    */
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Scanner scInt = new Scanner(System.in);
        Principal main = new Principal();
        main.setUp();

        while (condicion){

            System.out.println("/----------------------------------------MENU----------------------------------------/");
            System.out.print("\n");
            System.out.println("1-. Dar de alta un pasaje nuevo introduciendo sus datos por teclado.");
            System.out.print("\n");
            System.out.println("2-. Introduciendo el identificador de vuelo, visualiza todos los datos del pasaje.");
            System.out.print("\n");
            System.out.println("3-. Actualizar los datos de un pasajero.");
            System.out.print("\n");
            System.out.println("4-. Dar de baja todos los pasajes de un pasajero introduciendo su código por teclado.");
            System.out.print("\n");
            System.out.println("5-. Visualiza el importe total recaudado en los pasajes de un vuelo, introduciendo por teclado el\n" +
                    "identificador del mismo.");
            System.out.print("\n");
            System.out.println("0-. SALIR.");

            int opcion = scInt.nextInt();

            switch (opcion){

                case 1:
                    main.ejercicio01();
                    break;

                case 2:
                    main.ejercicio02();
                    break;

                case 3:
                    main.ejercicio03();
                    break;

                case 4:
                    main.ejercicio04();
                    break;

                case 5:
                    main.ejercicio05();
                    break;

                case 0:
                    System.out.println("Muchas Gracias!");
                    condicion = false;
                    break;

                default:
                    System.out.println("Por favor introduzca un numero entre el 1 y el 8. [0 para salir]");

            }
        }
    }

    /**
     * Método que corresponde con el apartado número 6 del enunciado
     * de la PEVAL.
     */
    private void ejercicio05() {

        String identificadorNuevo = "";

        Session sessionListado = sessionFactory.openSession();

        Query listado = sessionListado.createQuery("FROM Vuelo");

        List<Vuelo> misVuelos = listado.getResultList();
        Iterator<Vuelo> iter = misVuelos.iterator();

        System.out.print("\n");
        System.out.println("---LISTADO DE TODOS LOS IDENTIFICADORES DE LOS VUELOS---");
        System.out.print("\n");

        while(iter.hasNext()){
            Vuelo vuelo = (Vuelo) iter.next();
            System.out.println(vuelo.getIdentificador());
            System.out.println("------------");
        }

        System.out.print("\n");
        System.out.println("---FIN DE LISTADO---");
        System.out.print("\n");

        sessionListado.close();

        //Realizamos la consulta.

        Session session = sessionFactory.openSession();

        while (comprobacion){

            System.out.println("Introduce el identificador del vuelo del cual, quiera consultar el importe total: ");
            identificadorNuevo = sc.nextLine().toUpperCase().trim();

            Query q = session.createQuery("FROM Vuelo WHERE identificador = :identificador");
            q.setParameter("identificador",identificadorNuevo);

            try {
                Vuelo vuelo = (Vuelo) q.getSingleResult();
                comprobacion = false;
            }catch (NoResultException e){
                System.out.println("---EL VUELO NO EXISTE---");
            }
        }
        comprobacion = true;

        Query q = session.createQuery("SELECT SUM(pvp) FROM Pasaje WHERE identificador.identificador =: identificadorNuevo");
        q.setParameter("identificadorNuevo",identificadorNuevo);

        System.out.println("El importe total del vuelo : "+identificadorNuevo+" es de --> "+q.uniqueResult());
        session.close();
    }

    /**
     * Método que corresponde con el apartado número 5 del enunciado
     * de la PEVAL.
     */
    private void ejercicio04() {

        int codigo = 0;

        Session session = sessionFactory.openSession();

        Query listado = session.createQuery("FROM Pasajero");

        System.out.print("\n");
        System.out.println("---LISTADO DE TODOS LOS PASAJEROS CON SU CÓDIGO Y NOMBRE---");
        System.out.print("\n");

        List<Pasajero> misPasajeros = listado.getResultList();
        Iterator<Pasajero> iter = misPasajeros.iterator();

        while(iter.hasNext()){
            Pasajero pasajero = (Pasajero) iter.next();
            System.out.println(pasajero.getCod()+" "+pasajero.getNombre());
            System.out.println("-------------------");
        }

        System.out.print("\n");
        System.out.println("---FIN DEL LISTADO---");
        System.out.print("\n");

        session.close();

        //Comenzamos con el delete.
        Session sessionDelete = sessionFactory.openSession();
        Transaction tx = sessionDelete.beginTransaction();

        while(comprobacion){

            System.out.println("Introduce el codigo de pasajero que desea dar de baja a todo sus pasajes: ");

            try {

                codigo = new Scanner(System.in).nextInt();

                if(codigo < 0){

                    System.out.println("----NO SE PUEDE INTRODUCIR UN VALOR NEGATIVO----");

                }else {

                    Query q = sessionDelete.createQuery("FROM Pasajero WHERE cod = :codPasajero");
                    q.setParameter("codPasajero",codigo);

                    try {
                        Pasajero pasajero = (Pasajero) q.getSingleResult();
                        comprobacion = false;
                    }catch (NoResultException e){
                        System.out.println("---EL PASAJERO NO EXISTE---");
                    }
                }
            }catch (InputMismatchException e){

                System.out.println("----DEBES INTRODUCIR UN NUMERO----");

            }
        }
        comprobacion = true;

        Query q = sessionDelete.createQuery("DELETE FROM Pasaje WHERE pasajero_cod.cod =: codigo ");
        q.setParameter("codigo",codigo);

        System.out.println("Se han borrado " + q.executeUpdate() + " pasajes");
        session.close();
    }

    /**
     * Método que corresponde con el apartado número 4 del enunciado
     * de la PEVAL.
     */
    private void ejercicio03() {

        String nombre ="";
        String numeroTLF = "";
        String direccion = "";
        String pais = "";
        int codigo = 0;

        //Abrimos la session.
        Session session = sessionFactory.openSession();

        System.out.print("\n");
        System.out.println("---LISTADO DE TODOS LOS PASAJEROS CON SU CÓDIGO Y NOMBRE---");
        System.out.print("\n");

        Query listado = session.createQuery("FROM Pasajero");

        List<Pasajero> misPasajeros = listado.getResultList();
        Iterator<Pasajero> iter = misPasajeros.iterator();

        while(iter.hasNext()){
            Pasajero pasajero = (Pasajero) iter.next();
            System.out.println(pasajero.getCod()+" "+pasajero.getNombre());
            System.out.println("-------------------");
        }

        System.out.print("\n");
        System.out.println("---FIN DEL LISTADO---");
        System.out.print("\n");

        session.close();

        //Comenzamos con el UPDATE.

        Session sessionUpdate = sessionFactory.openSession();
        Transaction tx = sessionUpdate.beginTransaction();

        while(comprobacion){

            System.out.println("Introduzca el codigo del pasajero a modificar: ");

            try {
                codigo = new Scanner(System.in).nextInt();

                if(codigo < 0 ){

                    System.out.println("----NO SE PUEDE INTRODUCIR UN VALOR NEGATIVO----");

                }else{
                    Query q = sessionUpdate.createQuery("FROM Pasajero WHERE cod = :codPasajero");
                    q.setParameter("codPasajero",codigo);

                    try {
                        Pasajero pasajero = (Pasajero) q.getSingleResult();
                        comprobacion = false;
                    }catch (NoResultException e){
                        System.out.println("---EL PASAJERO NO EXISTE---");
                    }
                }
            }catch (InputMismatchException e ){

                System.out.println("----DEBES INTRODUCIR UN NUMERO----");
            }
        }
        comprobacion = true;

        while(comprobacion){
            System.out.println("Introduzca el nuevo nombre: ");
            nombre = sc.nextLine();
            comprobacion = comprobarCadenaTexto(nombre,comprobacion);
        }
        comprobacion = true;

        System.out.println("Introduzca el nuevo numero de TLF: ");
        numeroTLF = sc.nextLine();

        while (comprobacion){
            System.out.println("Introduzca la nueva direccion: ");
            direccion = sc.nextLine();

            comprobacion = comprobarCadenaTexto(direccion,comprobacion);
        }
        comprobacion = true;

        while (comprobacion){

            System.out.println("Introduzca el nuevo pais: ");
            pais = sc.nextLine();

            comprobacion = comprobarCadenaTexto(pais,comprobacion);
        }
        comprobacion = true;


        Query q = sessionUpdate.createQuery("UPDATE Pasajero SET nombre =: nombre, tlf =: numeroTLF, direccion =: direccion, pais =: pais WHERE cod =: codigo");
        q.setParameter("codigo",codigo);
        q.setParameter("nombre",nombre);
        q.setParameter("numeroTLF",numeroTLF);
        q.setParameter("direccion",direccion);
        q.setParameter("pais",pais);

        System.out.println("Se han modificado " + q.executeUpdate() + " registros");
        tx.commit();

        sessionUpdate.close();
    }

    /**
     * Método que corresponde con el apartado número 3 del enunciado
     * de la PEVAL.
     */
    private void ejercicio02() {
        String identificadorSelec="";
        /*
        Sacamos un listado de todos los vuelos que tenemos en
        la BASE DE DATOS.
         */
        Session sessionListado = sessionFactory.openSession();

        Query listado = sessionListado.createQuery("FROM Vuelo");

        List<Vuelo> misVuelos = listado.getResultList();
        Iterator<Vuelo> iter = misVuelos.iterator();

        System.out.print("\n");
        System.out.println("---LISTADO DE TODOS LOS IDENTIFICADORES DE LOS VUELOS---");
        System.out.print("\n");

        while(iter.hasNext()){
            Vuelo vuelo = (Vuelo) iter.next();
            System.out.println(vuelo.getIdentificador());
            System.out.println("------------");
        }

        System.out.print("\n");
        System.out.println("---FIN DE LISTADO---");
        System.out.print("\n");

        sessionListado.close();

        //Abrimos la session.
        Session session = sessionFactory.openSession();

        //Pedimos el idetificador de vuelo que queramos comprobar.

        while(comprobacion){

            System.out.println("Introduzca el identificador del vuelo que desee comprobar: ");
            identificadorSelec = sc.nextLine().toUpperCase().trim();

            Query q = session.createQuery("FROM Vuelo WHERE identificador = :identificador");
            q.setParameter("identificador",identificadorSelec);

            try {
                Vuelo vuelo = (Vuelo) q.getSingleResult();
                comprobacion = false;
            }catch (NoResultException e){
                System.out.println("---EL VUELO NO EXISTE---");
            }
        }
        comprobacion = true;


        Query qPasaje = session.createQuery("FROM Pasaje WHERE identificador.identificador = :identificadorSelec");
        qPasaje.setParameter("identificadorSelec",identificadorSelec);

        List<Pasaje> misPasajes = qPasaje.getResultList();
        Iterator<Pasaje> iterPasaje = misPasajes.iterator();

        while(iterPasaje.hasNext()){
            Pasaje pasaje = (Pasaje) iterPasaje.next();
            System.out.println("IDE --> "+pasaje.getIdentificador().getIdentificador());
            System.out.println("ORIGEN --> " + pasaje.getIdentificador().getAeropuerto_origen() +" // "+ "DESTINO --> " +pasaje.getIdentificador().getAeropuerto_origen()+
                    " // "+"FECHA --> "+pasaje.getIdentificador().getFecha_vuelo());
            System.out.println("\tCLASE --> " + pasaje.getClase());
            System.out.println("\t\tNOMBRE PASAJERO: "+pasaje.getPasajero_cod().getNombre() + " // " + "CODIGO PASAJE" +pasaje.getPasajero_cod().getCod() +
                    " // " + "NUMERO ASIENTOS " + pasaje.getNumAsientos());
            System.out.println("-------------------------------------------------");
        }
    }

    /**
     * Método que corresponde con el apartado número 2 del enunciado
     * de la PEVAL.
     */
    private void ejercicio01() {

         /*
         Variables propias de este método que utilizamos mas tarde,
          */

        int codPasajero = 0;
        String identificador = "";
        int numAsiento = 0;
        String clase ="";
        float pvp = 0;

        //Abrimos la session para los listados
        Session sessionListado = sessionFactory.openSession();

        Query listado = sessionListado.createQuery("FROM Pasajero");

        System.out.print("\n");
        System.out.println("---LISTADO DE TODOS LOS PASAJEROS CON SU CÓDIGO Y NOMBRE---");
        System.out.print("\n");

        List<Pasajero> misPasajeros = listado.getResultList();
        Iterator<Pasajero> iter = misPasajeros.iterator();

        while(iter.hasNext()){
            Pasajero pasajero = (Pasajero) iter.next();
            System.out.println(pasajero.getCod()+" "+pasajero.getNombre());
            System.out.println("-------------------");
        }

        System.out.print("\n");
        System.out.println("---FIN DEL LISTADO---");
        System.out.print("\n");

        //Abrimos la session.
        Session sessionCod = sessionFactory.openSession();

        Query qCod = sessionCod.createQuery("SELECT MAX(cod) FROM Pasaje");
        int cod = (int)  qCod.uniqueResult();
        cod = ((int) cod + 1);


        //Abrimos la session
        Session session = sessionFactory.openSession();

        //Creamos el objeto de Pasaje
        Pasaje pasaje = new Pasaje();

        /*
        Comenzamos a pedir por teclado al cliente
        los datos del nuevo pasaje.
         */
        pasaje.setCod(cod);

        while (comprobacion){

            System.out.println("Introduzca el código del pasajero que porta este pasaje: ");

            try {

                codPasajero = new Scanner(System.in).nextInt();

                if(codPasajero < 0){

                    System.out.println("----NO SE PUEDE INTRODUCIR UN VALOR NEGATIVO----");
                }
                else {

                    Query q = session.createQuery("FROM Pasajero WHERE cod = :codPasajero");
                    q.setParameter("codPasajero",codPasajero);

                    try {
                        Pasajero pasajero = (Pasajero) q.getSingleResult();
                        comprobacion = false;
                    }catch (NoResultException e){
                        System.out.println("---EL PASAJERO NO EXISTE---");
                    }
                }
            }catch (InputMismatchException e){

                System.out.println("----DEBES INTRODUCIR UN NUMERO----");
            }
        }

        pasaje.setPasajero_cod(session.get(Pasajero.class, codPasajero));
        comprobacion = true;

        //Listado Los identificadores de vuelo.
        Session sessionVuelos = sessionFactory.openSession();
        Query listadoVuelo = sessionVuelos.createQuery("FROM Vuelo");
        List<Vuelo> misVuelos = listadoVuelo.getResultList();
        Iterator<Vuelo> iterVuelos = misVuelos.iterator();

        System.out.print("\n");
        System.out.println("---LISTADO DE TODOS LOS IDENTIFICADORES DE LOS VUELOS---");
        System.out.print("\n");

        while(iterVuelos.hasNext()){
            Vuelo vuelo = (Vuelo) iterVuelos.next();
            System.out.println(vuelo.getIdentificador());
            System.out.println("------------");
        }

        System.out.print("\n");
        System.out.println("---FIN DE LISTADO---");
        System.out.print("\n");
        sessionVuelos.close();
        //Cerramos la session.

        while(comprobacion){

            System.out.println("Introduzca el código del vuelo al que esta asociado: ");
            identificador = sc.nextLine().toUpperCase().trim();

            Query q = session.createQuery("FROM Vuelo WHERE identificador = :identificador");
            q.setParameter("identificador",identificador);

            try {
                Vuelo vuelo = (Vuelo) q.getSingleResult();
                comprobacion = false;
            }catch (NoResultException e){
                    System.out.println("---EL VUELO NO EXISTE---");
            }

        }
        comprobacion = true;
        pasaje.setIdentificador(session.get(Vuelo.class,identificador));

        while (comprobacion){

            System.out.println("Introduzca el numero de asientos: ");
            try{

                //numAsiento = scInt.nextInt();
                numAsiento = new Scanner(System.in).nextInt();

                if(numAsiento < 0){

                    System.out.println("----NO SE PUEDE INTRODUCIR UN VALOR NEGATIVO----");
                }
                else{
                    comprobacion = false;
                }
            }catch (InputMismatchException e){

                System.out.println("----DEBES INTRODUCIR UN NUMERO----");
            }
        }

        pasaje.setNumAsientos(numAsiento);

        comprobacion = true;

        while(comprobacion){

            System.out.println("Introduzca la clase en la que vuela: ");
            clase = sc.nextLine();

            comprobacion = comprobarCadenaTexto(clase,comprobacion);
        }
        pasaje.setClase(clase);
        comprobacion = true;

        while(comprobacion){

            System.out.println("Introduzca el pvp: ");
            try {

                pvp = new Scanner(System.in).nextFloat();

                if (pvp < 0 ){

                    System.out.println("----NO SE PUEDE INTRODUCIR UN VALOR NEGATIVO----");
                }
                else{
                    comprobacion = false;
                }
            }catch (InputMismatchException e){

                System.out.println("----DEBES INTRODUCIR UN NUMERO----");
            }
        }

        pasaje.setPvp(pvp);
        comprobacion = true;

        /*
        Hacemos la transición y guardamos.
         */
        session.beginTransaction();
        session.save(pasaje);

        /*
        Hacemos commit y cerramos la session.
         */
        session.getTransaction().commit();
        session.close();


    }
    /**
     * Método para iniciar la conexión.
     */
    private void setUp() {

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            System.err.println("ERROR --> No se pudo conectar");
            e.printStackTrace();
        }
    }

    public static boolean comprobarCadenaTexto (String cadena, boolean comprobacion){


        try {

            Integer.parseInt(cadena);
            System.out.println("/--LA CADENA INTRODUCIDA ES UN NUMERO--/");
            System.out.print("\n");

        }catch (NumberFormatException e){

            comprobacion = false;
        }

        return comprobacion;
    }
}
