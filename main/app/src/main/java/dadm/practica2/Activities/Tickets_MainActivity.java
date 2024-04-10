package dadm.practica2.Activities;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import dadm.practica2.Interfaces.Intent_Properties;
import dadm.practica2.R;
import dadm.practica2.ListViewAdapters_Ticket.Card_ListView_Adapter;
import dadm.practica2.ListViewAdapters_Ticket.List_ListView_Adapter;
import dadm.practica2.ListViewAdapters_Ticket.Tile_ListView_Adapter;
import dadm.practica2.DataTransformationObjects.DtoTicket;
import dadm.practica2.Helpers.TicketHelper;

public class Tickets_MainActivity extends AppCompatActivity implements Intent_Properties, NavigationView.OnNavigationItemSelectedListener {

    //VISTAS
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    //LISTVIEW Y SUS ADAPTADORES
    private ListView lvTickets;
    private List_ListView_Adapter list_Adapter;
    private Tile_ListView_Adapter tile_Adapter;
    private Card_ListView_Adapter card_Adapter;

    //OTROS
    private TicketHelper usdbh;
    private ArrayList<DtoTicket> arrayTickets;
    private Intent intent;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tickets_mainactivity);

        //INSTANCIAMOS LAS VISTAS
        lvTickets = findViewById(R.id.listViewTickets);
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        //TOOLBAR Y NAVIGATION DRAWER
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //ABRIMOS LA BD EN MODO ESCRITURA Y CARGAMOS LOS TICKETS
        cargarTickets();

        //MOSTRAR TICKETS CON EL ADAPTADOR POR DEFECTO
        createAndSet_list_Adapter();
        registerForContextMenu(lvTickets); //Añadir un menu contextual al list View

        //FUNCIONALIDAD A LAS VISTAS
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Tickets_MainActivity.this , AddTicket_Activity.class));
            }
        });

    }

    private void cargarTickets() {
        usdbh = new TicketHelper(this, null);
        arrayTickets = usdbh.obtenerTickets();
        if (arrayTickets.isEmpty()) {
            Toast.makeText(getBaseContext(), getString(R.string.no_registros), Toast.LENGTH_LONG).show();
        }
    }

    private void createAndSet_list_Adapter () {
        list_Adapter = new List_ListView_Adapter(this, arrayTickets);
        lvTickets.setAdapter(list_Adapter);
    }

    private void createAndSet_tile_Adapter () {
        tile_Adapter = new Tile_ListView_Adapter(this, arrayTickets);
        lvTickets.setAdapter(tile_Adapter);
    }

    private void createAndSet_card_Adapter () {
        card_Adapter = new Card_ListView_Adapter(this, arrayTickets);
        lvTickets.setAdapter(card_Adapter);
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
                intent = new Intent(Tickets_MainActivity.this , AddTicket_Activity.class);
                intent.putExtra(PARAM1,true);
                intent.putExtra(PARAM2, info.position);
                startActivity(intent);
                return true;
            case R.id.borrar:
                usdbh.borrarTicket(arrayTickets.get(info.position).getId());
                arrayTickets = usdbh.obtenerTickets();
                list_Adapter = new List_ListView_Adapter(this, arrayTickets);
                lvTickets.setAdapter(list_Adapter);
                list_Adapter.notifyDataSetChanged();
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
            createAndSet_list_Adapter ();
            return true;
        } else if (item.getItemId() == R.id.actionbar_tile) {
            createAndSet_tile_Adapter ();
            return true;
        } else if (item.getItemId() == R.id.actionbar_card) {
            createAndSet_card_Adapter ();
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
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_categorias) {
            startActivity(new Intent(Tickets_MainActivity.this,Categoria_Activity.class));
        } else if (id == R.id.nav_profesor) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://universite.umh.es/profesores/fichaprofesor.asp?NP=193122")));
        } else if (id == R.id.nav_contactos) {
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("content://contacts/people/")));
        } else if (id == R.id.nav_developer) {
            Intent intent= new Intent(Intent.ACTION_WEB_SEARCH );
            intent.putExtra(SearchManager.QUERY, "Android Developer");
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Toast.makeText(Tickets_MainActivity.this, getString(R.string.disponible), Toast.LENGTH_LONG).show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
