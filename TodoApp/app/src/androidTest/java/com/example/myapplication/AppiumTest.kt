package com.example.myapplication

import io.appium.java_client.MobileElement
import io.appium.java_client.android.Activity
import io.appium.java_client.android.AndroidDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.DesiredCapabilities
import org.testng.Assert
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


class AndroidBasicInteractionsTest : BaseTest() {
    private var driver: AndroidDriver<WebElement>? = null
    private val myAppActivity = ".MainActivity"
    private val myPackage = "com.example.myapplication"

    @BeforeClass
    @Throws(IOException::class)
    fun setUp() {
        val classpathRoot = File(System.getProperty("user.dir")!!)
        val appDir = File(classpathRoot, "../app")
        val app = File(appDir.canonicalPath, "build/outputs/apk/debug/app-debug.apk")
        val capabilities = DesiredCapabilities()
        capabilities.setCapability("app", app.absolutePath)
        capabilities.setCapability("automationName", "UiAutomator2")
        capabilities.setCapability("deviceName", "emulator-5554")
        capabilities.setCapability("allowTestPackages", true)
        capabilities.setCapability("fullReset", true)
        driver = AndroidDriver(serviceUrl, capabilities)
        driver!!.startActivity(Activity(myPackage, myAppActivity))
    }

    @AfterClass
    fun tearDown() {
        if (driver != null) {
//            driver!!.removeApp("com.example.myapplication")
            driver!!.quit()
        }
    }

    @Test
    fun testCreateNewTask() {
        val addTaskFAB =
            driver!!.findElementById("com.example.myapplication:id/add_task_fab") as MobileElement
        addTaskFAB.click()

        driver!!.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS)

        val addTaskTitleEditText =
            driver!!.findElementById("com.example.myapplication:id/add_task_title_edit_text") as MobileElement
        addTaskTitleEditText.sendKeys("New Task")

        val addTaskDescriptionEditText =
            driver!!.findElementById("com.example.myapplication:id/add_task_description_edit_text") as MobileElement
        addTaskDescriptionEditText.sendKeys("Task description")

        val saveTaskFAB =
            driver!!.findElementById("com.example.myapplication:id/save_task_fab") as MobileElement
        saveTaskFAB.click()

        driver!!.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS)

        val titleNewTask =
            driver!!.findElementById("com.example.myapplication:id/title_text") as MobileElement
        val descriptionTask = driver!!.findElementById("com.example.myapplication:id/description_text") as MobileElement
        val checkBox = driver!!.findElementById("com.example.myapplication:id/check_box_completed") as MobileElement

        Assert.assertEquals(titleNewTask.text,"New Task")
        Assert.assertEquals(descriptionTask.text,"Task description")
        Assert.assertEquals(checkBox.isSelected, false)
    }

    @Test
    fun testDeleteTask() {
        testCreateNewTask()

        val newTask = driver!!.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.widget.RelativeLayout/androidx.recyclerview.widget.RecyclerView/android.widget.RelativeLayout") as MobileElement
        newTask.click()

        driver!!.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS)

        val deleteButton = driver!!.findElementById("com.example.myapplication:id/delete_task") as MobileElement
        deleteButton.click()
    }

    @Test
    fun testUpdateTask() {
        testCreateNewTask()

        val newTask = driver!!.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.widget.RelativeLayout/androidx.recyclerview.widget.RecyclerView/android.widget.RelativeLayout") as MobileElement
        newTask.click()

        driver!!.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS)

        val addTaskTitleEditText =
            driver!!.findElementById("com.example.myapplication:id/add_task_title_edit_text") as MobileElement
        addTaskTitleEditText.sendKeys("New Task Update")

        val addTaskDescriptionEditText =
            driver!!.findElementById("com.example.myapplication:id/add_task_description_edit_text") as MobileElement
        addTaskDescriptionEditText.sendKeys("Task description Update")

        val checkBox = driver!!.findElementById("com.example.myapplication:id/check_box_completed_edit_task") as MobileElement
        checkBox.click()

        val saveTaskFAB =
            driver!!.findElementById("com.example.myapplication:id/save_task_fab") as MobileElement
        saveTaskFAB.click()

        driver!!.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS)

        val titleTaskUpdate =
            driver!!.findElementById("com.example.myapplication:id/title_text") as MobileElement
        val descriptionTaskUpdate = driver!!.findElementById("com.example.myapplication:id/description_text") as MobileElement
        val checkBoxUpdate = driver!!.findElementById("com.example.myapplication:id/check_box_completed") as MobileElement

        Assert.assertEquals(titleTaskUpdate.text,"New Task Update")
        Assert.assertEquals(descriptionTaskUpdate.text,"Task description Update")
        Assert.assertEquals(checkBoxUpdate.getAttribute("checked"), "true")
    }

    @Test
    fun testViewTaskTypePage() {
        val toolbarTittle = driver!!.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.LinearLayout/android.view.ViewGroup/android.widget.TextView")
        Assert.assertEquals(toolbarTittle.text, "All Tasks")

        val options = driver!!.findElementByAccessibilityId("More options")
        options.click()

        val incompleteTaskType = driver!!.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[2]/android.widget.LinearLayout")
        incompleteTaskType.click()
        Assert.assertEquals(toolbarTittle.text, "Incomplete Tasks")

        options.click()
        driver!!.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS)

        val completedTaskType = driver!!.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[3]/android.widget.LinearLayout")
        completedTaskType.click()
        Assert.assertEquals(toolbarTittle.text, "Completed Task")
    }
}