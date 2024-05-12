package br.univesp.pji610.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val userName: String,
    val password: String,
    val imgPath: String,
    val email: String,
    val celular: Double,
    val telegram: String,
    val isAdmin: Boolean,
    val createdAt: String,
)