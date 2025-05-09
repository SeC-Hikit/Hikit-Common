package org.hikit.common.adapter

import com.fasterxml.jackson.databind.ObjectMapper
import org.hikit.common.adapter.response.AltitudeApiRequestPoint
import org.hikit.common.adapter.response.AltitudeApiResponse
import org.hikit.common.adapter.response.AltitudeServiceRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

const val ALTITUDE_CALL_RETRIES = 3
const val ALTITUDE_CALL_CHUNK_SIZE = 500

@Service
class AltitudeServiceAdapter @Autowired constructor(private val objectMapper: ObjectMapper,
                                                    @Value("\${microservice.oa.port:8080}") private val portToAltitudeService : Int,
                                                    @Value("\${microservice.oa.host:127.0.0.1}") private val hostToAltitudeService : String) {

    private val logger: Logger = Logger.getLogger(AltitudeServiceAdapter::class.java.name)
    private val pathToServiceApi: String = "$hostToAltitudeService:$portToAltitudeService/api/v1/lookup"



    fun getElevationsByLongLat(latitude: Double,
                               longitude: Double): List<Double> {
        val apiGetEndpoint = "http://$pathToServiceApi?locations=$latitude,$longitude"
        return try {
            val getCall = URL(apiGetEndpoint).readText()
            val gsonBuilder: AltitudeApiResponse =
                objectMapper.readValue(getCall, AltitudeApiResponse::class.java)
            listOf(gsonBuilder.results.first().elevation)
        } catch (e: Exception) {
            logger.severe("Could not connect to altitude service or read its response")
            emptyList()
        }
    }

    fun getElevationsByLongLat(coordinates: List<Pair<Double, Double>>): List<Double> {

        val coordinatesChunks = coordinates.chunked(ALTITUDE_CALL_CHUNK_SIZE)

        val result : MutableList<Double> = mutableListOf()

        coordinatesChunks.forEach { chunk ->
            val coordinateAltitudeList = callAltitudeWithExponentialBackoff(chunk)

            if(chunk.size == coordinateAltitudeList.size) {
                result.addAll(coordinateAltitudeList)
            } else {
                //in case of error the result contains the same number of elements of the input
                logger.info("chunk.size ${chunk.size} different than coordinateAltitudeList.size ${coordinateAltitudeList.size}")
                result.addAll(MutableList(coordinates.size) { 0.0 })
            }
        }
        return result
    }


    private fun callAltitudeWithExponentialBackoff(coordinates: List<Pair<Double, Double>>) : List<Double> {

        val postData: ByteArray = buildAltitudeRequest(coordinates)

        var retryCounter = 1
        while(retryCounter <= ALTITUDE_CALL_RETRIES) {

            try {
                val connection = buildAltitudeRequestConnection(postData.size)
                val outputStream = DataOutputStream(connection.outputStream)

                outputStream.write(postData)
                outputStream.flush()

                if(connection.responseCode == HttpURLConnection.HTTP_OK) {

                    val inputStream = DataInputStream(connection.inputStream)
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val output: String = reader.readLine()

                    val gsonBuilder: AltitudeApiResponse = objectMapper.readValue(output, AltitudeApiResponse::class.java)
                    return gsonBuilder.results.map { elem -> elem.elevation }

                } else {
                    retryCounter++
                    logger.warning("retrying... $retryCounter time(s)")
                    TimeUnit.SECONDS.sleep((retryCounter * retryCounter * 1L))
                }
            } catch (exception: Exception) {
                retryCounter++
                logger.severe("exception retrying... $retryCounter time(s)")
                TimeUnit.SECONDS.sleep((retryCounter * retryCounter * 1L))
            }
        }

        return listOf()
    }

    private fun buildAltitudeRequestConnection(contentSize : Int) : HttpURLConnection {

        val apiGetEndpoint = "http://$pathToServiceApi"
        val getCall = URL(apiGetEndpoint)

        val connection = getCall.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true

        connection.setRequestProperty("charset", "utf-8")
        connection.setRequestProperty("Content-length", contentSize.toString())
        connection.setRequestProperty("Content-Type", "application/json")

        return connection
    }

    private fun buildAltitudeRequest(coordinates: List<Pair<Double, Double>>): ByteArray {
        val requestObject = AltitudeServiceRequest(coordinates.map { elem -> AltitudeApiRequestPoint(elem.first, elem.second) })
        val requestMessage = objectMapper.writeValueAsString(requestObject)
        return requestMessage.toByteArray(StandardCharsets.UTF_8)
    }
}