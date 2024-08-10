package com.OndaByte.GestionComercio.DAO;

import java.lang.reflect.Field;
import java.util.List;
import java.util.ArrayList;
import org.sql2o.Connection;

import com.OndaByte.GestionComercio.util.Log;

/**
 * ABMDAO generico SQL para entidades simples, soporta maximo una herencia de entidad, ej Usuario extend ObjetoBD
 * @author Fran
 * @param <T>
 */
public abstract class ABMDAO <T> {

    private List<Field> campos = new ArrayList<Field>();

    abstract public Class<T> getClase();
    
    abstract public String getClave();

    public ABMDAO(){setCampos();}
    
    public String getTabla(){return this.getClase().getSimpleName();}

    private void setCampos(){
        Class clase = this.getClase();
        while(clase.getName().contains("OndaByte") && clase.getName().contains("modelo")){
            for (Field f : clase.getDeclaredFields()){
                campos.add(f);
            }
            clase = clase.getSuperclass();
        }
    }

    public boolean alta(T t) {
        try(Connection con = DAOSql2o.getSql2o().beginTransaction()){
            if(t.getClass() != this.getClase()){
                throw (new Exception("ERROR: el objeto pasado por parametro es del tipo incorrecto, el tipo de este DAO es: "+this.getClase().getName()));
            }
            String columnas = " (";
            String valores = " (";
            for (Field f : this.campos){
                if (f.getName().equals(this.getClave())) continue;
                columnas = columnas + f.getName() + ",";
                valores = valores + ":" + f.getName() + ",";
            }
            valores = valores.substring(0,valores.length()-1) + ")";
            columnas = columnas.substring(0,columnas.length()-1)+ ")";
            String query;
            query = "INSERT INTO " + this.getTabla() + columnas + " VALUES" + valores;
            con.createQuery(query).bind(t).executeUpdate();
            con.commit();
            return true;
        }
        catch (Exception e){
            Log.log(e, ABMDAO.class);
        }
        return false;
    }

    public boolean modificar(T t) {
        try(Connection con = DAOSql2o.getSql2o().beginTransaction()){
            if(t.getClass() != this.getClase()){
                throw (new Exception("ERROR: el objeto pasado por parametro es del tipo incorrecto, el tipo de este DAO es: "+this.getClase().getName()));
            }
            String set="";
            String query;
            for (Field f : this.campos) {
                if(f.getName().equals(this.getClave()) || f.getName().equals("ultMod") || f.getName().equals("creado")) continue;
                set = set + f.getName() + "=:" + f.getName()+", ";
            }
            if(set.length()>2)
                set = set.substring(0,set.length()-2);
            query = "UPDATE " + this.getTabla() + " SET " + set + " WHERE "+this.getClave() + "=:"+this.getClave();
            con.createQuery(query).bind(t).executeUpdate();
            con.commit();
            return true;
        }
        catch (Exception e){
            Log.log(e, ABMDAO.class);
        }
        return false;
    }

    public boolean baja(String id, boolean borrar){
        try(Connection con = DAOSql2o.getSql2o().open()){
            String query;
            query = (borrar ? "DELETE FROM ": "UPDATE ") 
                + this.getTabla() 
                + (borrar ? " " : " SET estado=\"INACTIVO\" ")+"WHERE "+this.getClave() + "=:"+this.getClave()
                + (borrar ? "" : " AND estado=\"ACTIVO\"");
            con.createQuery(query, true).addParameter(this.getClave(), id).executeUpdate();
            return true;
        }
        catch(Exception e) {
            Log.log(e, ABMDAO.class);
        }
        return false;
    }
    
    public List<T> listar(){
        try{
            Class c = this.getClase();
            String query = "SELECT * FROM "+ this.getTabla() +" WHERE estado=\"ACTIVO\"";
            Connection con = DAOSql2o.getSql2o().open();
            return con.createQuery(query).executeAndFetch(c);
        }
        catch (Exception e){
            Log.log(e, ABMDAO.class);
        }
        return null;
    }

    public List<T> listar(String... ids){
        try{
            String aux="";
            for (String id : ids){
                aux += this.getTabla()+"."+this.getClave()+"="+id+" OR ";
            }
            aux = aux.length() > 2 ? aux.substring(0,aux.length()-4) : aux;

            String query = "SELECT DISTINCT * FROM "+ this.getTabla() + " WHERE "+aux +" AND estado=\"ACTIVO\"";
            Connection con = DAOSql2o.getSql2o().open();
            return con.createQuery(query).executeAndFetch(this.getClase());
        }
        catch(Exception e) {
            Log.log(e, ABMDAO.class);
        }
        return null;
    }

    public T filtrar(String id){
        try {
            String query = "SELECT * FROM "+ this.getTabla() + " WHERE "+ this.getClave()+" = :"+this.getClave();
            Connection con = DAOSql2o.getSql2o().open();
            return con.createQuery(query).addParameter(this.getClave(), id).executeAndFetchFirst(this.getClase());
        } catch (Exception e) {
            Log.log(e, ABMDAO.class);
        }
        return null;
    }

    public List<T> filtrar(List<String> campos, List<String> valores, List<Integer> condiciones){
        try{
            if(campos == null || valores == null || condiciones == null || condiciones.size() != campos.size() || campos.size() != valores.size()){
                throw(new Exception("Las listas deben tener el mismo tamaño"));
            }
            String queryAux = " ";
            int i = 0; 
            for (String campo : campos){
                if(!this.campos.stream().anyMatch(x -> x.getName().equals(campo))){
                    throw(new Exception("El campo \""+campo+"\" no existe"));
                } 
                switch (condiciones.get(i)) {
                    case 0:
                        queryAux +=campos.get(i)+"=\""+valores.get(i)+"\" AND ";
                        break;
                    case 1:
                        queryAux +=campos.get(i)+"<=\""+valores.get(i)+"\" AND ";
                        break;
                    case 2:
                        queryAux +=campos.get(i)+"<\""+valores.get(i)+"\" AND ";
                        break;
                    case 3:
                        queryAux +=campos.get(i)+">=\""+valores.get(i)+"\" AND ";
                        break;
                    case 4:
                        queryAux +=campos.get(i)+">\""+valores.get(i)+"\" AND ";
                        break;
                    case 5:
                        queryAux +=campos.get(i)+" LIKE \""+valores.get(i)+"\" AND ";
                        break;
                }
                i++;
            }
            if(queryAux.length() > 1){queryAux = queryAux.substring(0, queryAux.length()-5);}
            String query = "SELECT * FROM "+ this.getTabla() + " WHERE";
            query+= queryAux; 

            Connection con = DAOSql2o.getSql2o().open();
            return con.createQuery(query).executeAndFetch(this.getClase());
        }
        catch (Exception e){
            Log.log(e, ABMDAO.class);
        }
        return null;
    }
}
