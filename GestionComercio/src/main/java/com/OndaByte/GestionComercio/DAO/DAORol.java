package com.OndaByte.GestionComercio.DAO;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sql2o.Connection;

import com.OndaByte.GestionComercio.modelo.Rol;

/**
 * DAORol
 */
public class DAORol extends ABMDAO<Rol> {
    private String clave = "id";

	@Override
	public Class<Rol> getClase() {
        return Rol.class;
	}

	@Override
	public String getClave() {
        return this.clave;
	}

    public List<Rol> getRolesUsuario(int id){
        String query = "SELECT Rol.* FROM Rol JOIN UsuarioRol ON Rol.id = UsuarioRol.rol_id WHERE UsuarioRol.usuario_id=:id";
        try(Connection con = DAOSql2o.getSql2o().open()){
            return con.createQuery(query).addParameter("id",id).executeAndFetch(Rol.class);
        }
      catch(Exception e) {
            Logger.getLogger(DAORol.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

}
