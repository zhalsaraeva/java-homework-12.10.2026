import java.util.Objects;

public final class BankAccount {
    private final String accountNumber;      
    private final String ownerName;         
    private Balance balance;                
    private final long dailyLimit;           
    private long withdrawnToday;             
    private boolean isActive;                

    public BankAccount(String accountNumber, String ownerName, Balance initialBalance, long dailyLimit) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Номер счёта не может быть пустым");
        }
        if (ownerName == null || ownerName.isBlank()) {
            throw new IllegalArgumentException("Имя владельца не может быть пустым");
        }
        if (initialBalance == null) {
            throw new IllegalArgumentException("Начальный баланс не может быть null");
        }
        if (dailyLimit < 0) {
            throw new IllegalArgumentException("Дневной лимит не может быть отрицательным");
        }
        
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = initialBalance;
        this.dailyLimit = dailyLimit;
        this.withdrawnToday = 0;
        this.isActive = true;
    }


    public Balance getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }
    public boolean isActive() { return isActive; }

    
    public void resetDailyLimit() {
        this.withdrawnToday = 0;
    }

    
    public void deactivate() {
        this.isActive = false;
    }

   
    public void activate() {
        this.isActive = true;
    }

    public void transfer(BankAccount target, Balance amount) {
    if (target == null) {
        throw new IllegalArgumentException("Целевой счёт не может быть null");
    }
    if (amount == null) {
        throw new IllegalArgumentException("Сумма перевода не может быть null");
    }
    if (amount.getAmount() <= 0) {
        throw new IllegalArgumentException("Сумма перевода должна быть больше нуля");
    }
    if (!this.isActive) {
        throw new IllegalStateException("Счёт не активен");
    }
    if (this == target) {
        throw new IllegalArgumentException("Нельзя перевести деньги на тот же счёт");
    }

    long amountInRublesKopecks = calculateAmountInRublesKopecks(amount);
    if (this.withdrawnToday + amountInRublesKopecks > this.dailyLimit) {
        throw new IllegalStateException("Превышен дневной лимит операций");
    }

    Balance newSourceBalance = this.balance.subtract(amount);
    Balance newTargetBalance = target.balance.add(amount);

    this.balance = newSourceBalance;
    target.balance = newTargetBalance;

    this.withdrawnToday += amountInRublesKopecks;
}

    private long calculateAmountInRublesKopecks(Balance amount) {
        return (amount.getAmount() * amount.getCurrency().getPriceInRublesKopecks()) / 100;
    }

    @Override
    public String toString() {
        String status = isActive ? "Активен" : "Заблокирован";
        return String.format("Счёт: %s | Владелец: %s | Баланс: %s | Лимит: %.2f ₽ | " +
                "Потрачено сегодня: %.2f ₽ | Статус: %s",
                accountNumber, ownerName, balance.toString(),
                dailyLimit / 100.0, withdrawnToday / 100.0, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}