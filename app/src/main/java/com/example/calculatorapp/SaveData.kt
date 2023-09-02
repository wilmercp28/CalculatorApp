package com.example.calculatorapp

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class SaveData (){

     fun saveDataToFile(
        data: String,
        fileName: String,
        context: Context
    ){
        val file = File(context.filesDir,fileName)
        try {
            val outputStream = FileOutputStream(file)
            outputStream.write(data.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun readDataFromFile(
        fileName: String,
        context: Context
    )
    : String {
        val file = File(context.filesDir, fileName)
        val stringBuilder = StringBuilder()
        try {
            val inputStream = FileInputStream(file)
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                stringBuilder.append(String(buffer, 0, bytesRead))
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
    fun saveListToFile(fileName: String, list: List<String>, context: Context) {
        val data = list.joinToString("\n").trim()
        saveDataToFile(data, fileName, context)
    }
    fun loadListFromFile(fileName: String, context: Context): MutableList<String> {
        val data = readDataFromFile(fileName, context)
        Log.d("Data",data)
        return if (data.isNotBlank()) {
            data.split("\n").toMutableList()
        } else {
            mutableListOf()
        }
    }
}