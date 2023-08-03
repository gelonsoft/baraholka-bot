package baraholkateam.rest.controller;

import baraholkateam.rest.model.ActualObyavleniye;
import baraholkateam.rest.model.CurrentObyavleniye;
import baraholkateam.rest.service.ActualObyavleniyeService;
import baraholkateam.util.AllTags;
import baraholkateam.util.TelegramUserInfo;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    private ActualObyavleniyeService actualObyavleniyeService;

    @RequestMapping(method = RequestMethod.POST, value = "/my_obyavleniyes",
            headers = {"content-type=application/json"})
    public ResponseEntity<List<ActualObyavleniye>> getUserObyavleniyes(@RequestBody TelegramUserInfo userInfo) {
        Long userId = userInfo.getId();

        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!controllerHelper.checkUserRights(userInfo)
                || !controllerHelper.checkIsUserChannelMember(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<ActualObyavleniye> obyavleniyes = actualObyavleniyeService.getByChatId(userId);

        return new ResponseEntity<>(obyavleniyes, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add_obyavleniye",
            headers = {"content-type=application/json"})
    public ResponseEntity<HttpStatus> addNewObyavleniye(@RequestBody JsonNode json) {
        TelegramUserInfo userInfo;
        CurrentObyavleniye currentObyavleniye;

        try {
            userInfo = controllerHelper.getUserInfo(json);
            currentObyavleniye = controllerHelper.getCurrentObyavleniye(json);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!controllerHelper.checkUserRights(userInfo)
                || !controllerHelper.checkIsUserChannelMember(userInfo.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (!controllerHelper.addNewObyavleniye(currentObyavleniye, json)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete_o/{message_id}",
            headers = {"content-type=application/json"})
    public ResponseEntity<HttpStatus> deleteObyavleniye(@RequestBody TelegramUserInfo userInfo,
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

        actualObyavleniyeService.removeObyavleniye(messageId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/search_obyavleniyes",
            headers = {"content-type=application/json"})
    public ResponseEntity<List<ActualObyavleniye>> searchObyavleniyes(@RequestBody JsonNode json) {
        TelegramUserInfo userInfo;
        List<String> tagsList;

        try {
            userInfo = controllerHelper.getUserInfo(json);
            tagsList = controllerHelper.getTagsList(json);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long userId = userInfo.getId();

        if (userId == null || tagsList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!controllerHelper.checkUserRights(userInfo)
                || !controllerHelper.checkIsUserChannelMember(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String[] allTags = tagsList.toArray(String[]::new);

        List<ActualObyavleniye> obyavleniyes = actualObyavleniyeService.tagsSearch(allTags);

        return new ResponseEntity<>(obyavleniyes, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/all_tags",
            headers = {"content-type=application/json"})
    public ResponseEntity<AllTags> getAllTags(@RequestBody TelegramUserInfo userInfo) {
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
