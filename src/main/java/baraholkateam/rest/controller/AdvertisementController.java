package baraholkateam.rest.controller;

import baraholkateam.rest.repository.ActualAdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер взаимодействия с клиентами по REST API
 */
@RestController
@RequestMapping("/api")
public class AdvertisementController {

    @Autowired
    ActualAdvertisementRepository actualAdvertisementRepository;


}
