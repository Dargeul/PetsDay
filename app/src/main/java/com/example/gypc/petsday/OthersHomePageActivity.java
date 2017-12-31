package com.example.gypc.petsday;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.gypc.petsday.adapter.HSDetailPetAdapter;
import com.example.gypc.petsday.adapter.OthersPetAdapter;
import com.example.gypc.petsday.model.Pet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gypc on 2017/12/31.
 */

public class OthersHomePageActivity extends AppCompatActivity {
    private List<Pet> pets;
    private OthersPetAdapter othersPetAdapter;
    private RecyclerView petsListRV;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.others_homepage);
        final String bitmap = "https://f11.baidu.com/it/u=3240141704,604792825&fm=72";
        pets = new ArrayList<>();
        pets.add(new Pet(1, "Toto", 1, "Cat",
                12, "boy", "2017-12-12", bitmap, 666));
        pets.add(new Pet(1, "Toto", 1, "Cat",
                12, "boy","2017-12-12", bitmap, 666));
        pets.add(new Pet(1, "Toto", 1, "Cat",
                12, "boy", "2017-12-12", bitmap, 666));
        petsListRV = (RecyclerView)findViewById(R.id.petListRV);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(OthersHomePageActivity.this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        othersPetAdapter = new OthersPetAdapter(R.layout.others_pet);
        othersPetAdapter.addData(pets);
        petsListRV.setLayoutManager(layoutManager1);
        petsListRV.setAdapter(othersPetAdapter);
    }

}
