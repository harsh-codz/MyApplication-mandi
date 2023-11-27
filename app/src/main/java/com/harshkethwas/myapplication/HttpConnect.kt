package com.harshkethwas.myapplication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpConnect {


    class HttpConnection {

        var res: String = ""
        suspend fun connect(url: URL, postData: String): String {
            withContext(Dispatchers.IO)
            {
                val http = url.openConnection() as HttpURLConnection
                http.requestMethod = "POST"
                http.doOutput = true
                http.useCaches = false

                DataOutputStream(http.outputStream).use { it.writeBytes(postData) }
                BufferedReader(InputStreamReader(http.inputStream)).use { br ->
                    var line: String?
                    while (br.readLine().also { line = it } != null) {
                        res = line.toString()
                    }
                }
                http.disconnect()
            }
            return res
        }
    }
}