package com.fjd.creditosmovil.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FOTOS")
public class FotosEntity {
    @PrimaryKey(autoGenerate = true)
    public int ID;
    public String ID_FOTO;
    public String N_FOTOS;
    public String MES;
    public String ANO;
    public String FOTO;
    public String ESTADO;

    public String FIRMA;
}
