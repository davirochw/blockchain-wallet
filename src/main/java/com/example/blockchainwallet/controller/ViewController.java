package com.example.blockchainwallet.controller;

import com.example.blockchainwallet.service.BlockchainService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    private final BlockchainService blockchainService;

    public ViewController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("blocks", blockchainService.getBlockchain());
        return "index";
    }
}