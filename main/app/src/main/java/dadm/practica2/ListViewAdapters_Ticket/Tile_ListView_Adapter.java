package dadm.practica2.ListViewAdapters_Ticket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dadm.practica2.Helpers.ImageHelper;
import dadm.practica2.R;
import dadm.practica2.DataTransformationObjects.DtoTicket;

public class Tile_ListView_Adapter extends BaseAdapter {

    //PROPIEDADES
    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<DtoTicket> arrayTickets;
    private ImageHelper imageHelper;

    private TextView textViewTitle;
    private ImageView img;

    //CONSTRUCTOR
    public Tile_ListView_Adapter(Context context, ArrayList<DtoTicket> array) {
        super();
        mContext=context;
        arrayTickets=array;
        imageHelper = new ImageHelper(mContext);
    }

    // getView method is called for each item of ListView
    public View getView(int position, View view, ViewGroup parent) {
        // inflate the layout for each item of listView
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.tile_itemlist, null);

        // get the reference of textViews
        img = (ImageView) view.findViewById(R.id.img);
        textViewTitle = (TextView) view.findViewById(R.id.titleTile);

        // Set the Sender number and smsBody to respective TextViews
        textViewTitle.setText(arrayTickets.get(position).getTitulo());
        imageHelper.setImageFromPathString(arrayTickets.get(position).getFoto(),img);

        return view;
    }

    public Object getItem(int position) { return position; }

    public long getItemId(int position) { return position; }
    // return the number of records in cursor
    public int getCount() { return arrayTickets.size(); }
}