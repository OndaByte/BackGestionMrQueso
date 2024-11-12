package com.OndaByte.GestionComercio.control;
import java.util.ArrayList;
import java.util.List;

import com.OndaByte.GestionComercio.DAO.DAORol;
import com.OndaByte.GestionComercio.DAO.DAOUsuario;
import com.OndaByte.GestionComercio.modelo.Usuario;
import com.OndaByte.GestionComercio.peticiones.LoginPost;
import com.OndaByte.GestionComercio.util.Seguridad;

import spark.Request;
import spark.Response;
import spark.Route;
import org.mindrot.jbcrypt.BCrypt;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UsuarioControl {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Route usuarios = (Request req, Response res) -> {
        DAOUsuario dao = new DAOUsuario();
        List<Usuario> usuarios = dao.listar();
        res.status(200);
		return usuarios.toString();
    };
	// FALTA DEVOLVER TODOS LOS PERMISOS DEL USUARIO
    public static Route login = (Request req, Response res) -> {
		LoginPost peticion = objectMapper.readValue(req.body(), LoginPost.class);
        //ESTO TENGO QUE MOVERLO A MANEJADOR DE EXCEPCIONES/CONTROLES
        if(peticion.getUsuario() == null || peticion.getContra() == null) {
            res.status(400);
            return "Usuario y Contraseña requeridos";
        }
		DAOUsuario dao = new DAOUsuario();
		DAORol daoRol = new DAORol();
		Usuario aux = dao.getUsuario(peticion.getUsuario());
        if (aux != null && BCrypt.checkpw(peticion.getContra(), aux.getContra())){
			
            res.body("{ \"token\" : \" "+Seguridad.getToken(aux.getUsuario())+", \"permisos\" : " + daoRol.getPermisosUsuario(aux.getId()));
            res.status(200);
			return res.body();
        }
        else{
            res.status(500);
            return "Error al loguear";
        }
    };

    public static Route cambiarcontra = (Request req, Response res) -> {
        DAOUsuario dao = new DAOUsuario();
        String usuario = req.queryParams("usuario");
        String contra = req.queryParams("contra");
        String nueva = req.queryParams("nueva");
        
        //ESTO TENGO QUE MOVERLO A MANEJADOR DE EXCEPCIONES/CONTROLES
        if(usuario == null || contra == null) {
            res.status(400);
            return "Usuario y Contraseña requeridos";
        }
        Usuario aux = dao.getUsuario(usuario);
        if (BCrypt.checkpw(contra, aux.getContra())){
            aux.setContra(BCrypt.hashpw(nueva, BCrypt.gensalt()));
            if(dao.modificar(aux)){
                res.status(201);
                return "Contraseña actualizada";
            }
            else{
                res.status(404);
                return "ERROR: No se pudo actualizar la contraseña";
            }
        }
        else{
            res.status(500);
            return "Error al loguear";
        }
    };

    public static Route registrar = (Request req, Response res) -> {
        DAOUsuario dao = new DAOUsuario();
        LoginPost peticion = objectMapper.readValue(req.body(), LoginPost.class);
        //ESTO TENGO QUE MOVERLO A MANEJADOR DE EXCEPCIONES/CONTROLES
        if(peticion.getUsuario() == null || peticion.getContra() == null) {
            res.status(400);
            return "Usuario y Contraseña requeridos";
        }
        Usuario nuevo = new Usuario();
        nuevo.setUsuario(peticion.getUsuario());
        nuevo.setContra(BCrypt.hashpw( peticion.getContra(), BCrypt.gensalt()));
        if(dao.alta(nuevo)){
            res.status(201);
            return "Registro exitoso";
        }
        else{
            res.status(500);
            return "Error al registrar";
        }
    };

    public static Route loginForm = (Request req, Response res) -> {
        return "no implementado sory";
    };
    public static Route baja = (Request req, Response res) -> {
        String id = req.queryParams("id");
        String borrar = req.queryParams("borrar");
        DAOUsuario dao = new DAOUsuario();
        return dao.baja(id,Boolean.valueOf(borrar));
    };
}
