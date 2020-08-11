package com.ataulm.whatsnext

import android.os.Parcelable
import androidx.annotation.Px
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Images(private val images: List<Image>): Parcelable {

    fun bestFor(@Px desiredWidth: Int): Image? {
        if (images.isEmpty()) {
            return null
        }
        return images.reduce { currentImage, nextImage ->
            if (bothBiggerThanDesired(currentImage, nextImage, desiredWidth)) {
                return smallestOf(currentImage, nextImage)
            }
            largestOf(currentImage, nextImage)
        }
    }

    private fun bothBiggerThanDesired(first: Image, second: Image, width: Int) =
            first.width > width && second.width > width

    private fun largestOf(currentImage: Image, nextImage: Image) =
            if (currentImage.width > nextImage.width) currentImage else nextImage

    private fun smallestOf(first: Image, second: Image) =
            if (first.width < second.width) first else second
}
