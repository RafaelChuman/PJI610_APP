package br.univesp.pji610.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.univesp.pji610.database.model.IoT
import br.univesp.pji610.database.model.IoT_GroupIoT
import kotlinx.coroutines.flow.Flow

@Dao
interface IoTDAO {

    @Insert
    suspend fun save(iot: IoT)

    @Query(
        """
        SELECT * 
        FROM IoT 
        INNER JOIN GroupIoT ON GroupIoT.id = IoT.groupId
        INNER JOIN RescueGroup ON RescueGroup.groupId = GroupIoT.id
        WHERE RescueGroup.userID = :userId"""
    )
    fun getAllByUser(
        userId: String,
    ): Flow<List<IoT_GroupIoT>>

    @Query(
        """
        SELECT * 
        FROM IoT 
        WHERE id = :iotId"""
    )
    fun getById(
        iotId: String,
    ): Flow<IoT>?

    @Query("DELETE FROM IoT WHERE id = :id")
    suspend fun remove(id: String)

    @Query("""SELECT * FROM IoT """)
    suspend fun getAll(): IoT?
}