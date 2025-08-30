package ru.netology.web.test;


import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.web.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.web.data.DataGenerator.Registration.getUser;
import static ru.netology.web.data.DataGenerator.getRandomLogin;
import static ru.netology.web.data.DataGenerator.getRandomPassword;

public class InternetBankTest {
    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Успешная авторизация с активным зарегистрированным пользователем")
    public void testSuccessLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=\"login\"] input").setValue(registeredUser.getLogin());
        $("[data-test-id=\"password\"] input").setValue(registeredUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $("h2").shouldHave(Condition.text("Личный кабинет"));

    }

    @Test
    @DisplayName("Авторизация с незарегистрированым пользователем")
    public void testGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=\"login\"] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=\"password\"] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"] .notification__title").shouldHave(Condition.exactText("Ошибка"), Duration.ofSeconds(10));
        $("[data-test-id=\"error-notification\"] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }


    @Test
    @DisplayName("Авторизация с заблокированым и зарегистрированным пользователем")
    public void testGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=\"login\"] input").setValue(blockedUser.getLogin());
        $("[data-test-id=\"password\"] input").setValue(blockedUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"] .notification__title").shouldHave(Condition.exactText("Ошибка"), Duration.ofSeconds(10));
        $("[data-test-id=\"error-notification\"] .notification__content").shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(10));

    }


    @Test
    @DisplayName("Авторизация с невалидным полем логина")
    public void testGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=\"login\"] input").setValue(wrongLogin);
        $("[data-test-id=\"password\"] input").setValue(registeredUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"] .notification__title").shouldHave(Condition.exactText("Ошибка"), Duration.ofSeconds(10));
        $("[data-test-id=\"error-notification\"] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Авторизация с невалидным полем пароль")
    public void testGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=\"login\"] input").setValue(registeredUser.getLogin());
        $("[data-test-id=\"password\"] input").setValue(wrongPassword);
        $("[data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"] .notification__title").shouldHave(Condition.exactText("Ошибка"), Duration.ofSeconds(10));
        $("[data-test-id=\"error-notification\"] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }


}
