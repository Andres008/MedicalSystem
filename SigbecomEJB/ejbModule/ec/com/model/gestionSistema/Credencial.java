/**
 * 
 */
package ec.com.model.gestionSistema;

import ec.com.model.dao.entity.UsrSocio;

/**
 * Token de seguridad que se conforma del ID del usuario, el codigo de seguridad
 * (codificado con MD5) y la direccion IP del host cliente.
 * 
 * @author mrea
 *
 */
public class Credencial {
	private UsrSocio objUsrSocio;
	private String direccionIP;
	private String correo;
	private String primerInicio;

	public Credencial() {
	}

	

	public UsrSocio getObjUsrSocio() {
		return objUsrSocio;
	}



	public void setObjUsrSocio(UsrSocio objUsrSocio) {
		this.objUsrSocio = objUsrSocio;
	}



	public String getDireccionIP() {
		return direccionIP;
	}

	public void setDireccionIP(String direccionIP) {
		this.direccionIP = direccionIP;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getPrimerInicio() {
		return primerInicio;
	}

	public void setPrimerInicio(String primerInicio) {
		this.primerInicio = primerInicio;
	}

}
