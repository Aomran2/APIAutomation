package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentTestNGITestListener implements ITestListener {

    public static ExtentReports extent ;
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>(); //threadLocal handle parallel test execution, each test is independent

    //this method called when test suite is starts
    @Override
    public void onStart (ITestContext context){
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("extent.html"); //instance of ExtentSparkReporter and specifies the file name
        sparkReporter.config().setTheme(Theme.STANDARD); //the theme of the report
        sparkReporter.config().setDocumentTitle("Test Report");
        sparkReporter.config().setReportName("RestAssured Test Report");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter); // here I know where I attach the report "extent.html"
    }

    //flush finalizes the information to test report "Results added to report"
    @Override
    public void onFinish (ITestContext context){
        extent.flush();
    }


    //create new test with the name of the method, then assign to current thread "test.set(extentTest)" test is instance to threadLocal
    @Override
    public void onTestStart (ITestResult result){
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }


    //log when test pass
    @Override
    public void onTestSuccess (ITestResult result){
        test.get().pass("Test passed");
    }


    //log when test fail
    @Override
    public void onTestFailure (ITestResult result){
        test.get().fail("Test Failed");
    }


    //log when test skipped
    @Override
    public void onTestSkipped (ITestResult result){
        test.get().skip("Test Skipped");
    }

}
