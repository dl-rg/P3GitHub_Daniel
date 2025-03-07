package practicas;

import java.util.Arrays;

/**
 * @author Daniel Ramirez Gurruchaga
 * @author Juan Facundo Luciaw
 *         Esta clase se utiliza para diferenciar los equipos.
 */
public class Equipo {

	private int saldo = 50;
	Personaje ejercito[] = new Personaje[0];

	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_RESET = "\u001B[0m";

	/**
	 * Metodo para comprobar que el saldo es suficiente
	 * 
	 * @param p Personaje
	 * @return boolean
	 */
	public boolean saldoSuficiente(Personaje p) {
		if (getSaldo() >= p.getCoste())
			return true;
		else
			return false;
	}

	/**
	 * Comprueba si algun personaje del ejercito esta dañado
	 * 
	 * @return boolean
	 */
	public boolean vidaPochaEjercito() {
		for (int i = 0; i < ejercito.length; i++) {
			if (ejercito[i].getVidaActual() != ejercito[i].getVidaInicial())
				return true;
		}
		return false;
	}

	/**
	 * Metodo para curar ejercito
	 */
	public void curar() {
		int num;
		for (int i = 0; i < ejercito.length; i++) {
			if (ejercito[i].getVidaActual() != ejercito[i].getVidaInicial()) {
				num = (int) (Math.random() * (ejercito[i].getVidaInicial() + 1));

				if (ejercito[i].getVidaActual() + num >= ejercito[i].vidaInicial)
					ejercito[i].setVidaActual(ejercito[i].getVidaInicial());

				else
					ejercito[i].setVidaActual(ejercito[i].getVidaActual() + num);

			} else
				continue;
		}
	}

	/**
	 * Metodo para añadir un personaje al array ejercito
	 * 
	 * @param p Personaje
	 */
	public void insertarPjEjercito(Personaje p) {
		if (saldoSuficiente(p)) {
			ejercito = Arrays.copyOf(ejercito, ejercito.length + 1);
			ejercito[ejercito.length - 1] = p;
		}
	}

	/**
	 * Comprueba si el ejercito esta vacio
	 * 
	 * @return boolean
	 */
	public boolean ejercitoVacio() {
		return ejercito.length == 0;
	}

	public int getLenght() {
		return ejercito.length;
	}

	public int getSaldo() {
		return saldo;
	}

	public void setSaldo(int saldo) {
		this.saldo = saldo;
	}
}