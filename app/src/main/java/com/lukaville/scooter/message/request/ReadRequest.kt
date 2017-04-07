package com.lukaville.scooter.message.request

import com.lukaville.scooter.message.Message
import com.lukaville.scooter.utils.toUnsignedInt

class ReadRequest(val command: Int, val params: List<Int>) : Message {

    override val bytes: ByteArray
        get() {
            val bytes = ByteArray(size = params.size + 8)

            bytes[0] = 0x55.toByte()
            bytes[1] = 0xAA.toByte()
            bytes[2] = (params.size + 2).toByte()
            bytes[3] = 0x20.toByte()
            bytes[4] = 0x01.toByte()
            bytes[5] = command.toByte()

            params.forEachIndexed { index, param ->
                bytes[6 + index] = param.toByte()
            }

            val sum = (2..5 + params.size).sumBy { bytes[it].toUnsignedInt() }
            val v = sum xor 0xFFFF and 0xFFFF

            bytes[6 + params.size] = (v and 0xFF).toByte()
            bytes[7 + params.size] = (v shr 8).toByte()

            return bytes
        }

}