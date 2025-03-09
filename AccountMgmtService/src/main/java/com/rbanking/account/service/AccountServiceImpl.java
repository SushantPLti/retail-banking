package com.rbanking.account.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rbanking.account.feign.NotificationClient;
import com.rbanking.account.model.EmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rbanking.account.DTO.AccountCurrencyDTO;
import com.rbanking.account.DTO.AccountDTO;
import com.rbanking.account.DTO.AccountUpdateDTO;
import com.rbanking.account.DTO.TransactionDto;
import com.rbanking.account.DTO.UpdateMinimumBalanceRequest;
import com.rbanking.account.entities.Account;
import com.rbanking.account.entities.AgeCategory;
import com.rbanking.account.entities.FreezeRemark;
import com.rbanking.account.entities.Status;
import com.rbanking.account.exception.AccountNotFoundException;
import com.rbanking.account.exception.MinBalanceException;
import com.rbanking.account.feign.CustomersClient;
import com.rbanking.account.feign.TransactionsClient;
import com.rbanking.account.model.SuccessResponse;
import com.rbanking.account.repo.AccountRepository;
import com.rbanking.account.util.AccountWrapper;
import com.rbanking.account.util.Constant;
import com.rbanking.account.util.CustomDateTime;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;

import static com.rbanking.account.util.Constant.ACCOUNT_CREATED;

/**
 * Implementation of the AccountService interface. Provides functionality to
 * create, retrieve, update, view balance, delete accounts, and update account
 * status.
 */
@Service
public class AccountServiceImpl implements AccountService {

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private AccountRepository accountRepo;

	@Value("${account.balance.min.senior}")
	double seniorMinBalance;

	@Value("${account.balance.min.regular}")
	double regularMinBalance;

	@Value("${account.balance.min.minor}")
	double minorMinBalance;

	@Value("${account.inactive.period.months}")
	double inActivePeriodMonths;

	@Autowired
	private NotificationClient notificationClient;

	@Autowired
	private TransactionsClient transactionsClient;

	@Autowired
	private CustomersClient customersClient;
	
	@Autowired
	private JwtService jwtService;

	/**
	 * Constructor for AccountServiceImpl.
	 *
	 * @param accountRepo the repository used for account operations
	 */
	public AccountServiceImpl(AccountRepository accountRepo) {
		this.accountRepo = accountRepo;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Transactional
	@Override
	@CircuitBreaker(name = "createAccountBreaker", fallbackMethod = "createAccountFallback")
	public SuccessResponse createAccount(AccountDTO account) {
		Account dbAccount = AccountWrapper.toAccountEntity(account);

		LocalDateTime timestamp = CustomDateTime.getTimeSpecificZone();
		dbAccount.setCreatedAt(timestamp);
		dbAccount.setUpdatedAt(timestamp);

		// set minimum balance based on the criteria
		String category = customersClient.ageCategory(account.getCustId());
		AgeCategory ageCategory = AgeCategory.valueOf(category);
		double minBalance = switch (ageCategory) {
		case REGULAR -> regularMinBalance;
		case SENIOR_CITIZEN -> seniorMinBalance;
		default -> minorMinBalance;
		};
		dbAccount.setMinBalance(minBalance);

		if (account.getBalance() < minBalance) {
			throw new MinBalanceException("Balance is less than min balance of " + minBalance);
		}

		dbAccount = accountRepo.save(dbAccount);

		logger.debug("Account created for customerID {} with account number : {}", dbAccount.getCustId(),
				dbAccount.getAccountNumber());

		TransactionDto transactionDto = new TransactionDto(dbAccount.getAccountNumber(), Constant.ACCOUNT_OPEN,
				dbAccount.getBalance());
		transactionsClient.updateBalance(transactionDto);

		String successMessage = "Congratulations! Your account has been successfully created. Your new account number is "
				+ dbAccount.getAccountNumber() + ". Please keep this number safe for your future transactions.";

		EmailRequest emailRequest = new EmailRequest(
				"sushant.poman@ltimindtree.com",
				ACCOUNT_CREATED,
				successMessage
				);
		notificationClient.sendEmail(emailRequest);

		SuccessResponse response = new SuccessResponse();
		response.setResponseCode(HttpStatus.CREATED.value());
		response.setMessage(successMessage);
		response.setData(dbAccount);
		return response;
	}

	public SuccessResponse createAccountFallback(AccountDTO account) {
		SuccessResponse response = new SuccessResponse();
		response.setResponseCode(HttpStatus.SERVICE_UNAVAILABLE.value());
		response.setMessage("Service is currently unavailable. Please try again later. ");
		response.setData(null);
		return response;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Optional<Account> getAccount(Long accountNumber) {
		Optional<Account> account = accountRepo.findByAccountNumber(accountNumber);

		if (!account.isPresent()) {
			throw new AccountNotFoundException("Account not found -- " + accountNumber);
		}
		logger.debug("Account found with accountNumber : {}", accountNumber);
		return account;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public String updateAccountBal(Long accountNo, Double balance) {

		Optional<Account> optionalAccount = accountRepo.findByAccountNumber(accountNo);

		if (optionalAccount.isPresent()) {
			Account account = optionalAccount.get();
			account.setBalance(balance);
			account.setLastTransactionDate(CustomDateTime.getTimeSpecificZone());
			accountRepo.save(account);
			logger.debug("Your account {} has been successfully updated with the {} {}", account.getAccountNumber(),
					account.getBalance(), account.getCurrency());
			return "Your account has been successfully updated with the " + balance + " amount";
		} else
			throw new AccountNotFoundException("Account not found -- " + accountNo);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public AccountCurrencyDTO viewBalance(Long accountNo) {

		Optional<Account> optionalAccount = accountRepo.findByAccountNumber(accountNo);

		if (optionalAccount.isPresent()) {
			Account account = optionalAccount.get();
			logger.debug("Balance for account {} is {} {}", account.getAccountNumber(), account.getBalance(),
					account.getCurrency());
			return new AccountCurrencyDTO(account.getBalance(), account.getCurrency(), account.getMinBalance(), account.getStatus());
		} else
			throw new AccountNotFoundException("Account not found -- " + accountNo);

	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public SuccessResponse deleteAccount(Long accountNo) {

		accountRepo.deleteById(accountNo);
		logger.info("Account deleted for {}", accountNo);
		SuccessResponse response = new SuccessResponse();
		response.setResponseCode(HttpStatus.OK.value());
		response.setMessage(
				"Your account has been successfully deleted. If you have any further questions or need assistance, please contact our customer support team.");

		return response;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public SuccessResponse updateAccountStatus(AccountUpdateDTO account, HttpServletRequest request) {
		String authorizationHeader = request.getHeader(Constant.AUTHORIZATION);
		String bearerToken = (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
				? authorizationHeader.substring(7)
				: null;

		String role = jwtService.extractRoles(bearerToken).get(0);

		return processStatusUpdate(account, role);
	}

	private SuccessResponse processStatusUpdate(AccountUpdateDTO account, String role) {
		switch (account.getStatus()) {
		case BLOCKED:
			return handleBlockedStatus(account, role);
		case CLOSED:
			return handleClosedStatus(account, role);
		case INACTIVE:
		case ACTIVE:
			return handleActiveInactiveStatus(account, role);
		default:
			return unauthorizedResponse(role);
		}
	}

	private SuccessResponse handleBlockedStatus(AccountUpdateDTO account, String role) {
		if (role.contains(Constant.OPERATOR) || role.contains(Constant.MANAGER)) {
			return updateBlockedStatus(account);
		}
		return unauthorizedResponse(role);
	}

	private SuccessResponse handleClosedStatus(AccountUpdateDTO account, String role) {
		if (role.contains(Constant.MANAGER)) {
			return updateClosedStatus(account);
		}
		return unauthorizedResponse(role);
	}

	private SuccessResponse handleActiveInactiveStatus(AccountUpdateDTO account, String role) {
		if (role.contains(Constant.CUSTOMER) || role.contains(Constant.OPERATOR) || role.contains(Constant.MANAGER)) {
			return updateActiveInactiveStatus(account, role);
		}
		return unauthorizedResponse(role);
	}

	private SuccessResponse unauthorizedResponse(String role) {
		SuccessResponse response = new SuccessResponse();
		response.setResponseCode(HttpStatus.UNAUTHORIZED.value());
		response.setMessage("Action is not applicable for the role " + role);
		response.setData(HttpStatus.UNAUTHORIZED);
		return response;
	}

	private SuccessResponse updateActiveInactiveStatus(AccountUpdateDTO account, String role) {
		Optional<Account> optionalAccount = accountRepo.findByAccountNumber(account.getAccountNumber());
		if (optionalAccount.isPresent()) {
			Account accountRes = optionalAccount.get();
			return processAccountStatus(account, role, accountRes);
		}
		throw new AccountNotFoundException("Account not found -- " + account.getAccountNumber());
	}

	private SuccessResponse processAccountStatus(AccountUpdateDTO account, String role, Account accountRes) {
		SuccessResponse response = new SuccessResponse();
		if (account.getStatus() == Status.BLOCKED && role.contains("CUSTOMER")) {
			return unauthorizedResponse(role);
		} else if (account.getStatus() == Status.CLOSED) {
			response.setResponseCode(HttpStatus.UNAUTHORIZED.value());
			response.setMessage("Your Account is closed, connect with Bank and create new account");
			response.setData(HttpStatus.UNAUTHORIZED);
		} else if (account.getStatus() == accountRes.getStatus()) {
			response.setResponseCode(HttpStatus.ALREADY_REPORTED.value());
			response.setMessage("Your Account is already " + account.getStatus());
			response.setData(HttpStatus.ALREADY_REPORTED);
		} else {
			updateAccountDetails(account, accountRes);
			response.setResponseCode(HttpStatus.OK.value());
			response.setMessage("Your account status has been successfully updated to " + account.getStatus());
			response.setData(account);
		}
		return response;
	}

	private void updateAccountDetails(AccountUpdateDTO account, Account accountRes) {
		accountRes.setStatus(account.getStatus());
		accountRes.setUpdateStatusRemark(account.getRemark());
		accountRes.setUpdatedAt(CustomDateTime.getTimeSpecificZone());
		accountRes.setUpdatedBy(account.getUpdatedBy());
		accountRepo.save(accountRes);
		logger.debug("Account status has been successfully updated to {} for {}", accountRes.getStatus(),
				accountRes.getAccountNumber());
	}

	private SuccessResponse updateBlockedStatus(AccountUpdateDTO account) {
		Optional<Account> optionalAccount = accountRepo.findByAccountNumber(account.getAccountNumber());
		if (optionalAccount.isPresent()) {
			Account accountRes = optionalAccount.get();
			return processBlockedStatus(account, accountRes);
		}
		throw new AccountNotFoundException("Account not found -- " + account.getAccountNumber());
	}

	private SuccessResponse processBlockedStatus(AccountUpdateDTO account, Account accountRes) {
		SuccessResponse response = new SuccessResponse();
		if (accountRes.getStatus() != account.getStatus()) {
			updateAccountDetails(account, accountRes);
			response.setResponseCode(HttpStatus.OK.value());
			response.setMessage("Your account status has been updated to " + account.getStatus());
			response.setData(account);
		} else {
			response.setResponseCode(HttpStatus.ALREADY_REPORTED.value());
			response.setMessage("Your Account is already " + account.getStatus());
			response.setData(HttpStatus.ALREADY_REPORTED);
		}
		return response;
	}

	private SuccessResponse updateClosedStatus(AccountUpdateDTO account) {
	    Optional<Account> optionalAccount = accountRepo.findByAccountNumber(account.getAccountNumber());
	    if (optionalAccount.isEmpty()) {
	        throw new AccountNotFoundException("Account not found -- " + account.getAccountNumber());
	    }

	    Account accountRes = optionalAccount.get();
	    SuccessResponse response = new SuccessResponse();

	    if (accountRes.getBalance() != 0) {
	        return createForbiddenResponse("Your account balance is not 0");
	    }

	    if (accountRes.getStatus() != Status.INACTIVE) {
	        return createForbiddenResponse("Account is not inactive");
	    }

	    updateAccountDetails(account, accountRes);
	    response.setResponseCode(HttpStatus.OK.value());
	    response.setMessage("Your account status has been successfully updated to " + account.getStatus());
	    response.setData(account);
	    return response;
	}

	private SuccessResponse createForbiddenResponse(String message) {
	    SuccessResponse response = new SuccessResponse();
	    response.setResponseCode(HttpStatus.FORBIDDEN.value());
	    response.setMessage(message);
	    response.setData(null);
	    return response;
	}


	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public SuccessResponse updateMinBalance(UpdateMinimumBalanceRequest updateMinBalReq) {

		accountRepo.updateMinBalanceByAgeCategory(updateMinBalReq.getMinBalance(), updateMinBalReq.getAgeCategory());
		SuccessResponse response = new SuccessResponse();
		response.setResponseCode(HttpStatus.OK.value());
		response.setMessage("The minimum balance " + updateMinBalReq.getMinBalance()
				+ " has been successfully updated for age category " + updateMinBalReq.getAgeCategory());
		response.setData(updateMinBalReq);
		return response;
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public SuccessResponse getAccountNumbers(Long customerId) {

		Optional<List<Account>> accounts = accountRepo.findByCustId(customerId);
		List<Long> accountNumbers = accounts
		        .map(list -> list.stream()
		                .map(Account::getAccountNumber)
		                .collect(Collectors.toList()))
		        .orElseGet(ArrayList::new);

		SuccessResponse response = new SuccessResponse();
		response.setResponseCode(HttpStatus.OK.value());
		response.setMessage("Numbers of accounts with cust id " + customerId + " is " + accountNumbers.size());
		response.setData(accountNumbers);
		return response;
	}

	public boolean isAccountInactive(Account account) {
		LocalDateTime lastTransactionDate = account.getLastTransactionDate();
		LocalDateTime currentDate = LocalDateTime.now();
		long monthsBetween = ChronoUnit.MONTHS.between(lastTransactionDate, currentDate);
		return monthsBetween >= inActivePeriodMonths;
	}

	// updated account stautus using by scheduler
	@Override
	public void updateAccountStatus(Account account) {
        if (isAccountInactive(account)) {
        	account.setStatus(Status.INACTIVE);
        	account.setUpdateStatusRemark(FreezeRemark.INACTIVITY);
        	logger.info("{} Your account is inactive. Please perform a transaction to reactivate it.",account.getAccountNumber());
        } 
    	accountRepo.save(account);
	}

	@Override
	public String getCustomerDetailsByAccount(Long accountNumber) {
        String email = accountRepo.findCustomerEmailByAccountNumber(accountNumber);

        return email;
	}


}
