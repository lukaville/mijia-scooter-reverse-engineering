package com.lukaville.scooter.message.response

import com.lukaville.scooter.message.Message
import com.lukaville.scooter.utils.toHexString

class ReadResponse(val from: Int, val data: ByteArray) : Message {

    override val bytes: ByteArray
        get() = TODO("not implemented")

    override fun toString(): String {
        return "Read response with data: ${data.toHexString()}. Range: $from-${from + data.size}."
    }

}