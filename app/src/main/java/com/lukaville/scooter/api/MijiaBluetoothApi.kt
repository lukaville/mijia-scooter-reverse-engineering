package com.lukaville.scooter.api

import com.lukaville.scooter.message.parse.ResponseParser
import com.lukaville.scooter.message.request.ReadRequest
import com.lukaville.scooter.message.response.ReadResponse
import com.lukaville.scooter.utils.toHexString
import com.polidea.rxandroidble.RxBleConnection
import rx.Observable
import java.util.*
import java.util.concurrent.TimeUnit

class MijiaBluetoothApi(val connection: RxBleConnection) {

    private val parser = ResponseParser()

    fun readValue(from: Int, count: Int): Observable<ReadResponse?> {
        val message = ReadRequest(from, listOf(count * 2))

        return sendMessage(message.bytes).map {
            try {
                parser.parse(it) as ReadResponse
            } catch (e: Exception) {
                e.printStackTrace()
                return@map null
            }
        }
    }

    fun dumpMemory(): Observable<ByteArray> {
        // dump all values by reading 32 value chunks
        // one value - 16-bit unsigned int
        // 8 times x 32 values x 16-bit = 512 bytes
        return Observable
                .range(0, 8)
                .map { it * 32 }
                .concatMap { readValue(it, 32) }
                .filter { it != null }
                .map { it!!.data }
                .reduce { left: ByteArray?, right: ByteArray? ->
                    if (left == null && right != null) {
                        right
                    } else if (right == null && left != null) {
                        left
                    } else {
                        left!! + right!!
                    }
                }
    }

    private fun sendMessage(bytes: ByteArray): Observable<ByteArray> {
        println("Sending: ${bytes.toHexString()}")

        val result = connection
                .setupNotification(UUID.fromString(READ_CHARACTERISTIC_UUID))
                .flatMap { it }
                .doOnNext {
                    println("Response part: ${it.toHexString()}")
                }
                .timeout(200, TimeUnit.MILLISECONDS)
                .onErrorResumeNext(Observable.empty())
                .collect({ mutableListOf<ByteArray>() }) { list, bytes -> list.add(bytes) }
                .map { list ->
                    val length = list.sumBy { it.size }
                    val array = ByteArray(length)
                    var currentIndex = 0
                    list.forEach {
                        System.arraycopy(it, 0, array, currentIndex, it.size)
                        currentIndex += it.size
                    }
                    array
                }
                .doOnNext {
                    println("Response: ${it.toHexString()}")
                }

        connection.writeCharacteristic(UUID.fromString(WRITE_CHARACTERISTIC_UUID), bytes).subscribe()
        return result
    }

}

private const val WRITE_CHARACTERISTIC_UUID = "6e400002-b5a3-f393-e0a9-e50e24dcca9e"
private const val READ_CHARACTERISTIC_UUID = "6e400003-b5a3-f393-e0a9-e50e24dcca9e"