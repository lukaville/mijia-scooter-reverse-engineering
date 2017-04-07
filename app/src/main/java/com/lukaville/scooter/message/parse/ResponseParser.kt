package com.lukaville.scooter.message.parse

import com.lukaville.scooter.message.Message
import com.lukaville.scooter.message.response.ReadResponse
import com.lukaville.scooter.utils.toUnsignedInt

class ResponseParser {

    fun parse(bytes: ByteArray): Message {

        if (bytes[0] != 0x55.toByte() || bytes[1] != 0xAA.toByte()) {
            throw ParsingError("Message header is incorrect")
        }

        return when {
            bytes.hasType(0x23, 0x01) -> parseRead(bytes)
            else -> throw ParsingError("Unknown type")
        }

    }

    private fun parseRead(bytes: ByteArray): ReadResponse {
        val length = bytes[2].toUnsignedInt() - 2
        val from = bytes[6].toUnsignedInt()
        val data = bytes.copyOfRange(6, 6 + length)
        return ReadResponse(from, data)
    }

    private fun ByteArray.hasType(first: Int, second: Int): Boolean {
        return get(3).toInt() == first && get(4).toInt() == second
    }

}