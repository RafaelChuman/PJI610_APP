package br.univesp.pji610.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.univesp.pji610.database.model.GroupIoT
import br.univesp.pji610.database.model.IoT
import kotlinx.coroutines.flow.Flow


@Dao
interface GroupIoTDAO {

    @Insert
    suspend fun save(group: GroupIoT)

    @Query("""SELECT * FROM GroupIoT
        INNER JOIN RescueGroup ON RescueGroup.groupId = GroupIoT.id
        WHERE RescueGroup.userID = :userId""")
    fun getAllByUser(userId: String): Flow<List<GroupIoT>>

    @Query("""SELECT name FROM GroupIoT""")
    suspend fun getName(): List<String>

    @Query("DELETE FROM GroupIoT WHERE id = :id")
    suspend fun remove(id: String)


    @Query(""" SELECT * FROM GroupIoT WHERE id = :groupIotId""")
    fun getById(
        groupIotId: String,
    ): Flow<GroupIoT>?

}