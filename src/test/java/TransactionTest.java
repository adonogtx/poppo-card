import com.adonogtx.poppocard.exception.PoppoCardTransactionException;
import com.adonogtx.poppocard.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionTest {
    @Test
    void testRechargeTransactionSuccess(){

        PoppoCard card = new PoppoCard();
        Location location = new Location("Poppo", LocationType.KONBINI);
        Transaction transaction = new Transaction(BigDecimal.valueOf(10.0), TransactionType.RECHARGE, location, LocalDateTime.now());
        try {
            card.rechargeCard(transaction);
        } catch (PoppoCardTransactionException e) {
            throw new RuntimeException(e);
        }
        System.out.println(card.getBalance());

        Assertions.assertEquals(transaction,card.getTransactionsHistory().getLast());

    }

    @Test
    void testChargeTransactionSuccess(){

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

        Assertions.assertEquals(charge,card.getTransactionsHistory().getLast());

    }

}
