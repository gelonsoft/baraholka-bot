package baraholkateam.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllTags implements Serializable {

    @JsonProperty("city")
    private final List<String> city;

    @JsonProperty("actuality")
    private final List<String> actuality;

    @JsonProperty("advertisement_type")
    private final List<String> advertisementType;

    @JsonProperty("product_categories")
    private final List<String> productCategories;

    public AllTags() {
        city = new ArrayList<>(Arrays.stream(Tag.values())
                .filter(tag -> tag.getTagType() == TagType.City)
                .map(Tag::getName)
                .toList());
        actuality = new ArrayList<>(Arrays.stream(Tag.values())
                .filter(tag -> tag.getTagType() == TagType.Actuality)
                .map(Tag::getName)
                .toList());
        advertisementType = new ArrayList<>(Arrays.stream(Tag.values())
                .filter(tag -> tag.getTagType() == TagType.AdvertisementType)
                .map(Tag::getName)
                .toList());
        productCategories = new ArrayList<>(Arrays.stream(Tag.values())
                .filter(tag -> tag.getTagType() == TagType.ProductCategories)
                .map(Tag::getName)
                .toList());
    }
}
