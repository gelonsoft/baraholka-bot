package baraholkateam.util;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import java.util.List;

public class Advertisement {
    private Long authorId;
    private List<InputFile> photos;
    private List<String> tags;
    private String description;
    private int price;
    private List<String> contacts;
    private boolean isActual;

    public Advertisement() {
    }

    public static class AdvertisementBuilder {
        private final Advertisement ad;

        public AdvertisementBuilder() {
            this.ad = new Advertisement();
        }

        public Advertisement build() {
            return ad;
        }

        public AdvertisementBuilder setAuthorId(Long authorId) {
            ad.authorId = authorId;
            return this;
        }

        public AdvertisementBuilder setPhotos(List<InputFile> photos) {
            ad.photos = photos;
            return this;
        }

        public AdvertisementBuilder setTags(List<String> tags) {
            ad.tags = tags;
            return this;
        }

        public AdvertisementBuilder setDescription(String description) {
            ad.description = description;
            return this;
        }

        public AdvertisementBuilder setPrice(int price) {
            ad.price = price;
            return this;
        }

        public AdvertisementBuilder setContacts(List<String> contacts) {
            ad.contacts = contacts;
            return this;
        }

        public AdvertisementBuilder setActual(boolean isActual) {
            ad.isActual = isActual;
            return this;
        }
    }
}
