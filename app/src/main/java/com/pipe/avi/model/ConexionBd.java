package com.pipe.avi.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionBd extends SQLiteOpenHelper {


    public ConexionBd(@Nullable Context context) {
        super(context, Constantes.Name_BD, null, Constantes.VERSION_BD);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE ASPIRANTE(idASPIRANTE INTEGER PRIMARY KEY, nombre_completo TEXT, email TEXT,  telefono TEXT, password TEXT)");

        db.execSQL("CREATE TABLE ADMIN(idADMIN INTEGER PRIMARY KEY, nombre TEXT, email TEXT, password TEXT)");

        db.execSQL("CREATE TABLE REDES(idREDES INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, descripcion TEXT)");

        db.execSQL("CREATE TABLE CENTRO(idCENTRO INTEGER PRIMARY KEY AUTOINCREMENT, descripcion TEXT)");

        db.execSQL("CREATE TABLE AMBIENTE(idAMBIENTE INTEGER PRIMARY KEY AUTOINCREMENT, descripcion TEXT, caracteristicas TEXT, centroId INTEGER, FOREIGN KEY(centroId) REFERENCES CENTRO(idCENTRO))");

        db.execSQL("CREATE TABLE PROGRAMA(idPROGRAMA INTEGER PRIMARY KEY AUTOINCREMENT,nombre TEXT, descripcion TEXT, redesId INTEGER, centroId INTEGER, FOREIGN KEY(redesId) REFERENCES REDES(idREDES), FOREIGN KEY(centroId) REFERENCES CENTRO(idCENTRO))");

        db.execSQL("CREATE TABLE TEST(idTEST INTEGER PRIMARY KEY AUTOINCREMENT, descripcion TEXT)");

        db.execSQL("CREATE TABLE REPORTE(idREPORTE INTEGER PRIMARY KEY AUTOINCREMENT, fecha TEXT, testId INTEGER, aspiranteId INTEGER, FOREIGN KEY(testId) REFERENCES TEST(idTEST), FOREIGN KEY(aspiranteId) REFERENCES ASPIRANTE(idASPIRANTE))");

        db.execSQL("CREATE TABLE RECOMENDACION(idRECOMENDACION INTEGER PRIMARY KEY AUTOINCREMENT,nombre TEXT, descripcion TEXT, programaId INTEGER, reporteId INTEGER, FOREIGN KEY(programaId) REFERENCES PROGRAMA(idPROGRAMA), FOREIGN KEY(reporteId) REFERENCES REPORTE(idREPORTE))");

        db.execSQL("CREATE TABLE PREGUNTAS(idPREGUNTAS INTEGER PRIMARY KEY AUTOINCREMENT, descripcion TEXT, testId INTEGER, FOREIGN KEY(testId) REFERENCES TEST(idTEST))");

        db.execSQL("CREATE TABLE RESPUESTAS(idRESPUESTAS INTEGER PRIMARY KEY AUTOINCREMENT, descripcion TEXT, preguntaId INTEGER, FOREIGN KEY(preguntaId) REFERENCES PREGUNTAS(idPREGUNTAS))");

        db.execSQL("CREATE TABLE RESPUESTASASPIRANTE(idRESPUESTASASP INTEGER PRIMARY KEY AUTOINCREMENT, aspiranteId INTEGER, testId INTEGER, preguntaId INTEGER, respuestaId INTEGER, reporteId INTEGER, FOREIGN KEY(aspiranteId) REFERENCES ASPIRANTE(idASPIRANTE), FOREIGN KEY(testId) REFERENCES TEST(idTEST), FOREIGN KEY(preguntaId) REFERENCES PREGUNTAS(idPREGUNTAS), FOREIGN KEY(respuestaId) REFERENCES RESPUESTAS(idRESPUESTAS), FOREIGN KEY(reporteId) REFERENCES REPORTE(idREPORTE))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE ASPIRANTE");
        db.execSQL("DROP TABLE ADMIN");
        db.execSQL("DROP TABLE REDES");
        db.execSQL("DROP TABLE CENTRO");
        db.execSQL("DROP TABLE AMBIENTE");
        db.execSQL("DROP TABLE PROGRAMA");
        db.execSQL("DROP TABLE TEST");
        db.execSQL("DROP TABLE REPORTE");
        db.execSQL("DROP TABLE RECOMENDACION");
        db.execSQL("DROP TABLE PREGUNTAS");
        db.execSQL("DROP TABLE RESPUESTAS");
        db.execSQL("DROP TABLE RESPUESTASASPIRANTE");

    }
}
