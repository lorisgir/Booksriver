package com.example.booksriver.data.dto

import com.example.booksriver.data.model.IDto
import com.example.booksriver.data.model.Profile
import com.google.gson.annotations.SerializedName

data class ProfileDto(
    val id: Int,
    val username: String,
    @SerializedName("userFollowedCount") val follower: Int,
    @SerializedName("userFollowsCount") var following: Int,
    @SerializedName("userLibraryViews") val libraries: List<UserLibraryDto>,
    val followed: Boolean
) : IDto {
    override fun fromDto(param: Any?): Profile {
        return Profile(
            id = id,
            username = username,
            follower = follower,
            following = following,
            followed = followed,
            libraries = if (libraries.isEmpty()) emptyList() else libraries.map { it.fromDto() }
        )
    }
}
