package com.security.spring.bank.bankName.service;

import com.security.spring.bank.bankName.dto.*;

public interface BankNameService {
    BankNameResponse saveBankName(BankNameRequest data);
    BankNameResponse getBankNameAll(String token);
    BankNameResponse getBankNameById(Long id);
    BankNameResponse updateBankName(BankNameRequest data);

    BankNameAuthResponse saveBankNameAuth(BankNameAuthRequest data, String ar7Id);
    BankNameAuthResponse updateBankNameAuth(String ar7Id,BankNameAuthRequest data);
    BankNameAuthResponse getBankNameAuth(String ar7Id);

    BankNameAuthResponse getAuthenticatedBankNameList(String ar7id);

    BankNameAuthResponse getParentAuthenticatedBankNameList(int bankTypeId);
}
