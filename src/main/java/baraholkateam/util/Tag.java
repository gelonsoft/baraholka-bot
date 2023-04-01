package baraholkateam.util;

public enum Tag {
    Moscow(TagType.City, "#Москва"),
    SPB(TagType.City, "#СПб"),
    Ekaterinburg(TagType.City, "#Екатеринбург"),
    Chelyabinsk(TagType.City, "#Челябинск"),
    Ulyanovsk(TagType.City, "#Ульяновск"),
    Omsk(TagType.City, "#Омск"),
    Belgorod(TagType.City, "#Белгород"),
    Petropavlovsk(TagType.City, "#Петропавловск"),
    Perm(TagType.City, "#Пермь"),
    Volgograd(TagType.City, "#Волгоград"),
    Kirov(TagType.City, "#Киров"),
    Khabarovsk(TagType.City, "#Хабаровск"),
    Actual(TagType.Actuality, "#актуально"),
    Sale(TagType.AdvertisementType, "#продажа"),
    Exchange(TagType.AdvertisementType, "#обмен"),
    Gift(TagType.AdvertisementType, "#дар"),
    Clothes(TagType.ProductCategories, "#одежда"),
    Shoes(TagType.ProductCategories, "#обувь"),
    ChildrenProducts(TagType.ProductCategories, "#детские_товары"),
    BeautyAndHealth(TagType.ProductCategories, "#красота_и_здоровье"),
    Books(TagType.ProductCategories, "#книги"),
    Hobby(TagType.ProductCategories, "#хобби"),
    HomeAppliance(TagType.ProductCategories, "#домашняя_техника"),
    Electronics(TagType.ProductCategories, "#электроника"),
    Sport(TagType.ProductCategories, "#спорт"),
    Other(TagType.ProductCategories, "#другое"),
    MenGoods(TagType.ProductCategories, "#мужское"),
    WomenGoods(TagType.ProductCategories, "#женское");

    private final TagType tagType;
    private final String name;

    Tag(TagType tagType, String name) {
        this.tagType = tagType;
        this.name = name;
    }

    public TagType getTagType() {
        return tagType;
    }

    public String getName() {
        return name;
    }
}
