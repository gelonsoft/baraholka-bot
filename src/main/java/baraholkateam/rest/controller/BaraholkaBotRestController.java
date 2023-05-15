package baraholkateam.rest.controller;

import baraholkateam.rest.model.ActualAdvertisement;
import baraholkateam.rest.model.CurrentAdvertisement;
import baraholkateam.rest.service.ActualAdvertisementService;
import baraholkateam.util.AllTags;
import baraholkateam.util.TelegramUserInfo;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер взаимодействия с клиентами по REST API.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class BaraholkaBotRestController {

    @Autowired
    private BaraholkaBotRestControllerHelper controllerHelper;

    @Autowired
    private ActualAdvertisementService actualAdvertisementService;

    @RequestMapping(method = RequestMethod.POST, value = "/my_advertisements",
            headers = {"content-type=multipart/form-data"})
    public ResponseEntity<List<ActualAdvertisement>> getUserAdvertisements(@ModelAttribute TelegramUserInfo userInfo) {
        Long userId = userInfo.getId();

        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!controllerHelper.checkUserRights(userInfo)
                || !controllerHelper.checkIsUserChannelMember(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<ActualAdvertisement> advertisements = actualAdvertisementService.getByChatId(userId);

        controllerHelper.convertPhotoIdsToPhotoBase64Strings(advertisements);

        return new ResponseEntity<>(advertisements, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add_advertisement",
            headers = {"content-type=application/json"})
    public ResponseEntity<HttpStatus> addNewAdvertisement(@RequestBody JsonNode json) {
        TelegramUserInfo userInfo;
        CurrentAdvertisement currentAdvertisement;

        try {
            userInfo = controllerHelper.getUserInfo(json);
            currentAdvertisement = controllerHelper.getCurrentAdvertisement(json);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!controllerHelper.checkUserRights(userInfo)
                || !controllerHelper.checkIsUserChannelMember(userInfo.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (!controllerHelper.addNewAdvertisement(currentAdvertisement, json)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete_advertisement/{message_id}",
            headers = {"content-type=multipart/form-data"})
    public ResponseEntity<HttpStatus> deleteAdvertisement(@ModelAttribute TelegramUserInfo userInfo,
                                                          @PathVariable("message_id") Long messageId) {
        Long userId = userInfo.getId();

        if (userId == null || messageId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!controllerHelper.checkUserRights(userInfo)
                || !controllerHelper.checkIsUserChannelMember(userId)
                || !controllerHelper.isUserMessageOwner(userId, messageId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            controllerHelper.deleteMessage(messageId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        actualAdvertisementService.removeAdvertisement(messageId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/search_advertisements",
            headers = {"content-type=multipart/form-data"})
    public ResponseEntity<List<ActualAdvertisement>> searchAdvertisements(@ModelAttribute TelegramUserInfo userInfo,
                                                                          @RequestParam String tags) {
        Long userId = userInfo.getId();

        if (userId == null || tags == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!controllerHelper.checkUserRights(userInfo)
                || !controllerHelper.checkIsUserChannelMember(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String[] allTags = tags.split(" ");

        List<ActualAdvertisement> advertisements = actualAdvertisementService.tagsSearch(allTags);

        controllerHelper.convertPhotoIdsToPhotoBase64Strings(advertisements);

        return new ResponseEntity<>(advertisements, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/all_tags",
            headers = {"content-type=multipart/form-data"})
    public ResponseEntity<AllTags> getAllTags(@ModelAttribute TelegramUserInfo userInfo) {
        Long userId = userInfo.getId();

        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!controllerHelper.checkUserRights(userInfo)
                || !controllerHelper.checkIsUserChannelMember(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(new AllTags(), HttpStatus.OK);
    }
}
