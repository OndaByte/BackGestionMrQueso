package com.OndaByte.GestionComercio.filtros;
 

import io.javalin.http.Context;
import io.javalin.http.Handler;

import com.OndaByte.GestionComercio.util.Seguridad;

/**
 * FiltroAutenticador
 * Autor: Fran
 */
public class FiltroAutenticador {
    public static Handler filtro = ctx -> {
        String aux = ctx.header("Token"); // Obtiene el encabezado "Token"
        
        if (aux != null) {
            String token = Seguridad.validar(aux); // Valida el token recibido
            if (token != null && token.equals(aux)) {
                ctx.header("Token", token); // Agrega el token al encabezado de respuesta
                ctx.status(204); // Devuelve un código de estado 204 (sin contenido)
            } else {
                ctx.redirect("/login"); // Redirige al login si el token no es válido
            }
        } else {
            ctx.redirect("/login"); // Redirige al login si no se encuentra el token
        }
    };
}

/**
 * 
 * 
 * 
import spark.Filter;
import spark.Request;
import spark.Response;

import com.OndaByte.GestionComercio.util.Seguridad;


public class FiltroAutenticador {
    public static Filter filtro = (Request req, Response res) -> {
        String aux = req.headers("Token");
        if (aux != null){
            String token = Seguridad.validar(aux);
            if(token != null && token.equals(aux)){
                res.header("Token", token);
                res.status(204);
            }
            else{
                res.redirect("/login");
            }
        }
        else{
            res.redirect("/login");
        }
    };
}

 */