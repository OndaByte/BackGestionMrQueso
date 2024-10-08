package com.OndaByte.GestionComercio.modelo;

/**
 * Usuario
 * @author Fran
 */
public class Usuario extends ObjetoBD{
	private String contra;
    private String usuario;

	public String getContra() {
		return contra;
	}
	public void setContra(String contra) {
		this.contra = contra;
	}
    public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

    public String toString(){
        return "{"+super.toString()+" \"contra\" : \""+this.contra+"\", \"usuario\" : \""+this.usuario+"\"}";
    }

}
