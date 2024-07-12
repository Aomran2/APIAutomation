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
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart (ITestContext context){
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("extent.html");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("Test Report");
        sparkReporter.config().setReportName("RestAssured Test Report");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    @Override
    public void onFinish (ITestContext context){
        extent.flush();
    }


    @Override
    public void onTestStart (ITestResult result){
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess (ITestResult result){
        test.get().pass("Test passed");
    }


    @Override
    public void onTestFailure (ITestResult result){
        test.get().fail("Test Failed");
    }

    @Override
    public void onTestSkipped (ITestResult result){
        test.get().skip("Test Skipped");
    }

}
