package com.example.proyectoiot

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.io.InputStream
import java.security.KeyFactory
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.PKCS8EncodedKeySpec
import java.util.UUID
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext

class MQTTmanager(private val mqttServerUri: String,
                  private val clientId: UUID,
                  private val topic: String,
                  private val qos: Int,
                  private val applicationContext: Context) {
    private lateinit var mqttClient: MqttClient
    private lateinit var luminosity: String

    fun connect(responseSub: MutableState<String>) {
        try {
            // Cargar el certificado de cliente desde el archivo
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val certificateInputStream: InputStream = applicationContext.assets.open("cert.der")
            val certificate = certificateFactory.generateCertificate(certificateInputStream) as X509Certificate

            // Cargar la clave privada desde el archivo
            val keyInputStream: InputStream = applicationContext.assets.open("private.der")
            val privateKeyBytes = keyInputStream.readBytes()
            val keyFactory = KeyFactory.getInstance("RSA")
            val privateKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)
            val privateKey: PrivateKey = keyFactory.generatePrivate(privateKeySpec)

            // Crear el contexto SSL
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null)
            keyStore.setCertificateEntry("alias", certificate)
            keyStore.setKeyEntry("alias", privateKey, null, arrayOf(certificate))

            val sslContext = SSLContext.getInstance("TLSv1.2")
            val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
            keyManagerFactory.init(keyStore, null)
            sslContext.init(keyManagerFactory.keyManagers, null, null)

            // Configurar las opciones de conexión MQTT
            val options = MqttConnectOptions()
            options.socketFactory = sslContext.socketFactory

            // Crear el cliente MQTT y establecer la conexión
            mqttClient = MqttClient(mqttServerUri, clientId.toString(), MemoryPersistence())
            mqttClient.connect(options)
            // Suscribirse al tema MQTT
            subscribeToTopic(responseSub)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        mqttClient.disconnect()
    }

    private fun subscribeToTopic(responseSub: MutableState<String>) {
        if (mqttClient.isConnected) {
            mqttClient.subscribe(topic, qos) { _, message ->
                luminosity = message.payload.toString(Charsets.UTF_8)
                Log.d("TEST-MQTT", luminosity)
                responseSub.value += luminosity
                // Aquí puedes manejar la respuesta recibida del MQTT, enviarla a la actividad principal, etc.
            }
        }
    }
    fun publish(message: String) {
        if (mqttClient.isConnected) {
            val mqttMessage = MqttMessage()
            mqttMessage.payload = message.toByteArray(Charsets.UTF_8)
            mqttClient.publish(topic, mqttMessage)
        }
    }
}