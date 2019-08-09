package com.jabar.jabarwifi;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jabar.jabarwifi.model.Data;
import com.jabar.jabarwifi.model.Saldo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button lima, duapuluh;
    TextView hasillima, hasilduapuluh, jumlah;
    public Integer hasil, jumlahsaldo;

    private DatabaseReference mDatabase, database;
    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lima = findViewById(R.id.pulsalima);
        duapuluh = findViewById(R.id.pulsadua);
        hasillima = findViewById(R.id.tv_hasillima);
        hasilduapuluh = findViewById(R.id.tv_hasilduapuluh);
        jumlah = findViewById(R.id.tv_total);

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        final String uId = mUser.getUid();
        mDialog = new ProgressDialog(this);


        final Random randomNumbers = new Random();


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat tanggal = new SimpleDateFormat("dd-MM-yyyy");
        final String datenow = tanggal.format(cal.getTime());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        //hitungsaldo terlebih dahulu
        hitungsaldo(5000);
        hitungsaldo(20000);
        updatesaldo("hitung",0);

        lima.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.setMessage("Processing....");
                mDialog.show();
                int randomValue = randomNumbers.nextInt();
                Data datainsert = new Data("wifilima", datenow, 5000);

                mDatabase.child("pemasukan").child(String.valueOf(randomValue)).setValue(datainsert);
                Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
                hitungsaldo(5000);
                updatesaldo("pemasukan",5000);

            }
        });

        duapuluh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.setMessage("Processing....");
                mDialog.show();
                final int randomValue = randomNumbers.nextInt();
                Data datainsert = new Data("wifiduapuluh", datenow, 20000);

                mDatabase.child("pemasukan").child(String.valueOf(randomValue)).setValue(datainsert);
                Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
                hitungsaldo(20000);
                updatesaldo("pemasukan",20000);
            }
        });

    }

    public void hitungsaldo(final int kategori){
        Query query = mDatabase.child("pemasukan").orderByChild("uang").equalTo(kategori);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChildren()){
                    hasil = 0;
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                        Data p = snapshot.getValue(Data.class);
                        hasil += p.getUang();
                        String total = Integer.toString(hasil);
                        if(kategori==5000){
                            hasillima.setText(total);
                        }
                        else {
                            hasilduapuluh.setText(total);
                        }


                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // display message if error occurs
                Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void updatesaldo(final String kategori, final int uang){
//        database = FirebaseDatabase.getInstance().getReference().child("saldo");
//        database.keepSynced(true);

        //ambil saldo
        Query query = mDatabase.child("saldo");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String totalstring = (String) dataSnapshot.child("jumlah").getValue();
                int total = Integer.parseInt(totalstring);
                if(kategori =="pemasukan"){
                    jumlahsaldo= total+uang;
                }
                else if (kategori=="pengeluaran"){
                    jumlahsaldo= total-uang;
                }
                else {
                    jumlahsaldo= total;
                }
                String a = String.valueOf(jumlahsaldo);
                jumlah.setText(a);

                Saldo hasilsaldo = new Saldo(a);
                mDatabase.child("saldo").setValue(hasilsaldo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
