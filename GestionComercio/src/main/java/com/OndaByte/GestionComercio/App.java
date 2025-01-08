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
        // Crear la instancia del servidor Javalin
        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost());
            });
        }).start(7000);

        // Filtro de autenticación para rutas protegidas
        Handler filtroAutenticador = ctx -> FiltroAutenticador.filtro(ctx);

        // Rutas protegidas
        app.before("/protegido/*", filtroAutenticador);
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
        app.patch("/Productos/:id/SumarStock", ProductoControl::sumarStock); // Cambiado a PATCH por ser actualización parcial
        app.delete("/Productos/:id", ProductoControl::baja);
        app.put("/Productos/:id/Modificar", ProductoControl::modificar); // Cambiado a PUT por ser una actualización completa
    }
}
/**
import static spark.Spark.*;

 * App
 * @author Fran

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
*/