package br.univesp.pji610.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.univesp.pji610.database.model.GroupIoT

@Dao
interface GroupIoTDAO {

    @Insert
    suspend fun save(group: GroupIoT)

    @Query("""SELECT * FROM GroupIoT""")
    suspend fun getAll(): GroupIoT?

}