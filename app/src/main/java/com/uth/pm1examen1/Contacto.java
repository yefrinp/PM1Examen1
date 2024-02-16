package com.uth.pm1examen1;

public class Contacto {
    // Clase Contacto

    public class Contacto {
        private String pais;
        private String nombre;
        private String telefono;
        private String nota;
        private byte[] imagen; // La imagen se guarda como un arreglo de bytes

        // Constructor
        public Contacto(String pais, String nombre, String telefono, String nota, byte[] imagen) {
            this.pais = pais;
            this.nombre = nombre;
            this.telefono = telefono;
            this.nota = nota;
            this.imagen = imagen;
        }
        // Métodos de acceso
        public String getPais() {
            return pais;
        }

        public void setPais(String pais) {
            this.pais = pais;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getNota() {
            return nota;
        }

        public void setNota(String nota) {
            this.nota = nota;
        }

        public byte[] getImagen() {
            return imagen;
        }

        public void setImagen(byte[] imagen) {
            this.imagen = imagen;
        }
    }
//DatabaseHelper para SQLite

    import android.content.Context;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;

    public class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "contactos.db";
        private static final int DATABASE_VERSION = 1;

        private static final String TABLE_CONTACTOS = "contactos";
        private static final String COLUMN_ID = "id";
        private static final String COLUMN_PAIS = "pais";
        private static final String COLUMN_NOMBRE = "nombre";
        private static final String COLUMN_TELEFONO = "telefono";
        private static final String COLUMN_NOTA = "nota";
        private static final String COLUMN_IMAGEN = "imagen";
        // Crear tabla
        private static final String CREATE_TABLE_CONTACTOS = "CREATE TABLE " + TABLE_CONTACTOS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PAIS + " TEXT," +
                COLUMN_NOMBRE + " TEXT," +
                COLUMN_TELEFONO + " TEXT," +
                COLUMN_NOTA + " TEXT," +
                COLUMN_IMAGEN + " BLOB" + ")";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_CONTACTOS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTOS);
            onCreate(db);
        }
    }


    }
