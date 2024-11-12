package com.OndaByte.GestionComercio;
import com.OndaByte.GestionComercio.control.ProductoControl;
import com.OndaByte.GestionComercio.control.UsuarioControl;
import com.OndaByte.GestionComercio.filtros.FiltroAutenticador;


import static spark.Spark.*;
/**
 * App
 * @author Fran
 */
public class App 
{
    public static void main( String[] args )
    {
        before("/protegido/*", FiltroAutenticador.filtro);
        post("/protegido/actualizar", UsuarioControl.cambiarcontra);
        get("/protegido/usuarios", UsuarioControl.usuarios);
        post("/protegido/eliminar", UsuarioControl.baja);
        post("/registrar", UsuarioControl.registrar);
        get("/login", UsuarioControl.loginForm);
        post("/login", UsuarioControl.login);


        get("/Productos", ProductoControl.listar);
        post("/Productos/Alta", ProductoControl.alta);
        post("/Productos/:id/SumarStock", ProductoControl.sumarStock);
        delete("/Productos/:id", ProductoControl.baja);
        post("/Productos/:id/Modificar", ProductoControl.modificar);
    }
}
