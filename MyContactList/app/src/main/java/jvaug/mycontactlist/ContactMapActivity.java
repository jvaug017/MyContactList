package jvaug.mycontactlist;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class ContactMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        contact = (Contact) extras.get("current_contact");
    }

    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {
        if (contact == null) {
            Toast.makeText(ContactMapActivity.this, "No contact passed", Toast.LENGTH_LONG).show();
            return;
        }
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Geocoder geo = new Geocoder(this);
        List<Address> addresses = null;

        String address_string = contact.getAddress() + ", " +
                contact.getCity() + ", " +
                contact.getState() + " " +
                contact.getZipCode();

        try {
            addresses = geo.getFromLocationName(address_string, 1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0 && addresses.get(0) != null) {
            LatLng point = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

            map.addMarker(new MarkerOptions().position(point).title(contact.getName()).snippet(address_string));
            map.moveCamera(CameraUpdateFactory.newLatLng(point));
        }

    }
}
