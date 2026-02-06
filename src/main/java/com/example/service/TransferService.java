package com.example.service;

import com.example.dto.TransferRequestDto;

public interface TransferService {

    public boolean isValidTransfer(TransferRequestDto transferRequestDto);
    public void executeTransfer(TransferRequestDto transferRequestDto);
    public void transfer(TransferRequestDto transferRequestDto);
}
