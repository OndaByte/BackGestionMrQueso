package com.OndaByte.GestionComercio.DAO;

import com.OndaByte.GestionComercio.modelo.Venta;

public class DAOVenta extends ABMDAO<Venta> {
	private String clave = "id";

	public Class<Venta> getClase() {
		return Venta.class;
	}

	public String getClave() {
		return this.clave;
	}	
}
