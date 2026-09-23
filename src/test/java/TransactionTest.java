import com.adonogtx.poppocard.exception.InsufficientBalanceException;
import com.adonogtx.poppocard.exception.InvalidValueException;
import com.adonogtx.poppocard.exception.OperationNotAllowedException;
import com.adonogtx.poppocard.exception.PoppoCardTransactionException;
import com.adonogtx.poppocard.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionTest {
    @Test
    void testRechargeTransactionSuccess() {

        PoppoCard card = new PoppoCard();
        Location location = new Location("Poppo", LocationType.KONBINI);
        Transaction transaction = new Transaction(BigDecimal.valueOf(10.0), TransactionType.RECHARGE, location, LocalDateTime.now());
        try {
            card.rechargeCard(transaction);
        } catch (PoppoCardTransactionException e) {
            throw new RuntimeException(e);
        }
        System.out.println(card.getBalance());

        Assertions.assertEquals(transaction, card.getTransactionsHistory().getLast());

    }

    @Test
    void testChargeTransactionSuccess() {

        PoppoCard card = new PoppoCard();
        Location konbini = new Location("Poppo", LocationType.KONBINI);
        Location bar = new Location("Serena", LocationType.BAR);
        Transaction recharge = new Transaction(BigDecimal.valueOf(10.0), TransactionType.RECHARGE, konbini, LocalDateTime.now());
        Transaction charge = new Transaction(BigDecimal.valueOf(5.0), TransactionType.CHARGE, bar, LocalDateTime.now());

        try {
            card.rechargeCard(recharge);
        } catch (PoppoCardTransactionException e) {
            throw new RuntimeException(e);
        }

        try {
            card.chargeCard(charge);
        } catch (PoppoCardTransactionException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(charge, card.getTransactionsHistory().getLast());

    }

    @Test
    void testChargeRejectedByInsufficientBalance() {
        PoppoCard card = new PoppoCard();
        Location bar = new Location("Serena", LocationType.BAR);
        Transaction charge = new Transaction(BigDecimal.valueOf(5.0), TransactionType.CHARGE, bar, LocalDateTime.now());

        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            card.chargeCard(charge);
        });

    }

    @Test
    void testRechargeOperationNotAllowed() {
        PoppoCard card = new PoppoCard();
        Location bar = new Location("Serena", LocationType.BAR);
        Transaction recharge = new Transaction(BigDecimal.valueOf(10.0), TransactionType.RECHARGE, bar, LocalDateTime.now());

        Assertions.assertThrows(OperationNotAllowedException.class, () -> {
            card.rechargeCard(recharge);
        });

    }

    @Test
    void testChargeOperationNotAllowed() {
        PoppoCard card = new PoppoCard();
        Location atm = new Location("ATM", LocationType.VENDING_MACHINE);
        Transaction charge = new Transaction(BigDecimal.valueOf(5.0), TransactionType.CHARGE, atm, LocalDateTime.now());
        Transaction recharge = new Transaction(BigDecimal.valueOf(10.0), TransactionType.RECHARGE, atm, LocalDateTime.now());

        try {
            card.rechargeCard(recharge);
        } catch (PoppoCardTransactionException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertThrows(OperationNotAllowedException.class, () -> {
            card.chargeCard(charge);
        });

    }

    @Test
    void testChargeInvalidValue() {
        PoppoCard card = new PoppoCard();
        Location atm = new Location("ATM", LocationType.VENDING_MACHINE);
        Transaction charge = new Transaction(BigDecimal.valueOf(0.0), TransactionType.CHARGE, atm, LocalDateTime.now());

        Assertions.assertThrows(InvalidValueException.class, () -> {
            card.chargeCard(charge);
        });
    }

    @Test
    void testRechargeInvalidValue() {
        PoppoCard card = new PoppoCard();
        Location atm = new Location("ATM", LocationType.VENDING_MACHINE);
        Transaction recharge = new Transaction(BigDecimal.valueOf(-10.0), TransactionType.RECHARGE, atm, LocalDateTime.now());

        Assertions.assertThrows(InvalidValueException.class, () -> {
            card.rechargeCard(recharge);
        });
    }

    @Test
    void testUnsupportedOperation() {
        PoppoCard card = new PoppoCard();
        Location atm = new Location("ATM", LocationType.VENDING_MACHINE);
        Transaction recharge = new Transaction(BigDecimal.valueOf(10.0), TransactionType.RECHARGE, atm, LocalDateTime.now());

        try {
            card.rechargeCard(recharge);
        } catch (PoppoCardTransactionException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            card.getTransactionsHistory().add(recharge);
        });
    }

    @Test
    void testTransactionsAtLocation(){
        PoppoCard card = new PoppoCard();
        Location konbini = new Location("Poppo", LocationType.KONBINI);
        Location subway = new Location("JR", LocationType.SUBWAY);
        Transaction konbiniRecharge = new Transaction(BigDecimal.valueOf(100.0), TransactionType.RECHARGE, konbini, LocalDateTime.now());
        Transaction konbiniCharge = new Transaction(BigDecimal.valueOf(20.0), TransactionType.CHARGE, konbini, LocalDateTime.now());
        Transaction konbiniCharge2 = new Transaction(BigDecimal.valueOf(50.0), TransactionType.CHARGE, konbini, LocalDateTime.now());
        Transaction subwayRecharge = new Transaction(BigDecimal.valueOf(100.0), TransactionType.RECHARGE, subway, LocalDateTime.now());
        Transaction subwayCharge = new Transaction(BigDecimal.valueOf(10.0), TransactionType.CHARGE, subway, LocalDateTime.now());

        try {
            card.rechargeCard(subwayRecharge);
        } catch (PoppoCardTransactionException e) {
            throw new RuntimeException(e);
        }

        try {
            card.chargeCard(subwayCharge);
        } catch (PoppoCardTransactionException e) {
            throw new RuntimeException(e);
        }

        try {
            card.rechargeCard(konbiniRecharge);
        } catch (PoppoCardTransactionException e) {
            throw new RuntimeException(e);
        }

        try {
            card.chargeCard(konbiniCharge);
        } catch (PoppoCardTransactionException e) {
            throw new RuntimeException(e);
        }

        try {
            card.chargeCard(konbiniCharge2);
        } catch (PoppoCardTransactionException e) {
            throw new RuntimeException(e);
        }

        List<Transaction> transactionsAtKonbini = card.getTransactionsAtLocation(konbini);

        Assertions.assertTrue(transactionsAtKonbini.contains(konbiniRecharge));
        Assertions.assertTrue(transactionsAtKonbini.contains(konbiniCharge));
        Assertions.assertTrue(transactionsAtKonbini.contains(konbiniCharge2));
        Assertions.assertFalse(transactionsAtKonbini.contains(subwayCharge));

    }
}
