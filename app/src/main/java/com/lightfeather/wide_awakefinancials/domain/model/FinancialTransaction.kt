package com.lightfeather.wide_awakefinancials.domain.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = TransactionCategory::class,
        parentColumns = arrayOf("category_id"),
        childColumns = arrayOf("sourceId"),
        onDelete = ForeignKey.NO_ACTION,
        onUpdate = ForeignKey.SET_NULL
    )]
)
data class FinancialTransaction(
    val sourceId: Int,
    val description: String,
    val creationTime: Long,
    val amount: Double,
    val type: TransactionType,
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readDouble(),
        if (parcel.readString()
                .equals("INCOME", true)
        ) TransactionType.INCOME else TransactionType.EXPENSE
    ) {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(sourceId)
        parcel.writeString(description)
        parcel.writeLong(creationTime)
        parcel.writeDouble(amount)
        parcel.writeValue(id)
        parcel.writeString(type.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FinancialTransaction> {
        override fun createFromParcel(parcel: Parcel): FinancialTransaction {
            return FinancialTransaction(parcel)
        }

        override fun newArray(size: Int): Array<FinancialTransaction?> {
            return arrayOfNulls(size)
        }
    }

}
