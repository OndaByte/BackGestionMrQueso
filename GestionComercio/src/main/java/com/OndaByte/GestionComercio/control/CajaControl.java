package com.OndaByte.GestionComercio.control;

/**
 * CajaControl
 */
import java.util.List;

import com.OndaByte.GestionComercio.DAO.DAOProducto;
import com.OndaByte.GestionComercio.DAO.DAOCaja;
import com.OndaByte.GestionComercio.modelo.Caja;
import com.OndaByte.GestionComercio.modelo.Producto;
import com.fasterxml.jackson.databind.ObjectMapper;

import spark.Request;
import spark.Response;
import spark.Route;

public class CajaControl {

    private static ObjectMapper objectMapper = new ObjectMapper();

	
	/**
	* Method: Get.
	* Params: dineroI
 	*/
    public static Route abrirCaja = (Request req, Response res) -> {
		try{
			float dineroI = Float.parseFloat(req.params("dineroI"));
			if(dineroI<0){
				res.status(400);
				return "";

		    }
		}catch(Exception nfe){
			//logger
		}
	    DAOCaja dao = new DAOCaja();
		Caja actual = dao.getCaja();
		if(actual != null){
			res.status(400);
			return actual.toString();
		}
		
		
        return usuarios.toString();
    };

    public static Route alta = (Request req, Response res) -> {
        DAOProducto dao = new DAOProducto();
        Producto nuevo = objectMapper.readValue(req.body(), Producto.class);
        if (dao.alta(nuevo)){
            res.status(201);
            return "Alta exitosa";
        }
        res.status(500);
        return "Error al insertar producto";
    };

    public static Route modificar = (Request req, Response res) -> {
        DAOProducto dao = new DAOProducto();
        String id = req.params(":id");
        Producto nuevo = objectMapper.readValue(req.body(), Producto.class);
        nuevo.setId(Integer.parseInt(id));
        if (dao.modificar(nuevo)){
            res.status(201);
            return "Actualizacion exitosa";
        }
        res.status(500);
        return "Error al actualizar el producto";
    };

    public static Route baja = (Request req, Response res) -> {
        DAOProducto dao = new DAOProducto();
        String id = req.params(":id");
        if (dao.baja(id, false)){
            res.status(200);
            return "Baja exitosa";
        }
        res.status(500);
        return "Erro al dar de baja";
    };

    public static Route sumarStock = (Request req, Response res) -> {
        DAOProducto dao = new DAOProducto();
        String id = req.params(":id");
        String cant = req.queryParams("cant");
        if (dao.actualizarStock(id,cant)){
            res.status(200);
            return "Stock actualizado";
        }
        res.status(500);
        return "Error al actualizar";
    };
}

}
