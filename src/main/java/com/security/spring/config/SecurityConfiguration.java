package com.security.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static com.security.spring.user.role.Permission.*;
import static com.security.spring.user.role.Role.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfiguration(
            @Lazy JWTAuthenticationFilter jwtAuthFilter,
            @Lazy AuthenticationProvider authenticationProvider
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                // Permit all access to the following paths
                .requestMatchers(
                        "/api/v1/democontroller/**",
                        "/api/v1/user/checkAr7Id/**",
                        "/api/v1/auth/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/ws/**",
                        "/v3/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/wabjars/**",
                        "/swagger-ui.html",
                        "/v1/api/seamless/",
                        "/api/v1/gameSoft/Seamless/**",
                        "/api/v1/files/**",
                        "/api/test/**",
                         "/download/**",
                         "/api/v1/shan/**"
//                        "/api/v1/gt/**",
//                        "/api/v1/gameproduct/**"
                        ).permitAll()

                .requestMatchers(GET,"api/v1/promotion-unit").permitAll()
                .requestMatchers(PUT,"api/v1/promotion-unit").hasRole(ADMIN.name())

                                .requestMatchers("api/v1/dashboard/ads/**").hasRole(ADMIN.name())
                                .requestMatchers("api/v1/ads/**").permitAll()

                // /api/v1/user end point all
                .requestMatchers("api/v1/user/**").hasAnyRole(ADMIN.name(), SENIORMASTER.name(), MASTER.name(),AGENT.name(),USER.name())
                .requestMatchers(GET, "api/v1/user/profile/**").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(SENIORMASTER_READ),String.valueOf(MASTER_READ),String.valueOf(AGENT_READ),String.valueOf(USER_READ))
                .requestMatchers(GET, "api/v1/user/profile/**").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(SENIORMASTER_READ),String.valueOf(MASTER_READ),String.valueOf(AGENT_READ),String.valueOf(USER_READ))
                .requestMatchers(PUT, "/api/v1/user/parentUserUpdate").hasAnyAuthority(String.valueOf(ADMIN_UPDATE),String.valueOf(SENIORMASTER_UPDATE),String.valueOf(MASTER_UPDATE),String.valueOf(AGENT_UPDATE),String.valueOf(USER_UPDATE))
                .requestMatchers(GET, "api/v1/user/allUser").hasAuthority("admin:read")

                                .requestMatchers("/api/v1/game-table/**").hasAnyRole("ADMIN","USER")
                // /api/v1/user end point all
                .requestMatchers(PUT, "api/v1/user/unit").hasAuthority(String.valueOf(ADMIN_UPDATE))
                .requestMatchers(GET, "api/v1/user/unit").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(SENIORMASTER_READ),String.valueOf(MASTER_READ),String.valueOf(AGENT_READ),String.valueOf(USER_READ))
                // Role and authority checks for /commission endpoint
                        .requestMatchers("api/v1/commission").hasAnyRole(String.valueOf(ADMIN),String.valueOf(SENIORMASTER),String.valueOf(MASTER),String.valueOf(AGENT))
                // Role and authority checks for /ban endpoint
                .requestMatchers("api/v1/user/banstatus").hasRole("ADMIN")
                .requestMatchers(GET, "api/v1/user/banstatus").hasAuthority("admin:read")
                .requestMatchers(PUT, "api/v1/user/banstatus/**").hasAuthority("admin:update")

                // Role and authority checks for /userByRole/{role} endpoint
                .requestMatchers(GET, "/api/v1/userByRole/{role}").hasAuthority("admin:read")

                // Role and authority checks for /Downline User/ endpoint
                .requestMatchers("api/v1/user/**").hasAnyRole(ADMIN.name(), SENIORMASTER.name(), MASTER.name(),AGENT.name(),USER.name())
                .requestMatchers(GET, "api/v1/user/downline/**").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(SENIORMASTER_READ),String.valueOf(MASTER_READ),String.valueOf(AGENT_READ),String.valueOf(USER_READ))
                // Role and Authority checks for /report endpoint
                .requestMatchers("api/v1/rp/**").permitAll()
                .requestMatchers(GET, "api/v1/rp/report-details/**").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(SENIORMASTER_READ),String.valueOf(MASTER_READ),String.valueOf(AGENT_READ),String.valueOf(USER_READ))
                 // Role and Authority checks for /shan-report
                .requestMatchers("api/v1/srp/spacetech-report/**").permitAll()

                // Role and Authority checks for shan-game
                .requestMatchers("api/v1/spacetech-game/**").hasRole("USER")
                // Role and authority checks for /units endpoint
                .requestMatchers("api/v1/un/**").hasAnyRole(ADMIN.name(), SENIORMASTER.name(), MASTER.name(),AGENT.name(),USER.name())
                .requestMatchers(POST, "api/v1/un/unit").hasAuthority(String.valueOf(ADMIN_CREATE))
                .requestMatchers(GET, "api/v1/un/unit").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(SENIORMASTER_READ),String.valueOf(MASTER_READ),String.valueOf(AGENT_READ),String.valueOf(USER_READ))
                .requestMatchers(GET, "api/v1/un/unit/**").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(SENIORMASTER_READ),String.valueOf(MASTER_READ),String.valueOf(AGENT_READ),String.valueOf(USER_READ))
                .requestMatchers(PUT, "api/v1/un/unit").hasAuthority(String.valueOf(ADMIN_UPDATE))
                .requestMatchers(GET, "api/v1/un/unitTransferMinus").hasAuthority(String.valueOf(ADMIN_READ))
                .requestMatchers(GET, "api/v1/un/transitionHistoryAll/**").hasAuthority(String.valueOf(ADMIN_READ))
                .requestMatchers(GET,"api/v1/un/checkUser/**").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(SENIORMASTER_READ),String.valueOf(MASTER_READ),String.valueOf(AGENT_READ),String.valueOf(USER_READ))
                .requestMatchers(GET,"api/v1/un/transitionHistoryOwn/**").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(SENIORMASTER_READ),String.valueOf(MASTER_READ),String.valueOf(AGENT_READ),String.valueOf(USER_READ))
                .requestMatchers(GET,"api/v1/un/transitionHistory/**").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(SENIORMASTER_READ),String.valueOf(MASTER_READ),String.valueOf(AGENT_READ))
                .requestMatchers(GET, "api/v1/un/unitCreateHistory").hasAuthority(String.valueOf(ADMIN_READ))

                // Role and authority checks for /commission endpoint
                .requestMatchers("api/v1/cmm/**").hasAnyRole(ADMIN.name(),SENIORMASTER.name(),MASTER.name(),AGENT.name())
                .requestMatchers(POST, "api/v1/cmm/commission").hasAuthority(String.valueOf(ADMIN_CREATE))
                .requestMatchers(GET, "api/v1/cmm/commission").hasAuthority(String.valueOf(ADMIN_READ))
                .requestMatchers(PUT, "api/v1/cmm/commission").hasAuthority(String.valueOf(ADMIN_UPDATE))

                // Role and authority checks for /commissionAuth endpoint
                .requestMatchers(POST, "api/v1/cmm/commissionAuth").hasAuthority(String.valueOf(ADMIN_CREATE))
                .requestMatchers(GET, "api/v1/cmm/commissionAuth").hasAuthority(String.valueOf(ADMIN_READ))
                .requestMatchers(GET, "api/v1/cmm/commissionAuthByParent").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(SENIORMASTER_READ),String.valueOf(MASTER_READ),String.valueOf(AGENT_READ))
                .requestMatchers(PUT, "api/v1/cmm/commissionAuth").hasAuthority(String.valueOf(ADMIN_UPDATE))

                // Role and authority checks for /userCommission endpoint
                .requestMatchers(POST, "api/v1/cmm/userCommission").hasAnyAuthority(String.valueOf(ADMIN_CREATE),String.valueOf(SENIORMASTER_CREATE),String.valueOf(MASTER_CREATE),String.valueOf(AGENT_CREATE))
                .requestMatchers(GET, "api/v1/cmm/userCommission").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(SENIORMASTER_READ),String.valueOf(MASTER_READ),String.valueOf(AGENT_READ))
                .requestMatchers(PUT, "api/v1/cmm/userCommission").hasAnyAuthority(String.valueOf(ADMIN_UPDATE),String.valueOf(SENIORMASTER_UPDATE),String.valueOf(MASTER_UPDATE),String.valueOf(AGENT_UPDATE))

                // Role and authority checks for /otherunit endpoint
                .requestMatchers("api/v1/otherunit/maintransfer").hasAnyRole("ADMIN","SENIORMASTER","MASTER","AGENT")
                .requestMatchers(POST, "api/v1/otherunit/maintransfer").hasAnyAuthority("admin:create","seniormaster:create","master:create","agent:create")
                .requestMatchers(GET, "api/v1/otherunit/maintransfer").hasAnyAuthority("admin:read","seniormaster:read","master:read","agent:read")
                .requestMatchers(PUT, "api/v1/otherunit/maintransfer").hasAnyAuthority("admin:update","seniormaster:update","master:update","agent:read")

                // Game Soft API
//                 Role and authority checks for /gametype endpoint
                .requestMatchers(GET, "api/v1/gt/gametype/**").permitAll()
                .requestMatchers(POST, "api/v1/gt/gametype").hasAuthority(ADMIN_CREATE.name())
                .requestMatchers(PUT, "api/v1/gt/gametype").hasAuthority(ADMIN_UPDATE.name())
                .requestMatchers(DELETE, "api/v1/gt/gametype/**").hasAuthority(ADMIN_DELETE.name())

//                 Role and authority checks for /gameprovider endpoint
                .requestMatchers( "api/v1/gameproduct/**").hasAnyRole("ADMIN","USER")



                // Role and authority checks for /lunchgame endpoint
                .requestMatchers("api/v1/lg/**").hasAnyRole("ADMIN","SENIORMASTER","MASTER","AGENT","USER")
                .requestMatchers(GET, "api/v1/lg/lunchgame").hasAnyAuthority("admin:read","seniormaster:read","master:read","agent:read","user:read")
                .requestMatchers(POST, "api/v1/lg/lunchgame").hasAuthority("admin:create")
                .requestMatchers(PUT, "api/v1/lg/lunchgame").hasAuthority("admin:update")
                .requestMatchers(DELETE, "api/v1/lg/lunchgame").hasAuthority("admin:delete")

                // Role and authority checks for /pullreport endpoint
                .requestMatchers("api/v1/pr/**").hasRole("ADMIN")
                .requestMatchers(GET, "api/v1/pr/pullreport").hasRole("admin:read")
                .requestMatchers(POST, "api/v1/pr/pullreport").hasAuthority("admin:create")
                .requestMatchers(PUT, "api/v1/pr/pullreport").hasAuthority("admin:update")
                .requestMatchers(DELETE, "api/v1/pr/pullreport").hasAuthority("admin:delete")
                .requestMatchers(GET, "api/v1/pr/pullreportbysettlementdate").hasRole("admin:read")
                .requestMatchers(POST, "api/v1/pr/pullreportbysettlementdate").hasAuthority("admin:create")
                .requestMatchers(PUT, "api/v1/pr/pullreportbysettlementdate").hasAuthority("admin:update")
                .requestMatchers(DELETE, "api/v1/pr/pullreportbysettlementdate").hasAuthority("admin:delete")

                // Role and authority checks for /getgamelist endpoint
                .requestMatchers("/api/v1/ggl/**").hasAnyRole("ADMIN","SENIORMASTER","MASTER","AGENT","USER")
                .requestMatchers(GET, "api/v1/ggl/getgamelist").hasAnyAuthority("admin:read","seniormaster:read","master:read","agent:read","user:read")
                .requestMatchers(POST, "api/v1/ggl/getgamelist").hasAuthority("admin:create")
                .requestMatchers(PUT, "api/v1/ggl/getgamelist").hasAuthority("admin:update")
                .requestMatchers(DELETE, "api/v1/ggl/getgamelist").hasAuthority("admin:delete")

                // Role and authority checks for /getbetdetail endpoint
                .requestMatchers("/api/v1/gbd/**").hasRole("ADMIN")
                .requestMatchers(GET, "api/v1/gbd/Report/BetDetail/**").hasRole("admin:read")

                // Role and authority checks for /PullReportByWagerIDs endpoint
                .requestMatchers("/api/v1/prbw/**").hasRole("ADMIN")
                .requestMatchers(GET, "api/v1/prbw/PullReportByWagerIDs/**").hasRole("admin:read")

                // Role and authority checks for /PullReportByWagerIDs endpoint
                .requestMatchers("/api/v1/pprc/**").hasRole("ADMIN")
                .requestMatchers(GET, "api/v1/pprc/PullPendingRecords/**").hasRole("admin:read")

                // Role and authority checks for /FetchReport endpoint
                .requestMatchers("/api/v1/frc/**").hasRole("ADMIN")
                .requestMatchers(GET, "api/v1/frc/FetchReport/**").hasRole("admin:read")


                // Role and authority checks for /MarkReport endpoint
                .requestMatchers("/api/v1/mr/**").hasRole("ADMIN")
                .requestMatchers(POST, "api/v1/mr/MarkReport/**").hasRole("admin:read")

                // Role and authority checks for /GetMemberOutstandingBalance endpoint
                .requestMatchers("/api/v1/gmob/**").hasRole("ADMIN")
                .requestMatchers(POST, "api/v1/gmob/GetMemberOutstandingBalance/**").hasRole("admin:read")

                // Role and authority checks for /ResolveMemberOutstandingBalance endpoint
                .requestMatchers("/api/v1/rmob/**").hasRole("ADMIN")
                .requestMatchers(POST, "api/v1/rmob/ResolveMemberOutstandingBalance/**").hasRole("admin:read")

                // Role and authority checks for /BankType endpoint
                .requestMatchers("/api/v1/bt/**").hasAnyRole("ADMIN","SENIORMASTER","MASTER","AGENT","USER")
                .requestMatchers(POST, "api/v1/bt/bankType/**").hasRole("ADMIN")
                .requestMatchers(PUT, "api/v1/bt/bankType/**").hasRole("ADMIN")
                .requestMatchers(DELETE, "api/v1/bt/bankType/**").hasRole("ADMIN")
                .requestMatchers(GET, "api/v1/bt/bankType/**").hasAnyAuthority("admin:read","seniormaster:read","master:read","agent:read","user:read")

                // Role and authority checks for /bankTypeAuth endpoint
                .requestMatchers("/api/v1/bt/**").hasAnyRole("ADMIN","SENIORMASTER","MASTER","AGENT","USER")
                .requestMatchers(POST, "api/v1/bt/bankTypeAuth/**").hasAnyAuthority("admin:create","seniormaster:create","master:create","agent:create")
                .requestMatchers(PUT, "api/v1/bt/bankTypeAuth/**").hasAnyAuthority("admin:update","seniormaster:update","master:update","agent:update")
                .requestMatchers(GET, "api/v1/bt/bankTypeAuth/**").hasAnyAuthority("admin:read","seniormaster:read","master:read","agent:read","user:read")
                .requestMatchers(GET, "api/v1/bt/bankTypeAuthUpline/**").hasAnyAuthority("admin:read","seniormaster:read","master:read","agent:read","user:read")

                // Role and authority checks for /BankName endpoint
                .requestMatchers("/api/v1/bn/**").hasAnyRole("ADMIN","SENIORMASTER","MASTER","AGENT","USER")
                .requestMatchers(POST, "api/v1/bn/bankName/**").hasRole("ADMIN")
                .requestMatchers(PUT, "api/v1/bn/bankName/**").hasRole("ADMIN")
                .requestMatchers(DELETE, "api/bn/bankName/**").hasRole("ADMIN")
                .requestMatchers(GET, "api/v1/bn/bankName/**").hasAnyAuthority("admin:read","seniormaster:read","master:read","agent:read","user:read")

                .requestMatchers(POST, "api/v1/bn/bankNameAuth/**").hasAnyAuthority("admin:create","seniormaster:create","master:create","agent:create")
                .requestMatchers(PUT, "api/v1/bn/bankNameAuth/**").hasAnyAuthority("admin:update","seniormaster:update","master:update","agent:update")
                .requestMatchers(DELETE, "api/bn/bankNameAuth/**").hasAnyAuthority("admin:delete","seniormaster:delete","master:delete","agent:delete")
                .requestMatchers(GET, "api/v1/bn/bankNameAuth/**").hasAnyAuthority("admin:read","seniormaster:read","master:read","agent:read","user:read")

                .requestMatchers("/api/v1/ba/**").hasAnyRole("ADMIN","SENIORMASTER","MASTER","AGENT","USER")
                .requestMatchers(POST, "/api/v1/ba/bankAcc/**").hasAnyAuthority("admin:create","seniormaster:create","master:create","agent:create")
                .requestMatchers(PUT, "/api/v1/ba/bankAcc/**").hasAnyAuthority("admin:update","seniormaster:update","master:update","agent:update")
                .requestMatchers(DELETE, "/api/ba/bankAcc/**").hasAnyAuthority("admin:delete","seniormaster:delete","master:delete","agent:delete")
                .requestMatchers(GET, "/api/v1/ba/bankAcc/**").hasAnyAuthority("admin:read","seniormaster:read","master:read","agent:read","user:read")
                .requestMatchers(GET, "/api/v1/ba/bankAccParent/**").hasAnyAuthority("admin:read","seniormaster:read","master:read","agent:read","user:read")

                .requestMatchers("/api/v1/dp/**").hasAnyRole("ADMIN","SENIORMASTER","MASTER","AGENT","USER")
                .requestMatchers(POST, "/api/v1/dp/deposit/**").hasAnyAuthority("DEPOSIT_CREATE")
                .requestMatchers(PUT, "/api/v1/dp/deposit/**").hasAnyAuthority("admin:update","seniormaster:update","master:update","agent:update","user:update")
                .requestMatchers(GET, "/api/v1/dp/deposit/**").hasAnyAuthority("admin:read","seniormaster:read","master:read","agent:read","user:read")
                .requestMatchers(GET, "/api/v1/dp/depositFrom/**").hasAnyAuthority("admin:read","seniormaster:read","master:read","agent:read","user:read")
                .requestMatchers(GET, "/api/v1/dp/depositTo/**").hasAnyAuthority("admin:read","seniormaster:read","master:read","agent:read","user:read")
                .requestMatchers(GET, "/api/v1/dp/depositAll/**").hasAuthority("admin:read")

                .requestMatchers("/api/v1/wd/**").hasAnyRole("ADMIN","SENIORMASTER","MASTER","AGENT","USER")
                .requestMatchers(POST, "/api/v1/wd/withdraw/**").hasAnyAuthority("admin:create","seniormaster:create","master:create","agent:create","user:create")
                .requestMatchers(PUT, "/api/v1/wd/withdraw/**").hasAnyAuthority("admin:update","seniormaster:update","master:update","agent:update","user:update")
                .requestMatchers(DELETE, "/api/wd/withdraw/**").hasAnyAuthority("admin:delete")
                .requestMatchers(GET, "/api/v1/wd/withdraw/**").hasAnyAuthority("admin:read","seniormaster:read","master:read","agent:read","user:read")
                .requestMatchers(GET, "/api/v1/wd/withdrawFrom/**").hasAnyAuthority("admin:read","seniormaster:read","master:read","agent:read","user:read")
                .requestMatchers(GET, "/api/v1/wd/withdrawTo/**").hasAnyAuthority("admin:read","seniormaster:read","master:read","agent:read","user:read")

                // Any other request requires authentication
                .anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
