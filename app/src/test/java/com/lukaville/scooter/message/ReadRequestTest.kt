package com.lukaville.scooter.message

import com.lukaville.scooter.message.request.ReadRequest
import com.lukaville.scooter.utils.toHexString
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Test

class ReadRequestTest {

    @Test
    fun batteryMessage() {
        val message = ReadRequest(0xB0, listOf(0x0C))

        val hex = message.toBytes().toHexString()

        assertThat(hex, IsEqual("55AA032001B00C1FFF"))
    }

    @Test
    fun otherMessage() {
        val message = ReadRequest(0x3A, listOf(0x04))

        val hex = message.toBytes().toHexString()

        assertThat(hex, IsEqual("55AA0320013A049DFF"))
    }

}