package com.example.test

import com.example.SampleRequest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SampleRequestTest {

    @Test
    fun testSampleRequestCreation() {
        val sampleRequest = SampleRequest(data = "test data")
        assertEquals("test data", sampleRequest.data)
    }

    @Test
    fun testSampleRequestGetData() {
        val sampleRequest = SampleRequest(data = "sample data")
        val retrievedData = sampleRequest.data
        assertEquals("sample data", retrievedData)
    }

    @Test
    fun testSampleRequestSerialization() {
        val sampleRequest = SampleRequest(data = "serialization test")
        val json = Json.encodeToString(SampleRequest.serializer(), sampleRequest)
        val expected = """{"data":"serialization test"}"""
        assertEquals(expected, json)
    }

    @Test
    fun testSampleRequestDeserialization() {
        val json = """{"data":"deserialization test"}"""
        val sampleRequest = Json.decodeFromString<SampleRequest>(json)
        assertEquals("deserialization test", sampleRequest.data)
    }

    @Test
    fun testSampleRequestCopy() {
        val original = SampleRequest(data = "original data")
        val copied = original.copy(data = "copied data")
        assertEquals("original data", original.data)
        assertEquals("copied data", copied.data)
    }

    @Test
    fun testSampleRequestEquals() {
        val request1 = SampleRequest(data = "same data")
        val request2 = SampleRequest(data = "same data")
        val request3 = SampleRequest(data = "different data")
        
        assertEquals(request1, request2)
        assert(request1 != request3)
    }

    @Test
    fun testSampleRequestHashCode() {
        val request1 = SampleRequest(data = "test data")
        val request2 = SampleRequest(data = "test data")
        assertEquals(request1.hashCode(), request2.hashCode())
    }

    @Test
    fun testSampleRequestToString() {
        val request = SampleRequest(data = "toString test")
        val stringRepresentation = request.toString()
        assert(stringRepresentation.contains("SampleRequest"))
        assert(stringRepresentation.contains("toString test"))
    }
}