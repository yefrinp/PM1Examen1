package com.uth.pm1examen1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uth.pm1examen1.db.DbContactos;
import com.uth.pm1examen1.models.Contactos;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditarActivity extends AppCompatActivity {

    EditText txtNombre, txtTelefono, txtNota;
    Button btnGuarda;
    boolean correcto = false;
    Contactos contacto;
    int id = 0;
    public long idPais;
    FloatingActionButton btnTakePhoto;
    ImageView avatar;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static String currentPhotoPath, EditFoto, rutaTempFoto;
    static final int PETICION_ACCESO_CAM = 100;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Actualizar Contacto");
        }

        Spinner spinnerPais = findViewById(R.id.ComboPais);
        String[] countries = getResources().getStringArray(R.array.country_names);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPais.setAdapter(adapter);
        spinnerPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                long selectedItemId = id;
                idPais = selectedItemId;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        txtNombre = findViewById(R.id.txtNombre);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtNota = findViewById(R.id.txtNota);
        btnGuarda = findViewById(R.id.btnGuarda);
        btnGuarda.setText(R.string.btn_editar);
        avatar = findViewById(R.id.avatar);

        btnTakePhoto = findViewById(R.id.btn_change_image);
        avatar = (ImageView) findViewById(R.id.avatar);
        btnTakePhoto.setOnClickListener(view -> permisos());

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("ID");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("ID");
        }

        final DbContactos dbContactos = new DbContactos(EditarActivity.this);
        contacto = dbContactos.verContacto(id);

        if (contacto != null) {
            spinnerPais.setSelection(contacto.getPais());
            txtNombre.setText(contacto.getNombre());
            txtTelefono.setText(contacto.getTelefono());
            txtNota.setText(contacto.getNota());

            File foto = new File(contacto.getAvatar());
            avatar.setImageURI(Uri.fromFile(foto));
            rutaTempFoto = contacto.getAvatar();
        }

        btnGuarda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idPais == 0 ){
                    ShowAlerta("Debe seleccionar un pais.");
                }
                else if(txtNombre.getText().toString().equals("") ){
                    ShowAlerta("Debe Registrar un Nombre.");
                }
                else if(txtTelefono.getText().toString().equals("") ){
                    ShowAlerta("Debe Registrar un numero de Telefono.");
                }
                else if(txtNota.getText().toString().equals("") ){
                    ShowAlerta("Debe Registrar una Nota.");
                }
                else{
                    if (currentPhotoPath.equals("")){
                        EditFoto = rutaTempFoto;
                    }else {
                        EditFoto = currentPhotoPath;
                    }
                    correcto = dbContactos.editarContacto(id, idPais, txtNombre.getText().toString(), txtTelefono.getText().toString(), txtNota.getText().toString(), EditFoto);

                    if(correcto){
                        Toast.makeText(EditarActivity.this, "CONTACTO MODIFICADO", Toast.LENGTH_LONG).show();
                        lista();
                    } else {
                        Toast.makeText(EditarActivity.this, "ERROR AL MODIFICAR CONTACTO", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    private void permisos() {

        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED  &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PETICION_ACCESO_CAM);
        }else {
            patchTakePicture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PETICION_ACCESO_CAM){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                patchTakePicture();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Se necesitan permisos de acceso", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File foto = new File(currentPhotoPath);
            avatar.setImageURI(Uri.fromFile(foto));
            galleryAddImage();
        }
    }

    private  void galleryAddImage(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void patchTakePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.uth.pm1examen1.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    private void ShowAlerta(String mensaje) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Atencion");
        dialog.setMessage(mensaje);
        dialog.setPositiveButton("Aceptar", null);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void lista(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}