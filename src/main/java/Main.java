import model.Account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;


public class Main {

    public static void main(String[] args) throws InterruptedException {
        List<Account> accountList = new ArrayList<>(100);


        for (int i = 0; i < 100; i++) {
            accountList.add(new Account(String.valueOf(i), new BigDecimal(10000)));
        }
        System.out.println("Sum on the beginning: "+ sum(accountList));

        try (var executorService = Executors.newFixedThreadPool(100)) {
            for (int i = 0; i < 100000; i++) {
                executorService.submit(() -> {
                    int accountId1 = ThreadLocalRandom.current().nextInt(100);
                    int accountId2 = ThreadLocalRandom.current().nextInt(100);
                    if (accountId1 == accountId2) {
                        return;
                    }
                    BigDecimal transfer = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1000));
                    Account account1 = accountList.get(accountId1);
                    Account account2 = accountList.get(accountId2);
                    transferTask(account1, account2, transfer);
                });
            }

            executorService.shutdown();

            if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
                executorService.shutdownNow();
            }
        }

        accountList.forEach(acc -> System.out.println("Account id: "+acc.getId() + ": "+acc.getBalance()));
        System.out.println("Sum after all: "+ sum(accountList));
    }

    public static void transferTask(Account source, Account destination, BigDecimal transfer){
        Account acc1 = Integer.parseInt(source.getId()) < Integer.parseInt(destination.getId())
                ? source : destination;

        Account acc2 = acc1 == source ? destination: source;

        System.out.println("Trying execute Transfer: "+transfer.toString()+ " from source " + source.getId() +" to destination "+destination.getId());

        acc1.getReentrantLock().lock();
        try {
            acc2.getReentrantLock().lock();
            try {
                if (source.getBalance().compareTo(transfer) < 0) {
                    System.out.println("FAILURE execute Transfer: "+transfer.toString()+ " from source " + source.getId() +" to destination "+destination.getId());
                    return;
                }
                System.out.println("SUCCESS execute Transfer: "+transfer.toString()+ " from source " + source.getId() +" to destination "+destination.getId());
                source.withdraw(transfer);
                destination.deposit(transfer);
            } finally {
                acc2.getReentrantLock().unlock();
            }
        } finally {
            acc1.getReentrantLock().unlock();
        }
    }

    public static BigDecimal sum(List<Account> accountList) {
        return accountList.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
    }
}