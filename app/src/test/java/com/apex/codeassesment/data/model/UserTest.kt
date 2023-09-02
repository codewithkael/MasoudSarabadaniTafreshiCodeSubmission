package com.apex.codeassesment.data.model

import org.junit.Assert
import org.junit.Test

class UserTest {

    @Test
    fun testCreatesRandomUser(){
        val randomUser = User.createRandom()

        Assert.assertNotNull(randomUser)
        Assert.assertNotNull(randomUser.email)
        Assert.assertNotNull(randomUser.location)
        Assert.assertNotNull(randomUser.email)
        Assert.assertNotNull(randomUser.dob)
    }

    @Test
    fun testCreateRandomUserHasDefinedValues(){
        val randomUser = User.createRandom()

        Assert.assertNotNull(randomUser.name?.first)
        Assert.assertNotNull(randomUser.name?.last)
        Assert.assertNotNull(randomUser.location?.coordinates)
        Assert.assertNotNull(randomUser.email)
        Assert.assertNotNull(randomUser.dob?.age)
    }


}