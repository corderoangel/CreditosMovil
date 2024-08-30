package com.fjd.creditosmovil.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.fjd.creditosmovil.data.local.entities.FotosEntity;

import java.util.List;

@Dao
public interface DAO {
    //////////////////////////// INSERTS ///////////////////////////////////
    @Insert
    long insert(FotosEntity entity);

    //////////////////////////// QUERYS ///////////////////////////////////

    @Query("SELECT * FROM FOTOS WHERE ID_FOTO like:photoId AND ESTADO LIKE:state")
    List<FotosEntity> getPhotosById(String photoId, String state);

    @Query("SELECT * FROM FOTOS WHERE ID_FOTO like:photoId AND ESTADO LIKE:state AND TYPE LIKE:type")
    FotosEntity getPhotoSent(String photoId, String type, String state);

    @Query("SELECT * FROM FOTOS WHERE ID_FOTO like:PhotoId AND TYPE LIKE:type AND ESTADO ='N' ")
    FotosEntity findPhotoById(String PhotoId, String type);


    /////////////////////////// DELETES /////////////////
    @Query("DELETE FROM FOTOS WHERE  ID=:id")
    int deletePhotoFindId(int id);

    @Query("UPDATE FOTOS SET ESTADO ='S' WHERE ID =:id")
    void updatePhotoStateById(String id);

    @Query("UPDATE FOTOS SET ESTADO ='F' WHERE ID =:id")
    void updatePhotoStateFailedById(String id);


}
