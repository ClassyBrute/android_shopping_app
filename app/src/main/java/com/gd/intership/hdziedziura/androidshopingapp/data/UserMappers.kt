package com.gd.intership.hdziedziura.androidshopingapp.data

import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.UserDto
import com.gd.intership.hdziedziura.androidshopingapp.domain.model.User

fun UserDto.toUser(): User = User(id, address.toAddress(), email, username, password, name.toName(), phone)

fun UserDto.NameDto.toName() = User.Name(firstName, lastName)

fun UserDto.GeolocationDto.toGeolocation() = User.Geolocation(lat, long)

fun UserDto.AddressDto.toAddress() = User.Address(geolocation.toGeolocation(), city, street, number, zipcode)

fun User.toUserDto(): UserDto = UserDto(id, address.toAddressDto(), email, username, password, name.toNameDto(), phone)

fun User.Name.toNameDto() = UserDto.NameDto(firstName, lastName)

fun User.Geolocation.toGeolocationDto() = UserDto.GeolocationDto(lat, long)

fun User.Address.toAddressDto() = UserDto.AddressDto(geolocation.toGeolocationDto(), city, street, number, zipcode)
