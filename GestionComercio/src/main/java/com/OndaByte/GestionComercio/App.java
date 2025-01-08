package com.OndaByte.GestionComercio;
import com.OndaByte.GestionComercio.control.ProductoControl;
import com.OndaByte.GestionComercio.control.UsuarioControl;
import com.OndaByte.GestionComercio.filtros.FiltroAutenticador;

import io.javalin.Javalin;
import io.javalin.http.Handler;

/**
 * App
 * Autor: Fran
 */
public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(4567);

        // Rutas protegidas
        app.before("/protegido/*", FiltroAutenticador::filtro);
        app.post("/protegido/actualizar", UsuarioControl::cambiarcontra);
        app.get("/protegido/usuarios", UsuarioControl::usuarios);
        app.post("/protegido/eliminar", UsuarioControl::baja);

        // Rutas públicas de usuario
        app.post("/registrar", UsuarioControl::registrar);
        app.get("/login", UsuarioControl::loginForm);
        app.post("/login", UsuarioControl::login);

        // Rutas para productos
        app.get("/Productos", ProductoControl::listar);
        app.post("/Productos/Alta", ProductoControl::alta);
        app.patch("/Productos/{id}/SumarStock", ProductoControl::sumarStock); // Cambiado a PATCH por ser actualización parcial
        app.delete("/Productos/{id}", ProductoControl::baja);
        app.put("/Productos/{id}/Modificar", ProductoControl::modificar); // Cambiado a PUT por ser una actualización completa
    }
}
