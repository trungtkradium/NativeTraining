package com.example.myapplication

import io.appium.java_client.service.local.AppiumDriverLocalService
import org.testng.annotations.AfterSuite
import org.testng.annotations.BeforeSuite
import java.io.IOException
import java.net.URL


abstract class BaseTest {
    private lateinit var service: AppiumDriverLocalService
    @BeforeSuite
    @Throws(IOException::class)
    fun globalSetup() {
        service = AppiumDriverLocalService.buildDefaultService()
        service.start()
    }

    @AfterSuite
    fun globalTearDown() {
            service.stop()
    }

    val serviceUrl: URL
        get() = service.url
}