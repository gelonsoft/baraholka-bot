package baraholkateam.bot;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

public interface TgFileLoader {
    File downloadFileByFilePath(String filePath) throws TelegramApiException;
}
