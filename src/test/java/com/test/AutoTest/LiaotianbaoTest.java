package com.test.AutoTest;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

/**
 * 撸聊天宝金币 
 * 欲先撸金币，请提前登录好你的账号
 *
 */
public class LiaotianbaoTest {

	AndroidDriver<?> driver = null;
	boolean init = false;
	String port = "4723";
	String bp = "4724";
	String udid = "AKC7N18515007324";// 填入你的Android设备序列号
	Dimension sizestr = null;
	int x = 0;
	int y = 0;
	int Center_X = 0;
	int Center_Y = 0;

	@Before
	public void beforeClass() throws MalformedURLException, InterruptedException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "6.0");// Android设备版本
		// capabilities.setCapability(MobileCapabilityType.APP,
		// app.getAbsolutePath());// apk路径
		capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.bullet.messenger");
		capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
				"com.smartisan.flashim.main.activity.MainActivity");
		capabilities.setCapability("androidUseRunningApp", true);
		capabilities.setCapability(MobileCapabilityType.UDID, udid);// 设备唯一序列号
		capabilities.setCapability(MobileCapabilityType.NO_RESET, true);// 不重新安装app
		capabilities.setCapability("noSign", true);// 禁止重签名
		capabilities.setCapability("unicodeKeyboard", true);// 使用appium输入法输入中文
		capabilities.setCapability("resetKeyboard", true);// 重设键盘为appium
		driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:" + port + "/wd/hub"), capabilities);
		Thread.sleep(5000);
	}

	@After
	public void afterClass() {
		driver.quit();
	}

	@Test
	public void testAndroid() throws InterruptedException {
		// 判断是否弹出邀请页面
		if (getElementById("com.bullet.messenger:id/invite_img") != null) {
			driver.findElementById("com.bullet.messenger:id/bullet_close_btn").click();
			Thread.sleep(1000);
		}
		driver.findElementByXPath(
				"//android.widget.TextView[@resource-id='com.bullet.messenger:id/bottom_tab_text' and @text='领钱']")
				.click();
		Thread.sleep(1000);
		// 截图留存刷新闻前的金币数量
		getAppiumScreenShot("刷新闻前的金币数量");
		// 开始刷新闻
		driver.findElementByXPath(
				"//android.widget.TextView[@resource-id='com.bullet.messenger:id/bottom_tab_text' and @text='新闻']")
				.click();
		Thread.sleep(1000);
		// 刷新闻资讯
		driver.findElementByXPath("//android.widget.TextView[@text='新闻资讯' and @index='0']").click();
		Thread.sleep(1000);
		// 定义你要刷的个数
		for (int a = 0; a < 10; a++) {
			// 左划到不同标签的新闻资讯
			moveView("LEFT");
			for (int b = 1; b <= 10; b++) {
				System.out.println("这是第" + b + "次循环");
				if (b != 1) {
					// 下拉刷新获取最新新闻
					moveView("DOWN");
				}
				// 如果只刷新闻标题，不刷新闻正文，可注释该for循环
				for (int i = 1; i <= 3; i++) {
					driver.findElementByXPath(
							"//android.support.v4.view.ViewPager/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.ListView/android.widget.LinearLayout["
									+ i + "]")
							.click();
					for (int j = 0; j <= 5; j++) {
						if (j == 0) {
							Thread.sleep(2000);
						}
						moveView("UP");
						Thread.sleep(2000);
					}
					driver.navigate().back();
				}
			}
		}
		// 截图留存刷新闻后的金币数量
		driver.findElementByXPath(
				"//android.widget.TextView[@resource-id='com.bullet.messenger:id/bottom_tab_text' and @text='领钱']")
				.click();
		Thread.sleep(1000);
		getAppiumScreenShot("刷新闻后的金币数量");
	}

	@SuppressWarnings("rawtypes")
	public void swipe(int start_x, int start_y, int end_x, int end_y, double duration) {
		new TouchAction(driver).press(PointOption.point(start_x, start_y))
				.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(Math.round(duration))))
				.moveTo(PointOption.point(end_x, end_y)).release().perform();
	}

	public void moveView(String orientation) {
		try {
			if (sizestr == null) {
				sizestr = driver.manage().window().getSize();
				x = sizestr.width;
				y = sizestr.height;
				Center_X = x / 2;
				Center_Y = y / 2;
			} else {
				if (orientation.equals("UP")) {
					System.out.println(
							"滑动坐标：(" + Center_X + "," + Center_Y + "," + Center_X + "," + (Center_Y - 500) + ")");
					swipe(Center_X, Center_Y, Center_X, (Center_Y - 500), 2);
					System.out.println("成功向上滑动了");
				} else if (orientation.equals("DOWN")) {
					System.out.println(
							"滑动坐标：(" + Center_X + "," + Center_Y + "," + Center_X + "," + (Center_Y + 500) + ")");
					swipe(Center_X, Center_Y, Center_X, (Center_Y + 500), 2);
					System.out.println("成功向下滑动了");
				} else if (orientation.equals("RIGHT")) {
					System.out.println(
							"滑动坐标：(" + (Center_X - 100) + "," + Center_Y + "," + (x - 100) + "," + Center_Y + ")");
					swipe((Center_X - 100), Center_Y, (x - 100), Center_Y, 0.5);
					System.out.println("成功向右滑动了");
				} else if (orientation.equals("LEFT")) {
					System.out.println("滑动坐标：(" + (Center_X + 100) + "," + Center_Y + "," + 1 + "," + Center_Y + ")");
					swipe((Center_X + 100), Center_Y, 1, Center_Y, 0.5);
					System.out.println("成功向左滑动了");
				} else {
					System.out.println("传入的滑动方向参数有误！！");
				}
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public WebElement getElementById(String id) {
		try {
			return driver.findElementById(id);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Appium屏幕截图
	 * 
	 * @param name 文件名
	 */
	public String getAppiumScreenShot(String name) {
		// 图片保存位置
		String fileName = System.getProperty("user.dir") + "/" + name + "-" + getCurrentDateTime() + ".jpg";
		File screenShotFile = driver.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(screenShotFile, new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return fileName;
	}

	public static String getCurrentDateTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");// 设置日期格式
		return df.format(new Date());
	}

}
