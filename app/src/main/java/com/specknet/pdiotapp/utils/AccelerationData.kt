package com.specknet.pdiotapp.utils

import android.os.Parcel
import android.os.Parcelable

data class AccelerationData(
    val x: Float,
    val y: Float,
    val z: Float
) : Parcelable {

    // Write data to the Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(x)
        parcel.writeFloat(y)
        parcel.writeFloat(z)
    }

    // Usually returns 0; you can ignore this for simple use cases
    override fun describeContents(): Int = 0

    // Companion object with CREATOR to recreate AccelerationData from a Parcel
    companion object CREATOR : Parcelable.Creator<AccelerationData> {
        override fun createFromParcel(parcel: Parcel): AccelerationData {
            // Read data in the same order it was written
            val x = parcel.readFloat()
            val y = parcel.readFloat()
            val z = parcel.readFloat()
            return AccelerationData(x, y, z)
        }

        override fun newArray(size: Int): Array<AccelerationData?> = arrayOfNulls(size)
    }
}