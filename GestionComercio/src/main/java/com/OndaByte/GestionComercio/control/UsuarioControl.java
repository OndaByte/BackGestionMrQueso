package com.OndaByte.GestionComercio.control;
import java.util.List;

import com.OndaByte.GestionComercio.DAO.DAORol;
import com.OndaByte.GestionComercio.DAO.DAOUsuario;
import com.OndaByte.GestionComercio.modelo.Usuario;
import com.OndaByte.GestionComercio.peticiones.LoginPost;
import com.OndaByte.GestionComercio.util.Seguridad;


import io.javalin.http.Context;

import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

  public class UsuarioControl{
        private static ObjectMapper objectMapper = new ObjectMapper();
	
        public static void usuarios (Context ctx) {
            DAOUsuario dao = new DAOUsuario();
            List<Usuario> usuarios = dao.listar();
            ctx.status(200).json(usuarios);
        }

public static void login(Context ctx) {
		LoginPost peticion;
		try {
			peticion = objectMapper.readValue(ctx.body(), LoginPost.class);

			//ESTO TENGO QUE MOVERLO A MANEJADOR DE EXCEPCIONES/CONTROLES
			if(peticion.getUsuario() == null || peticion.getContra() == null) {
				ctx.status(400).result("Usuario y contraseña requeridos");
				return;
			}

			DAOUsuario dao = new DAOUsuario();
			DAORol daoRol = new DAORol();
			Usuario aux = dao.getUsuario(peticion.getUsuario());

			if (aux != null && BCrypt.checkpw(peticion.getContra(), aux.getContra())){
				ctx.status(200).result("{ \"token\" : \""+Seguridad.getToken(aux.getUsuario())+"\", \"permisos\" : " + daoRol.getPermisosUsuario(aux.getId()) +"}\n");
			}
			else{
				ctx.status(500).result("Error al loguear");
			}
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

	public static void cambiarcontra(Context ctx) {
        String usuario = ctx.queryParam("usuario");
        String contra = ctx.queryParam("contra");
        String nueva = ctx.queryParam("nueva");

        //ESTO TENGO QUE MOVERLO A MANEJADOR DE EXCEPCIONES/CONTROLES
        if(usuario == null || contra == null) {
            ctx.status(400).result("Usuario y Contraseña requeridos");
        }

        DAOUsuario dao = new DAOUsuario();
        Usuario aux = dao.getUsuario(usuario);

        if (BCrypt.checkpw(contra, aux.getContra())){
            aux.setContra(BCrypt.hashpw(nueva, BCrypt.gensalt()));
            if(dao.modificar(aux)){
                ctx.status(201).result("Contraseña actualizada");
            }
            else{
                ctx.status(404).result("ERROR: No se pudo actualizar la contraseña");
            }
        }
        else{
            ctx.status(500).result("Error al loguear");
        }
    }

    public static void registrar(Context ctx) {
        LoginPost peticion;
		try {
			peticion = objectMapper.readValue(ctx.body(), LoginPost.class);
			//ESTO TENGO QUE MOVERLO A MANEJADOR DE EXCEPCIONES/CONTROLES
			if(peticion.getUsuario() == null || peticion.getContra() == null) {
				ctx.status(400).result("Usuario y Contraseña requeridos");
			}

			DAOUsuario dao = new DAOUsuario();
			Usuario nuevo = new Usuario();
			nuevo.setUsuario(peticion.getUsuario());
			nuevo.setContra(BCrypt.hashpw( peticion.getContra(), BCrypt.gensalt()));

			if(dao.alta(nuevo)){
				ctx.status(201).result("Registro exitoso");
			}
			else{
				ctx.status(500).result("Error al registrar");
			}
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    public static void loginForm(Context ctx){
        ctx.status(404).result("no implementado sory");
    }

	public static void baja(Context ctx) {
        String id = ctx.queryParam("id");
        String borrar = ctx.queryParam("borrar");

        if (id == null || borrar == null) {
            ctx.status(400).result("ID y borrar son requeridos");
            return;
        }

        DAOUsuario dao = new DAOUsuario();
        boolean resultado = dao.baja(id, Boolean.parseBoolean(borrar));
        ctx.status(200).result(String.valueOf(resultado));
    }
	
}
