package practica5;

import java.util.Scanner;

/**
 * @author Daniel Ramirez Gurruchaga
 * @author Juan Facundo Luciaw
 *         Esta clase contiene el main y todos los metodos que se utilizan en este.
 */
public class JuegoMain {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        Tablero tablero = new Tablero();
        int turno = 1;

        System.out.println("Inicio Juego");
        System.out.println(Equipo.ANSI_RED + "Jugador 1: Rojo" + Equipo.ANSI_RESET);
        Equipo e1 = new Equipo();
        System.out.println(Equipo.ANSI_BLUE + "Jugador 2: Azul" + Equipo.ANSI_RESET);
        Equipo e2 = new Equipo();
        System.out.println("----------------------------------------------------------------");
        System.out.println(Equipo.ANSI_RED + "Jugador 1 " + Equipo.ANSI_RESET + "-- Creación de Ejercito");
        System.out.println("----------------------------------------------------------------");

        do {
            if (e1.getSaldo() == 0)
                turno = 2;

            if (turno == 2 && e2.getSaldo() <= 50) {
                System.out.println(Equipo.ANSI_BLUE + "Jugador 2" + Equipo.ANSI_RESET + " -- Creación de Ejercito");
                System.out.println("----------------------------------------------------------------");
            }
            infoPersonajes();

            System.out.println("¿Que personaje quieres añadir?: ");
            int tipo = sc.nextInt();
            if (tipo < 1 || tipo > 4) {
                System.out.println(Equipo.ANSI_RED + "Solo se permiten opciones del 1-4" + Equipo.ANSI_RESET);
                continue;
            }

            Personaje pj = crearPersonaje(tipo, turno);
            if (!comprobarSaldo(turno == 1 ? e1 : e2, tipo, turno))
                continue;

            int fila;
            int columna;

            do {

                do {
                    pedirFilas();
                    fila = sc.nextInt();
                    if (turno == 1)
                        pedirColumnasPj1();
                    else
                        pedirColumnasPj2();
                    columna = sc.nextInt();
                } while (turno == 1 ? !comprobarPosInicial(1, fila, columna) : !comprobarPosInicial(2, fila, columna));

                if (!tablero.huecoLibre(fila, columna))
                    System.out
                            .println(Equipo.ANSI_RED + "Esa posición ya esta ocupada, elija otra" + Equipo.ANSI_RESET);

            } while (!tablero.huecoLibre(fila, columna));

            crearEjercito(turno == 1 ? e1 : e2, tablero, turno, pj, fila, columna);

        } while (e2.getSaldo() > 0);

        System.out.println("--------------------------------------------------------------");
        System.out.println("Ejercitos creados");
        System.out.println("Cambio de fase");

        do {
            if (turno == 0) {
                System.out.println("Ejercito Destruido");
                break;
            } else
                turno = 1;

            int opcion;

            do {

                tablero.imprimirTablero();
                imprimirOpciones(turno);
                opcion = sc.nextInt();
                switch (opcion) {
                    case 1:
                        mover(tablero, turno);
                        turno = turno == 1 ? 2 : 1;
                        break;

                    case 2:
                        turno = atacar(tablero, turno, turno == 1 ? e2 : e1);
                        break;

                    case 3:
                        if (!curar(turno, turno == 1 ? e1 : e2))
                            break;
                        else
                            turno = turno == 1 ? 2 : 1;
                        break;

                    default:
                        System.out.println(Equipo.ANSI_RED + "Solo se permiten opciones del 1-3" + Equipo.ANSI_RESET);
                        break;
                }
            } while (turno != 0);

        } while (true);

        System.out.println("Fin de la partida");
        if (e1.ejercitoVacio())
            System.out.println(Equipo.ANSI_GREEN + "Ganador jugador 2" + Equipo.ANSI_RESET);
        else
            System.out.println(Equipo.ANSI_GREEN + "Ganador jugador 1" + Equipo.ANSI_RESET);

    }

    /**
     * Metodo para imprimir informacion de los personajes
     */
    static void infoPersonajes() {
        System.out.print("1 --> ");
        new Soldado(0).infoPj();
        System.out.print("2 --> ");
        new Caballero(0).infoPj();
        System.out.print("3 --> ");
        new Arquero(0).infoPj();
        System.out.print("4 --> ");
        new Lancero(0).infoPj();
        System.out.println();
    }

    /**
     * Metodo para crear personajes
     * 
     * @param tipo    tipo de personaje
     * @param jugador jugador que lo crea
     * @return Personaje
     */
    static Personaje crearPersonaje(int tipo, int jugador) {
        switch (tipo) {
            case 1:
                return new Soldado(jugador);
            case 2:
                return new Caballero(jugador);
            case 3:
                return new Arquero(jugador);
            case 4:
                return new Lancero(jugador);
            default:
                return null;
        }
    }

    /**
     * Metodo para comprobar saldo
     * 
     * @param equipo  equipo del que queremos comprobar el saldo
     * @param tipo    personaje del que queremos conocer el coste
     * @param jugador jugador que crea el personaje
     * @return boolean
     */
    static boolean comprobarSaldo(Equipo equipo, int tipo, int jugador) {
        if (equipo.getSaldo() < crearPersonaje(tipo, jugador).getCoste()) {
            System.out.println(Equipo.ANSI_RED + "No tienes saldo suficiente para este personaje" + Equipo.ANSI_RESET);
            return false;
        }
        return true;
    }

    /**
     * Metodo para crear ejercito
     * 
     * @param equipo  equipo que esta creando el ejercito
     * @param tablero array tablero
     * @param jugador jugador que crea el personaje
     * @param pj      personaje
     * @param fila    fila en la que queremos insertar el personaje
     * @param columna columna en la que queremos insertar el personaje
     */
    static void crearEjercito(Equipo equipo, Tablero tablero, int jugador, Personaje pj, int fila,
            int columna) {
        equipo.insertarPjEjercito(pj);
        tablero.insertarPj(fila, columna, pj);
        tablero.imprimirTablero();
        equipo.setSaldo(equipo.getSaldo() - tablero.getCostePj(fila, columna));
        System.out.println("Saldo restante: " + equipo.getSaldo());
        System.out.println();
    }

    /**
     * Metodo para pedir filas
     */
    static void pedirFilas() {
        System.out.println("¿En que fila desea colocar al soldado?: ");
        System.out.println("Fila: (0-7)");
    }

    /**
     * Metodo para pedir columnas al jugador 1
     */
    static void pedirColumnasPj1() {
        System.out.println("¿En que columna desea colocar al soldado?: ");
        System.out.println("Columna: (0-1)");
    }

    /**
     * Metodo para pedir columnas al jugador 2
     */
    static void pedirColumnasPj2() {
        System.out.println("¿En que columna desea colocar al soldado?: ");
        System.out.println("Columna: (6-7)");
    }

    /**
     * Comprobar que el personaje se crea en una celda valida
     * 
     * @param turno
     * @param fila
     * @param columna
     * @return boolean
     */
    static boolean comprobarPosInicial(int turno, int fila, int columna) {
        if (turno == 1) {
            if ((fila < 0 || fila > 7) || (columna < 0 || columna > 1)) {
                System.out.println(Equipo.ANSI_RED + "Tiene que ser una posición válida" + Equipo.ANSI_RESET);
                return false;
            } else
                return true;
        } else if ((fila < 0 || fila > 7) || (columna < 6 || columna > 7)) {
            System.out.println(Equipo.ANSI_RED + "Tiene que ser una posición válida" + Equipo.ANSI_RESET);
            return false;
        }
        return true;
    }

    /**
     * Metodo para imprimir opciones (mover, atacar, curar)
     * 
     * @param turno turno
     */
    static void imprimirOpciones(int turno) {
        if (turno == 1)
            System.out.println(Equipo.ANSI_RED + "Jugador 1:" + Equipo.ANSI_RESET);
        else
            System.out.println(Equipo.ANSI_BLUE + "Jugador 2:" + Equipo.ANSI_RESET);

        System.out.println("Que acción desea realizar: ");
        System.out.println("1- Mover");
        System.out.println("2- Atacar");
        System.out.println("3- Curar");
    }

    /**
     * Metodo para mover un personaje
     * 
     * @param tablero array tablero
     * @param turno   turno
     */
    static void mover(Tablero tablero, int turno) {
        int fila;
        int columna;
        int fila2;
        int columna2;

        do {
            System.out.println("Introduzca la fila del personaje a mover:");
            fila = sc.nextInt();
            System.out.println("Introduzca la columna del personaje a mover:");
            columna = sc.nextInt();
        } while (!tablero.comprobarPjPropio(fila, columna, turno));

        do {
            System.out.println("Introduzca la nueva fila del personaje");
            fila2 = sc.nextInt();
            System.out.println("Introduzca la nueva columna del personaje:");
            columna2 = sc.nextInt();
        } while (!tablero.moverPj(fila, columna, fila2, columna2, turno));
    }

    /**
     * Metodo para atacar
     * 
     * @param tablero array tablero
     * @param turno   turno
     * @param equipo  equipo contrario para saber si el personaje es enemigo o no
     * @return int
     */
    static int atacar(Tablero tablero, int turno, Equipo equipo) {
        int fila;
        int columna;
        int fila2;
        int columna2;

        do {
            System.out.println("Introduzca la fila del personaje con el que atacar:");
            fila = sc.nextInt();
            System.out.println("Introduzca la columna del personaje con el que atacar:");
            columna = sc.nextInt();
        } while (!tablero.comprobarPjPropio(fila, columna, turno));

        do {
            System.out.println("Introduzca la fila del personaje al que quiere agredir:");
            fila2 = sc.nextInt();
            System.out.println("Introduzca la columna del personaje al que quiere agredir:");
            columna2 = sc.nextInt();
        } while (!tablero.comprobarEnemigo(fila2, columna2, turno));

        if (tablero.atacar(fila, columna, fila2, columna2, equipo)) {
            if (equipo.ejercitoVacio()) {
                tablero.imprimirTablero();
                return 0;
            } else
                return turno == 1 ? 2 : 1;
        } else
            return turno == 1 ? 1 : 2;

    }

    /**
     * Metodo para curar ejercito
     * 
     * @param turno  turno
     * @param equipo equipo que quiere curar
     * @return boolean
     */
    static boolean curar(int turno, Equipo equipo) {
        if (!equipo.vidaPochaEjercito()) {
            System.out.println(Equipo.ANSI_GREEN + "Tus tropas están joya, no se pueden curar" + Equipo.ANSI_RESET);
            return false;
        } else {
            System.out.println("Tus tropas se toman un respiro para recuperar fuerzas");
            equipo.curar();
            System.out.println("Tus tropas se sienten mejor");
            return true;
        }
    }

}