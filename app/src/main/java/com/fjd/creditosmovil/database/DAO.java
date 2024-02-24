package com.fjd.creditosmovil.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.fjd.creditosmovil.database.entities.FotosEntity;

import java.util.List;

@Dao
public interface DAO {
    //////////////////////////// INSERTS ///////////////////////////////////
    @Insert
    long insert(FotosEntity entity);

    //////////////////////////// QUERYS ///////////////////////////////////

    //ORDENES SINCRONIZADAS

    @Query("SELECT * FROM FOTOS WHERE ID_FOTO like:id_foto AND ESTADO LIKE:state")
    List<FotosEntity> getFotosFindId(String id_foto, String state);

    @Query("SELECT * FROM FOTOS WHERE ID_FOTO like:id_foto AND TYPE LIKE:type")
    FotosEntity getFotoFindById(String id_foto, String type);

    /////////////////////////// DELETES /////////////////
    @Query("DELETE FROM FOTOS WHERE ID=:id AND ID_FOTO=:id_foto")
    int deleteFotoFindID(String id, String id_foto);

    @Query("UPDATE FOTOS SET ESTADO ='S' WHERE ID =:id")
    int updatePhotosState(String id);


}
