package baraholkateam.command;

import baraholkateam.database.SQLExecutor;
import baraholkateam.util.Advertisement;
import baraholkateam.util.Tag;
import baraholkateam.util.TagType;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchAdvertisements_AddAdvertisementType extends Command {
    private static final String CHOOSE_ADVERTISEMENT_TYPE = """
            Выберите тип объявления.
            Вы можете выбрать несколько хэштегов, нажав на них, либо не выбрать ни один.
            Для подтверждения выбора, нажмите на кнопку '%s'.""";
    private final Map<Long, String> chosenTags;
    private final SQLExecutor sqlExecutor;

    public SearchAdvertisements_AddAdvertisementType(String commandIdentifier, String description,
                                                     Map<Long, Message> lastSentMessage, Map<Long, String> chosenTags,
                                                     SQLExecutor sqlExecutor) {
        super(commandIdentifier, description, lastSentMessage);
        this.chosenTags = chosenTags;
        this.sqlExecutor = sqlExecutor;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        // TODO убрать метод
        sqlExecutor.removeAllData();
        // TODO убрать метод-заглушку
        createAds();

        String chosenTagsString = chosenTags.get(chat.getId());

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CHOSEN_HASHTAGS, chosenTagsString == null ? NO_HASHTAGS : chosenTagsString),
                showNextButton());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CHOOSE_ADVERTISEMENT_TYPE, NEXT_BUTTON_TEXT),
                getTags(TagType.AdvertisementType, true));
    }

    // TODO убрать метод-заглушку
    private void createAds() {
        Advertisement ad1 = new Advertisement(new ArrayList<>(1), "описание1", List.of(Tag.Exchange, Tag.Sale, Tag.Belgorod, Tag.Hobby), 1000L, List.of("контакт 1", "контакт 2"))
                .setChatId(1L)
                .setMessageId(2L)
                .setCreationTime(123L)
                .setNextUpdateTime(456L);
        Advertisement ad2 = new Advertisement(new ArrayList<>(1), "описание2", List.of(Tag.Exchange, Tag.Sale, Tag.Belgorod), 2000L, List.of("контакт 3"))
                .setChatId(3L)
                .setMessageId(4L)
                .setCreationTime(789L)
                .setNextUpdateTime(101112L);
        Advertisement ad3 = new Advertisement(new ArrayList<>(1), "описание3", List.of(Tag.Sale, Tag.Belgorod, Tag.Hobby), 3000L, List.of("контакт 4", "контакт 5", "контакт 6"))
                .setChatId(5L)
                .setMessageId(6L)
                .setCreationTime(131415L)
                .setNextUpdateTime(161718L);
        Advertisement ad4 = new Advertisement(new ArrayList<>(1), "описание4", List.of(Tag.Exchange, Tag.Sale, Tag.Belgorod, Tag.Hobby, Tag.Books), 4000L, new ArrayList<>(1))
                .setChatId(7L)
                .setMessageId(8L)
                .setCreationTime(192021L)
                .setNextUpdateTime(222324L);
        Advertisement ad5 = new Advertisement(new ArrayList<>(1), "описание5", List.of(Tag.Exchange, Tag.Sale, Tag.Belgorod, Tag.Hobby, Tag.Ekaterinburg), 5000L, List.of("контакт 7"))
                .setChatId(9L)
                .setMessageId(10L)
                .setCreationTime(252627L)
                .setNextUpdateTime(282930L);

        sqlExecutor.insertNewAdvertisement(ad1);
        sqlExecutor.insertNewAdvertisement(ad2);
        sqlExecutor.insertNewAdvertisement(ad3);
        sqlExecutor.insertNewAdvertisement(ad4);
        sqlExecutor.insertNewAdvertisement(ad5);
    }
}
