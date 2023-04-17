package baraholkateam.command;

import baraholkateam.bot.BaraholkaBot;
import baraholkateam.util.State;
import baraholkateam.util.Tag;
import baraholkateam.util.TagType;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewAdvertisement_ConfirmType extends Command {
    private static final String CONFIRM_TYPE_TEXT = """
                Вы выбрали тип объявления: %s.
                Теперь выберите категории, наиболее подходящие для описания вашего товара""";

    public NewAdvertisement_ConfirmType(String commandIdentifier, String description,
                                        Map<Long, Message> lastSentMessage) {
        super(commandIdentifier, description, lastSentMessage);
    }
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        KeyboardButton addCategoriesButton = new KeyboardButton();
        addCategoriesButton.setText(State.NewAdvertisement_AddCategories.getDescription());

        KeyboardButton mainMenuButton = new KeyboardButton();
        mainMenuButton.setText(State.MainMenu.getDescription());

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(addCategoriesButton);
        keyboardFirstRow.add(mainMenuButton);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<Tag> tags = BaraholkaBot.getNewAdvertisement(chat.getId()).getTags();
        StringBuilder sb = new StringBuilder();
        for (Tag tag : tags) {
            if (tag.getTagType() == TagType.AdvertisementType) {
                sb.append(tag.getName()).append(", ");
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CONFIRM_TYPE_TEXT, sb), replyKeyboardMarkup);
    }
}
