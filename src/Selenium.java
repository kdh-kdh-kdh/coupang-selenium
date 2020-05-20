import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Selenium {
	// WebDriver
	private WebDriver driver;

	// Properties
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static final String WEB_DRIVER_PATH = "D:\\�����Ͽ�\\chromedriver.exe";

	public Selenium() {
		super();

		// System Property SetUp
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
		options.addArguments("disable-gpu");
		options.addArguments("disable-infobars");
		options.addArguments("--disable-extensions");
		options.addArguments("headless");

		// Driver SetUp
		driver = new ChromeDriver(options);
	}

	int code = 1;

	public void crawlImg(Map<String, String> list) {
		String title = "";

		try {
			Iterator<String> iterator = list.keySet().iterator();
			BufferedImage bi = null;

			while (iterator.hasNext()) {
				int i = 1;
				title = iterator.next();
				File file = new File("D:\\alrba\\" + title);

				if (!file.exists()) {
					file.mkdirs();
				} else
					continue;

				String href = list.get(title);

				driver.get(href);
				System.out.println(title + " OPEN");
				WebElement sendKey = driver.findElement(By.tagName("body"));


				Thread.sleep(1000);
				sendKey.sendKeys(Keys.END);
				Thread.sleep(1000);
				sendKey.sendKeys(Keys.END);
				Thread.sleep(1000);
				sendKey.sendKeys(Keys.END);
				if (code == 1) {
					List<WebElement> webElements = driver.findElement(By.id("desc-lazyload-container"))
							.findElements(By.tagName("img"));

					System.out.println("������� ? : " + webElements.size());

					System.out.println("�̹��� �Ľ���");
					// ���̹��� �Ľ�
					for (WebElement webElement : webElements) {
						String imageLink = webElement.getAttribute("src");
						String filePath = "D:\\alrba\\" + title + "/" + (i++) + ".jpg";
						saveImage(imageLink, filePath);

					}

					System.out.println("�Ľ̿Ϸ�");
					System.out.println("���λ��� �Ľ���");
					// ���λ��� �Ľ�
					int mainIndex = 1;
					
					List<WebElement> webElements2 = driver.findElement(By.xpath("//*[@id=\"mod-detail-bd\"]/div[2]/div[13]/div/div/div/div[1]/div[2]/ul")).findElements(By.tagName("li"));
					for (WebElement main : webElements2) {
						main.click();
						//main.findElement(By.className("box-img")).click();

						String filePath = "D:/alrba/" + title + "/m" + (mainIndex++) + ".jpg";
						String imageLink = driver.findElement(By.xpath("//*[@id=\"mod-detail-bd\"]/div[1]/div/div/div/div/div[1]/div/a/img"))
								.getAttribute("src");
						System.out.println(imageLink);
						saveImage(imageLink, filePath);
					}
					System.out.println("���λ��� �Ľ̿Ϸ�");

				} else if (code == 2) {
					int mainIndex = 1;
					List<WebElement> detailImageList = driver.findElement(By.xpath("//*[@id=\"description\"]"))
							.findElements(By.tagName("img"));
					for (WebElement image : detailImageList) {

						String imageLink = image.getAttribute("src");
						String filePath = "D:\\alrba\\" + title + "/" + (i++) + ".jpg";
						saveImage(imageLink, filePath);
					}

					// ���λ��� �Ľ�
					List<WebElement> mainImageList = driver.findElement(By.xpath("//*[@id=\"J_isku\"]/div/dl[2]/dd/ul"))
							.findElements(By.tagName("li"));
					for (WebElement mainImage : mainImageList) {
						mainImage.click();
						String imageLink = driver.findElement(By.xpath("//*[@id=\"J_ImgBooth\"]")).getAttribute("src");
						String filePath = "D:/alrba/" + title + "/m" + (mainIndex++) + ".jpg";
						saveImage(imageLink, filePath);

					}
				}
			}

		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException�߻� ������ ������ �ٽ� �ٿ�ε� �մϴ�.");
			deleteDirectory(title);
			crawlImg(list);
		} catch (org.openqa.selenium.NoSuchElementException e) {
			e.printStackTrace();
			if (code == 1)
				code = 2;
			else
				code = 1;
			deleteDirectory(title);

			crawlImg(list);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		finally {

		}

	}

	/** ���� ���� **/
	public void deleteDirectory(String title) {
		File deleteDirectory = new File("D:/alrba/" + title);

		System.out.println("������� : " + deleteDirectory.getPath());

		File[] deleteFileList = deleteDirectory.listFiles();
		for (File deleteFile : deleteFileList) {
			System.out.println(deleteFile.getName());
			deleteFile.delete();
		}
		deleteDirectory.delete();
	}

	/** �̹��� ���� **/
	public void saveImage(String imageLink, String filePath) {
		File file = new File(filePath);
		BufferedImage bi = null;
		URL url;
		try {
			url = new URL(imageLink);
			bi = ImageIO.read(url);
			ImageIO.write(bi, "jpg", file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> crawlTitle(int min, int max) {
		String url = "https://www.chinatmall.co.kr/user/mypage/one?o=2";
		HashMap<String, String> list = new HashMap<String, String>();
		try {
			driver.get(url);

			List<WebElement> webElements = driver.findElement(By.className("tbl_course"))
					.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

			for (WebElement webElement : webElements) {
				String title = webElement.findElement(By.className("tdLeft")).findElement(By.tagName("img"))
						.getAttribute("alt");
				String href = webElement.findElement(By.className("subject")).getAttribute("href");

				int num = 0;
				if (isStringInt(title.substring(0, 2))) {
					num = Integer.parseInt(title.substring(0, 2));
				}

				if (min <= num && num <= max) {
					list.put(title, href);
				}
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return list;

	}

	public String crawlKeyword(String keyword) {
		List<WebElement> titleList = new ArrayList<WebElement>();
		String url = "https://www.itemscout.io/keyword/";
		StringBuilder sb = new StringBuilder();
		try {
			driver.get(url);
			WebDriverWait wait = new WebDriverWait(driver, 10);
			WebElement webElement = driver.findElement(By.className("input-keyword"));
			webElement.sendKeys(keyword);

			webElement = driver.findElement(By.className("btn-search-keyword"));
			webElement.click();

			Thread.sleep(1000);
			titleList = driver.findElements(By.className("related-keyword-item"));

			for (WebElement list : titleList) {
				// System.out.print(list.getText() + ",");
				sb.append(list.getText() + ",");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/** ���ڸ� true �ƴϸ� false **/
	public static boolean isStringInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/** ���� ��ϰ������� **/
	public void getDirectoryList(int index) {
		String isDir = "D:\\alrba\\";
		File directory = null;

		int newWidth = 500;
		int newHeight = 500;
		Image image;
		// ���� ���丮

		for (File info : new File(isDir).listFiles()) {
			if (info.isDirectory()) {
				if (isStringInt(info.getName().substring(0,2))&&index == Integer.parseInt(info.getName().substring(0, 2))) {
					System.out.println(info.getName());
					directory = info;
					break;
				}
			}
		}

		for (File info : directory.listFiles()) {
			if (info.isFile()) {
				if (info.getName().substring(0, 1).equals("m")) {
					try {
						image = ImageIO.read(info);
						
						// �̹��� ��������
						// Image.SCALE_DEFAULT : �⺻ �̹��� �����ϸ� �˰��� ���
						// Image.SCALE_FAST : �̹��� �ε巯�򺸴� �ӵ� �켱
						// Image.SCALE_REPLICATE : ReplicateScaleFilter Ŭ������ ��üȭ �� �̹��� ũ�� ���� �˰���
						// Image.SCALE_SMOOTH : �ӵ����� �̹��� �ε巯���� �켱
						// Image.SCALE_AREA_AVERAGING : ��� �˰��� ���
						Image resizeImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

						// �� �̹��� �����ϱ�
						BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
						Graphics g = newImage.getGraphics();
						g.drawImage(resizeImage, 0, 0, null);
						g.dispose();
						ImageIO.write(newImage, "jpg", info);

					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		}

		System.out.println("���� �̹����� ���������� ������¡ �߽��ϴ�.");

	}

	/** ��ǰ ����ϱ� **/
	public void insertCommodity(String code, int index, String size, String color, String keyword) {
		System.out.println("size : " + size);
		String url = "https://wing.coupang.com/tenants/seller-web/vendor-inventory/form?displayCategoryCode=" + code;
		String isDir = "D:\\alrba\\";
		try {
			driver.get(url);

			// �귣�� �Է�
			WebElement brand = driver.findElement(By.id("brand"));
			brand.sendKeys("���");

			// ������ �Է�
			WebElement manufacture = driver.findElement(By.id("manufacture"));
			manufacture.sendKeys("���");

			// ��ǰ�����̸� ��������
			String directoryName = selectDirectory(isDir, index);

			// ��ϻ�ǰ�� �Է�
			String productName = directoryName.split("\\(")[0];
			WebElement productNameInput = driver.findElement(By.id("productName"));
			productNameInput.sendKeys(productName);

			// ��ǰ�� �Է�
			String generalProductName = productName.split("\\.")[1];
			WebElement generalProductNameInput = driver.findElement(By.id("generalProductName"));
			generalProductNameInput.sendKeys(generalProductName);

			// ������ �Է�
			driver.findElement(By.xpath(
					"//*[@id=\"vendor-inventory-item-section\"]/div[2]/div[2]/div[1]/div/div/fieldset[1]/div/div/div[2]/div/div/span/input"))
					.sendKeys(size);

			Thread.sleep(500);
			// ���� �Է�
			driver.findElement(By.xpath(
					"//*[@id=\"vendor-inventory-item-section\"]/div[2]/div[2]/div[1]/div/div/fieldset[2]/div/div/div[2]/div/div/span/input"))
					.sendKeys(color);

			// �ɼ� �����ϱ� Ŭ��
			driver.findElement(By.xpath(
					"//*[@id=\"vendor-inventory-item-section\"]/div[2]/div[2]/div[1]/div/div/div/div/div[2]/button"))
					.click();

			Thread.sleep(1000);

			// table �� ���� row ��������
			WebElement selectInput = driver.findElement(By.id("itemAttribute")).findElements(By.tagName("tr")).get(1);

			// ���� ������ �� ���
			String price = directoryName.split("\\(")[1];
			price = price.replaceAll("\\)", "");
			selectInput.findElements(By.tagName("td")).get(13).click();
			selectInput.findElements(By.tagName("td")).get(13).findElement(By.tagName("input")).sendKeys(price);
			driver.findElement(By.xpath("//*[@id=\"itemAttribute_salePrice\"]/div[1]/button")).click();

			// �ʱ� ������ ���
			selectInput.findElements(By.tagName("td")).get(18).click();
			selectInput.findElements(By.tagName("td")).get(18).findElement(By.tagName("input")).sendKeys("100");
			driver.findElement(By.xpath("//*[@id=\"itemAttribute_maximumBuyCount\"]/div[1]/button")).click();

			// ������� �˾�
			selectInput.findElements(By.tagName("td")).get(21).click();

			Thread.sleep(1000);
			driver.findElement(By.xpath("//*[@id=\"noticeCategoryName\"]")).click();

			driver.findElement(By.xpath("//*[@id=\"noticeCategoryName\"]/option[2]")).click();
			// ������� �˾����� �������� ���� ���� �� ���
			List<WebElement> list = driver.findElements(By.xpath("//*[@id=\"noticeContents\"]/tbody/tr"));
			for (WebElement content : list) {
				if (!content.findElements(By.tagName("td")).get(1).getText().equals("ǰ����������")) {
					content.findElement(By.tagName("textarea")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					content.findElement(By.tagName("textarea")).sendKeys(Keys.BACK_SPACE);
					content.findElement(By.tagName("textarea")).sendKeys("�� ������ ����");
				}
			}
			driver.findElement(By.xpath("//*[@id=\"applyNoticeToAllRowsButton\"]")).click();

			// ��� �ҿ�Ⱓ ���
			selectInput.findElements(By.tagName("td")).get(23).click();
			selectInput.findElements(By.tagName("td")).get(23).findElement(By.tagName("input")).sendKeys("9");
			driver.findElement(By.xpath("//*[@id=\"itemAttribute_outboundShippingTime\"]/div[1]/button")).click();

			// ���ڵ� ���
			selectInput.findElements(By.tagName("td")).get(24).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//*[@id=\"barcodeForm\"]/div[2]/div[1]/label[2]/input")).click();
			Thread.sleep(500);
			// driver.findElement(By.xpath("//*[@id=\"emptyBarcodeReasonSelector\"]"));
			driver.findElement(By.xpath("//*[@id=\"emptyBarcodeReasonSelector\"]/option[3]")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//*[@id=\"applyBarcodeToAllRowsButton\"]")).click();

			// ���� �Ű�� ���� ���
			selectInput.findElements(By.tagName("td")).get(25).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//*[@id=\"certificationForm\"]/div[2]/label[3]/input")).click();
			driver.findElement(By.xpath("//*[@id=\"applyCertificationToAllRowsButton\"]")).click();
			Thread.sleep(500);

			// �˻��� ���
			driver.findElement(By.xpath("//*[@id=\"search-tag-section\"]/div[1]/div[2]/button")).click();
			Thread.sleep(500);
			driver.findElement(
					By.xpath("//*[@id=\"search-tag-section\"]/div[2]/div[2]/div/div[1]/div[2]/div/span/input"))
					.sendKeys(keyword);
			driver.findElement(
					By.xpath("//*[@id=\"search-tag-section\"]/div[2]/div[2]/div/div[1]/div[2]/div/span/input"))
					.sendKeys(Keys.ENTER);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String selectDirectory(String isDir, int index) {
		for (File info : new File(isDir).listFiles()) {
			if (info.isDirectory()) {
				if (isStringInt(info.getName().substring(0, 2))&&index==Integer.parseInt(info.getName().substring(0,2))){
					return info.getName();
				}
			}
		}
		return null;
	}

}
