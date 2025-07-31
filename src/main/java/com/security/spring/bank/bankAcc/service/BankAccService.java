package com.security.spring.bank.bankAcc.service;

import com.security.spring.bank.bankAcc.dto.BankAccRequest;
import com.security.spring.bank.bankAcc.dto.BankAccResponse;
import com.security.spring.bank.bankAcc.dto.BankAccStatusRequest;

public interface BankAccService {
    public BankAccResponse saveBankAcc(BankAccRequest data,String ar7Id);
    public BankAccResponse getBankAccById(Integer bankAccId,String ar7Id);
//    public BankAccResponse getBankAccByName(Integer bankNameId,String ar7Id);
    public BankAccResponse getBankAccByParent(Integer bankNameId,String ar7Id);
    public BankAccResponse getBankAccAllByParent(String ar7Id, int bankNameId);
    public BankAccResponse updateBankAcc(String ar7Id, BankAccRequest data);
    public BankAccResponse getBankAccByOwn(String ar7Id);
    public BankAccResponse changeBankAccStatus(String ar7Id, BankAccStatusRequest data);


}
