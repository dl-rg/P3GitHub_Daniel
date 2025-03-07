package practica5;

import java.util.Arrays;

/**
 * @author Daniel Ramirez Gurruchaga
 * @author Juan Facundo Luciaw
 *         Esta clase contiene el tablero y todos los metodos que se utilizan en
 *         este.
 */
public class Tablero {

	Personaje tablero[][];

	/**
	 * Metodo constructor
	 */
	Tablero() {
		tablero = new Personaje[8][8];
	}

	/**
	 * Comprueba que la celda introducida este libre
	 * 
	 * @param fila
	 * @param columna
	 * @return boolean
	 */
	public boolean huecoLibre(int fila, int columna) {
		if (tablero[fila][columna] != null)
			return false;

		else
			return true;
	}

	/**
	 * Comprueba que la celda introducida existe
	 * 
	 * @param fila
	 * @param columna
	 * @return boolean
	 */
	public boolean huecoExiste(int fila, int columna) {
		if ((fila < 0 || columna < 0) || (fila > 7 || columna > 7))
			return false;
		return true;
	}

	/**
	 * Inserta el personaje introducido en el array tablero
	 * 
	 * @param fila
	 * @param columna
	 * @param pj
	 */
	public void insertarPj(int fila, int columna, Personaje pj) {
		tablero[fila][columna] = pj;
	}

	/**
	 * Devuelve el coste del personaje que se encuentre en esa posicion
	 * 
	 * @param fila
	 * @param columna
	 * @return int
	 */
	public int getCostePj(int fila, int columna) {
		return tablero[fila][columna].getCoste();
	}

	/**
	 * Comprueba que el personaje pertenece al jugador
	 * 
	 * @param fila
	 * @param columna
	 * @param equipo
	 * @return boolean
	 */
	public boolean comprobarPjPropio(int fila, int columna, int equipo) {
		if (!huecoExiste(fila, columna)) {
			System.out.println(Equipo.ANSI_RED + "Tiene que ser una posicion válida" + Equipo.ANSI_RESET);
			return false;
		} else if (huecoLibre(fila, columna)) {
			System.out.println(Equipo.ANSI_RED + "La posición está vacía" + Equipo.ANSI_RESET);
			return false;
		} else if (tablero[fila][columna].jugador != equipo) {
			System.out.println(Equipo.ANSI_RED + "Tienes que elegir un personaje de tu ejercito" + Equipo.ANSI_RESET);
			return false;
		}

		return true;
	}

	/**
	 * Comprueba que el personaje pertenezca al enemigo
	 * 
	 * @param fila
	 * @param columna
	 * @param equipo
	 * @return boolean
	 */
	public boolean comprobarEnemigo(int fila, int columna, int equipo) {
		if (!huecoExiste(fila, columna)) {
			System.out.println(Equipo.ANSI_RED + "Tiene que ser una posicion válida" + Equipo.ANSI_RESET);
			return false;
		} else if (huecoLibre(fila, columna)) {
			System.out.println(Equipo.ANSI_RED + "La posición está vacía" + Equipo.ANSI_RESET);
			return false;
		} else if (tablero[fila][columna].jugador == equipo) {
			System.out.println(Equipo.ANSI_RED + "Tienes que elegir un personaje enemigo" + Equipo.ANSI_RESET);
			return false;
		}
		return true;
	}

	/**
	 * Metodo utilizado para mover personaje
	 * 
	 * @param fila     posicion actual
	 * @param columna  posicion actual
	 * @param fila2    posicion destino
	 * @param columna2 posicion destino
	 * @param equipo
	 * @return boolean
	 */
	public boolean moverPj(int fila, int columna, int fila2, int columna2, int equipo) {

		if (!huecoExiste(fila2, columna2)) {
			System.out.println(Equipo.ANSI_RED + "Tiene que ser una posicion válida" + Equipo.ANSI_RESET);
			return false;
		} else if (!huecoLibre(fila2, columna2)) {
			System.out.println(Equipo.ANSI_RED + "La posición ya está ocupada" + Equipo.ANSI_RESET);
			return false;
		}

		else {
			tablero[fila2][columna2] = tablero[fila][columna];
			tablero[fila][columna] = null;
			return true;
		}

	}

	/**
	 * Metodo utilizado para atacar
	 * 
	 * @param fila     posicion actual
	 * @param columna  posicion actual
	 * @param fila2    posicion destino
	 * @param columna2 posicion destino
	 * @param e        Equipo
	 * @return boolean
	 */
	public boolean atacar(int fila, int columna, int fila2, int columna2, Equipo e) {

		if (!filaEnRango(fila, columna, fila2) && !columnaEnRango(fila, columna, columna2)) {
			System.out.println("El personaje seleccionado no está en el radio de ataque");
			return false;
		}

		else if (filaEnRango(fila, columna, fila2) && !columnaEnRango(fila, columna, columna2)) {
			System.out.println("El personaje seleccionado no está en el radio de ataque");
			return false;
		}

		else if (!filaEnRango(fila, columna, fila2) && columnaEnRango(fila, columna, columna2)) {
			System.out.println("El personaje seleccionado no está en el radio de ataque");
			return false;
		}

		else {

			int ataquefinal = tablero[fila][columna].agredir() - tablero[fila2][columna2].defensa();

			if (ataquefinal <= 0)
				System.out.println(Equipo.ANSI_GREEN + "Ataque bloqueado" + Equipo.ANSI_RESET);

			else if (tablero[fila2][columna2].getVidaActual() > ataquefinal)
				tablero[fila2][columna2].setVidaActual(tablero[fila2][columna2].getVidaActual() - ataquefinal);

			else {
				for (int i = 0; i < e.ejercito.length; i++) {
					if (tablero[fila2][columna2].equals(e.ejercito[i])) {

						System.arraycopy(e.ejercito, i + 1, e.ejercito, i, e.ejercito.length - i - 1);
						e.ejercito = Arrays.copyOf(e.ejercito, e.ejercito.length - 1);
					}
				}
				tablero[fila2][columna2] = null;
			}

			return true;
		}
	}

	/**
	 * Metodo sobreescrito equals
	 * 
	 * @param obj
	 * @return boolean
	 */
	@Override
	public boolean equals(Object obj) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; i < 8; i++) {
				if (tablero[i][j].equals(obj))
					return true;
			}
		}
		return false;
	}

	/**
	 * Metodo para comprobar rango de ataque
	 * 
	 * @param fila
	 * @param columna
	 * @param fila2
	 * @return boolean
	 */
	public boolean filaEnRango(int fila, int columna, int fila2) {
		int num = fila - fila2;
		if (num < 0)
			num *= -1;

		if (num <= tablero[fila][columna].getRadioAtaque())
			return true;
		return false;

	}

	/**
	 * Metodo para comprobar rango de ataque
	 * 
	 * @param fila
	 * @param columna
	 * @param columna2
	 * @return boolean
	 */
	public boolean columnaEnRango(int fila, int columna, int columna2) {
		int num = columna - columna2;
		if (num < 0)
			num *= -1;

		if (num <= tablero[fila][columna].getRadioAtaque())
			return true;
		return false;
	}

	/**
	 * Metodo para imprimir tablero
	 */
	public void imprimirTablero() {
		System.out.print(" ");
		for (int j = 0; j < tablero[0].length; j++)
			System.out.printf("%-1s%5d", "", j);

		System.out.println();
		for (int i = 0; i < tablero.length; i++) {
			System.out.printf("%-3d", i);
			for (int j = 0; j < tablero[i].length; j++) {
				if (tablero[i][j] == null)
					System.out.printf("|%-5s", " ");
				else
					System.out.printf("|%-5s", tablero[i][j].pjEnTablero());

			}
			System.out.println("|");
		}
	}

}
