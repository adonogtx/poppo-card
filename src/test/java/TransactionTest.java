
import com.adonogtx.poppocard.exception.InsufficientBalanceException;
import com.adonogtx.poppocard.exception.InvalidValueException;
import com.adonogtx.poppocard.exception.OperationNotAllowedException;
import com.adonogtx.poppocard.exception.PoppoCardTransactionException;
import com.adonogtx.poppocard.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

public class TransactionTest {

    @Test
    void testNullLocationName(){

        Assertions.assertThrows(NullPointerException.class, () -> {
            new Location(null, LocationType.KONBINI);
        });

    }

    @Test
    void testNullAmountOnCharge()   {

        PoppoCard card = new PoppoCard();


        NullPointerException e = Assertions.assertThrows(NullPointerException.class, () -> {
            card.chargeCard(null, new Location("Serena", LocationType.BAR));
        });
        Assertions.assertEquals("amount cannot be null", e.getMessage());

    }

    @Test
    void testRechargeTransactionSuccess() throws PoppoCardTransactionException {

        PoppoCard card = new PoppoCard();
        Location location = new Location("Poppo", LocationType.KONBINI);

        Transaction transaction = card.rechargeCard(
                new BigDecimal("10.00"),
                location
        );

        Assertions.assertEquals(new BigDecimal("10.00"), card.getBalance());
        Assertions.assertEquals(transaction, card.getTransactionsHistory().getLast());
    }

    @Test
    void testChargeTransactionSuccess() throws PoppoCardTransactionException {

        PoppoCard card = new PoppoCard();
        Location konbini = new Location("Poppo", LocationType.KONBINI);
        Location bar = new Location("Serena", LocationType.BAR);

        card.rechargeCard(new BigDecimal("10.00"), konbini);

        Transaction charge = card.chargeCard(
                new BigDecimal("5.00"),
                bar
        );

        Assertions.assertEquals(charge, card.getTransactionsHistory().getLast());
        Assertions.assertEquals(new BigDecimal("5.00"), card.getBalance());
    }

    @Test
    void testChargeRejectedByInsufficientBalance() {

        PoppoCard card = new PoppoCard();
        Location bar = new Location("Serena", LocationType.BAR);

        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            card.chargeCard(new BigDecimal("5.00"), bar);
        });
    }

    @Test
    void testRechargeOperationNotAllowed() {

        PoppoCard card = new PoppoCard();
        Location bar = new Location("Serena", LocationType.BAR);

        Assertions.assertThrows(OperationNotAllowedException.class, () -> {
            card.rechargeCard(new BigDecimal("10.00"), bar);
        });
    }

    @Test
    void testChargeOperationNotAllowed() throws PoppoCardTransactionException {

        PoppoCard card = new PoppoCard();
        Location atm = new Location("ATM", LocationType.VENDING_MACHINE);

        Assertions.assertThrows(OperationNotAllowedException.class, () -> {
            card.chargeCard(new BigDecimal("5.00"), atm);
        });
    }

    @Test
    void testChargeInvalidValue() {

        PoppoCard card = new PoppoCard();
        Location bar = new Location("Serena", LocationType.BAR);


        Assertions.assertThrows(InvalidValueException.class, () -> {
            card.chargeCard(new BigDecimal("0.00"), bar);
        });
    }

    @Test
    void testRechargeInvalidValue() {

        PoppoCard card = new PoppoCard();
        Location konbini = new Location("Poppo", LocationType.KONBINI);

        Assertions.assertThrows(InvalidValueException.class, () -> {
            card.rechargeCard(new BigDecimal("-10.00"), konbini);
        });
    }

    @Test
    void testUnsupportedOperation() throws PoppoCardTransactionException {

        PoppoCard card = new PoppoCard();
        Location konbini = new Location("Poppo", LocationType.KONBINI);

        Transaction recharge = card.rechargeCard(new BigDecimal("10.00"), konbini);

        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            card.getTransactionsHistory().add(recharge);
        });
    }

    @Test
    void testTransactionsAtLocation() throws PoppoCardTransactionException {

        PoppoCard card = new PoppoCard();

        Location konbini = new Location("Poppo", LocationType.KONBINI);
        Location subway = new Location("JR", LocationType.SUBWAY);

        Transaction subwayRecharge = card.rechargeCard(
                new BigDecimal("100.00"),
                subway
        );

        Transaction subwayCharge = card.chargeCard(
                new BigDecimal("10.00"),
                subway
        );

        Transaction konbiniRecharge = card.rechargeCard(
                new BigDecimal("100.00"),
                konbini
        );

        Transaction konbiniCharge = card.chargeCard(
                new BigDecimal("20.00"),
                konbini
        );

        Transaction konbiniCharge2 = card.chargeCard(
                new BigDecimal("50.00"),
                konbini
        );

        List<Transaction> transactionsAtKonbini =
                card.getTransactionsAtLocation(konbini);

        Assertions.assertTrue(transactionsAtKonbini.contains(konbiniRecharge));
        Assertions.assertTrue(transactionsAtKonbini.contains(konbiniCharge));
        Assertions.assertTrue(transactionsAtKonbini.contains(konbiniCharge2));
        Assertions.assertFalse(transactionsAtKonbini.contains(subwayCharge));
    }

    @Test
    void testTransactionHistoryOrder() throws PoppoCardTransactionException {

        PoppoCard card = new PoppoCard();
        Location konbini = new Location("Poppo Store", LocationType.KONBINI);

        Transaction konbiniRecharge = card.rechargeCard(
                new BigDecimal("100.00"),
                konbini
        );

        Transaction konbiniCharge = card.chargeCard(
                new BigDecimal("20.00"),
                konbini
        );

        Transaction konbiniCharge2 = card.chargeCard(
                new BigDecimal("50.00"),
                konbini
        );

        List<Transaction> cardTransactionsHistory =
                card.getTransactionsHistory();

        Assertions.assertEquals(konbiniRecharge, cardTransactionsHistory.get(0));
        Assertions.assertEquals(konbiniCharge, cardTransactionsHistory.get(1));
        Assertions.assertEquals(konbiniCharge2, cardTransactionsHistory.get(2));
    }
}
