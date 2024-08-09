package com.OndaByte.GestionComercio.DAO;

import com.OndaByte.GestionComercio.modelo.Producto;

public class DAOProducto extends ABMDAO<Producto>{
    private String clave = "id";

    public DAOProducto(){
        super();
    }

    public Class<Producto> getClase(){
        return Producto.class;
    };

    public String getClave(){return this.clave;}
    
}
