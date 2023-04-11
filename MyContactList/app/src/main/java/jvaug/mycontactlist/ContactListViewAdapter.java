package jvaug.mycontactlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactListViewAdapter extends RecyclerView.Adapter {
    private List<Contact> contacts;
    private Context parentContext;
    boolean isDeleting;
    public class ContactListViewHolder extends RecyclerView.ViewHolder {

        public TextView textName;
        public TextView textAddress;
        public TextView textCity;
        public TextView textState;
        public TextView textZipCode;
        public TextView textHomePhone;
        public TextView textCellPhone;
        public TextView textEmail;
        public TextView textDate;
        public Button deleteButton;


        public ContactListViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textView_name);
            textAddress = itemView.findViewById(R.id.textView_address);
            textCity = itemView.findViewById(R.id.textView_city);
            textState = itemView.findViewById(R.id.textView_state);
            textZipCode = itemView.findViewById(R.id.textView_zip);
            textHomePhone = itemView.findViewById(R.id.textView_homephone);
            textCellPhone = itemView.findViewById(R.id.textView_cellphone);
            textEmail = itemView.findViewById(R.id.textView_email);
            textDate = itemView.findViewById(R.id.textView_date);
            deleteButton = itemView.findViewById(R.id.button_delete);


            itemView.setTag(this);
        }

        public TextView getTextName() {
            return textName;
        }
        public TextView getTextAddress() {
            return textAddress;
        }
        public TextView getTextCity() {
            return textCity;
        }
        public TextView getTextState() {
            return textState;
        }
        public TextView getTextZipCode() {
            return textZipCode;
        }
        public TextView getTextHomePhone() {
            return textHomePhone;
        }
        public TextView getTextCellPhone() {
            return textCellPhone;
        }
        public TextView getTextEmail() {
            return textEmail;
        }
        public TextView getTextDate() {
            return textDate;
        }
        public Button getDeleteButton() {
            return deleteButton;
        }
    }

    public ContactListViewAdapter(List<Contact> list, Context context) {
        contacts = list;
        parentContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_layout, parent, false);
        return new ContactListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ContactListViewHolder rvh = (ContactListViewHolder) holder;
        Contact c = contacts.get(position);
        rvh.getTextName().setText(c.getName());
        rvh.getTextAddress().setText(c.getAddress());
        rvh.getTextCity().setText(c.getCity());
        rvh.getTextState().setText(c.getState());
        rvh.getTextZipCode().setText(String.valueOf(c.getZipCode()));
        // All disabled due to seemingly not necessary to display
//        rvh.getTextHomePhone().setText(c.getHomePhone());
//        rvh.getTextCellPhone().setText(c.getCellPhone());
//        rvh.getTextEmail().setText(c.getEmail());
//        rvh.getTextDate().setText(c.getBirthday());

        // If deleting switch is enabled, make the delete buttons visible, otherwise invisible
        if (isDeleting) {
            rvh.getDeleteButton().setVisibility(View.VISIBLE);
            rvh.getDeleteButton().setOnClickListener(view -> {
                deleteItem(position);
            });
        } else {
            rvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }


    }
    @Override
    public int getItemCount() {
        return contacts.size();
    }

    // Delete a contact based on their position in the recycle view
    private void deleteItem(int position) {
        Contact c = contacts.get(position);
        ContactDataBaseAccessor ds = new ContactDataBaseAccessor(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.delete_contact(c.getID());
            ds.close();
            if (didDelete) {
                contacts.remove(position);
                notifyDataSetChanged();
            }
            else {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {

        }
    }

    // Change the recorded state of delete switch
    public void setDeleting(boolean deleting) {
        isDeleting = deleting;
    }

}
