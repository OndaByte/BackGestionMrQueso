package com.OndaByte.GestionComercio.control;
import java.util.ArrayList;
import java.util.List;

import com.OndaByte.GestionComercio.DAO.DAOProducto;
import com.OndaByte.GestionComercio.modelo.Producto;
import com.OndaByte.GestionComercio.util.Seguridad;

import spark.Request;
import spark.Response;
import spark.Route;
import org.mindrot.jbcrypt.BCrypt;

public class ProductoControl {

    public static Route listar = (Request req, Response res) -> {
        DAOProducto dao = new DAOProducto();
        List<Producto> usuarios = dao.listar();
        res.status(200);
        return usuarios.toString();
    };

    public static Route alta = (Request req, Response res) -> {
        DAOProducto dao = new DAOProducto();
        String usuario = req.queryParams("usuario");
        String contra = req.queryParams("contra");
        return "Error al registrar";
    };
}
