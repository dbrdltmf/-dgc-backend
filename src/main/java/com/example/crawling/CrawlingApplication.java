package com.example.crawling;

import common.DayOfWeekEnum;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class CrawlingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlingApplication.class, args).getBean(CrawlingApplication.class).test();
    }

    public void test(){
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        WebDriver driver = new ChromeDriver(options);

        int todayOfMonth = LocalDate.now().getDayOfMonth();

        try {
            driver.get("https://mc.skhystec.com/V3/menu.html");
            driver.findElement(By.xpath("//*[@id=\"btnC_CJ\"]")).click();
            driver.findElement(By.xpath("//*[@id=\"btnR_12\"]")).click();
            driver.findElement(By.xpath("//*[@id=\"btnT_L\"]")).click();    //현재 청주 2캠 중식만 기능

            int weekIndex = DayOfWeekEnum.valueOf(
                    driver.findElement(By.className("menu_date")).getAttribute("placeholder").substring(12,13))
                    .ordinal();
            int dayIndex = 0;
            while(weekIndex <= DayOfWeekEnum.일.ordinal()) { //일요일까지만 받아오기

                driver.findElement(By.xpath("/html/body/div[2]/section[2]/nav/p")).click(); //달력 열기
                List<WebElement> menuDateButton = driver.findElements(By.className("ui-state-default"));  //달력 a 하나하나..
                menuDateButton.get(todayOfMonth -1 + dayIndex).click();


                Thread.sleep(1500); //자바스크립트 로드 시간보다 자바 컴파일이 더 빨라 억지로 슬립을 줌..

                List<WebElement> menuList = driver.findElements(By.className("menu_list"));

                for (WebElement element : menuList) {
                    String imgUrl = element.findElement(By.tagName("img")).getAttribute("src");
                    String menuRestaurant = element.findElement(By.className("menu_restaurant")).getText();
                    String[] menuName = element.findElement(By.className("menu_name")).getText().split(" - ");
                    String mainMenu = menuName[0];
                    String menuKcal = menuName[1];
                    String menuCountryOfOrigin = element.findElement(By.className("menu_CountryofOrigin")).getText();
                    List<WebElement> sideDish = element.findElements(By.cssSelector("ul.menu_sideDish_list_wrap > li"));

                    //                System.out.println(element.getText());

                    System.out.println("------"+(todayOfMonth+dayIndex)+"("+DayOfWeekEnum.values()[weekIndex]+")--------------------------");
                    System.out.println("제조이미지 : " + imgUrl);
                    System.out.println("메뉴종류 : " + menuRestaurant);
                    System.out.println("메인메뉴 : " + mainMenu);
                    System.out.println("총열량 : " + menuKcal);
                    System.out.println("원산지 : " + menuCountryOfOrigin);

                    for (WebElement webElement : sideDish) {
                        System.out.println("사이드 : " + webElement.getText());
                    }

                    System.out.println("-----------------------------------");

                }
                weekIndex++;
                dayIndex++;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

}
