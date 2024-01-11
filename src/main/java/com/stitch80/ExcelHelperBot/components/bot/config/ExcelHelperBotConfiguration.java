package com.stitch80.ExcelHelperBot.components.bot.config;

import com.stitch80.ExcelHelperBot.components.bot.ExcelHelperBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class ExcelHelperBotConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(ExcelHelperBot excelHelperBot) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(excelHelperBot);
        return telegramBotsApi;
    }

}