package com.example.micropulse7.cec_bulletin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Suggestion extends AppCompatActivity {
    //private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseSuggestion;
    private ListView suggestionlist;
    private FirebaseListAdapter<com.example.micropulse7.cec_bulletin.models.Suggestion> adapter3;
    private Button remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);


       // mDatabase = FirebaseDatabase.getInstance().getReference();
             mDatabaseSuggestion = FirebaseDatabase.getInstance().getReference().child("Suggestion");
        suggestionlist = (ListView) findViewById(R.id.suggestion_list);
        adapter3 = new FirebaseListAdapter<com.example.micropulse7.cec_bulletin.models.Suggestion>(this,
                com.example.micropulse7.cec_bulletin.models.Suggestion.class,
                R.layout.suggestionholder,
                mDatabaseSuggestion) {
            @Override
            protected void populateView(final View v, final com.example.micropulse7.cec_bulletin.models.Suggestion model, int position) {
                final String suggestion_key = getRef(position).getKey();

                remove = (Button) v.findViewById(R.id.rmvSuggestionBtn);

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //firebase remove selected value
                        mDatabaseSuggestion.child(suggestion_key).removeValue();
                    }
                });


                mDatabaseSuggestion.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        TextView content = (TextView) v.findViewById(R.id.suggest_content);
                        TextView stud_name = (TextView) v.findViewById(R.id.suggest_user_name);

                        stud_name.setText(model.getUsername());
                        content.setText(model.getContent());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
/*
                mDatabaseSuggestion.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

//                        TextView content = (TextView) v.findViewById(R.id.suggest_content);
                        TextView content = (TextView) v.findViewById(R.id.suggest_content);
                                TextView stud_name = (TextView) v.findViewById(R.id.suggest_user_name);

                        stud_name.setText(model.getUsername());
                        content.setText(model.getContent());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

*/
            }
        };
        suggestionlist.setAdapter(adapter3);
    }
}
