import java.util.Objects;

public final class Balance {
    private final long amount; 
    private final Currency currency;

    public Balance(long amount, Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Валюта не может быть null");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Сумма не может быть отрицательной: " + amount);
        }
        this.amount = amount;
        this.currency = currency;
    }

    public long getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

 
    public Balance add(Balance other) {
        if (other == null) {
            throw new IllegalArgumentException("Добавляемый баланс не может быть null");
        }
        if (other.amount < 0) {
            throw new IllegalArgumentException("Нельзя добавить отрицательную сумму");
        }
        
        Balance converted = other.convertTo(this.currency);
        return new Balance(this.amount + converted.amount, this.currency);
    }


    public Balance subtract(Balance other) {
        if (other == null) {
            throw new IllegalArgumentException("Вычитаемый баланс не может быть null");
        }
        Balance converted = other.convertTo(this.currency);
        long newAmount = this.amount - converted.amount;
        if (newAmount < 0) {
            throw new IllegalArgumentException("Недостаточно средств для операции");
        }
        return new Balance(newAmount, this.currency);
    }

   
    public Balance convertTo(Currency targetCurrency) {
        if (targetCurrency == null) {
            throw new IllegalArgumentException("Целевая валюта не может быть null");
        }
        if (this.currency == targetCurrency) {
            return this;
        }
        
        long rublesKopecks = (this.amount * this.currency.getPriceInRublesKopecks()) / 100;
        long targetAmount = (rublesKopecks * 100) / targetCurrency.getPriceInRublesKopecks();
        return new Balance(targetAmount, targetCurrency);
    }

    @Override
    public String toString() {
        double mainAmount = amount / 100.0;
        return String.format("%.2f %s", mainAmount, currency.getSymbol());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Balance balance = (Balance) o;
        return amount == balance.amount && currency == balance.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}