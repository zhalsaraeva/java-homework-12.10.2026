public class Main {
    public static void main(String[] args) {
        System.out.println("=== Демонстрация работы BankAccount ===\n");

       
        Balance rubBalance1 = new Balance(20_000_000, Currency.RUB); 
        Balance usdBalance = new Balance(10_000, Currency.USD);       

        BankAccount account1 = new BankAccount("RU123456789", "Алексеев Алексей", rubBalance1, 10_000_000);
        BankAccount account2 = new BankAccount("RU987654321", "Иван Иванов", usdBalance, 10_000_000);
        BankAccount account3 = new BankAccount("RU111111111", "Петр Петров", new Balance(10_00, Currency.RUB), 10_000_000);

        System.out.println("1. Счета до операций:");
        System.out.println(account1);
        System.out.println(account2);
        System.out.println(account3);
        System.out.println();

        System.out.println("2. Перевод 2000.00 руб со счёта 1 на счёт 2...");
        account1.transfer(account2, new Balance(200_000, Currency.RUB));
        System.out.println(account1);
        System.out.println(account2);
        System.out.println();

      
        System.out.println("3. Перевод 10.00 $ со счёта 2 на счёт 1 (с конвертацией)...");
        account2.transfer(account1, new Balance(1_000, Currency.USD));
        System.out.println(account1);
        System.out.println(account2);
        System.out.println();

        System.out.println("4. Попытка перевести 10000.00 руб со счёта 3 (недостаточно средств):");
        try {
            account3.transfer(account1, new Balance(1_000_000, Currency.RUB));
        } catch (IllegalArgumentException e) {
            System.out.println("УСПЕШНО перехвачена ошибка: " + e.getMessage());
        }
        System.out.println();

        System.out.println("5. Попытка превысить дневной лимит со счёта 1 (перевод 99000 руб):");
        try {
            account1.transfer(account2, new Balance(9_900_000, Currency.RUB));
        } catch (IllegalStateException e) {
            System.out.println("УСПЕШНО перехвачена ошибка: " + e.getMessage());
        }
        System.out.println();


        System.out.println("6. Блокировка счёта 1 и попытка перевода:");
        account1.deactivate();
        System.out.println(account1);
        try {
            account1.transfer(account2, new Balance(100_000, Currency.RUB));
        } catch (IllegalStateException e) {
            System.out.println("УСПЕШНО перехвачена ошибка: " + e.getMessage());
        }
        System.out.println();

        System.out.println("7. Конвертация баланса счёта 2 в рубли:");
        Balance rubBalance2 = account2.getBalance().convertTo(Currency.RUB);
        System.out.println("Баланс счёта 2 в рублях: " + rubBalance2);
        System.out.println();

        System.out.println("=== Все тесты завершены успешно! ===");
    }
}