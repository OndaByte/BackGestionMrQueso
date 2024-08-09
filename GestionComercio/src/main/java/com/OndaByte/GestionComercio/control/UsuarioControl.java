package com.OndaByte.GestionComercio.control;
import java.util.ArrayList;
import java.util.List;

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

    public static Route login = (Request req, Response res) -> {
        LoginPost peticion = objectMapper.readValue(req.body(), LoginPost.class);
        //ESTO TENGO QUE MOVERLO A MANEJADOR DE EXCEPCIONES/CONTROLES
        if(peticion.getUsuario() == null || peticion.getContra() == null) {
            res.status(400);
            return "Usuario y Contraseña requeridos";
        }
        Usuario aux = getUsuario(peticion.getUsuario());

        if (aux != null && BCrypt.checkpw(peticion.getContra(), aux.getContra())){
            res.header("Token",Seguridad.getToken(peticion.getUsuario()));
            res.status(200);
            return "Loguin exitoso";
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
        Usuario aux = getUsuario(usuario);
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

    private static Usuario getUsuario(String usuario){
        DAOUsuario dao = new DAOUsuario();
        List<String> campos = new ArrayList();
        List<String> valores = new ArrayList();
        List<Integer> condiciones = new ArrayList();
        campos.add("usuario");
        valores.add(usuario);
        condiciones.add(0);
        List<Usuario> usuarios = dao.filtrar(campos, valores, condiciones);
        if(usuarios.size()>0){
            return usuarios.get(0);
        }
        else{
            return null;
        }
    }
}
