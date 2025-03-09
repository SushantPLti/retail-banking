package com.rbanking.account.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.rbanking.account.controller.AccountController;
import com.rbanking.account.entities.Account;
import com.rbanking.account.repo.AccountRepository;
import com.rbanking.account.service.AccountService;

@Service
public class ScheduleTask {

	private static final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);
	
	private AccountRepository accountRepo;
	
	private AccountService accountService;

	
	public ScheduleTask(AccountRepository accountRepo, AccountService accountService) {
		this.accountRepo = accountRepo;
		this.accountService = accountService;
	}
	
	/**
	 * Constructor for AccountController.
	 *
	 * @param accountService the account service to be used
	 */
//	public ScheduleTask( AccountService accountService) {
//		this.accountService = accountService;
//	}
//	
    // This method will run at 12:00 AM every day
//    @Scheduled(cron = "*/30 * * * * *")
	@Scheduled(cron = "0 0 0 * * ?")
	public void monitorAccountStatus() {
    	List<Account> accounts = accountRepo.findAll();
        for (Account account : accounts) {
            accountService.updateAccountStatus(account);
        }
	}

}
