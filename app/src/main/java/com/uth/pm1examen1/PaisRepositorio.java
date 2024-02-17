package com.uth.pm1examen1;

public class PaisRepositorio {


    public class PaisRepositorio {

        private SQLiteConexion sqliteConexion;

        public PaisRepositorio(Context context) {
            sqliteConexion = new SQLiteConexion(context);
        }

        public void agregarPais(String nombre) {
            try {
                sqliteConexion.abrirConexion();
                SQLiteDatabase database = sqliteConexion.obtenerDatabase();

                ContentValues values = new ContentValues();
                values.put("nombre", nombre);

                database.insert("paises", null, values);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                sqliteConexion.cerrarConexion();
            }
        }

        public void editarPais(int id, String nuevoNombre) {
            try {
                sqliteConexion.abrirConexion();
                SQLiteDatabase database = sqliteConexion.obtenerDatabase();

                ContentValues values = new ContentValues();
                values.put("nombre", nuevoNombre);

                String[] whereArgs = {String.valueOf(id)};

                database.update("paises", values, "_id=?", whereArgs);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                sqliteConexion.cerrarConexion();
            }
        }

        public void eliminarPais(int id) {
            try {
                sqliteConexion.abrirConexion();
                SQLiteDatabase database = sqliteConexion.obtenerDatabase();

                String[] whereArgs = {String.valueOf(id)};

                database.delete("paises", "_id=?", whereArgs);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                sqliteConexion.cerrarConexion();
            }
        }

        public List<Pais> obtenerTodosLosPaises() {
            List<Pais> listaPaises = new ArrayList<>();

            try {
                sqliteConexion.abrirConexion();
                SQLiteDatabase database = sqliteConexion.obtenerDatabase();

                Cursor cursor = database.query("paises", null, null, null, null, null, null);

                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex("_id"));
                    String nombre = cursor.getString(cursor.getColumnIndex("nombre"));

                    Pais pais = new Pais(id, nombre);
                    listaPaises.add(pais);
                }

                cursor.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                sqliteConexion.cerrarConexion();
            }

            return listaPaises;
        }
    }

}
