package com.OndaByte.GestionComercio.control;

/**
 * CajaControl
 */
import java.util.List;

import com.OndaByte.GestionComercio.DAO.DAOProducto;
import com.OndaByte.GestionComercio.DAO.DAOCaja;
import com.OndaByte.GestionComercio.modelo.Caja;
import com.OndaByte.GestionComercio.modelo.Producto;
import com.OndaByte.GestionComercio.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;

import spark.Request;
import spark.Response;
import spark.Route;

public class CajaControl {

    private static ObjectMapper objectMapper = new ObjectMapper();
	
	//AbrirCaja
	public static Route abrirCaja = (Request req, Response res) -> {
		try{

			float dineroI = Float.parseFloat(req.params("dineroI"));
			if(dineroI<0){
				res.status(400);
				return "Error. No podes abrir la caja con saldo negativo mostro.";
		    }

			DAOCaja dao = new DAOCaja();
			Caja actual = dao.getCaja();
			if(actual != null){
				res.status(400);
				return "Caja ya abierta kpo";
			}
			if(dao.alta(new Caja(dineroI))){
				res.status(200);
				return "Caja abierta con exito kpo";
			}
		}catch(Exception e){
			Log.log(e,CajaControl.class);
		}
		
    };
	//CerrarCaja
    public static Route alta = (Request req, Response res) -> {
        try{
			DAOCaja dao = new DAOCaja();
			dao.cerrarCaja();
			res.status(200);
			return "Caja cerrada kpo";
		}
		catch (Exception e){
			res.status(400);
			return "No hay cajas abiertas kpo";
		}
    };
	//Caja
    public static Route getCaja = (Request req, Response res) -> {
		try{
			DAOCaja dao = new DAOCaja();
			Caja aux = dao.getCaja();
			if(aux == null){
				res.status(404);
				return "No hay caja man";
			}
			res.status(200);
			return aux.toString();
		}
		catch(Exception e){
			res.status(404);
			return "No se que paso man, error inesperado";
		}
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
