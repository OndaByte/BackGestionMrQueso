package com.OndaByte.GestionComercio.control;
import java.util.List;

import com.OndaByte.GestionComercio.DAO.DAOProducto;
import com.OndaByte.GestionComercio.modelo.Producto;
import com.OndaByte.GestionComercio.util.Seguridad;
import com.fasterxml.jackson.databind.ObjectMapper;

import spark.Request;
import spark.Response;
import spark.Route;

public class ProductoControl {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Route listar = (Request req, Response res) -> {
        DAOProducto dao = new DAOProducto();
        List<Producto> usuarios = dao.listar();
        res.status(200);
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
}
