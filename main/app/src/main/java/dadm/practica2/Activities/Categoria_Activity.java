package dadm.practica2.Activities;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import dadm.practica2.DataTransformationObjects.DtoCategoria;
import dadm.practica2.Helpers.CategoriaHelper;
import dadm.practica2.Interfaces.Intent_Properties;
import dadm.practica2.ListViewAdapters_Categoria.Tile_ListView_Adapter;
import dadm.practica2.R;

public class Categoria_Activity extends AppCompatActivity implements Intent_Properties, NavigationView.OnNavigationItemSelectedListener{

    //VISTAS
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    //LIST VIEW Y SUS ADAPTADORES
    private ListView lvCategorias;
    private Tile_ListView_Adapter tile_Adapter;

    //OTROS
    private CategoriaHelper categoriaHelper;
    private ArrayList<DtoCategoria> arrayCategorias;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorias_activity);

        //INSTANCIAMOS LAS VISTAS
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        lvCategorias = findViewById(R.id.listViewCategorias);

        //TOOLBAR Y NAVIGATION DRAWER
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //CARGAR CATEGORIAS, ADAPTADORES Y MENU CONTEXTUAL
        cargarCategorias();
        tile_Adapter = new Tile_ListView_Adapter(this, arrayCategorias);
        lvCategorias.setAdapter(tile_Adapter);
        registerForContextMenu(lvCategorias);

        //LOGICA VISTAS
        fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(new Intent(Categoria_Activity.this , AddCategoria_Activity.class));
            }
        });
    }


    private void cargarCategorias() {
        arrayCategorias = new ArrayList<DtoCategoria>();
        categoriaHelper  = new CategoriaHelper();
        categoriaHelper.cargarCategorias(getApplicationContext());
        arrayCategorias = categoriaHelper.getCategorias();

        if (arrayCategorias.isEmpty()) {
            Toast.makeText(getBaseContext(), getString(R.string.no_registros), Toast.LENGTH_LONG).show();
        }
    }

    // CONTEXT MENU //
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_open_delete_register, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.ver:
                intent = new Intent(Categoria_Activity.this , AddCategoria_Activity.class);
                intent.putExtra(PARAM1,true);
                intent.putExtra(PARAM2, info.position);
                startActivity(intent);
                return true;
            case R.id.borrar:
                categoriaHelper.cargarCategorias(getApplicationContext());
                arrayCategorias = categoriaHelper.getCategorias();
                categoriaHelper.deleteCategoria(info.position,getApplicationContext());
                categoriaHelper.cargarCategorias(getApplicationContext());
                arrayCategorias = categoriaHelper.getCategorias();
                tile_Adapter = new Tile_ListView_Adapter(this, arrayCategorias);
                lvCategorias.setAdapter(tile_Adapter);
                tile_Adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    // ACTION BAR/TOOLBAR //
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionbar_list) {
            return true;
        } else if (item.getItemId() == R.id.actionbar_tile) {
            lvCategorias.setAdapter(tile_Adapter);
            return true;
        } else if (item.getItemId() == R.id.actionbar_card) {
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // NAVIGATION DRAWER //
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_tickets) {
            startActivity(new Intent(Categoria_Activity.this,Tickets_MainActivity.class));
        } else if (id == R.id.nav_categorias) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_profesor) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://universite.umh.es/profesores/fichaprofesor.asp?NP=193122")));
        } else if (id == R.id.nav_contactos) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/")));
        } else if (id == R.id.nav_developer) {
            Intent intent= new Intent(Intent.ACTION_WEB_SEARCH );
            intent.putExtra(SearchManager.QUERY, "Android Developer");
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Toast.makeText(Categoria_Activity.this, getString(R.string.disponible), Toast.LENGTH_LONG).show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}