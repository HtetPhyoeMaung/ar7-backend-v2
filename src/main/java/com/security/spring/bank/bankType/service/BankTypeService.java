package com.security.spring.bank.bankType.service;

import com.security.spring.bank.bankType.dto.BankTypeAuthRequest;
import com.security.spring.bank.bankType.dto.BankTypeAuthResponse;
import com.security.spring.bank.bankType.dto.BankTypeRequest;
import com.security.spring.bank.bankType.dto.BankTypeResponse;

public interface BankTypeService {
    public BankTypeResponse saveBankType(BankTypeRequest data);
    public BankTypeResponse getBankTypeAll();
    public BankTypeResponse findBankTypeById(Long bankTypeId);
    public BankTypeResponse updateBankType(BankTypeRequest data);

    public BankTypeAuthResponse saveBankTypeAuth(BankTypeAuthRequest data,String ar7Id);
    public BankTypeAuthResponse getBankTypeAuthAll(String ar7Id);
    public BankTypeAuthResponse changeStatusBankTypeAuth(String ar7Id,BankTypeAuthRequest data);

    BankTypeAuthResponse getParentBankTypeAuthList();
}
