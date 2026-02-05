package com.example.expensex.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "transactions" ,
    foreignKeys = [
        ForeignKey(entity = AccountEntity::class ,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ,
        ForeignKey(entity = CategoryEntity::class ,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class TransactionEntity (
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val userId : String ,
    val title : String ,
    val amount : Double ,
    val type : String ,
    val date : Long ,
    val accountId : Int ,
    val categoryId : Int ?
)