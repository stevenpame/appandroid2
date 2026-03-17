package com.pipe.avi.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Manager {

    private ConexionBd conexionBd;
    private SQLiteDatabase db;

    public Manager(Context context){
        conexionBd = new ConexionBd(context);
    }

    public void openBdWr(){
        db = conexionBd.getWritableDatabase();
    }

    public void openBdRd(){
        db = conexionBd.getReadableDatabase();
    }

    public void closeBd(){
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    // 🔥 INSERTAR ASPIRANTE CORREGIDO
    public long insertAspirante(Aspirante aspirante){

        openBdWr();

        ContentValues values = new ContentValues();

        values.put("idASPIRANTE", aspirante.getIdASPIRANTE());
        values.put("nombre_completo", aspirante.getNombreCompleto());
        values.put("fechaNacimiento", aspirante.getFechaNacimiento());
        values.put("email", aspirante.getEmail());
        values.put("telefono", aspirante.getTelefono());
        values.put("barrio", aspirante.getBarrio());
        values.put("direccion", aspirante.getDireccion());
        values.put("ocupacion", aspirante.getOcupacion());
        values.put("institucion", aspirante.getInstitucion());
        values.put("password", aspirante.getPassword());

        long id = db.insert("ASPIRANTE", null, values);

        closeBd();

        return id;
    }

    // 🔥 INSERTAR ADMIN
    public long insertAdmin(Admin admin) {

        openBdWr();

        ContentValues values = new ContentValues();
        values.put("idADMIN", admin.getId());
        values.put("nombre", admin.getNombre());
        values.put("email", admin.getEmail());
        values.put("password", admin.getPassword());

        long id = db.insert("ADMIN", null, values);

        closeBd();

        return id;
    }
}
