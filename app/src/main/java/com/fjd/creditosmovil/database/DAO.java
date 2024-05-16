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

    @Query("SELECT * FROM FOTOS WHERE ID_FOTO like:id_foto AND ESTADO LIKE:state AND TYPE LIKE:type")
    FotosEntity getFotoFindId(String id_foto,String type ,String state);

    @Query("SELECT * FROM FOTOS WHERE ID_FOTO like:id_foto AND TYPE LIKE:type AND ESTADO ='N' ")
    FotosEntity getFotoFindById(String id_foto, String type);

    @Query("SELECT count(*) FROM FOTOS WHERE ID_FOTO like:id_foto AND ESTADO ='S' ")
    int getSuccessPhoto(String id_foto);

    /////////////////////////// DELETES /////////////////
    @Query("DELETE FROM FOTOS WHERE  ID_FOTO=:id_foto")
    int deleteFotoFindID(String id_foto);


    @Query("UPDATE FOTOS SET ESTADO ='S' WHERE ID =:id")
    int updatePhotosState(String id);

    @Query("UPDATE FOTOS SET ESTADO ='F' WHERE ID =:id")
    int updatePhotosStateFailed(String id);


}
