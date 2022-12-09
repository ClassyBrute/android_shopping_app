package com.gd.intership.hdziedziura.androidshopingapp.domain.model

data class User(
    val id: Int = 0,
    val address: Address = Address.EMPTY,
    val email: String = "",
    val username: String,
    val password: String,
    val name: Name = Name.EMPTY,
    val phone: String = ""

) {
    data class Address(
        val geolocation: Geolocation,
        val city: String,
        val street: String,
        val number: Int,
        val zipcode: String
    ) {
        companion object {
            val EMPTY = Address(Geolocation.EMPTY, "", "", 0, "")
        }
    }

    data class Geolocation(
        val lat: String,
        val long: String
    ) {
        companion object {
            val EMPTY = Geolocation("", "")
        }
    }

    data class Name(
        val firstName: String,
        val lastName: String
    ) {
        companion object {
            val EMPTY = Name("", "")
        }
    }
}
