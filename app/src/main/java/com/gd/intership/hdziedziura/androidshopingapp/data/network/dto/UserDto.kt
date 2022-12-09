package com.gd.intership.hdziedziura.androidshopingapp.data.network.dto

data class UserDto(
    val id: Int = 0,
    val address: AddressDto = AddressDto.EMPTY,
    val email: String = "",
    val username: String,
    val password: String,
    val name: NameDto = NameDto.EMPTY,
    val phone: String = "",
    val __v: Int = 0

) {

    data class AddressDto(
        val geolocation: GeolocationDto,
        val city: String,
        val street: String,
        val number: Int,
        val zipcode: String
    ) {
        companion object {
            val EMPTY = AddressDto(GeolocationDto.EMPTY, "", "", 0, "")
        }
    }

    data class GeolocationDto(
        val lat: String,
        val long: String
    ) {
        companion object {
            val EMPTY = GeolocationDto("", "")
        }
    }

    data class NameDto(
        val firstName: String,
        val lastName: String
    ) {
        companion object {
            val EMPTY = NameDto("", "")
        }
    }
}
