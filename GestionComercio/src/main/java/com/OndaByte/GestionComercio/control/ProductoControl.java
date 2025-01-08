package com.OndaByte.GestionComercio.control;
import java.util.List;

import com.OndaByte.GestionComercio.DAO.DAOProducto;
import com.OndaByte.GestionComercio.modelo.Producto;
import com.OndaByte.GestionComercio.util.Controles;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;

public class ProductoControl{
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static void listar(Context ctx) {
        DAOProducto dao = new DAOProducto();
        List<Producto> productos = dao.listar();
        ctx.status(200).json(productos);
    }

    public static void alta(Context ctx) {
        DAOProducto dao = new DAOProducto();
        Producto nuevo;
		try {
			nuevo = objectMapper.readValue(ctx.body(), Producto.class);
			if (dao.alta(nuevo)){
				ctx.status(201).result("Alta exitosa");
				return;
			}
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		ctx.status(500).result("Error al insertar producto");
    }

    public static void modificar(Context ctx)  {
        DAOProducto dao = new DAOProducto();
        String id = ctx.pathParam("id");
        Producto nuevo;
		try {
			nuevo = objectMapper.readValue(ctx.body(), Producto.class);
			 nuevo.setId(Integer.parseInt(id));
			 if (dao.modificar(nuevo)){
				 ctx.status(201).result("Actualización exitosa");
				 return;
			 }
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		ctx.status(500).result("Error al actualizar el producto");
    }

    public static void baja(Context ctx) {
        DAOProducto dao = new DAOProducto();
		
		String id = ctx.pathParam("id");
        if (dao.baja(id, false)){
            ctx.status(200).result("Baja exitosa");
			return;
        }
		
		ctx.status(500).result("Error al dar de baja");
    }

    public static void sumarStock(Context ctx) {
        DAOProducto dao = new DAOProducto();
		
		String id = ctx.pathParam("id");
		String cant = ctx.queryParam("cant");
        if (dao.actualizarStock(id,cant)){
			
			ctx.status(200).result("Stock actualizado");
			return;
        }
		
		ctx.status(500).result("Error al actualizar");
    };

	public void controlesProducto(Producto nuevo){
        Controles.parametroStringNoVacioNulo("nombre", nuevo.getNombre());
		//        parametroStringNoVacioNulo("ingredientes_receta", nuevo.getIngredientes_receta());
        Controles.parametrosNoCeroNegativo("precio_costo",nuevo.getPrecio_costo(),true);
        Controles.parametrosNoCeroNegativo("precio_venta",nuevo.getPrecio_venta(),false);
        Controles.parametrosNoCeroNegativo("stock_actual", nuevo.getStock_actual(),true); 
        if(nuevo.getPrecio_costo() > nuevo.getPrecio_venta()){
            throw new IllegalArgumentException("El costo del producto no puede ser mayor a su precio de venta.");
        }
    }
}



/*
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

	 public void controlesProducto(Producto nuevo){
        Controles.parametroStringNoVacioNulo("nombre", nuevo.getNombre());
//        parametroStringNoVacioNulo("ingredientes_receta", nuevo.getIngredientes_receta());
        Controles.parametrosNoCeroNegativo("precio_costo",nuevo.getPrecio_costo(),true);
        Controles.parametrosNoCeroNegativo("precio_venta",nuevo.getPrecio_venta(),false);
        Controles.parametrosNoCeroNegativo("stock_actual", nuevo.getStock_actual(),true); 
        if(nuevo.getPrecio_costo() > nuevo.getPrecio_venta()){
            throw new IllegalArgumentException("El costo del producto no puede ser mayor a su precio de venta.");
        }
    }
}
*/
