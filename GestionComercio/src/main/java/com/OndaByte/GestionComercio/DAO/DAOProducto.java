package com.OndaByte.GestionComercio.DAO;

import com.OndaByte.GestionComercio.modelo.Producto;
import com.OndaByte.GestionComercio.util.Log;

public class DAOProducto extends ABMDAO<Producto>{
    private String clave = "id";

    public DAOProducto(){
        super();
    }

    public Class<Producto> getClase(){
        return Producto.class;
    };

    public String getClave(){return this.clave;}

    public boolean actualizarStock(String id, String cant){
        Producto aux = this.filtrar(id);
        if (aux != null){
            try {
				aux.sumarStock(Integer.parseInt(cant));
			} catch (Exception e) {
                Log.log(e,DAOProducto.class);
			}
            this.modificar(aux);
            return true;
        }
        return false;
    }
    
}
