package com.gacnik.diplomska.naloga.controller;

import com.gacnik.diplomska.naloga.model.MacRequest;
import com.gacnik.diplomska.naloga.service.NodeRedService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/nodered")
public class NodeRedController {
    private final Logger log = LoggerFactory.getLogger(NodeRedController.class);
    @Autowired
    private NodeRedService nodeRedService;
    @PostMapping("/device")
    public ResponseEntity<Void> testDevices(@RequestBody MacRequest macs) throws Exception {
        log.info("\nnew devices: {}, \n disconected devices {}"
                ,macs.getNewDevices(), macs.getOldDevices());
        nodeRedService.recordTimeByNetworkDevices(macs);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
