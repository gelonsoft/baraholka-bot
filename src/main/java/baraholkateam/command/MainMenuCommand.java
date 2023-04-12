package baraholkateam.command;

import baraholkateam.database.SQLExecutor;
import baraholkateam.util.Advertisement;
import baraholkateam.util.State;
import baraholkateam.util.Tag;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainMenuCommand extends Command {
    // TODO добавить новые возможности
    private static final String MAIN_MENU = """
            Добро пожаловать в главное меню. Здесь Вы можете:
            1. Создать новое объявление: /%s;
            2. Удалить созданное объявление: /!!!добавить команду!!!;
            3. Найти нужные объявления по хэштегам: /%s.
            4. Получить справку по командам бота: /%s.""";
    private final SQLExecutor sqlExecutor;

    public MainMenuCommand(String commandIdentifier, String description, Map<Long, Message> lastSentMessage,
                           SQLExecutor sqlExecutor) {
        super(commandIdentifier, description, lastSentMessage);
        this.sqlExecutor = sqlExecutor;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        // TODO убрать метод-заглушку
        createAds(chat.getId());

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(MAIN_MENU, State.NewAdvertisement.getIdentifier(),
                        State.SearchAdvertisements.getIdentifier(), State.Help.getIdentifier()), null);
    }

    // TODO убрать метод-заглушку
    private void createAds(Long chatId) {
        Advertisement ad1 = new Advertisement(new ArrayList<>(1), "описание1", List.of(Tag.Exchange, Tag.Sale, Tag.Belgorod, Tag.Hobby), 1000L, List.of("контакт 1", "контакт 2"))
                .setChatId(chatId)
                .setMessageId(3L)
                .setCreationTime(System.currentTimeMillis())
                .setNextUpdateTime(System.currentTimeMillis());
        Advertisement ad2 = new Advertisement(new ArrayList<>(1), "описание2", List.of(Tag.Exchange, Tag.Sale, Tag.Belgorod), 2000L, List.of("контакт 3"))
                .setChatId(chatId)
                .setMessageId(4L)
                .setCreationTime(System.currentTimeMillis())
                .setNextUpdateTime(System.currentTimeMillis());
        Advertisement ad3 = new Advertisement(new ArrayList<>(1), "описание3", List.of(Tag.Sale, Tag.Belgorod, Tag.Hobby), 3000L, List.of("контакт 4", "контакт 5", "контакт 6"))
                .setChatId(chatId)
                .setMessageId(5L)
                .setCreationTime(System.currentTimeMillis())
                .setNextUpdateTime(System.currentTimeMillis());
        Advertisement ad4 = new Advertisement(new ArrayList<>(1), "описание4", List.of(Tag.Exchange, Tag.Sale, Tag.Belgorod, Tag.Hobby, Tag.Books), 4000L, new ArrayList<>(1))
                .setChatId(chatId)
                .setMessageId(6L)
                .setCreationTime(System.currentTimeMillis())
                .setNextUpdateTime(System.currentTimeMillis());
        Advertisement ad5 = new Advertisement(new ArrayList<>(1), "описание5", List.of(Tag.Exchange, Tag.Sale, Tag.Belgorod, Tag.Hobby, Tag.Ekaterinburg), 5000L, List.of("контакт 7"))
                .setChatId(chatId)
                .setMessageId(7L)
                .setCreationTime(System.currentTimeMillis())
                .setNextUpdateTime(System.currentTimeMillis());

        sqlExecutor.insertNewAdvertisement(ad1);
        sqlExecutor.insertNewAdvertisement(ad2);
        sqlExecutor.insertNewAdvertisement(ad3);
        sqlExecutor.insertNewAdvertisement(ad4);
        sqlExecutor.insertNewAdvertisement(ad5);
    }
}
