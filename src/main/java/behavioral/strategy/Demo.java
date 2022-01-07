package behavioral.strategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Первый в мире консольный интерет магазин.
 */
public class Demo {

  private static final Map<Integer, Integer> priceOnProducts = new HashMap<>();
  private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static final Order order = new Order();
  private static PayStrategy strategy;

  static {
    priceOnProducts.put(1, 2200);
    priceOnProducts.put(2, 1850);
    priceOnProducts.put(3, 1100);
    priceOnProducts.put(4, 890);
  }

  public static void main(String[] args) throws IOException {
    while (!order.isClosed()) {
      int cost;

      String continueChoice;
      do {
        System.out.print("""
            Please, select a product:
            1 - Mother board
            2 - CPU
            3 - HDD
            4 - Memory
            """);
        int choice = Integer.parseInt(reader.readLine());
        cost = priceOnProducts.get(choice);
        System.out.print("Count: ");
        int count = Integer.parseInt(reader.readLine());
        order.setTotalCost(cost * count);
        System.out.print("Do you wish to continue selecting products? Y/N: ");
        continueChoice = reader.readLine();
      } while (continueChoice.equalsIgnoreCase("Y"));

      if (strategy == null) {
        System.out.println("""
            Please, select a payment method:
            1 - PalPay
            2 - Credit Card""");
        String paymentMethod = reader.readLine();

        // Клиент создаёт различные стратегии на основании
        // пользовательских данных, конфигурации и прочих параметров.
        if (paymentMethod.equals("1")) {
          strategy = new PayByPayPal();
        } else {
          strategy = new PayByCreditCard();
        }
      }

      // Объект заказа делегирует сбор платёжных данны стратегии, т.к.
      // только стратегии знают какие данные им нужны для приёма оплаты.
      order.processOrder(strategy);

      System.out.print("Pay " + order.getTotalCost() + " units or Continue shopping? P/C: ");
      String proceed = reader.readLine();
      if (proceed.equalsIgnoreCase("P")) {
        // И наконец, стратегия запускает приём платежа.
        if (strategy.pay(order.getTotalCost())) {
          System.out.println("Payment has been successful.");
        } else {
          System.out.println("FAIL! Please, check your data.");
        }
        order.setClosed();
      }
    }
  }
}
