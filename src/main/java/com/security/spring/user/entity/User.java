package com.security.spring.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.security.spring.bank.bankAcc.entity.BankAcc;
import com.security.spring.bank.bankName.entity.BankNameAuth;
import com.security.spring.bank.bankType.entity.BankTypeAuth;
import com.security.spring.commission.entity.UserCommission;
import com.security.spring.deposit.entity.Deposit;
import com.security.spring.gamesoft.transaction.entity.GameSoftTransaction;
import com.security.spring.gamesoft.wager.entity.GameSoftWager;
import com.security.spring.promotion.entity.TicketBox;
import com.security.spring.unit.entity.TransitionHistory;
import com.security.spring.unit.entity.UserUnits;
import com.security.spring.user.role.Role;
import com.security.spring.withdraw.entity.TempoSaveWithdraw;
import com.security.spring.withdraw.entity.Withdraw;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails , Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" , nullable = false , unique = true)
    private Integer userId;

    @NotNull
    @NotEmpty
    @Column(name ="full_name" , nullable = false)
    private String name;

    @Column(name ="email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name ="ar7_id" , unique = true ,nullable = false)
    private String ar7Id;

    @NotNull
    @NotEmpty
    @Column(name ="password" , nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name ="role")
    private Role role;

    @Column(name="login_time")
    private LocalDateTime loginTime;

    @Column(name="status")
    private boolean status;

    @NotNull
    @NotEmpty
    @Column(name="secret_code")
    private String secretCode;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="unit_id", referencedColumnName = "id")
    @ToString.Exclude
    private UserUnits userUnits;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    private List<UserCommission> userCommission;

    private String parentUserId;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JoinColumn(name="register_status_id", referencedColumnName = "id")
    @ToString.Exclude
    @JsonIgnore
    private RegisterStatus registerStatus;

    @OneToMany(mappedBy = "fromUser" , cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TransitionHistory> transitionHistoryFroms;

    private String accessToken;

    private String refreshToken;

    @OneToMany(mappedBy = "toUser" , cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TransitionHistory> transitionHistoryTos;

    @ToString.Exclude
    @OneToMany(mappedBy = "ownerUser" , cascade = CascadeType.ALL)
    private List<BankTypeAuth> bankTypeAuthList;

    @ToString.Exclude
    @OneToMany(mappedBy = "ownerUser" , cascade = CascadeType.ALL)
    private List<BankNameAuth> bankNameAuthList;

    @OneToMany(mappedBy = "ownerUser" , cascade = CascadeType.ALL)
    private List<BankAcc> bankAccount;

    @OneToMany(mappedBy = "fromAcc" , cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Deposit> fromDeposits;

    @OneToMany(mappedBy = "toAcc" , cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Deposit> toDeposits;

    @OneToMany(mappedBy = "withdrawUser" , cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Withdraw> withdrawList;

    @OneToMany(mappedBy = "parentUser" , cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Withdraw> parentWithdraw;

    @OneToMany(mappedBy = "tempoWithdrawUser" , cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TempoSaveWithdraw> tempoWithdraws;

    @OneToMany(mappedBy = "gameSoftTransitionUser" , cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<GameSoftTransaction> gameSoftTransactionList;

    @OneToMany(mappedBy = "gameSoftWagerUser" , cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<GameSoftWager> wagerList;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_box_id")
    @ToString.Exclude
    private TicketBox ticketBox;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return ar7Id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean getStatus(){
        return status;
    }

}
