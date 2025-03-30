package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void setDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void startSetup() {
        open("http://localhost:9999");
    }

    @Test
    void happyCase() {
        String firstRegistrationDay = DataGenerator.generateDate(5, "dd.MM.yyyy");
        String secondRegistrationDay = DataGenerator.generateDate(10, "dd.MM.yyyy");
        DataGenerator.UserInfo user = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstRegistrationDay);
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement] .checkbox__box").click();
        $(".button__text").click();
        $("[data-test-id=success-notification]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=success-notification] .notification__title").shouldHave(text("Успешно!"));
        $("[data-test-id=success-notification] .notification__content").shouldHave(text("Встреча успешно запланирована на " + firstRegistrationDay));
        $("[data-test-id=success-notification] .icon-button").click();
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(secondRegistrationDay);
        $(".button__text").click();
        $("[data-test-id=replan-notification]").shouldBe(visible);
        $("[data-test-id=replan-notification] .notification__title").shouldHave(text("Необходимо подтверждение"));
        $("[data-test-id=replan-notification] .notification__content").shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id=replan-notification] .button__content").click();
        $("[data-test-id=success-notification]").shouldBe(visible);
        $("[data-test-id=success-notification] .notification__title").shouldHave(text("Успешно!"));
        $("[data-test-id=success-notification] .notification__content").shouldHave(text("Встреча успешно запланирована на " + secondRegistrationDay));
    }
}
