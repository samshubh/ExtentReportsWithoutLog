package com.qa.test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ExtentReportScreenshot {
    
	public static WebDriver driver;
	
	public static ExtentReports extent;
	public static ExtentTest extentTest;
	
	
	@BeforeTest
	  public void setExtent()
	  {
		extent = new ExtentReports(System.getProperty("user.dir")+"/test-output/Extent.html", true);
		
		//extent.addSystemInfo("Host Name","srijaytechLAP-1");
		extent.addSystemInfo("User Name","Shubham");
		extent.addSystemInfo("OS","Windows 7");
		extent.addSystemInfo("Java version","1.8.0_181");
	  }
	
	@AfterTest
	  public void endReport()
	  {
		extent.flush();
		
		extent.close();
	  }
	
	
	  public static String getScreenshot(WebDriver driver,String screenshotName) throws IOException
	  {
		  String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		  
		  TakesScreenshot ts = ((TakesScreenshot)driver);
		  
		   File src= ts.getScreenshotAs(OutputType.FILE);
		   
		   String destination = System.getProperty("user.dir") + "/FailedTestsScreenshots/" + screenshotName + date
					+ ".png";
		   
		   File finalDestination = new File(destination);
		   
		   FileUtils.copyFile(src, finalDestination);
		   
		return destination;
	  }
	
	@BeforeMethod
	  public void setUp() throws InterruptedException
	  {
		System.setProperty("webdriver.chrome.driver","F:\\ChromeDriver77\\chromedriver_win32\\chromedriver.exe");
		
		driver = new ChromeDriver();
		
		driver.manage().window().maximize();
		 driver.manage().deleteAllCookies();
		 
		  driver.manage().timeouts().pageLoadTimeout(40,TimeUnit.SECONDS);
		   
		    driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);

		      driver.get("https://www.abhibus.com/");
		       Thread.sleep(4000);
	  }
	
	
	   @Test(priority=1)
	  public void logoTest()
	  
	  {
		extentTest= extent.startTest("logo test");
		
		   boolean b =driver.findElement(By.xpath("//div[@class='wrap']//a//img")).isDisplayed();
		  System.out.println(b);
		  Assert.assertTrue(b);
	  }
	   
	   @Test(priority=2)
	    public void verifySearch() {
		   extentTest= extent.startTest("Search test");
		   
		  String text = driver.findElement(By.xpath("//a[@class='btnab icosearch12']")).getText();
		    System.out.println(text);
		   
	   }
	
	@AfterMethod
	  public void tearDown(ITestResult result) throws IOException
	  
	  {
		
		if(result.getStatus()==ITestResult.FAILURE)
		{
			extentTest.log(LogStatus.FAIL,"The test case failed is"+result.getName());
			  extentTest.log(LogStatus.FAIL,"The test case failed is"+result.getThrowable());
			  
			   String screenshotPath= ExtentReportScreenshot.getScreenshot(driver, result.getName());
			   
			   extentTest.log(LogStatus.FAIL,extentTest.addScreenCapture(screenshotPath));
		}
		
		else if(result.getStatus()==ITestResult.SKIP)
		{
			extentTest.log(LogStatus.SKIP,"The test case skipped is "+result.getName());
		}
		
		else if(result.getStatus()==ITestResult.SUCCESS)
		{
			extentTest.log(LogStatus.PASS,"The test cases passed is"+result.getName());
		}
		
		 extent.endTest(extentTest);
		driver.quit();
	  }
}
